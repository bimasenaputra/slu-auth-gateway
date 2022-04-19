package id.ac.ui.cs.advprog.authgateway.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class RequireAuthenticationFilter<T extends RequireAuthenticationFilter.Config> extends AbstractGatewayFilterFactory<T> {

    public <U> RequireAuthenticationFilter(U object) {
        super((Class<T>) object);
    }

    @Override
    public GatewayFilter apply(T config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            try {
                FirebaseToken token = authenticate(request);
                return chain.filter(mutate(exchange, token));
            } catch (FirebaseAuthException | IllegalArgumentException e) {
                return onError(exchange, e.getMessage(), HttpStatus.FORBIDDEN);
            } catch (Exception e) {
                return onError(exchange, e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        };
    }

    protected abstract ServerWebExchange mutate(ServerWebExchange exchange, FirebaseToken token);

    private FirebaseToken authenticate(ServerHttpRequest request) throws FirebaseAuthException, IllegalArgumentException {
        var cookies = request.getHeaders().getFirst(HttpHeaders.COOKIE);
        var idToken = extract(cookies).get("idToken");
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private Map<String, String> extract(String cookie) {
        var regex = "([^=]+)=([^\\;]+);\\s?";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(cookie);

        Map<String, String> result = new HashMap<>();
        while (matcher.find()) {
            result.put(matcher.group(1), matcher.group(2));
        }
        return result;
    }

    public static class Config {

    }

}
