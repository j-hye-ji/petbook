package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.PetDTO;
import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    // 반려견 추가
    @PostMapping("/add-pet")
    public Pet addPet(@RequestBody PetDTO petDTO) {
        // PetDTO -> Pet 엔티티로 변환
        Pet pet = new Pet();

        pet.setPetName(petDTO.getPetName());
        pet.setType(petDTO.getType());
        pet.setBreed(petDTO.getBreed());
        pet.setGender(petDTO.getGender());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setHealthStatus(petDTO.getHealthStatus());

        Pet savePet = petRepository.save(pet);

        return savePet;
    }

    // 모든 반려견 조회
    @GetMapping("/pet")
    public List<Pet> getPets() {
        return petRepository.findAll();
    }
}
