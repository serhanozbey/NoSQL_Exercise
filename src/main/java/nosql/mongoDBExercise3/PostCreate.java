package nosql.mongoDBExercise3;

import com.mongodb.InsertOptions;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import nosql.mongoDBExercise2.UserEntry;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/*Database configured with added index of unique username*/
public class PostCreate {
    
    private static final MongoClientURI mongoURI = new MongoClientURI("mongodb://localhost:27017");
    private static Morphia morphia;
    private static Datastore datastore;
    
    public static void main(String[] args) {
    
        morphia = new Morphia();
        datastore = morphia.mapPackage("nosql.mongoDBExercise2").createDatastore(new MongoClient(mongoURI), "admin");
    
        User user = new User("deniz", "deniz@gmail.com");
        try {
            datastore.save(user);
        } catch (Exception e) {
            //if exists, gets and assigns proper id to user.
            user.id = datastore.find(User.class).field("username").equal("deniz").get().id;
        }
        
        //TODO: change with User.username
        //TODO: change with User.getId
        Post post = new Post(user,"melabaa");
    
    
        datastore.save(post);
        
        
    }
    
}
