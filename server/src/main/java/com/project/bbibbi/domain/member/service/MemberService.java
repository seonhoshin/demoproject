package com.project.bbibbi.domain.member.service;

import com.project.bbibbi.auth.utils.loginUtils;
import com.project.bbibbi.domain.follow.repository.FollowRepository;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import com.project.bbibbi.domain.member.service.dto.request.MemberCreateServiceRequest;
import com.project.bbibbi.domain.member.service.dto.request.MemberUpdatePasswordApiServiceRequest;
import com.project.bbibbi.domain.member.service.dto.request.MemberUpdateServiceRequest;
import com.project.bbibbi.domain.member.service.dto.response.MemberResponse;
import com.project.bbibbi.global.exception.businessexception.memberexception.*;
import com.project.bbibbi.global.mail.service.MailService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final MailService mailService;
    private final FollowRepository followRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         MailService mailService,
                         FollowRepository followRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
        this.mailService = mailService;
    }

    public Long signup(MemberCreateServiceRequest request) {

        checkDuplicateEmail(request.getEmail());
        checkDuplicateNickname(request.getNickname());

        Member member = createMember(request);

        return memberRepository.save(member).getMemberId();
    }

    public MemberResponse getMember(Long memberId) {

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.orElseThrow(MemberNotFoundException::new);

         return MemberResponse.MemberInfoResponse(member);
    }

    private Member createMember(MemberCreateServiceRequest request) {
        return Member.createMember(
                request.getEmail(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword())
        );
    }

    public void updateMember(MemberUpdateServiceRequest request) {

        Long loginMemberId = loginUtils.getLoginId();
        if(loginMemberId == null) {
            throw new MemberNotLoginException();
        }

        comparisonMembers(loginMemberId, request.getMemberId());

        Member member = ExistingMember(loginMemberId);

        checkDuplicateNickname(request.getNickname(), member.getNickname());

        updateMember(member, request);

    }

    private void updateMember(Member member, MemberUpdateServiceRequest request) {

        member.updateMyInfo(
                request.getNickname(),
                request.getMyIntro(),
                request.getProfileImg());
    }

    public void updatePassword(MemberUpdatePasswordApiServiceRequest request) {


        Long loginMemberId = loginUtils.getLoginId();
        if(loginMemberId == null) {
            throw new MemberNotLoginException();
        }

                Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());
                Member member = optionalMember.orElseThrow(MemberNotFoundException::new);

            comparisonMembers(loginMemberId, request.getMemberId());

                checkPassword(request.getPassword(), member.getPassword());

                member.updatePassword(
                        passwordEncoder.encode(request.getNewPassword())
                );

    }

    public void deleteMember(Long memberId) {

        Long loginMemberId = loginUtils.getLoginId();
        if(loginMemberId == null) {
            throw new MemberNotLoginException();
        }

        comparisonMembers(loginMemberId, memberId);
        Member member = ExistingMember(loginMemberId);

        try {

            followRepository.deleteByMemberId(member.getMemberId());
            followRepository.deleteByFromMemberId(member.getMemberId());

            memberRepository.deleteById(member.getMemberId());
        } catch (EmptyResultDataAccessException ex) {
            throw new MemberNotFoundException();
        }

    }


    public void sendSignupEmail(String email) {

        checkcheckExistenceEmail(email);

        String code = mailService.sendLoginEmail(email);

    }

    public boolean checkCode(String email, String code) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String storedCode = member.getCheckCode();

            if (storedCode.equals(code)) {
                member.updateCheckUser(true);
                System.out.println("true");
                return true;
            } else {
                System.out.println("false");
                return false;
            }
        } else {
            throw new MemberNotFoundException();
        }
    }
    public void sendFindPasswordCodeToEmail(String email) {

        checkcheckExistenceEmail(email);

        mailService.sendPasswordEmail(email);

    }




    private void checkPassword(String password, String savedPassword) {
        if (!passwordEncoder.matches(password, savedPassword)) {
            throw new MemberPasswordException();
        }
    }


    private void checkDuplicateEmail(String email) {

        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member != null) {
            String memberEmail = member.getEmail();
            if (memberEmail != null && memberEmail.equals(email)) {
                throw new MemberlExistEmailException();
            }
        }
    }

    private void checkDuplicateNickname(String nickname) {

        Member member = memberRepository.findByNickname(nickname).orElse(null);
        if (member != null) {
            String memberNickname = member.getNickname();
            if (memberNickname != null && memberNickname.equals(nickname)) {
                throw new MemberExistNicknameException();}
        }
    }

    private void checkDuplicateNickname(String newNickname, String currentNickname) {
        if (newNickname != null && !newNickname.equals(currentNickname)) {
            Member member = memberRepository.findByNickname(newNickname).orElse(null);
            if (member != null) {
                throw new MemberExistNicknameException();
            }
        }
    }

    private void checkcheckExistenceEmail(String email) {

        Member member = memberRepository.findByEmail(email).orElse(null);

        if (!member.getEmail().equals(email)) {
            throw new MemberNotFoundException();
        }
    }

    private Member ExistingMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return member;
    }

    private void comparisonMembers(Long loginMemberId, Long memberId) {
        if (!loginMemberId.equals(memberId)) {
            throw new MemberAccessDeniedException();
        }
    }

    private boolean checkEmailCode(String email, String code) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String storedCode = member.getCheckCode();

            if (storedCode.equals(code)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new MemberNotFoundException();
        }
    }
}
