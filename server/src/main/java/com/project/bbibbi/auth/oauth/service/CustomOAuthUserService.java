package com.project.bbibbi.auth.oauth.service;

import com.project.bbibbi.auth.oauth.dto.OAuthAttributes;
import com.project.bbibbi.auth.oauth.oauthUserInfo.CustomOAuth2User;
import com.project.bbibbi.domain.member.entity.Member;
import com.project.bbibbi.domain.member.repository.MemberRepository;
import com.project.bbibbi.global.entity.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest memberRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(memberRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(memberRequest);

        /**
         * memberRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        String registrationId = memberRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = memberRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes, socialType); //

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getEmail(),
                createdMember.getRole(),
                createdMember.getMemberId(),
                createdMember.getProfileImg(),
                createdMember.getNickname()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if (NAVER.equalsIgnoreCase(registrationId)) {
            return SocialType.NAVER;
        } else if (KAKAO.equalsIgnoreCase(registrationId)) {
            return SocialType.KAKAO;
        } else {
            throw new IllegalArgumentException("지원하지 않는 registrationId입니다: " + registrationId);
        }


    }

    /**
     * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
     * 만약 찾은 회원이 있다면, 그대로 반환하고 없다면 saveMember()를 호출하여 회원을 저장한다.
     */
    private Member getMember(OAuthAttributes attributes, SocialType socialType) {
        Member findMember = memberRepository.findBySocialTypeAndSocialId(socialType,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if(findMember == null) {
            return saveMember(attributes, socialType);
        }
        return findMember;
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
     * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
     */
    private Member saveMember(OAuthAttributes attributes, SocialType socialType) {
        Member createdMember = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return memberRepository.save(createdMember);
    }
}
