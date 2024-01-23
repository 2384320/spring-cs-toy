package com.project.cs.member.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest {

    private String nickname;

    private String email;

    private String password;
}
