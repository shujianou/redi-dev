package com.redimybase.framework.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.redimybase.common.enums.ErrorEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回统一Bean
 *
 * @author Charlie, Irany 2018/5/13 23:50
 */
@Data
public class R<E> implements Serializable {

    private static final int SUCCESS = 200;
    private static final int FAILED = ErrorEnum.系统繁忙.getCode();


    private int code = SUCCESS;

    private String msg;

    @JsonSerialize( include = JsonSerialize.Inclusion.NON_NULL )
    private E data;


    private R() {
    }

    public R(E data) {
        this.data = data;
    }
    private R(int code) {
        this();
        this.code = code;
    }

    private R(int code, String msg) {
        this(code);
        this.msg = msg;
    }

    private R(Integer code, String msg, E data) {
        this(code, msg);
        this.data = data;
    }


    public static <T> R<T> ok() {
        return new R<>();
    }

    public static R fail() {
        return new R<>(FAILED);
    }

    public static R fail(String msg) {
        return new R<>(R.FAILED, msg);
    }

    @Deprecated
    public static R custom(Integer code, String msg) {
        return new R<>(code, msg);
    }

    public static R custom(ErrorEnum errorEnum) {
        return new R<>(errorEnum.getCode(), errorEnum.getDescription());
    }

    public static<T> R ok(T data) {
        return new R<>(SUCCESS, null, data);
    }

    public R<E> setData(E data) {
        this.data = data;
        return this;
    }
}
