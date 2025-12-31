package com.api_gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route for patient service
                .route("Patient-Service", r -> r
                        .path("/api/v1/patient-profile/**")
                        .filters(f -> f.rewritePath("/api/v1/patients/(?<path>.*)", "/${path}"))

//                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://Patient-Service"))

                // Route for doctor service
                .route("Doctor-Service", r -> r
                        .path("/api/v1/doctor-profile/**")
                        .filters(f -> f.rewritePath("/api/v1/doctors/(?<path>.*)", "/${path}"))

//                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://Doctor-Service"))

                // Route for Appointment service
                .route("Appointment-Service", r -> r
                        .path("/api/v1/appointments/**")
//                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://Appointment-Service"))


                // Route for AvailabilitySlot Service
                .route("AvailabilitySlot-Service", r -> r
                        .path("/api/v1/availability-slots/**")
                         .uri("lb://AvailabilitySlot-Service"))

                .route("AuthService", r -> r
                        .path("/api/v1/auth/**")
                        .uri("lb://AuthService"))

                 .build();
    }
}