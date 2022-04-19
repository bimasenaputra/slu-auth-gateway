package id.ac.ui.cs.advprog.authgateway.security;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@RefreshScope
@Component
public class AuthenticationFilter extends RequireAuthenticationFilter<AuthenticationFilter.Config> {

    public AuthenticationFilter() {
        super(AuthenticationFilter.Config.class);
    }

    @Override
    protected ServerWebExchange mutate(ServerWebExchange exchange, FirebaseToken token) {
        return exchange;
    }
}
