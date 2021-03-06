package id.ac.ui.cs.advprog.authgateway;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
                .setDatabaseUrl(System.getenv("FIREBASE_DB_URL"))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            defaultApp = FirebaseApp.initializeApp(options);
        }

        SpringApplication.run(AuthGatewayApplication.class, args);
    }
}