package com.hyeji.petbook.dto;

import com.hyeji.petbook.type.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "사용자 이름은 필수 입력 항목입니다.")
    private String userName;

    private UserRole userRole = UserRole.USER;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    private String phoneNumber;
}
