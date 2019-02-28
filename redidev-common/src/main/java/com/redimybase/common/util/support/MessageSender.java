package com.redimybase.common.util.support;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 消息接口
 *
 * @author Charlie
 * @version V1.0
 * @date 2019/2/19 21:10
 */
public interface MessageSender<M> extends Consumer<M> {

    /**
     * 获取内容
     *
     * @author Charlie
     * @date 2019/2/19 21:17
     */
    void send(M msg);

    @Override
    default void accept(M m) {
        send(m);
    }

    /**
     * 销毁
     *
     * @author Charlie
     * @date 2019/2/19 21:17
     */
    void destroy();

    /**
     * 初始化
     *
     * @author Charlie
     * @date 2019/2/19 21:17
     */
    void init();
}
