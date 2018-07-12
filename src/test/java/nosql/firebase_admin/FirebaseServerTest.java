package nosql.firebase_admin;

import com.google.firebase.auth.FirebaseAuthException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

//http://www.baeldung.com/junit-5-migration
@ExtendWith(TraceUnitExtension.class)
class FirebaseServerTest {
    
    
    //DB test
    @BeforeAll
    static void beforeAll(){
        System.out.println(Calendar.DATE);
        try {
            FirebaseServer.setup("..");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @BeforeEach
    void setUp() {
        //
    }
    
    @Test
    void readSingleDocument() throws ExecutionException, InterruptedException {
        FirebaseServer.readSingleDocument();
    }
    
    @Test
    void readCollectionDocuments() throws FirebaseAuthException, InterruptedException, ExecutionException {
        FirebaseServer.readCollectionDocuments();
    }
    
    @Test
    void transactionField() throws ExecutionException, InterruptedException {
        FirebaseServer.transactionField();
    }
    
    @Test
    void transactionObject() throws ExecutionException, InterruptedException {
        FirebaseServer.transactionObject();
    }
    
    @Test
    void batchedWrites() throws ExecutionException, InterruptedException {
        FirebaseServer.batchedWrites();
    }
    
    @Test
    void batchedWriteQuery() throws ExecutionException, InterruptedException {
        FirebaseServer.batchedWriteQuery();
    }
    
    @Test
    void batchedWriteUpdates() throws ExecutionException, InterruptedException {
        FirebaseServer.batchedWriteUpdates();
    }
    
    
}