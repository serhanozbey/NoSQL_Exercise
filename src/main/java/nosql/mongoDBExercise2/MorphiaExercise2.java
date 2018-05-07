package nosql.mongoDBExercise2;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import nosql.mongoDBExercise.MorphiaExercise;
import nosql.mongoDBExercise.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Iterator;

/*Referencing example, creating addressBook and referencing to the only ID of the other Users.*/

public class MorphiaExercise2 {

    static private final MongoClientURI mongoURI = new MongoClientURI("mongodb://localhost:27017");
    static private Morphia morphia;
    static private Datastore datastore;

    public static void main(String[] args) {

        setupMorphia();


        UserEntry userEntry1 = new UserEntry("serhan", "krakow");
        UserEntry userEntry2 = new UserEntry("yunus", "warsaw");

        datastore.save(userEntry2);
        userEntry1.addAddressBook(userEntry2);

        System.out.println(userEntry1);


        datastore.save(userEntry1);

        printDocs();

    }

    public static void setupMorphia() {
        morphia = new Morphia();
        datastore = morphia.createDatastore(new MongoClient(mongoURI), "admin");
        morphia.mapPackage("nosql.mongoDBExercise");
    }

    private static void printDocs() {

        Iterator it = datastore.getDB().getCollection("addressEntries").find();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }


}
