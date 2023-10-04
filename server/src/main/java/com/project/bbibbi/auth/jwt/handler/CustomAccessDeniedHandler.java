package com.project.bbibbi.auth.jwt.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String errorMessage = "access 토큰이 만료되었거나 찾을 수 없습니다. RefreshToken을 header에 보내주십시오.(deniedhandler)";

        response.getWriter().write(errorMessage);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 에러 코드 설정
        log.info("토큰만료로 403 전달하는 로직 메시지 : {}",accessDeniedException.getMessage());
    }
}
