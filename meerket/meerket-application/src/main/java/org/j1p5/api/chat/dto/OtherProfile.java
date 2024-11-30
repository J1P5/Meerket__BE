package org.j1p5.api.chat.dto;

import java.util.Objects;

public record OtherProfile(
        String otherNickname,
        String otherProfileImage,
        Long otherUserId
) {

    public OtherProfile{
        Objects.requireNonNull(otherNickname, "닉네임은 필수입니다.");
        Objects.requireNonNull(otherUserId, "id는 필수입니다.");
    }
}
