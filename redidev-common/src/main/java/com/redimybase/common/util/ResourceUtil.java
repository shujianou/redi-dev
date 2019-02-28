package com.redimybase.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/18 14:27
 */
public class ResourceUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);

    /**
     * 资源关闭
     *
     * @param closeables closeables
     * @author Charlie
     * @date 2019/2/18 14:31
     */
    public static void close(AutoCloseable... closeables) {
        if (closeables != null) {
            for (int i = 0; i < closeables.length; i++) {
                AutoCloseable c = closeables[i];
                if (c != null) {
                    try {
                        c.close();
                    } catch (Exception e) {
                        logger.error("资源关闭失败");
                        //ignore
                    }
                }
            }
        }
    }

}
