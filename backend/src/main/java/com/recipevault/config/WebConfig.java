package com.recipevault.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                // .addResourceLocations("file:uploads/")
                .addResourceLocations("file:/app/uploads/") // upload location for docker
                .setCachePeriod(0); // Prevent caching issues
    }
}

