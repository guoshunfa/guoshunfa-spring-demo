package com.panda.base.exception.custom;

public class PandaException extends RuntimeException{

    public PandaException(String message) {
        super(message);
    }

    public PandaException(Throwable cause) {
        super(cause);
    }

}
