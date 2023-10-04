package com.project.bbibbi.auth.oauth.oauthUserInfo;

import com.project.bbibbi.global.entity.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private String email;
    private Role role;
    private Long memberId;
    private String profileImg;
    private String nickname;


    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role, Long memberId, String profileImg, String nickname) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
        this.memberId = memberId;
        this.profileImg = profileImg;
        this.nickname = nickname;

    }
}

