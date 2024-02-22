package com.project.cs.common.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * [이름 규칙]
     * 도메인_에러_에러내용
     */

    //3xxx : 서버 관련 응답
    INPUT_ERROR_VALIDATION(3000, "입력값 유효성 검증 실패입니다."),

    //4xxx : 서버 관련 응답
    SERVER_ERROR_UNDEFINED(4000, "알수 없는 에러입니다. 개발자에게 문의바랍니다."),
    SERVER_ERROR_JSON_PARSE(4001, "잘못된 형식의 json body 입니다. 개발자에게 문의바랍니다."),

    //5xxx : 도메인 관련 응답
    //50xx : Member
    MEMBER_NOT_FOUND(5000, "회원을 찾지 못했습니다."),
    MEMBER_ACCESS_DENY(5001, "접근이 거부되었습니다."),
    MEMBER_INVALID_TOKEN(5002, "토큰이 올바르지 않습니다."),
    MEMBER_PASSWORD_MISMATCH(5003, "비밀번호가 올바르지 않습니다."),
    MEMBER_DUPLICATED_EMAIL(5004, "중복된 이메일입니다."),
    MEMBER_EXPIRED_TOKEN(5005, "토큰이 만료되었습니다."),

    //51xx : Image
    IMAGE_NOT_FOUND(5100, "이미지가 존재하지 않습니다.");

    private final int code;
    private final String message;
}