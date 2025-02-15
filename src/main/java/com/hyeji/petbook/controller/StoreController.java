package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.StoreDTO;
import com.hyeji.petbook.entity.Reservation;
import com.hyeji.petbook.entity.Store;
import com.hyeji.petbook.entity.StoreReservationTime;
import com.hyeji.petbook.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    // UserRole: ADMIN
    // 매장 등록 (ADMIN만 가능)
    @PostMapping("/add-store")
    public ResponseEntity<String> addStore(
            @RequestHeader(name = "Authorization") String token,  // JWT 토큰을 Authorization 헤더에서 받음
            @RequestBody StoreDTO storeDTO) {

        System.out.println("Received token: " + token);

        String message = storeService.addStore(token, storeDTO);
        return ResponseEntity.ok(message);
    }

    // 특정 매장의 예약 가능한 시간대 리스트 조회
    @GetMapping("/{storeId}/reservation-times")
    public ResponseEntity<List<StoreReservationTime>> getReservationTimes(@PathVariable Long storeId) {
        try {
            List<StoreReservationTime> reservationTimes = storeService.getReservationTimes(storeId);
            return ResponseEntity.ok(reservationTimes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 매장이 없을 경우 404 반환
        }
    }

    // 모든 매장 조회
    @GetMapping("/")
    public ResponseEntity<List<Store>> getStores() {
        List<Store> stores = storeService.getStores();
        return ResponseEntity.ok(stores);
    }

    // 매장 이름으로 조회
    @GetMapping("/store")
    public ResponseEntity<List<Store>> getStore(@RequestParam @Valid String storeName) {
        List<Store> stores = storeService.getStore(storeName);
        return ResponseEntity.ok(stores);
    }

//    // 매장 정보 수정
//    @PutMapping("/update-store/{id}")
//    public ResponseEntity<String> updateStore(@PathVariable(name="id") Long id, @RequestBody StoreDTO storeDTO) {
//        boolean result = storeService.updateStore(id, storeDTO);
//
//        if (result) {
//            return ResponseEntity.ok("매장 정보가 수정되었습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("매장을 찾을 수 없습니다.");
//        }
//    }

    // 매장 정보 수정 개선 (수정한 정보가 리턴되도록)
    @PutMapping("/update-store/{id}")
    public ResponseEntity<String> updateStoreRef(@PathVariable(name="id") Long id, @RequestBody StoreDTO storeDTO) {
        Store updateStore = storeService.updateStoreRef(id, storeDTO);

        if (updateStore == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        String responseMessage = String.format("매장 이름: %s\n", updateStore.getStoreName()) +
                String.format("매장 위치: %s\n", updateStore.getLocation()) +
                String.format("매장 설명: %s", storeDTO.getDescription());


        // 수정된 매장 정보를 포함하여 200 OK 상태 코드 반환
        return ResponseEntity.ok(responseMessage);
    }

    // 매장 삭제
    @DeleteMapping("/delete-store/{id}")
    public ResponseEntity<String> deleteStore(@PathVariable(name="id") Long id) {
        boolean result = storeService.deleteStore(id);

        if (result) {
            return ResponseEntity.ok("매장이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("매장을 찾을 수 없습니다.");
        }
    }
}
