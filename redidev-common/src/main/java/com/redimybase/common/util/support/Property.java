package com.redimybase.common.util.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/15 16:44
 */
public interface Property<T, R> extends Function<T, R>, Serializable {

}

