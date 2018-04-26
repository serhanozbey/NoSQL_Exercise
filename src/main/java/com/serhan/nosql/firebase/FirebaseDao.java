package com.serhan.nosql.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FirebaseDao {
    
    static private DatabaseReference mDatabase;
// ...
    
    
    public static void main(String[] args) throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("C:\\Users\\PLSEOZB\\Desktop\\JAVA_WORKS\\nosqllibs\\src\\main\\resources\\first-application-202308-firebase-adminsdk-rd5z1-8543997c7d.json");
    
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://first-application-202308.firebaseio.com")
                .build();
    
        FirebaseApp.initializeApp(options);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        writeNewUser("serhan","serhan@gmail.com");
        
    }
    
    static private void writeNewUser(String name, String email) {
        User user = new User(name, email);
        
        mDatabase.push().child("users").setValue(user);
    }

}
