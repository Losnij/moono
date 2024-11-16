package com.example.moono.dto;

import com.example.moono.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {

    private Long id;
    private String memberID;
    private String password;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .memberID(member.getMemberID())
                .password(member.getPassword())
                .build();
    }
}
