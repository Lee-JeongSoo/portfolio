package com.example.portfolio.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 연락 폼 요청.
 * website 는 사람에게 보이지 않는 스팸 방지용(honeypot) 필드로, 값이 들어오면 봇으로 간주한다.
 */
public record ContactRequest(
        @NotBlank(message = "이름을 입력해 주세요.")
        @Size(max = 50, message = "이름은 50자 이하로 입력해 주세요.")
        String name,

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 100자 이하로 입력해 주세요.")
        String email,

        @NotBlank(message = "메시지를 입력해 주세요.")
        @Size(min = 10, max = 2000, message = "메시지는 10자 이상 2000자 이하로 입력해 주세요.")
        String message,

        String website
) {
    public boolean isLikelySpam() {
        return website != null && !website.isBlank();
    }
}
