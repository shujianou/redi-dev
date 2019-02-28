package com.redimybase.common.util;

import com.redimybase.common.enums.ErrorEnum;
import com.redimybase.common.util.support.Property;
import com.redimybase.common.exceptions.BizException;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/15 15:01
 */
public final class BizAssert<B> {

    private B bean;

    private BizAssert(B bean) {
        this.bean = bean;
    }

    public static <B> BizAssert<B> of(B value) {
        return new BizAssert<>(value);
    }

    public BizAssert<B> ifNullThrow(ErrorEnum errorEnum) {
        if (ObjectUtils.isEmpty(bean)) {
            throw BizException.me(errorEnum);
        } else {
            return this;
        }
    }

    /**
     * 非空校验
     *
     * @param fields fields
     * @return com.redimybase.framework.utils.BizAssert<B>
     * @author Charlie
     * @date 2019/2/15 16:54
     */
    @SafeVarargs
    public final BizAssert<B> noNull(Property<B, ?>... fields) {
        Arrays.stream(fields)
                .forEach(field -> {
                    if (ObjectUtils.isEmpty(field.apply(bean))) {
                        throw BizException.me(
                                ErrorEnum.请求参数$不能为空.getCode(),
                                String.format(ErrorEnum.请求参数$不能为空.getDescription(), ReflectUtil.fieldName(field)));
                    }
                });
        return this;
    }


    /**
     * 状态判断
     *
     * @param mustTrue mustTrue
     * @param bizErrorEnum bizErrorEnum
     * @return com.redimybase.framework.utils.BizAssert<B>
     * @author Charlie
     * @date 2019/2/15 16:54
     */
    public BizAssert<B> mustTrue(Predicate<B> mustTrue, ErrorEnum bizErrorEnum) {
        if (mustTrue.negate().test(bean)) {
            throw BizException.me(bizErrorEnum);
        }
        return this;
    }


    /**
     * 状态判断
     *
     * @param mustTrue mustTrue
     * @param errorTip 提示语句
     * @return com.redimybase.framework.utils.BizAssert<B>
     * @author Charlie
     * @date 2019/2/15 16:54
     */
    public BizAssert<B> mustTrue(Predicate<B> mustTrue, String errorTip) {
        if (mustTrue.negate().test(bean)) {
            throw BizException.me(BizException.DEFAULT_ERROR, errorTip);
        }
        return this;
    }


    /**
     * 类型转化
     *
     * @param mapper mapper
     * @return com.redimybase.common.util.BizAssert<R>
     * @author Charlie
     * @date 2019/2/15 19:49
     */
    public <R> BizAssert<R> mapTo(Function<? super B, ? extends R> mapper){
        return BizAssert.of(mapper.apply(bean));
    }

    /**
     * 获取类
     *
     * @return B
     * @author Charlie
     * @date 2019/2/15 19:50
     */
    public B getData() {
        return bean;
    }
}
