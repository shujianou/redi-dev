package com.redimybase.framework.converter;

import com.redimybase.common.util.DateUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;import java.util.Date;

/**
 * 日期格式转换器
 * Created by Irany(欧书剑) 2017/7/19 0019 20:22
 */
@Component
public class DateConverter implements Converter<String,Date> {
    @Override
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
