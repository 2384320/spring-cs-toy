package com.project.cs.common.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    /**
     * [이름 규칙]
     * 도메인_성공_성공내용
     */

    //2xxxx : 성공 응답
    // 20xx : Member
    MEMBER_SUCCESS_SIGNUP(2000, "회원가입이 완료되었습니다."),
    MEMBER_SUCCESS_LOGIN(2001, " 로그인이 완료되었습니다."),
    MEMBER_SUCCESS_FOUND_MEMBER(2002, "회원을 찾았습니다."),

    //21xx : Image
    IMAGE_SUCCESS_SAVE(2100, "이미지를 저장하였습니다."),
    IMAGE_SUCCESS_DOWNLOAD(2101, "이미지를 불러왔습니다.");

    private final int code;
    private final String message;
}