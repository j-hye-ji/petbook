package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.StoreDTO;
import com.hyeji.petbook.entity.Store;
import com.hyeji.petbook.entity.StoreReservationTime;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.StoreRepository;
import com.hyeji.petbook.repository.StoreReservationTimeRepository;
import com.hyeji.petbook.repository.UserRepository;
import com.hyeji.petbook.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hyeji.petbook.type.UserRole.ADMIN;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreReservationTimeRepository storeReservationTimeRepository;
    private final JwtTokenUtil jwtTokenUtil;

    // 매장 등록 (ADMIN만 가능)
    public String addStore(String token, StoreDTO storeDTO) {
        String email = jwtTokenUtil.getClaimsFromToken(token).getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!user.getUserRole().equals(ADMIN)) {
            return "관리자만 매장을 등록할 수 있습니다.";
        }

        Store store = new Store();
        store.setStoreName(storeDTO.getStoreName());
        store.setLocation(storeDTO.getLocation());
        store.setDescription(storeDTO.getDescription());
        store.setOpeningTime(storeDTO.getOpeningTime());
        store.setClosingTime(storeDTO.getClosingTime());
        store.setAdmin(user);  // 관리자 설정

        Store savedStore = storeRepository.save(store);

        // 예약 가능한 시간대 생성 및 저장
        List<StoreReservationTime> reservationTimes = createReservationTimes(storeDTO, savedStore);
        storeReservationTimeRepository.saveAll(reservationTimes);

        return store.getStoreName() + " 매장이 등록되었습니다.";
    }

    // 예약 가능한 시간대 생성 메소드 (StoreDTO를 사용)
    private List<StoreReservationTime> createReservationTimes(StoreDTO storeDTO, Store store) {
        List<StoreReservationTime> reservationTimes = new ArrayList<>();

        LocalTime currentTime = storeDTO.getOpeningTime().plusHours(1); // 오픈 시간 + 1시간
        LocalTime endTime = storeDTO.getClosingTime().minusHours(1); // 마감 시간 - 1시간

        while (currentTime.isBefore(endTime)) {
            StoreReservationTime reservationTime = new StoreReservationTime();
            reservationTime.setStartTime(currentTime);
            reservationTime.setEndTime(currentTime.plusHours(1)); // 1시간 단위
            reservationTime.setStore(store);
            reservationTimes.add(reservationTime);

            // 다음 예약 시간으로 이동
            currentTime = currentTime.plusHours(1);
        }

        return reservationTimes;
    }

    // 특정 매장의 예약 가능한 시간대 리스트 조회
    public List<StoreReservationTime> getReservationTimes(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다."));
        return store.getReservationTimes(); // Store에서 예약 시간대 리스트 반환
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
