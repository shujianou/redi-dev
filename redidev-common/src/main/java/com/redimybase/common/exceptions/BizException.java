package com.redimybase.common.exceptions;

import com.redimybase.common.enums.ErrorEnum;
import lombok.Data;

/**
 * 业务异常
 * @author Irany,Chalie 2018/7/30 10:23
 */
@Data
public class BizException extends RuntimeException {

    public static final int DEFAULT_ERROR = -1;

    private int code;
    private String msg;

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public static BizException me(int code, String msg) {
        return new BizException(code, msg);
    }

    public static BizException me(ErrorEnum bze) {
        return new BizException(bze.getCode(), bze.getDescription());
    }


}
