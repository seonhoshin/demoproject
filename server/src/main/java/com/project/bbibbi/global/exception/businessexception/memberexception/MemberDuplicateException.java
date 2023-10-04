package com.project.bbibbi.global.exception.businessexception.memberexception;

import org.springframework.http.HttpStatus;

public class MemberDuplicateException extends MemberException{

    public static final String MESSAGE = "이미 존재하는 회원입니다.";
    public static final String CODE = "MEMBER-410";

    public MemberDuplicateException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}