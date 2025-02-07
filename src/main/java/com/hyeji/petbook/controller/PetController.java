package com.hyeji.petbook.controller;

import com.hyeji.petbook.dto.PetDTO;
import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.repository.PetRepository;
import com.hyeji.petbook.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetRepository petRepository;

    // 반려동물 등록
    @PostMapping("/add-pet")
    public ResponseEntity<String> addPet(@RequestBody PetDTO petDTO) {
        String message = petService.addPet(petDTO);
        return ResponseEntity.ok(message);
    }

    // 모든 반려동물 조회
    @GetMapping("/pet")
    public ResponseEntity<List<Pet>> getPets() {
        List<Pet> pets = petService.getPets();
        return ResponseEntity.ok(pets);
    }

    // 반려동물 정보 수정
    @PutMapping("/update-pet/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable(name = "id") Long id, @RequestBody PetDTO petDTO) {
        return ResponseEntity.ok(petService.updatePet(id, petDTO));
    }

    // 반려동물 삭제
    @DeleteMapping("/delete-pet/{id}")
    public ResponseEntity<String> deletePet(@PathVariable(name = "id") Long id) {
        String message = petService.deletePet(id);
        return ResponseEntity.ok(message);
    }
}
