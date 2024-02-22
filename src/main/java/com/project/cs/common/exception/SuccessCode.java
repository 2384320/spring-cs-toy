package com.project.cs.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    /**
     * [이름 규칙]
     * 도메인_에러_에러내용
     *
     * [성공 코드]
     * 2xxxx : 성공 응답
     */

    TEST_ETC(200, "성공 메시지");

    private final int code;
    private final String msg;
}