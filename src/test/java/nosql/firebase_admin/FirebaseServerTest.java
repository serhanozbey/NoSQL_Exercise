package nosql.firebase_admin;

import com.google.firebase.auth.FirebaseAuthException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

//http://www.baeldung.com/junit-5-migration
@ExtendWith(TraceUnitExtension.class)
class FirebaseServerTest {
    
    
    private final String NAME = "serhan";
    private final String EMAIL = "serhan@serhan.com";
    //first 3 exercises
    private final static String SINGLE_DOC_PATH = "single_document";
    private final static String TRANSACTIONS_PATH = "transactions";
    private final static String BATCHED_WRITE_PATH = "batched_write";
    private final static String[] collections = new String[]{SINGLE_DOC_PATH, TRANSACTIONS_PATH, BATCHED_WRITE_PATH};
    
    //DB test
    @BeforeAll
    static void beforeAll() {
        System.out.println("TEST STARTS: " + Instant.now());
        try {
            FirebaseServer.setup("..");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    //SECTION1: BASIC READ AND WRITE OPERATIONS
    @Test
    void writeReadDeleteSingleDocument() throws ExecutionException, InterruptedException {
        FirebaseServer.writeSingleDocument(NAME, EMAIL,SINGLE_DOC_PATH);
        //readSingleDocument
        assertEquals(new TransactionUser(NAME, EMAIL), FirebaseServer.readSingleDocumentObject(NAME, EMAIL,SINGLE_DOC_PATH));
        //deleteSingleDocument
        FirebaseServer.deleteSingleDocument(NAME,SINGLE_DOC_PATH);
    }
    
    @Test
    void readCollectionDocuments() throws FirebaseAuthException, InterruptedException, ExecutionException {
        FirebaseServer.readCollectionDocuments("posts");
    }
    //SECTION2: TRANSACTIONS
    @Test
    void transactionField() throws ExecutionException, InterruptedException {
        FirebaseServer.transactionField(TRANSACTIONS_PATH);
    }
    
    @Test
    void transactionObject() throws ExecutionException, InterruptedException {
        FirebaseServer.transactionObject(TRANSACTIONS_PATH);
    }
    //SECTION3: BATCHED WRITES
    @Test
    void batchedWrites() throws ExecutionException, InterruptedException {
        FirebaseServer.batchedWrites(BATCHED_WRITE_PATH);
    }
    
    @Test
    void batchedWriteQuery() throws ExecutionException, InterruptedException {
        FirebaseServer.batchedWriteQuery(BATCHED_WRITE_PATH);
    }
    
    @Test
    void batchedWriteUpdates() throws ExecutionException, InterruptedException {
        FirebaseServer.batchedWriteUpdates();
    }
    
    //removing all writes to database after tests.
    @AfterAll
    static void finish() throws ExecutionException, InterruptedException {
        //for(String path:collections) FirebaseServer.deleteCollection(path,2);
    }
}