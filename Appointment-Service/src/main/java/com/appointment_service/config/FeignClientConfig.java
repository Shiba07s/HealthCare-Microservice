package com.appointment_service.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {


    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authorizationHeader = request.getHeader("Authorization");

                if (authorizationHeader != null) {
                    requestTemplate.header("Authorization", authorizationHeader);
                    System.out.println("Forwarding Authorization Header: " + authorizationHeader);
                } else {
                    System.out.println("Authorization header is missing in request.");
                }
            } else {
                System.out.println("ServletRequestAttributes is null.");
            }
        };
    }

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null && authentication.getCredentials() instanceof String token) {
//                requestTemplate.header("Authorization", "Bearer " + token);
//            }
//        };
//    }
}
