package com.wwdj.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Vim 2018/12/26 18:35
 *
 * @author Vim
 */
@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        //单个文件大小限制
        factory.setMaxFileSize("500MB");
        //总上传数据量大小限制
        factory.setMaxRequestSize("500MB");
        return factory.createMultipartConfig();
    }
}
