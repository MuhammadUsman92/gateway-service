import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;
import java.util.Collections; // Add this import
import java.util.Arrays; // Add this import

@Configuration
public class GlobalCorsConfig extends org.springframework.web.cors.CorsConfiguration {

  @Bean
  public CorsWebFilter corsFilter() {
    org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
    corsConfiguration.addAllowedHeader("origin");
    corsConfiguration.addAllowedHeader("content-type");
    corsConfiguration.addAllowedHeader("accept");
    corsConfiguration.addAllowedHeader("authorization");
    corsConfiguration.addAllowedHeader("cookie");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsWebFilter(source);
  }
}

