package com.hyeji.petbook.repository;

import com.hyeji.petbook.entity.Store;
import com.hyeji.petbook.entity.StoreReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreName(String storeName);

    List<Store> findByStoreNameContaining(String storeName);

}
