package com.example.sumefly.firestore;

import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ManejadorFirebase {

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream serviceAccount =
                new FileInputStream("path/to/serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                //.setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://sumefly.firebaseapp.com")
                .build();

    }
}
