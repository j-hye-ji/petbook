package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.StoreDTO;
import com.hyeji.petbook.entity.Store;
import com.hyeji.petbook.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    // UserRole: ADMIN
    // 매장 관리자의 매장 등록
    @PostMapping("/add-store")
    public ResponseEntity<String> addStore(@RequestBody @Valid StoreDTO storeDTO) {
        String message = storeService.addStore(storeDTO);
        return ResponseEntity.ok(message);
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

}
