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
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;

//just download the service account json inside the project and you're ready to go.
public class FirebaseServer {
    
    //Deploying a servlet to Firebase App Engine
    /*https://cloud.google.com/solutions/mobile/mobile-firebase-app-engine-flexible*/
    //TODO: https://github.com/firebase/quickstart-java
    /*https://cloud.google.com/appengine/docs/java/*/
    
    
    //todo: write better tests. test connections and if things are retrieved in order.
    
    private static Firestore mFirestore;
    private static UserRecord mUser;
    
    //FIRESTORE AND AUTH SETUP
    public static void setup(String serviceAccountFile) throws IOException, FirebaseAuthException {
        FileInputStream serviceAccount =
                new FileInputStream(serviceAccountFile);
        
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        System.out.println("Getting user auth");
        
        FirebaseApp.initializeApp(options);
        mFirestore = FirestoreClient.getFirestore();
        
        mUser = FirebaseAuth.getInstance().getUser("66VJ5VWmmAfoAXFyVXhmMjD1rLl2");
        System.out.println(mUser.getDisplayName());
    }
    
    //READ AN EXISTING DOCUMENT IN COLLECTION
    public static void readSingleDocument() throws InterruptedException, ExecutionException {
        System.out.println("Reading a specific post");
        DocumentReference user = mFirestore.collection("posts").document("9FhJYNQ9nBuo6gHmWYji");
        
        ApiFuture<DocumentSnapshot> future = user.get();
        
        System.out.println(future.get().getData());
    }
    
    //READ ALL DOCUMENTS IN COLLECTION
    public static void readCollectionDocuments() throws FirebaseAuthException, InterruptedException, ExecutionException {
        System.out.println("Reading all posts");
        
        
        CollectionReference users = mFirestore.collection("posts");
        
        QuerySnapshot snapshots = users.get().get();
        
        Iterator<QueryDocumentSnapshot> it = snapshots.iterator();
        
        while (it.hasNext()) {
            System.out.println(it.next().getData());
        }
    }
    
    //SECTION: TRANSACTIONS
    //Perform a transaction. with rollback and logging. 1st is some fields of document, 2nd is setting or updating fields of an Object.
    
    // IMPORTANT INFORMATION RELATED TO TRANSACTIONS
        
        /*The transaction contains read operations after write operations. Read operations must always come before any write operations.
The transaction read a document that was modified outside of the transaction. In this case, the transaction automatically runs again. The transaction is retried a finite number of times.
A failed transaction returns an error and does not write anything to the database. You do not need to roll back the transaction; Cloud Firestore does this automatically.*/
    
    /*WARNING: Do not modify application state inside of your transaction functions. Doing so will introduce concurrency issues, because transaction functions can run multiple times and are not guaranteed to run on the UI thread. */
    
    //TRANSACTION OF FIELDS
    public  static void transactionField() throws InterruptedException, ExecutionException {
        
        System.out.println("Working on hardcoded single document");
        DocumentReference pushRef = mFirestore.collection("transactionExercise1").document("ChQueQ0fMkiPLUUeGXyD");
        
        ApiFuture<Void> transaction = mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void updateCallback(Transaction transaction) throws Exception {
                DocumentSnapshot snapshot = transaction.get(pushRef).get();
                //these fields are not validated, but when we work with objects, it will be easier to validate
                long counter = 0;
                Map<String, Object> map = new HashMap<>();
                //if doesn't exist, Transaction CREATE (transaction)
                if (snapshot.getData() == null) {
                    String name = mUser.getDisplayName();
                    String email = mUser.getEmail();
                    map.put("name", name.split(" ")[0]);
                    map.put("email", email.split("@")[0]);
                    map.put("counter", 1);
                    transaction.set(pushRef, map);
                    System.out.println("user details created.");
                    return null;
                } else {
                    //if exists, retrieve counter.
                    counter = snapshot.getLong("counter");
                }
                
                //Transaction UPDATE, if ANY DATA AT DOCUMENT exists
                System.out.println("Counter: " + counter);
                String coname = snapshot.get("name").toString();
                String conemail = snapshot.get("email").toString();
                
                System.out.println("concatenating name and email with database transaction update");
                map.put("name", coname.concat("ek"));
                map.put("email", conemail.concat(String.valueOf(counter)));
                map.put("counter", counter++);
                transaction.update(pushRef, map);
                return null;
            }
        });
        transaction.get();
    }
    
    //TRANSACTION WITH OBJECT (VALIDATION PROPERTIES)
    public static void transactionObject() throws InterruptedException, ExecutionException {
        System.out.println("Working on hardcoded single document");
        DocumentReference objectRef = mFirestore.collection("transactionExercise2").document("ChQueQ0fMkiPLUUeGXyD");
        
        ApiFuture<Void> transaction2 = mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void updateCallback(Transaction transaction) throws Exception {
                DocumentSnapshot snapshot = transaction.get(objectRef).get();
                TransactionUser user = new TransactionUser("dede", "dede@dede.com");
                if (snapshot.getData() == null) {
                    //if object doesn't exist, write with transaction
                    transaction.set(objectRef, user);
                    System.out.println("object created.");
                } else {
                    System.out.println("data exists");
                    // do some random updates.
                    //transaction.update(objectRef, "name", String.valueOf(snapshot.get("name")).concat(String.valueOf((int) (Math.random() * 10))), "email", String.valueOf(snapshot.get("email")).split("@")[0], "counter", snapshot.getLong("counter") + 1L);
                    
                }
                return null;
            }
        });
        transaction2.get();
    }
    
    //SECTION: BATCHED WRITE
    
    /*If you do not need to read any documents in your operation set, you can execute multiple write operations as a single batch that contains any combination of set(), update(), or delete() operations. A batch of writes completes atomically and can write to multiple documents.*/
    
    /*Batched writes are also useful for migrating large data sets to Cloud Firestore. A batched write can contain up to 500 operations and batching operations together reduces connection overhead resulting in faster data migration.*/
    
    //WRITING USERS IF THEY DON'T EXIST AT THE COLLECTIONS.
    public static void batchedWrites() throws InterruptedException, ExecutionException {
        System.out.println("Writing 3 documents ");
        WriteBatch batch = mFirestore.batch();
        //set database reference location
        CollectionReference firstRef = mFirestore.collection("batchedExercise1");
        //create docs to be pushed.
        TransactionUser user1 = new TransactionUser("serhan", "serhan@gmail.com");
        
        TransactionUser user2 = new TransactionUser("dede", "dede@gmail.com");
        
        TransactionUser user3 = new TransactionUser("mama", "mama@gmail.com");
        
        //add them to list and get iterator
        List<TransactionUser> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        Iterator<TransactionUser> it = userList.listIterator();
    
        //retrieve all existing objects to a list.
        List<TransactionUser> transactionUsers = firstRef.get().get().toObjects(TransactionUser.class);

        //checking database if created user exists, if not created and pushed to database
        while (it.hasNext()) {
            TransactionUser user = it.next();
            if (transactionUsers.contains(user)) {
                System.out.println("User already exists");
            } else {
                System.out.println("User written: " + user);
                batch.set(firstRef.document(), user);
            }
        }
        //commit batchedwrites.
        batch.commit();
    }
    
    //READ AN OBJECT IN COLLECTION
    public static void batchedWriteQuery() throws ExecutionException, InterruptedException {
        System.out.println("Querying data with name value");
        CollectionReference docRef = mFirestore.collection("batchedExercise1");
        Query q = docRef.whereEqualTo("name", "serhan").limit(1);
        
        QuerySnapshot querySnapshot = q.get().get();
        DocumentSnapshot document = querySnapshot.iterator().next();
        // asynchronously retrieve the document
        // ...
        // future.get() blocks on response
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        } else {
            System.out.println("No such document!");
        }
    }
    
    //UPDATING THE EDITED TIME
    public static void batchedWriteUpdates() throws ExecutionException, InterruptedException {
        System.out.println("Updating 3 documents");
        
        CollectionReference ref = mFirestore.collection("batchedExercise1");
        
        ListIterator<QueryDocumentSnapshot> itList = ref.get().get().getDocuments().listIterator();
        
        WriteBatch batch = mFirestore.batch();
        
        while (itList.hasNext()) {
            QueryDocumentSnapshot snapshot = itList.next();
            TransactionUser user = snapshot.toObject(TransactionUser.class);
            System.out.println("Writing to user: " + user.toString());
            String path = snapshot.getId();
            System.out.println(path);
            batch.update(ref.document(path), "name", user.getName());
            batch.update(ref.document(path), "counter", user.getCounter() + 1L);
            //if already exists, insert edit time, if not, created time.
            if (snapshot.contains("createdAt")) {
                System.out.println("Creation time: " + snapshot.getData().get("createdAt"));
            }
            //adding current time date as it's edited.
            Map<String, Object> map = new HashMap<>();
            String now = String.valueOf(Instant.now());
            map.put("editedAt", now);
            System.out.println(now);
            batch.set(ref.document(path), map, SetOptions.merge());
        }
        batch.commit();
    }
}
