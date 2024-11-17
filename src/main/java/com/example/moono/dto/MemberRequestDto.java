package com.example.moono.dto;

import com.example.moono.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRequestDto {

    private String memberID;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .memberID(this.memberID)
                .password(this.password)
                .build();
    }
}
