package com.project.cs.member.service;

import com.project.cs.exception.ErrorCode;
import com.project.cs.common.exception.ResultData;
import com.project.cs.common.exception.SVCException;
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

        ResultData result = new ResultData();
        result.setResult(
                SignResponse.createSignResponse(
                        foundMember,
                        jwtProvider.createToken(foundMember.getEmail(), foundMember.getRole())
                )
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> signup(SignRequest request) {
        if (memberRepository.existsByEmail(request.getEmail()))
            throw new SVCException(ErrorCode.MEMBER_DUPLICATED_EMAIL);

        Member member = Member.createMember(request, passwordEncoder.encode(request.getPassword()));
        ResultData result = new ResultData();
        result.setResult(memberRepository.save(member).getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Object> getMember(String email) {
        Member foundMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new SVCException(ErrorCode.MEMBER_NOT_FOUND));
        ResultData result = new ResultData();
        result.setResult(foundMember);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}