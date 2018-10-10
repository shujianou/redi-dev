package com.redimybase.framework.converter;

import com.redimybase.common.util.DateUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 日期格式转换器
 * Created by Irany(欧书剑) 2017/7/19 0019 20:22
 */
public class DateConverter implements Converter<String,Date> {

    public static final String DATETIME_PATTERN="yyyy-MM-DD HH:mm:dd:ss";

    public static final String DATE_PATTERN="yyyy-MM-dd";

    public static final String MONTH_PATTERN="yyyy-MM";

    public Date convert(String s) {
        if (StringUtils.hasText(s)){
            try{
                return DateUtils.toDate(s);
            }catch (Exception e){
                throw new IllegalArgumentException("Could not parse date"+e.getMessage(),e);
            }
        }
        return null;
    }
}
