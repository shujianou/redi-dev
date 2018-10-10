package com.redimybase.framework.aop.exception;

import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 请求拦截异常
 * Created by Irany 2018/7/30 10:21
 */
@RestControllerAdvice       //增强拦截
@Order(-1)  //AOP排序
@Slf4j
public class RequestAop {

    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> business(BusinessException e) {
        //TODO 这里写入操作日志
        //事务回滚
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }catch (NoTransactionException ex){
            log.warn("no transaction");
        }
        log.error("business error:", e);
        return new R<>(R.失败, e.getMessage());
    }

    /**
     * 无权访问异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> unAuth(UndeclaredThrowableException e) {

        //TODO 这里写入操作日志

        log.error("auth error:", e);
        return new R<>(R.无权限, e.getMessage());
    }

    /**
     * 无权调用接口异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> unApi(AuthorizationException e) {

        //TODO 这里写入操作日志,目前省略

        log.error("auth error:", e);
        return new R<>(R.无权限, e.getMessage());
    }

}
