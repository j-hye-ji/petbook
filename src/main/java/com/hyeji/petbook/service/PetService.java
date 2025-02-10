package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.PetDTO;
import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    // 반려동물 등록
    public String addPet(PetDTO petDTO) {
        Optional<Pet> optPet = petRepository.findByPetName(petDTO.getPetName());
        if (optPet.isPresent()) {
            return "이미 등록되어 있는 반려동물입니다.";
        }

        Pet pet = new Pet();

        pet.setPetName(petDTO.getPetName());
        pet.setType(petDTO.getType());
        pet.setBreed(petDTO.getBreed());
        pet.setGender(petDTO.getGender());
        pet.setBirthday(petDTO.getBirthday());
        pet.setHealthStatus(petDTO.getHealthStatus());

        petRepository.save(pet);

        return pet.getPetName() + " 반려동물이 등록되었습니다.";
    }

    // 모든 반려동물 조회
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    // 반려동물 정보 수정
    public Pet updatePet(Long id, PetDTO petDTO) {
        Optional<Pet> optPet = petRepository.findById(id);
        if (!optPet.isPresent()) {
            return null;
        }

        Pet updatePet = optPet.get();

        // petDTO의 각 필드가 null이 아니면 그 값으로 업데이트
        if (petDTO.getPetName() != null) {
            updatePet.setPetName(petDTO.getPetName());
        }
        if (petDTO.getType() != null) {
            updatePet.setType(petDTO.getType());
        }
        if (petDTO.getBreed() != null) {
            updatePet.setBreed(petDTO.getBreed());
        }
        if (petDTO.getGender() != null) {
            updatePet.setGender(petDTO.getGender());
        }
        if (petDTO.getBirthday() != null) {
            updatePet.setBirthday(petDTO.getBirthday());
        }
        if (petDTO.getHealthStatus() != null) {
            updatePet.setHealthStatus(petDTO.getHealthStatus());
        }

        return petRepository.save(updatePet);
    }

    // 반려동물 삭제
    public String deletePet(Long id) {
        Optional<Pet> optPet = petRepository.findById(id);
        if (!optPet.isPresent()) {
//            return "등록되어 있지 않은 반려동물입니다.";
            throw new IllegalArgumentException("등록되어 있지 않은 반려동물입니다.");
        }

        petRepository.delete(optPet.get());

        return optPet.get().getPetName() + " 삭제되었습니다.";
    }

}
