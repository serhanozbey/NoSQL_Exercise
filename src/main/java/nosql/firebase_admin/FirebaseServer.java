package nosql.firebase_admin;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

//just download the service account json inside the project and you're ready to go.
public class FirebaseServer {
    
    //Deploying a servlet to Firebase App Engine
    /*https://cloud.google.com/solutions/mobile/mobile-firebase-app-engine-flexible*/
    //TODO: https://github.com/firebase/quickstart-java
    /*https://cloud.google.com/appengine/docs/java/*/
    
    private static Firestore mFirestore;
    private static FirebaseAuth mAuth;
    private static ListenerRegistration mReg;
    private static DocumentReference mRef;
    
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount =
                new FileInputStream("...../-firebase-adminsdk-trfke-6412dfe59c.json");
    
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://...com")
                .build();
    
        
        FirebaseApp.initializeApp(options);
        mFirestore = FirestoreClient.getFirestore();
        
        //retrieving all of the documents inside the collection.
        
        CollectionReference users = mFirestore.collection("posts");
    
        QuerySnapshot snapshots = users.get().get();
    
        Iterator<QueryDocumentSnapshot> it = snapshots.iterator();
    
        while (it.hasNext()) {
            System.out.println(it.next().getData());
        }
        
        //retrieving single document
        
        /*DocumentReference user = mFirestore.collection("posts").document("9FhJYNQ9nBuo6gHmWYji");
        
        ApiFuture<DocumentSnapshot> future = user.get();
        
        System.out.println(future.get().getData());*/
    }
    
    
}
