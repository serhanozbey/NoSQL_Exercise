package nosql.mongoDBExercise3;

import com.mongodb.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Scanner;

/*Database configured with added index of unique username*/
public class PostCreate {
    
    private static final MongoClientURI mongoURI = new MongoClientURI("mongodb://localhost:27017");
    private static Morphia morphia;
    private static Datastore datastore;
    
    public static void main(String[] args) {
        
        morphia = new Morphia();
        datastore = morphia.mapPackage("nosql.mongoDBExercise2").createDatastore(new MongoClient(mongoURI), "admin");
        
        Scanner scanner = new Scanner(System.in);
    
        User user = saveOrGetUser(scanner);
        
        //saving to admin.posts
        Post post = savePost(scanner, user);
        
        //saving to admin.user-posts
        saveUserPost(user, post);
        
        //saving post comments
        savePostComment(scanner, user, post);
        
    }
    
    private static void saveUserPost(User user, Post post) {
        Query<UserPost> currentUserPost = datastore.find(UserPost.class, "_id", user.id).disableValidation();
        //creating with checking if theres already posts of that user existing
        UserPost userPost;
        if (currentUserPost.get() != null) {
            userPost = currentUserPost.get();
        } else {
            userPost = new UserPost(post);
        }
        
        userPost.addPost(post);
        UpdateOperations ops = datastore
                .createUpdateOperations(UserPost.class)
                .addToSet("posts", post);
        datastore.update(currentUserPost, ops, true);
    }
    
    private static Post savePost(Scanner scanner, User user) {
        System.out.println("Enter post");
        Post post = new Post(user, scanner.nextLine());
        datastore.save(post);
        return post;
    }
    
    private static User saveOrGetUser(Scanner scanner) {
        System.out.println("Enter username name, email");
        String username = scanner.nextLine();
        String email = scanner.nextLine();
        User user = new User(username,email);
        try {
            datastore.save(user);
        } catch (DuplicateKeyException e) {
            //if exists, gets and assigns proper userId to user.
            user.id = datastore.find(User.class).field("username").equal(username).get().id;
        }
        return user;
    }
    
    private static void savePostComment(Scanner scanner, User user, Post post) {
        post = datastore.find(Post.class, "id", new ObjectId(post.id.toHexString())).get();
        System.out.println("Enter first comment to your post");
        Comment comment = new Comment(user, scanner.nextLine());
        
        Query<PostComments> currentPostComments = datastore.find(PostComments.class, "_id", post.id).disableValidation();
        
        PostComments pc;
        
        if (currentPostComments.get() != null) {
            pc = currentPostComments.get();
        } else {
            pc = new PostComments(post, comment);
        }
        
        pc.addComment(comment);
        UpdateOperations ops2 = datastore
                .createUpdateOperations(PostComments.class)
                .addToSet("comments", comment);
        datastore.update(currentPostComments, ops2, true);
    }
    
}
