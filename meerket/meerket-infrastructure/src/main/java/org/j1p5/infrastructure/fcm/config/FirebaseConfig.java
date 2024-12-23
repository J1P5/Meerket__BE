package org.j1p5.infrastructure.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    private static final String fileName = "meerket-83e38-firebase-adminsdk-gyt9i-d2df62fdf4.json";

    @PostConstruct
    public void initializeFirebase() {
        try (InputStream serviceAccount =
                getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (serviceAccount == null) {
                throw new RuntimeException("Firebase 설정 파일을 찾을 수 없습니다.");
            }

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException("Firebase 초기화 실패: " + e.getMessage(), e);
        }
    }
}
