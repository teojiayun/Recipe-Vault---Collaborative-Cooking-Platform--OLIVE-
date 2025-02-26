// package com.recipevault.config;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig {
//     private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

//     @Bean
//     public WebMvcConfigurer corsConfigurer() {
//         logger.info("CorsConfig is being loaded... ");
//         return new WebMvcConfigurer() {
//             @Override
//             public void addCorsMappings(CorsRegistry registry) {
//                 logger.info("CORS mappings added: Allowing http://localhost:5173");
//                 registry.addMapping("/**")
//                         .allowedOrigins("http://localhost:5173")
//                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                         .allowedHeaders("*")
//                         .allowCredentials(true);
//             }
//         };
//     }
// }
