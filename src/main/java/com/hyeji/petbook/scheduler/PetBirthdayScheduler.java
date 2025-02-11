package com.hyeji.petbook.scheduler;

import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.repository.PetRepository;
import com.hyeji.petbook.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PetBirthdayScheduler {

    private final PetRepository petRepository;
    private final NotificationService notificationService;

    // 매일 아침 10시에 실행
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendBirthdayAlerts() {
        LocalDate today = LocalDate.now();

        // 오늘 날짜에 생일인 반려동물 리스트
        List<Pet> todayBirthdayPets = petRepository.findByBirthday(today);

        // 알림 보내기
        for (Pet todayBirthdayPet : todayBirthdayPets) {
            User user = todayBirthdayPet.getUser();
            notificationService.sendBirthdayAlert(user, todayBirthdayPet);
        }
    }
}
