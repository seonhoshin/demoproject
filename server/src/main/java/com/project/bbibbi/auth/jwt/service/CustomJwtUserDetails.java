package com.project.bbibbi.auth.jwt.service;

import com.project.bbibbi.global.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomJwtUserDetails extends User {

    private final Long memberId;
    private final Role role;
    private final boolean checkUser;
    private final String profileImg;
    private final String nickname;



    public CustomJwtUserDetails(Long memberId, String email, String password,
                                Role role, boolean checkUser, String profileImg, String nickname) {
        super(email, password, Collections.singleton(new SimpleGrantedAuthority(role.getKey())));
        this.memberId = memberId;
        this.role = role;
        this.checkUser = checkUser;
        this.profileImg = profileImg;
        this.nickname = nickname;

    }
}







