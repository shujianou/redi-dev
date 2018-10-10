package com.redimybase.framework.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常
 * Created by Irany 2018/7/30 10:23
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private Integer code;
    private String msg;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
