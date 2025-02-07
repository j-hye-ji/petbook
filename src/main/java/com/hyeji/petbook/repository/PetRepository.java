package com.hyeji.petbook.repository;

import com.hyeji.petbook.dto.PetDTO;
import com.hyeji.petbook.entity.Pet;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Pet> findByPetName(String petName);

}
