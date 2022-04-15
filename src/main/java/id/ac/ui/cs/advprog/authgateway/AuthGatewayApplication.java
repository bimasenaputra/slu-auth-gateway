package id.ac.ui.cs.advprog.authgateway;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class AuthGatewayApplication {
    public static FirebaseApp defaultApp;

    public static void main(String[] args) throws IOException {

        ClassLoader classLoader = AuthGatewayApplication.class.getClassLoader();

        InputStream file = classLoader.getResourceAsStream("PrivateKey.json");

        assert file != null;
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(file))
                .setDatabaseUrl("https://b13-project-f85c4-default-rtdb.firebaseio.com/")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            defaultApp = FirebaseApp.initializeApp(options);
        }

        SpringApplication.run(AuthGatewayApplication.class, args);
    }

    @Bean
    RouteLocator gateway (RouteLocatorBuilder rlb) {
        return rlb
                .routes()
                .route(routeSpec -> routeSpec
                        .path("/api/account/**")
                        .uri("http://localhost:8082"))
                .build();
    }
}