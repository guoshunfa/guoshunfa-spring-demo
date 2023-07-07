package com.panda.base;

/**
 * @ClassName UtilsException
 * @Description 请描述类的业务用途
 * @Author guoshunfa
 * @Date 2021/11/23 下午1:35
 * @Version 1.0
 **/
public class PandaUtilsException extends Exception {

    static final long serialVersionUID = -7034897190745766939L;
    
    public PandaUtilsException(String message) {
        super(message);
    }

    public PandaUtilsException() {
        super();
    }
}
