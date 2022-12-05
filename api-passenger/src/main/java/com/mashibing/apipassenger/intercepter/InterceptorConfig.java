package com.mashibing.apipassenger.intercepter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 49142
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public JwtIntercepter jwtIntercepter() {
        return new JwtIntercepter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtIntercepter())
                .addPathPatterns("/**")
                .excludePathPatterns("/noauthTest")
//                .excludePathPatterns("/authTest")
                .excludePathPatterns("/verification-code")
                .excludePathPatterns("/verification-code-check")
                .excludePathPatterns("/users")
                .excludePathPatterns("/token-refresh");
    }
}
