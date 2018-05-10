package nosql.mongoDBExercise3;

import com.mongodb.*;
import com.mongodb.operation.UpdateOperation;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.List;

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
        } catch (DuplicateKeyException e) {
            //if exists, gets and assigns proper userId to user.
            user.id = datastore.find(User.class).field("username").equal("deniz").get().id;
        }
        
        //TODO: change with User.username
        //TODO: change with User.getId
        Post post = new Post(user, "melabaa");
        
        datastore.save(post);
    
        Query<UserPost> currentUserPost = datastore.find(UserPost.class, "_id", user.id).disableValidation();
        UserPost userPost = currentUserPost.get();
    
        userPost.addPost(post);
        UpdateOperations ops = datastore
                .createUpdateOperations(UserPost.class)
                .addToSet("posts", post);
        datastore.update(currentUserPost, ops,true);
    
    
        
        /*Comment comment = new Comment(post, user,"sanada selamlar denuz");
        
        datastore.save(comment);
        */
    }
    
}
