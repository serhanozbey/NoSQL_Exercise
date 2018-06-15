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
import com.google.firebase.database.DataSnapshot;

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
        
        //TODO: FIRESTORE AND AUTH SETUP
        FileInputStream serviceAccount =
                new FileInputStream("");
    
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://.firebaseio.com")
                .build();
    
        
        FirebaseApp.initializeApp(options);
        mFirestore = FirestoreClient.getFirestore();
    
        
        //TODO: READ A DOCUMENT IN COLLECTION
        /*DocumentReference user = mFirestore.collection("posts").document("9FhJYNQ9nBuo6gHmWYji");
        
        ApiFuture<DocumentSnapshot> future = user.get();
        
        System.out.println(future.get().getData());*/
    
        
        
        //TODO: READ ALL DOCUMENTS IN COLLECTION
        mUser = FirebaseAuth.getInstance().getUser("66VJ5VWmmAfoAXFyVXhmMjD1rLl2");
        System.out.println(mUser.getDisplayName());
    
        CollectionReference users = mFirestore.collection("posts");
    
        QuerySnapshot snapshots = users.get().get();
    
        Iterator<QueryDocumentSnapshot> it = snapshots.iterator();
    
        while (it.hasNext()) {
            System.out.println(it.next().getData());
        }
    
        
        //TODO: TRANSACTIONS
        //TODO: Perform a transaction. with rollback and logging. 1st is some fields of document, 2nd is setting or updating fields of an Object.
        
        // IMPORTANT INFORMATION RELATED TO TRANSACTIONS
        
        /*The transaction contains read operations after write operations. Read operations must always come before any write operations.
The transaction read a document that was modified outside of the transaction. In this case, the transaction automatically runs again. The transaction is retried a finite number of times.
A failed transaction returns an error and does not write anything to the database. You do not need to roll back the transaction; Cloud Firestore does this automatically.*/
        
        /*WARNING: Do not modify application state inside of your transaction functions. Doing so will introduce concurrency issues, because transaction functions can run multiple times and are not guaranteed to run on the UI thread. */
        
        
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
                map.put("counter", counter++);
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
    
    
        //TODO: BATCHED WRITE
    
        /*If you do not need to read any documents in your operation set, you can execute multiple write operations as a single batch that contains any combination of set(), update(), or delete() operations. A batch of writes completes atomically and can write to multiple documents.*/
    
        /*Batched writes are also useful for migrating large data sets to Cloud Firestore. A batched write can contain up to 500 operations and batching operations together reduces connection overhead resulting in faster data migration.*/
    
        
        WriteBatch batch = mFirestore.batch();
    
        CollectionReference firstRef = mFirestore.collection("batchedExercise1");
    
        DocumentReference docRef1 = firstRef.document();
    
        DocumentReference docRef2 = firstRef.document();
    
        DocumentReference docRef3 = firstRef.document();
    
        TransactionUser user1 = new TransactionUser("serhan", "serhan@gmail.com");
        
        TransactionUser user2 = new TransactionUser("dede", "dede@gmail.com");
    
        TransactionUser user3 = new TransactionUser("mama", "mama@gmail.com");
        
        batch.set(docRef1, user1);
    
        batch.set(docRef2, user2);
        
        batch.update(docRef1, "name", "SerhanBatchUpdated");
    
        batch.update(docRef2, "name", "DedeBatchUpdated", "email", user2.getEmail().split("@")[0]);
    
        if (docRef3.get().get().exists()) {
            batch.set(docRef3, user3);
            System.out.println(user3 + "is added.");
        }else{
            batch.delete(docRef3);
            System.out.println(user3 + "is deleted");
        }
    
        System.out.println(batch.commit());
       
    }
    
    
    
    
}
