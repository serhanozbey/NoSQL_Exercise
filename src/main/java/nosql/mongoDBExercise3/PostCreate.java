package nosql.mongoDBExercise3;

import com.mongodb.*;
import com.mongodb.operation.UpdateOperation;
import org.bson.types.ObjectId;
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
        
        //saving to admin.posts
        Post post = new Post(user, "melabaa");
        datastore.save(post);
        
        //saving to admin.user-posts
        Query<UserPost> currentUserPost = datastore.find(UserPost.class, "_id", user.id).disableValidation();
        //creating with checking if theres already userPost existing
        UserPost userPost;
        if (currentUserPost != null) {
            userPost = currentUserPost.get();
        } else {
            userPost = new UserPost(post);
            
        }
        
        userPost.addPost(post);
        UpdateOperations ops = datastore
                .createUpdateOperations(UserPost.class)
                .addToSet("posts", post);
        datastore.update(currentUserPost, ops, true);
        
        //saving post comments
        post = datastore.find(Post.class, "id", new ObjectId("5af45904dad2243dd434245c")).get();
        
        Comment comment = new Comment(user, "sanada melaba");
    
        Query<PostComments> currentPostComments = datastore.find(PostComments.class, "_id", post.id).disableValidation();
        
        PostComments pc;
    
        if (currentPostComments.get() != null) {
            pc = currentPostComments.get();
        } else {
            pc = new PostComments(post,comment);
        }
    
        pc.addComment(comment);
        UpdateOperations ops2 = datastore
                .createUpdateOperations(PostComments.class)
                .addToSet("comments", comment);
        datastore.update(currentPostComments, ops2, true);
        
    }
    
}
