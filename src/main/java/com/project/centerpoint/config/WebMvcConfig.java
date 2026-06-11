package com.project.centerpoint.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir);
        String uploadAbsolutePath = uploadPath.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + uploadAbsolutePath + "/");
                
        Path systemUploadPath = Paths.get("uploads/system");
        String systemUploadAbsolutePath = systemUploadPath.toFile().getAbsolutePath();
        registry.addResourceHandler("/uploads/system/**")
                .addResourceLocations("file:" + systemUploadAbsolutePath + "/");
    }
}
