package com.hyeji.petbook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String petName;
    private String type;
    private String breed;
    private LocalDate birthday;
    private String gender;
    private String healthStatus;

    // 사용자와 반려동물 간의 관계 (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
