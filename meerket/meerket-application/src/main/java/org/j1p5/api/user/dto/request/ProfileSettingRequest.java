package org.j1p5.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfileSettingRequest(
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
                @Pattern(
                        regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,15}$",
                        message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
                String name) {}
