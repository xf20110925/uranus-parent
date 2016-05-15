package com.ptb.uranus.sdk.exception;

import org.apache.commons.configuration.ConfigurationException;

/**
 * Created by eric on 16/4/23.
 */
public class ConfigureFileException extends RuntimeException {
    public ConfigureFileException(Throwable e) {
        super("配置文件不正确",e);
    }
}
