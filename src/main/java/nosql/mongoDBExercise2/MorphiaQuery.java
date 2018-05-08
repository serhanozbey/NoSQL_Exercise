package nosql.mongoDBExercise2;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import nosql.mongoDBExercise.MorphiaExercise;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DeleteOptions;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryImpl;
import org.mongodb.morphia.query.Sort;

import java.util.*;

public class MorphiaQuery {
    
    private static final MongoClientURI mongoURI = new MongoClientURI("mongodb://localhost:27017");
    private static Morphia morphia;
    private static Datastore datastore;
    
    public static void main(String[] args) {
        morphia = new Morphia();
        datastore = morphia.mapPackage("nosql.mongoDBExercise2").createDatastore(new MongoClient(mongoURI), "admin");
        
        getSequentialIterator();
        
        try {
            addDummyData();
        } catch (Exception e) {
            System.out.println("Already at database, removing: \n");
            removeDummyData();
        }
    }
    
    private static void getSequentialIterator() {
        Iterator<UserEntry> listIterator = datastore.createQuery(UserEntry.class).asList().listIterator();
        
        while (listIterator.hasNext()) {
            UserEntry userEntry = listIterator.next();
            System.out.println(userEntry.toString());
            System.out.println("==================");
        }
    }
    
    private static void addDummyData() {
        List<UserEntry> userEntries = new ArrayList<>();
        userEntries.add(new UserEntry(("brian"), ("izmir")));
        userEntries.add(new UserEntry(("john"), ("urfa")));
        datastore.save(userEntries);
        System.out.println("Entries added.\n");
    }
    
    private static void removeDummyData() {
        Query<UserEntry> q = datastore.createQuery(UserEntry.class);
        List<UserEntry> list = q.order(Sort.descending("_id")).asList(new FindOptions().limit(2));
        System.out.println("Following are removed :\n "+list);
        Iterator<UserEntry> it = list.listIterator();
        list.forEach(i->datastore.delete(i));
    }
    
}
