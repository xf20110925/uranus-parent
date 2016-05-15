package com.ptb.uranus.spider.common.exception;


import com.ptb.utils.exception.PTBException;

/**
 * Created by eric on 16/1/12.<br>
 *
 * spider的异常类,所有SPIDER的用户自定义异常会由此类抛去<br>
 */
public class SpiderException extends PTBException {

    /**
     * Instantiates a new Ptb clawler exception.
     *
     * @param message the message
     */
    public SpiderException(String message) {
        super(message);
    }
}
