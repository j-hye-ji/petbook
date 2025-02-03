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

}
