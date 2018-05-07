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


    public static void main(String[] args) {

        MorphiaExercise.setupMorphia();


        UserEntry userEntry1 = new UserEntry("serhan", "krakow");
        UserEntry userEntry2 = new UserEntry("yunus", "warsaw");

        MorphiaExercise.getDatastore().save(userEntry2);
        userEntry1.addAddressBook(userEntry2);

        System.out.println(userEntry1);


        MorphiaExercise.getDatastore().save(userEntry1);

        printDocs();

    }

    private static void printDocs() {

        Iterator it = MorphiaExercise.getDatastore().getDB().getCollection("addressEntries").find();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }


}
