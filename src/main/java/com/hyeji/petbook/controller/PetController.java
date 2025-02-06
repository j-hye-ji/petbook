package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.PetDTO;
import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetRepository petRepository;

    // 반려동물 추가
    @PostMapping("/add-pet")
    public Pet addPet(@RequestBody PetDTO petDTO) {
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

    // 모든 반려동물 조회
    @GetMapping("/pet")
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    // 반려동물 정보 수정
    @PutMapping("/update-pet/{id}")
    public Pet updatePet(@PathVariable(name = "id") Long id, @RequestBody PetDTO petDTO) {
        Optional<Pet> optPet = petRepository.findById(id);
        if (!optPet.isPresent()) {
            return null;
        }

        Pet updatePet = optPet.get();
        updatePet.setPetName(petDTO.getPetName());
        updatePet.setType(petDTO.getType());
        updatePet.setBreed(petDTO.getBreed());
        updatePet.setGender(petDTO.getGender());
        updatePet.setBirthDate(petDTO.getBirthDate());
        updatePet.setHealthStatus(petDTO.getHealthStatus());

        Pet savePet = petRepository.save(updatePet);

        return savePet;
    }

    // 반려동물 삭제
    @DeleteMapping("/delete-pet/{id}")
    public String deletePet(@PathVariable(name = "id") Long id) {
        Optional<Pet> optPet = petRepository.findById(id);
        if (!optPet.isPresent()) {
//            return "등록되어 있지 않은 반려동물입니다.";
            throw new IllegalArgumentException("등록되어 있지 않은 반려동물입니다.");
        }

        petRepository.delete(optPet.get());
        return optPet.get().getPetName() + " 삭제 되었습니다.";
    }
}
