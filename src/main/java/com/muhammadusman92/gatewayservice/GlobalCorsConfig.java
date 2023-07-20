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
public class GlobalCorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://34.27.132.75:3000"));
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        configuration.setAllowedHeaders(Collections.singletonList("Content-Type"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public GlobalFilter customCorsFilter() {
        return (exchange, chain) -> {
            exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "http://34.27.132.75:3000");
            exchange.getResponse().getHeaders().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
            exchange.getResponse().getHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponse().getHeaders().set("Access-Control-Max-Age", "3600");
            exchange.getResponse().getHeaders().set("Access-Control-Allow-Credentials", "false");
            return chain.filter(exchange);
        };
    }
}
