package com.hyeji.petbook.service;

import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    private final PetRepository petRepository; // JPA 리포지토리나 DAO 클래스

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet addPet(Pet pet) {
        // 비즈니스 로직 추가 가능 (예: 유효성 검사, 로깅 등)
        return petRepository.save(pet); // 데이터베이스에 저장
    }
}
