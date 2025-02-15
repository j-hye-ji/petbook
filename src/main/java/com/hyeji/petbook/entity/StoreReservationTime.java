package com.hyeji.petbook.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreReservationTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;  // 예약 시작 시간
    private LocalTime endTime;    // 예약 종료 시간

    // 매장과 예약 시간의 관계
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonBackReference
    private Store store;
}
