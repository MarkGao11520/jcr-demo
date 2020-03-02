package com.example.jcrdemo.exception;

/**
 * @author gaowenfeng02
 * @desc
 * @date 2020-03-02
 */
public class DefaultException extends RuntimeException{

    public DefaultException(String message,Exception e){
        super(message,e);
    }

    public DefaultException(String message){
        super(message);
    }

    public DefaultException(Exception e){
        super(e);
    }
}
