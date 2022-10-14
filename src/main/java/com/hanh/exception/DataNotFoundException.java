package com.hanh.exception;

public class DataNotFoundException extends CustomException{
    private static final long serialVersionUID = -211667454324594101L;

    public DataNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}