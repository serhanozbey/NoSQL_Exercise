package nosql.mongoDBExercise;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Iterator;

public class MorphiaExercise {
    
    public static final MongoClientURI mongoURI = new MongoClientURI("mongodb://localhost:27017");
    public static Morphia morphia;
    public static Datastore datastore;
    public static void main(String[] args) {

        setupMorphia();
        
        printDocs();
        
        User user = new User("Deniz Can", "ul.Dmochowskiego 6/6 Warszawa");
        
        
        user.addAddressBook(new User("dana","krakow"));
        datastore.save(user);
        
        System.out.println("after write");
        printDocs();
    
        
        System.out.println(datastore.get(user).toString());
        
        
    }

    public static void setupMorphia() {
        morphia = new Morphia();
        datastore = morphia.createDatastore(new MongoClient(mongoURI), "admin");
        morphia.mapPackage("nosql.mongoDBExercise");
    }

    public static void printDocs() {
        Iterator it = datastore.getDB().getCollection("user").find();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public static MongoClientURI getMongoURI() {
        return mongoURI;
    }

    public static Morphia getMorphia() {
        return morphia;
    }

    public static Datastore getDatastore() {
        return datastore;
    }
}
