package id.ac.ui.cs.advprog.authgateway.security;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RefreshScope
@Component
public class SpliceIdParameterFilter extends RequireAuthenticationFilter<SpliceIdParameterFilter.Config> {

    public SpliceIdParameterFilter() {
        super(SpliceIdParameterFilter.Config.class);
    }

    @Override
    protected ServerWebExchange mutate(ServerWebExchange exchange, FirebaseToken token) {
        URI uri = exchange.getRequest().getURI();
        StringBuilder query = new StringBuilder();
        String originalQuery = uri.getRawQuery();
        if (StringUtils.hasText(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }

        query.append("uid");
        query.append("=");
        query.append(token.getUid());

        try {
            URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build(true).toUri();
            ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
            return exchange.mutate().request(request).build();
        } catch (RuntimeException var8) {
            throw new IllegalStateException("Invalid URI query: \"" + query.toString() + "\"");
        }
    }
}
