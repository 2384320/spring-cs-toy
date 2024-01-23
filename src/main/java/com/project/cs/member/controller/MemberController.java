package com.project.cs.member.controller;

import com.project.cs.member.domain.dto.SignRequest;
import com.project.cs.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/member/signup")
    public ResponseEntity<Object> signup(@RequestBody SignRequest request) {
        return memberService.signup(request);
    }

    @PostMapping("/api/member/login")
    public ResponseEntity<Object> login(@RequestBody SignRequest request) {
        return memberService.login(request);
    }

    @GetMapping("/member/get")
    public ResponseEntity<Object> getMember(@RequestParam String email) {
        return memberService.getMember(email);
    }

    @GetMapping("/admin/get")
    public ResponseEntity<Object> getAdmin(@RequestParam String email) {
        return memberService.getMember(email);
    }
}