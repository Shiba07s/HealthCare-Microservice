//package com.api_gateway.filter;
//
//import com.api_gateway.utils.JwtUtils;
//import com.netflix.discovery.converters.Auto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//
//
//@Component
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
//
//    @Autowired
//    private RouteValidator routeValidator;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            if (routeValidator.isSecured.test(exchange.getRequest())) {
//
//                // Check if Authorization header is present
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("Authorization header not present");
//                }
//
//                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    String token = authHeader.substring(7); // Remove "Bearer "
//
//                    try {
//                        // REST call to Auth Service
////                        restTemplate.getForObject("http://AuthService/api/v1/auth/validate?token=" + token, String.class);
//                    jwtUtils.validateToken(authHeader);
//                    } catch (Exception e) {
//                        throw new RuntimeException("Unauthorized access - token validation failed", e);
//                    }
//                } else {
//                    throw new RuntimeException("Invalid Authorization header");
//                }
//            }
//
//            return chain.filter(exchange);
//        });
//    }
//
//    public static class Config {
//
//    }
//}
