package com.hyeji.petbook.repository;

import com.hyeji.petbook.entity.StoreReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreReservationTimeRepository extends JpaRepository<StoreReservationTime, Long> {
}
