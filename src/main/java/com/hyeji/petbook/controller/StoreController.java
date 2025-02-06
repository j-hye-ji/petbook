package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.StoreDTO;
import com.hyeji.petbook.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
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
}
