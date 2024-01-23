package com.project.cs.security;

import com.project.cs.exception.ErrorCode;
import com.project.cs.exception.SVCException;
import com.project.cs.member.domain.entity.Member;
import com.project.cs.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new SVCException(ErrorCode.MEMBER_NOT_FOUND));
        return CustomUserDetails.createCustomUserDetails(member);
    }
}
