package com.hyeji.petbook.service;

import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendBirthdayAlert(User user, Pet pet) {
        String userEmail = user.getEmail();
        LocalDate petBirthday = pet.getBirthday();
        String petName = pet.getPetName();

        // 생일의 월과 일 추출
        int month = petBirthday.getMonthValue();
        int day = petBirthday.getDayOfMonth();

        // 이메일 메시지 객체 생성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);  // 수신자 이메일
        message.setSubject("🎉 생일 알림");  // 이메일 제목

        // 이메일 내용 작성
        String emailContent = String.format("%d월 %d일은 %s의 생일입니다. 생일을 진심으로 축하합니다! 🎂", month, day, petName);
        message.setText(emailContent);  // 이메일 내용

        // 이메일 전송
        javaMailSender.send(message);
    }

}
