package nosql.mongoDBExercise;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Iterator;

public class MorphiaExercise {
    
    static private final MongoClientURI mongoURI = new MongoClientURI("mongodb://localhost:27017");
    static private Morphia morphia;
    static private Datastore datastore;
    
    public static void main(String[] args) {
        
        morphia = new Morphia();
        datastore = morphia.createDatastore(new MongoClient(mongoURI), "admin");
        morphia.mapPackage("nosql.mongoDBExercise");
        
        printDocs();
        
        User user = new User("Deniz Can", "ul.Dmochowskiego 6/6 Warszawa");
        
        
        user.addAddressBook(new User("dana","krakow"));
        datastore.save(user);
        
        System.out.println("after write");
        printDocs();
    
        
        System.out.println(datastore.get(user).toString());
        
        
    }
    
    private static void printDocs() {
        Iterator it = datastore.getDB().getCollection("user").find();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
    
}
