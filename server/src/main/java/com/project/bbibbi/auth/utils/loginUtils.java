package com.project.bbibbi.auth.utils;

import com.project.bbibbi.auth.jwt.service.CustomJwtUserDetails;
import com.project.bbibbi.auth.oauth.oauthUserInfo.CustomOAuth2User;
import com.project.bbibbi.global.exception.businessexception.memberexception.MemberNotFoundException;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class loginUtils {

    public static Long getLoginId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof CustomJwtUserDetails) {
                CustomJwtUserDetails userDetails = (CustomJwtUserDetails) authentication.getPrincipal();
                return userDetails.getMemberId();
            } else if (authentication.getPrincipal() instanceof CustomOAuth2User) {
                CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
                return oauth2User.getMemberId();
            }
        }
        return null;


    }
}

