package com.pestrotech.task.config;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Value("${firebase.admin.credentials.path}")
    private String credentialsPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream("D:\\Downloads\\pesto-tech-assessment-firebase-adminsdk-xv9wv-3a9dc8ec49.json");
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
        FileInputStream serviceAccount = new FileInputStream(credentialsPath);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();
        return FirebaseApp.initializeApp(options);
//        if (FirebaseApp.getApps().isEmpty()) {
//            FileInputStream serviceAccount =
//                    new FileInputStream("D:/Downloads/pesto-tech-assessment-firebase-adminsdk-xv9wv-3a9dc8ec49.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://pesto-tech-assessment-default-rtdb.asia-southeast1.firebasedatabase.app")
//                    .build();
////
////        FirebaseApp.initializeApp(options);
//
//
//            return FirebaseApp.initializeApp(options);
//        } else {
//            return FirebaseApp.getInstance();
//        }
    }
}
