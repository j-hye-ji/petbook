package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.StoreDTO;
import com.hyeji.petbook.entity.Store;
import com.hyeji.petbook.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    // 매장 등록
    public String addStore(StoreDTO storeDTO) {
        Optional<Store> optStore = storeRepository.findByStoreName(storeDTO.getStoreName());
        if (optStore.isPresent()) {
            return "이미 등록되어 있는 매장입니다.";
        }

        Store store = new Store();
        store.setStoreName(storeDTO.getStoreName());
        store.setLocation(storeDTO.getLocation());
        store.setDescription(storeDTO.getDescription());

        storeRepository.save(store);

        return store.getStoreName() + " 매장이 등록되었습니다.";
    }
}
