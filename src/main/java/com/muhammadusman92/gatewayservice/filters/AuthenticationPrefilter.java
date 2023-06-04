package com.muhammadusman92.gatewayservice.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhammadusman92.gatewayservice.exception.AuthenticationException;
import com.muhammadusman92.gatewayservice.model.ConnValidationResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;


@Component
@Slf4j
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config> {

    @Autowired
    @Qualifier("excludedUrls")
    List<String> excludedUrls;

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    private final WebClient.Builder webClientBuilder;

    public AuthenticationPrefilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder=webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("**************************************************************************");
            log.info("URL is - " + request.getURI().getPath());
            String bearerToken = request.getHeaders().getFirst("Authorization");
            List<ServiceInstance> instances = discoveryClient.getInstances("authentication-service");
            log.info("Bearer Token: "+ bearerToken);
            if(isSecured.test(request)) {
                return webClientBuilder.build().get()
                        .uri("lb://AUTHENTICATION-SERVICE/api/v1/auth/")
                        .header("Authorization", bearerToken)
                        .retrieve().bodyToMono(ConnValidationResponse.class)
                        .map(response -> {
                            exchange.getRequest().mutate().header("userEmail", response.getUserEmail());
                            exchange.getRequest().mutate().header("userName", response.getUserName());
                            exchange.getRequest().mutate().header("auth-token", response.getToken());
                            return exchange;
                        }).flatMap(chain::filter).onErrorResume(error -> {
                            HttpStatus errorCode = null;
                            String errorMsg = "";
                            if (error instanceof WebClientResponseException) {
                                WebClientResponseException webCLientException = (WebClientResponseException) error;
                                errorCode = webCLientException.getStatusCode();
                                errorMsg = webCLientException.getStatusText();

                            } else {
                                errorCode = HttpStatus.BAD_GATEWAY;
                                errorMsg = HttpStatus.BAD_GATEWAY.getReasonPhrase();
                            }
//                            AuthorizationFilter.AUTH_FAILED_CODE
                            return onError(exchange, String.valueOf(errorCode.value()) ,errorMsg, "JWT Authentication Failed", errorCode);
                        });
            }

            return chain.filter(exchange);
        };
    }

    public Predicate<ServerHttpRequest> isSecured
            = request -> excludedUrls.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

    private Mono<Void> onError(ServerWebExchange exchange, String errCode,
                               String err, String errDetails,
                               HttpStatus httpStatus) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
//        ObjectMapper objMapper = new ObjectMapper();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        try {
            response.getHeaders().add("Content-Type", "application/json");
            AuthenticationException data = new AuthenticationException(errCode, err, errDetails);
            byte[] byteData = objectMapper.writeValueAsBytes(data);
            return response.writeWith(Mono.just(byteData).map(t -> dataBufferFactory.wrap(t)));

        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
        return response.setComplete();
    }
    @NoArgsConstructor
    public static class Config {


    }
}
