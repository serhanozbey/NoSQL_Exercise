package nosql.firebase_admin;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TraceUnitExtension implements AfterEachCallback, BeforeEachCallback {
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("STARTING METHOD: "+context.getDisplayName()+" -TIME: "+ dateFormat.format(new Date()) + "\n");
        
    }
    
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.println("\nENDING METHOD: "+context.getDisplayName()+" -TIME: "+  dateFormat.format(new Date())+ "\n");
    }
    
    
}
