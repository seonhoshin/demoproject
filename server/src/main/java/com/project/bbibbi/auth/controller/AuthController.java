package com.project.bbibbi.auth.controller;

import com.project.bbibbi.auth.controller.dto.AuthEmailCheckApiRequest;
import com.project.bbibbi.auth.controller.dto.AuthEmailSendPasswordApiRequest;
import com.project.bbibbi.auth.controller.dto.OauthJoinApiRequest;
import com.project.bbibbi.auth.controller.dto.AuthEmailSendApiRequest;
import com.project.bbibbi.auth.jwt.dto.Token;
import com.project.bbibbi.domain.member.controller.dto.MemberCreateApiRequest;
import com.project.bbibbi.domain.member.controller.dto.MemberFindPasswordApiRequest;
import com.project.bbibbi.domain.member.service.MemberService;
import com.project.bbibbi.global.response.ApiSingleResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;




@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;


    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberCreateApiRequest request) {

        Long memberId = memberService.signup(request.toCreateServiceRequest());

        URI uri = URI.create("/members" + memberId);

        return ResponseEntity.created(uri).build();
    }




    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

    @GetMapping("/oauth/code/kakao")
     public String kakaoTest() {
            return "카카오 코드 완료";
    }

    private HttpHeaders getHttpHeaders(Token token) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("Authorization", List.of(BEARER + token.getAccessToken()));
        map.put("Refresh", List.of(BEARER + token.getRefreshToken()));
        HttpHeaders tokenHeader = new HttpHeaders(map);
        return tokenHeader;
    }

    @PatchMapping("/password")
    private ResponseEntity<Void> findPassword(@RequestBody @Valid MemberFindPasswordApiRequest request) {
        return null;

    }


    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid AuthEmailSendApiRequest request) {

        memberService.sendSignupEmail(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/check")
    public ResponseEntity<ApiSingleResponse<Boolean>> confirmEmail(
            @RequestBody @Valid AuthEmailCheckApiRequest request) {

        boolean result = memberService.checkCode(request.getEmail(), request.getCode());

        return ResponseEntity.ok(ApiSingleResponse.ok(result));
    }

    @PostMapping("/email/password")
    public ResponseEntity<Void> sendEmailForPassword(@RequestBody @Valid AuthEmailSendPasswordApiRequest request) {

        memberService.sendFindPasswordCodeToEmail(request.getEmail());

        return ResponseEntity.noContent().build();
    }
}

