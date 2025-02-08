package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.StoreDTO;
import com.hyeji.petbook.entity.Store;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.StoreRepository;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.type.UserRole;
import com.hyeji.petbook.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    // 매장 등록 (ADMIN만 가능)
    public String addStore(String token, StoreDTO storeDTO) {
        String email = jwtTokenUtil.getClaimsFromToken(token).getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            return "관리자만 매장을 등록할 수 있습니다.";
        }

        Store store = new Store();
        store.setStoreName(storeDTO.getStoreName());
        store.setLocation(storeDTO.getLocation());
        store.setDescription(storeDTO.getDescription());
        store.setAdmin(user);  // 관리자 설정

        storeRepository.save(store);

        return store.getStoreName() + " 매장이 등록되었습니다.";
    }

    // 모든 매장 조회
    public List<Store> getStores() {
        return storeRepository.findAll();
    }

    // 매장 이름으로 조회 (부분 검색)
    public List<Store> getStore(String storeName) {
        if (storeName != null && !storeName.isEmpty()) {
            return storeRepository.findByStoreNameContaining(storeName);
        }
        return new ArrayList<>();
    }

    // 매장 정보 수정
    public boolean updateStore(Long id, StoreDTO storeDTO) {
        Optional<Store> optStore = storeRepository.findById(id);
        if (!optStore.isPresent()) {
            return false;
        }

        Store updateStore = optStore.get();

        if (storeDTO.getStoreName() != null) {
            updateStore.setStoreName(storeDTO.getStoreName());
        }

        if (storeDTO.getLocation() != null) {
            updateStore.setStoreName(storeDTO.getStoreName());
        }

        if (storeDTO.getDescription() != null) {
            updateStore.setDescription(storeDTO.getDescription());
        }

        storeRepository.save(updateStore);

        return true;
    }

    // 매장 정보 수정 개선 (수정한 정보가 리턴되도록)
    public Store updateStoreRef(Long id, StoreDTO storeDTO) {
        Optional<Store> optStore = storeRepository.findById(id);
        if (!optStore.isPresent()) {
            return null;
        }

        Store updateStore = optStore.get();

        if (storeDTO.getStoreName() != null) {
            updateStore.setStoreName(storeDTO.getStoreName());
        }

        if (storeDTO.getLocation() != null) {
            updateStore.setLocation(storeDTO.getLocation());
        }

        if (storeDTO.getDescription() != null) {
            updateStore.setDescription(storeDTO.getDescription());
        }

        storeRepository.save(updateStore);

        return updateStore;
    }

    // 매장 삭제
    public boolean deleteStore(Long id) {
        Optional<Store> optStore = storeRepository.findById(id);
        if (!optStore.isPresent()) {
            return false;
        }

        storeRepository.delete(optStore.get());

        return true;
    }
}
