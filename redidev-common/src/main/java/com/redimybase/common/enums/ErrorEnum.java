package com.redimybase.common.enums;

/**
 * 业务异常信息
 *
 * @author Charlie
 * @version V1.0
 * @date 2019/2/15 14:51
 */
public enum ErrorEnum {

    /**
     * 无权限
     */
    无权限(403, "无权限"),
    请求参数不能为空(1001, "请求参数不能为空"),
    请求参数$不能为空(1001, "请求参数(%s)不能为空"),
    未找到实体类(1100, "未找到实体类"),



    /**
     * 系统繁忙
     */
    系统繁忙(201, "系统繁忙,请稍后再试");

    private int code;
    private String description;

    ErrorEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

}
