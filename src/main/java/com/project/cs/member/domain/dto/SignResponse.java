package com.project.cs.member.domain.dto;

import com.project.cs.member.domain.entity.Member;
import com.project.cs.member.domain.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignResponse {

    private Long id;

    private String nickname;

    private String email;

    private MemberRole role;

    private String token;

    public static SignResponse createSignResponse(Member member, String token) {
        return SignResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .role(member.getRole())
                .token(token)
                .build();
    }
}
