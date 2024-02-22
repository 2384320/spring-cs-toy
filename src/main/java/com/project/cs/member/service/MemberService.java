package com.project.cs.member.service;

import com.project.cs.common.exception.SVCException;
import com.project.cs.common.response.CommonResponse;
import com.project.cs.common.response.code.ErrorCode;
import com.project.cs.common.response.code.SuccessCode;
import com.project.cs.member.domain.dto.SignRequest;
import com.project.cs.member.domain.dto.SignResponse;
import com.project.cs.member.domain.entity.Member;
import com.project.cs.member.repository.MemberRepository;
import com.project.cs.common.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public ResponseEntity<Object> login(SignRequest request) {
        Member foundMember = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new SVCException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), foundMember.getPassword()))
            throw new SVCException(ErrorCode.MEMBER_PASSWORD_MISMATCH);

        return new ResponseEntity<>(
                CommonResponse.success(
                        SuccessCode.MEMBER_SUCCESS_LOGIN,
                        SignResponse.createSignResponse(
                                foundMember,
                                jwtProvider.createToken(foundMember.getEmail(), foundMember.getRole())
                        )
                ),
                HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<Object> signup(SignRequest request) {
        if (memberRepository.existsByEmail(request.getEmail()))
            throw new SVCException(ErrorCode.MEMBER_DUPLICATED_EMAIL);

        Member member = Member.createMember(request, passwordEncoder.encode(request.getPassword()));

        return new ResponseEntity<>(
                CommonResponse.success(
                        SuccessCode.MEMBER_SUCCESS_SIGNUP,
                        memberRepository.save(member).getId()
                ),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Object> getMember(String email) {
        Member foundMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new SVCException(ErrorCode.MEMBER_NOT_FOUND));

        return new ResponseEntity<>(
                CommonResponse.success(
                        SuccessCode.MEMBER_SUCCESS_FOUND_MEMBER,
                        foundMember
                ),
                HttpStatus.OK
        );
    }
}