package com.hengda.frame.fileloader.exception;
/**
 * Created by shiwei on 2017/1/10.
 */
public class Exceptions {

    public static void illegalArgument(String msg, Object... params) {
        throw new IllegalArgumentException(String.format(msg, params));
    }

}