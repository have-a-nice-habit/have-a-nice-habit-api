package com.hanh.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@Getter
public class UserRequestDto {
    //닉네임은 최소 3자, 최대 10자
    @Size(min = 3, max=10)
    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;

    @Email
    @NotEmpty(message = "이메일은 필수항목입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;
}
