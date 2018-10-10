package com.redimybase.framework.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 接口返回统一Bean
 * Created by Irany 2018/5/13 23:50
 */
@Getter
@Setter
public class R<E> implements Serializable {

    public R() {
    }

    public R(E data) {
        this.data = data;
    }

    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(Integer code, String msg, E data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(Throwable throwable) {
        this.code = 失败;
        this.msg = throwable.toString();
    }

    public static R ok(){
        return new R<>(R.成功,"操作成功");
    }

    public static R fail(){
        return new R<>(R.失败,"操作失败");
    }
    public static R ok(String msg){
        return new R<>(R.成功,msg);
    }

    public static R fail(String msg){
        return new R<>(R.失败,msg);
    }

    public static R custom(Integer code,String msg){
        return new R<>(code,msg);
    }

    public static R custom(Integer code,String msg,Object data){
        return new R<>(R.成功,"操作成功",data);
    }




    @JsonSerialize(include =JsonSerialize.Inclusion.NON_NULL)
    private E data;

    private Integer code = 成功;

    private String msg = "操作成功";


    public static final int 成功 = 200;

    public static final int 失败 = 201;

    public static final int 无权限 = 403;
    public static final int 登录成功 = 666;

}
