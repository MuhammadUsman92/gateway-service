package com.muhammadusman92.gatewayservice;

import com.muhammadusman92.gatewayservice.filters.AuthenticationPrefilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableEurekaClient
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}
	@Bean
	public RouteLocator routes(
			RouteLocatorBuilder builder,
			AuthenticationPrefilter authFilter) {
		return builder.routes()
				.route("auth-service-route", r -> r.path("/authentication-service/**")
						.filters(f ->
								f.rewritePath("/authentication-service(?<segment>/?.*)", "$\\{segment}")
										.circuitBreaker(config -> config
												.setName("CircuitBreaker")
												.setFallbackUri("/authServiceFallBack"))
										.filter(authFilter.apply(
												new AuthenticationPrefilter.Config()))
						)
						.uri("lb://AUTHENTICATION-SERVICE"))
				.route("health-service-route", r -> r.path("/health-service/**")
						.filters(f ->
								f.rewritePath("/health-service(?<segment>/?.*)", "$\\{segment}")
										.filter(authFilter.apply(
												new AuthenticationPrefilter.Config()))
										.circuitBreaker(config -> config
												.setName("CircuitBreaker")
												.setFallbackUri("/healthServiceFallBack"))
						)
						.uri("lb://HEALTH-SERVICE"))
				.route("near-by-service", r -> r.path("/near-by-service/**")
						.filters(f ->
								f.rewritePath("/near-by-service(?<segment>/?.*)", "$\\{segment}")
										.filter(authFilter.apply(
												new AuthenticationPrefilter.Config()))
										.circuitBreaker(config -> config
												.setName("CircuitBreaker")
												.setFallbackUri("/nearbyServiceFallBack"))
						)
						.uri("lb://NEARBY-SERVICE"))
				.route("criminal-service-route", r -> r.path("/criminal-service/**")
						.filters(f ->
								f.rewritePath("/criminal-service(?<segment>/?.*)", "$\\{segment}")
										.filter(authFilter.apply(
												new AuthenticationPrefilter.Config()))
										.circuitBreaker(config -> config
												.setName("CircuitBreaker")
												.setFallbackUri("/criminalServiceFallBack"))
						)
						.uri("lb://CRIMINAL-SERVICE"))
				.build();
	}
	@Value("${spring.gateway.excludedURLsNew}")
	private String urlsStrings;

	@Bean
	@Qualifier("excludedUrls")
	public List<String> excludedUrls() {
		return Arrays.stream(urlsStrings.split(",")).collect(Collectors.toList());
	}

}
