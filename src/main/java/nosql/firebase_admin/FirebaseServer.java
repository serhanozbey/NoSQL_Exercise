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
    
    //TODO: Replace the tests as following:
    //TODO: make DB CRUD methods like DAO
    //TODO: write tests that writes and expects the same with a query to DB.
    
    //creating indexes at firestore is important for data validation.
    private static Firestore mFirestore;
    private static UserRecord mUser;
    
    //FIRESTORE AND AUTH SETUP
    static void setup(String serviceAccountFile) throws IOException, FirebaseAuthException {
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
    //SECTION1: BASIC READ AND WRITE OPERATIONS
    
    static void writeSingleDocument(String name, String email,String path) throws ExecutionException, InterruptedException {
        TransactionUser user = new TransactionUser(name,email);
        mFirestore.collection(path).add(user).get();
    }
    
    static void deleteSingleDocument(String name, String path) throws ExecutionException, InterruptedException {
        String id = mFirestore.collection(path).whereEqualTo("name", name).get().get().getDocuments().get(0).getId();
        mFirestore.collection(path).document(id).delete();
    }
    
    static void deleteCollection(String path, int batchSize) {
        try {
            // retrieve a small batch of documents to avoid out-of-memory errors
            CollectionReference collection = mFirestore.collection(path);
            ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
            }
            if (deleted >= batchSize) {
                // retrieve and delete another batch
                deleteCollection(path, batchSize);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }
    
    //QUERIES BY NAME, EMAIL AND READS AN EXISTING DOCUMENT IN COLLECTION, AS OBJECT.
    static TransactionUser readSingleDocumentObject(String name, String email,String path) throws InterruptedException, ExecutionException {
        CollectionReference ref = mFirestore.collection(path);
        Query q = ref.whereEqualTo("name", name).whereEqualTo("email", email);
        TransactionUser user = q.get().get().toObjects(TransactionUser.class).get(0);
        System.out.println("object query:\n" + user);
        return user;
    }
    
    static String readSingleDocumentField(String name,String email,String path) throws ExecutionException, InterruptedException {
        CollectionReference ref = mFirestore.collection(path);
        Query q = ref.whereEqualTo("name", name).whereEqualTo("email", email).select("name","email");
        List<QueryDocumentSnapshot> snapshots = q.get().get().getDocuments();
        StringBuilder builder = new StringBuilder();
        for (QueryDocumentSnapshot snapshot : snapshots) {
            System.out.print(snapshot.getData());
            builder.append(snapshot.getData());
        }
        System.out.println("\nfields query: ");
        return builder.toString();
    }
    
    //READ ALL DOCUMENTS IN COLLECTION
    static String readCollectionDocuments(String path) throws FirebaseAuthException, InterruptedException, ExecutionException {
        System.out.println("Reading all static posts");
        CollectionReference users = mFirestore.collection(path);
        QuerySnapshot snapshots = users.get().get();
        StringBuilder builder = new StringBuilder();
        for (QueryDocumentSnapshot snapshot : snapshots) {
            System.out.println(snapshot.getData());
            builder.append(snapshot.getData());
        }
        return builder.toString();
    }
    
    //SECTION2: TRANSACTIONS
    //Perform a transaction. with rollback and logging. 1st is some fields of document, 2nd is setting or updating fields of an Object.
    
    // IMPORTANT INFORMATION RELATED TO TRANSACTIONS
        
        /*The transaction contains read operations after write operations. Read operations must always come before any write operations.
The transaction read a document that was modified outside of the transaction. In this case, the transaction automatically runs again. The transaction is retried a finite number of times.
A failed transaction returns an error and does not write anything to the database. You do not need to roll back the transaction; Cloud Firestore does this automatically.*/
    
    /*WARNING: Do not modify application state inside of your transaction functions. Doing so will introduce concurrency issues, because transaction functions can run multiple times and are not guaranteed to run on the UI thread. */
    
    //TRANSACTION OF FIELDS
    static void transactionField(String path) throws InterruptedException, ExecutionException {
        //pushing current users name, email and counter.
        System.out.println("Working on hardcoded single document");
        DocumentReference pushRef = mFirestore.collection(path).document("transactionField");
        
        ApiFuture<Void> transaction = mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void updateCallback(Transaction transaction) throws Exception {
                DocumentSnapshot snapshot = transaction.get(pushRef).get();
                //these fields are not validated, but when we work with objects, it will be easier to validate
                Map<String, Object> map = new HashMap<>();
                //if doesn't exist, Transaction CREATE (transaction)
                if (snapshot.getData() == null) {
                    String name = mUser.getDisplayName();
                    String email = mUser.getEmail();
                    map.put("name", name.split(" ")[0]);
                    map.put("email", email);
                    transaction.set(pushRef, map);
                    System.out.println("user details created.");
                    return null;
                } else {
                    //if exists, put edit 'editedAt' timestamp.
                    String instant = Instant.now().toString();
                    map.put("lastQueryTime", instant);
                    System.out.println("last query time: "+ instant);
                    transaction.set(pushRef, map, SetOptions.merge());
                    return null;
                }
                
            }
        });
    }
    
    //TRANSACTION WITH OBJECT (VALIDATION PROPERTIES)
    static void transactionObject(String path) throws InterruptedException, ExecutionException {
        System.out.println("Working on hardcoded single document");
        DocumentReference objectRef = mFirestore.collection(path).document("transactionObject");
        
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
    
    
    //SECTION3: BATCHED WRITE
    
    /*If you do not need to read any documents in your operation set, you can execute multiple write operations as a single batch that contains any combination of set(), update(), or delete() operations. A batch of writes completes atomically and can write to multiple documents.*/
    
    /*Batched writes are also useful for migrating large data sets to Cloud Firestore. A batched write can contain up to 500 operations and batching operations together reduces connection overhead resulting in faster data migration.*/
    
    //WRITING USERS IF THEY DON'T EXIST AT THE COLLECTIONS.
    //write 3 users and push it. if already exists, push editedAt section.
    static void batchedWrites(String path) throws InterruptedException, ExecutionException {
        System.out.println("Writing 3 documents ");
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
    
        //set database reference location
        CollectionReference firstRef = mFirestore.collection(path);
        //retrieve all existing objects to a list.
        //checking database if created user exists, if not created and pushed to database
        WriteBatch batch = mFirestore.batch();
        while (it.hasNext()) {
            TransactionUser user = it.next();
            //querying with name, as it's determines as unique.
            QuerySnapshot query = firstRef.whereEqualTo("name", user.getName()).get().get();
            if (!query.isEmpty()) {
                String docpath = query.getDocuments().get(0).getId();
                Map<String, Object> map = new HashMap<>();
                String now = String.valueOf(Instant.now());
                map.put("editedAt", now);
                batch.set(firstRef.document(docpath),map,SetOptions.merge());
                System.out.println("User already exists");
            } else {
                System.out.println("User written: " + user);
                batch.create(firstRef.document(), user);
            }
        }
        //commit batchedwrites.
        batch.commit();
    }
    
    //READ AN OBJECT IN COLLECTION
    static void batchedWriteQuery(String path) throws ExecutionException, InterruptedException {
        //TODO: to be removed and replaced with a test called, query if batched write is written as expected.
        System.out.println("Querying data with name value");
        CollectionReference docRef = mFirestore.collection(path);
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
    static void batchedWriteUpdates() throws ExecutionException, InterruptedException {
        System.out.println("Updating 3 documents");
        
        CollectionReference ref = mFirestore.collection("batchedWriteExercise");
        
        ListIterator<QueryDocumentSnapshot> itList = ref.get().get().getDocuments().listIterator();
        
        WriteBatch batch = mFirestore.batch();
        
        while (itList.hasNext()) {
            QueryDocumentSnapshot snapshot = itList.next();
            TransactionUser user = snapshot.toObject(TransactionUser.class);
            System.out.println("Writing to user: " + user.toString());
            String path = snapshot.getId();
            System.out.println(path);
            batch.update(ref.document(path), "name", user.getName());
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
    
    static void clearDatabase(String... paths) throws ExecutionException, InterruptedException {
        for (String path : paths) {
            //TODO: cleanup database
        }
    }
}
