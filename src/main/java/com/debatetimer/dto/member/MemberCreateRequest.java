package com.debatetimer.dto.member;

import com.debatetimer.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberCreateRequest(
        @Schema(description = "멤버 닉네임", example = "콜리")
        String nickname
) {

    public Member toMember() {
        return new Member(nickname);
    }
}