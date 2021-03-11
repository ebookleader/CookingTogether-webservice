package com.jeongeun.project.springboot.service;

import com.jeongeun.project.springboot.domain.Member;
import com.jeongeun.project.springboot.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found member"));
    }
}
