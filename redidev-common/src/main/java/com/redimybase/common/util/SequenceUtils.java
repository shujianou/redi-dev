package com.redimybase.common.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 唯一单号生成
 * Created by Vim 2019/1/18 13:11
 *
 * @author Vim
 */
public class SequenceUtils {

    /**
     * 生成序列号(时间+随机3位数)
     */
    public static String getSequence() {
        String dateStr = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        String intStr = String.valueOf(RandomUtils.nextInt(100, 999));
        return dateStr + intStr;
    }


    /**
     * 生成序列号(时间+干扰码+随机3位数)
     */
    public static String getSequenceInStr(String defaultStr) {
        String dateStr = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        String intStr = String.valueOf(RandomUtils.nextInt(100, 999));
        return defaultStr + dateStr + intStr;
    }

}
