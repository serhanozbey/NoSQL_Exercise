package nosql.firebase_admin;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

//just download the service account json inside the project and you're ready to go.
public class FirebaseServer {
    
    //Deploying a servlet to Firebase App Engine
    /*https://cloud.google.com/solutions/mobile/mobile-firebase-app-engine-flexible*/
    //TODO: https://github.com/firebase/quickstart-java
    /*https://cloud.google.com/appengine/docs/java/*/
    
    private static Firestore mFirestore;
    private static UserRecord mUser;
    private static ListenerRegistration mReg;
    private static DocumentReference mRef;
    
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, FirebaseAuthException {
        FileInputStream serviceAccount =
                new FileInputStream("...");
    
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("...")
                .build();
    
        
        FirebaseApp.initializeApp(options);
        mFirestore = FirestoreClient.getFirestore();
        mUser = FirebaseAuth.getInstance().getUser("66VJ5VWmmAfoAXFyVXhmMjD1rLl2");
        //retrieving all of the documents inside the collection.
        
        CollectionReference users = mFirestore.collection("posts");
    
        QuerySnapshot snapshots = users.get().get();
    
        Iterator<QueryDocumentSnapshot> it = snapshots.iterator();
    
        while (it.hasNext()) {
            System.out.println(it.next().getData());
        }
    
        //TODO: retrieve a single document
        
        /*DocumentReference user = mFirestore.collection("posts").document("9FhJYNQ9nBuo6gHmWYji");
        
        ApiFuture<DocumentSnapshot> future = user.get();
        
        System.out.println(future.get().getData());*/
        
        //TODO: Perform a transaction. with rollback and logging. 1 Random fields, 2 An Object.
        
        
        //TRANSACTION, WITHOUT OBJECT, SOME FIELDS
        DocumentReference pushRef = mFirestore.collection("transactionExercise1").document("ChQueQ0fMkiPLUUeGXyD");
        
        ApiFuture<Void> transaction = mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void updateCallback(Transaction transaction) throws Exception {
                DocumentSnapshot snapshot = transaction.get(pushRef).get();
                //these fields are not validated, but when we work with objects, it will be easier to validate
                long counter=0;
                Map<String, Object> map = new HashMap<>();
                //if doesn't exist, create and quit (transaction)
                if (snapshot.getData()==null) {
                    String name = mUser.getDisplayName();
                    String email = mUser.getEmail();
                    map.put("name", name.split(" ")[0]);
                    map.put("email", email.split("@")[0]);
                    map.put("counter", 1);
                    transaction.set(pushRef, map);
                    System.out.println("user details created.");
                    return null;
                } else {
                    counter = snapshot.getLong("counter");
                }
                
                //update with transaction, if ANY DATA AT DOCUMENT exists
                System.out.println(counter);
                String coname = snapshot.get("name").toString();
                String conemail = snapshot.get("email").toString();
                
                map.put("name", coname.concat("oglu"));
                map.put("email", conemail.concat(String.valueOf(counter)));
                map.put("counter", ++counter);
                transaction.update(pushRef,map);
                return null;
            }
        });
        transaction.get();
        
        //TRANSACTION WITH OBJECT (VALIDATION PROPERTIES)
    
        DocumentReference objectRef = mFirestore.collection("transactionExercise2").document("ChQueQ0fMkiPLUUeGXyD");
        
        ApiFuture<Void> transaction2 = mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void updateCallback(Transaction transaction) throws Exception {
                DocumentSnapshot snapshot = transaction.get(objectRef).get();
                TransactionUser user = new TransactionUser("dede", "dede@dede.com");
                if (snapshot.getData()==null) {
                    transaction.set(objectRef, user);
                    System.out.println("object created.");
                }else{
                    System.out.println("data exists");
                    System.out.println(transaction.update(objectRef, "name", String.valueOf(snapshot.get("name")).concat(String.valueOf((int) (Math.random() * 10))), "email", String.valueOf(snapshot.get("email")).split("@")[0], "counter", snapshot.getLong("counter") + 1L));
                    
                }
                return null;
            }
        });
        transaction2.get();
    
    
        //TODO: Perform a batched write.
        
        
       
    }
    
    
    
    
}
