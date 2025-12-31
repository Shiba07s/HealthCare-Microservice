//package com.api_gateway.filter;
//
//
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import java.util.*;
//import java.util.List;
//import java.util.function.Predicate;
//
//@Component
//public class RouteValidator {
//
//  public static final List<String> openApiEndPoint=  List.of(
//            "/api/v1/auth/signup",
//            "/api/v1/auth/signin",
//            "/api/v1/auth/validate",
//            "/api/v1/secret/create"
//             );
//
//  public Predicate<ServerHttpRequest> isSecured=
//          serverHttpRequest -> openApiEndPoint
//                  .stream()
//                  .noneMatch(endPoint -> serverHttpRequest.getURI().getPath().contains(endPoint));
//}
