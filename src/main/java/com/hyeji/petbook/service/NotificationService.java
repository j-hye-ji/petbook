package com.hyeji.petbook.service;

import com.hyeji.petbook.entity.Pet;
import com.hyeji.petbook.entity.User;

/* 인터페이스 자체는 Spring Bean으로 등록되지 않기 때문에 별도의 어노테이션을 붙이지 않아도 됨. */
public interface NotificationService {

    // 반려동물 생일 알림
    void sendBirthdayAlert(User user, Pet pet);
}
