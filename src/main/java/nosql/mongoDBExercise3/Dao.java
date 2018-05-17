package nosql.mongoDBExercise3;

import com.mongodb.*;
import nosql.mongoDBExercise3.util.PassHasher;
import org.apache.commons.validator.routines.EmailValidator;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;
import java.util.Scanner;

/*Database configured with added index of unique username*/
public class Dao {
    
    //TODO: singleton for Morphia and MongoClient.
    private static MongoClientURI mongoURI;
    private static Morphia morphia;
    private static Datastore datastore;
    
    static {
        setupConnection();
    }
    
    public static void main(String[] args) {
        
        User user = loadUser();
        
        //saving to admin.posts
        Post post = savePost(user);
        //saving to admin.user-posts
        saveUserPost(user, post);
        
        //saving post comments
        savePostComment(user, post);
        
    }
    
    public static void setupConnection() {
        mongoURI = new MongoClientURI("mongodb://localhost:27017");
        morphia = new Morphia();
        datastore = morphia.mapPackage("nosql.mongoDBExercise2").createDatastore(new MongoClient(mongoURI), "admin");
        morphia.mapPackage("nosql.mongoDBExercise3");
    }
    
    public static void saveUserPost(User user, Post post) {
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
    
    public static Post savePost(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter post");
        Post post = new Post(user, scanner.nextLine());
        datastore.save(post);
        saveUserPost(user, post);
        return post;
    }
    
    //TODO: seperate the methods.
    public static User loadUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter username");
        String username = scanner.nextLine();
        Query<User> q = datastore.find(User.class);
        if (q.field("username").equal(username).count() != 0) {
            System.out.println("Enter password");
            String pass = scanner.nextLine();
            String stored_hash = q.project("password", true).get().password;
            if (PassHasher.checkPassword(pass, stored_hash)) {
                return datastore.find(User.class).field("username").equal(username).get();
            }
        }
        return null;
    }
    
    public static User registerUser() {
        User userNew;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter username");
                String user = scanner.nextLine();
                System.out.println("Enter password");
                String pass = scanner.nextLine().trim();
                System.out.println("Enter email");
                String email = scanner.nextLine();
                if(!EmailValidator.getInstance().isValid(email)) throw new Exception();
                userNew = new User(user, pass, email);
                break;
            } catch (Exception e) {
                System.out.println("wrong input, try again");
            }
        }
        datastore.save(userNew);
        System.out.println("Successfully registered.");
        
        return userNew;
    }
    

    
    public static void savePostComment(User user, Post post) {
        Scanner scanner = new Scanner(System.in);
        post = datastore.find(Post.class, "id", new ObjectId(post.id.toHexString())).get();
        System.out.println("\nEnter first comment to your post");
        Comment comment = new Comment(user, scanner.nextLine());
        
        Query<PostComments> currentPostComments = datastore.find(PostComments.class, "_id", post.id).disableValidation();
        
        PostComments pc;
        
        if (currentPostComments.get() != null) {
            pc = currentPostComments.get();
        } else {
            pc = new PostComments(post, comment);
            datastore.save(pc);
        }
        
        pc.addComment(comment);
        UpdateOperations ops2 = datastore
                .createUpdateOperations(PostComments.class)
                .addToSet("comments", comment);
        datastore.update(currentPostComments, ops2, true);
        
        System.out.println("Comment saved successfully.\n");
    }
    
    public static void printAllPosts() {
        List<DBObject> list = datastore.getDB().getCollection("posts").find().toArray();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + ": " + morphia.fromDBObject(datastore, Post.class, list.get(i)));
        }
    }
    
    public static List<DBObject> getAllPosts() {
        return datastore.getDB().getCollection("posts").find().toArray();
    }
    
    public static void printAllPostsAndComments() {
        List<DBObject> postsList = datastore.getDB().getCollection("posts").find().toArray();
        for (int i = 0; i < postsList.size(); i++) {
            Post post = morphia.fromDBObject(datastore, Post.class, postsList.get(i));
            System.out.println((i + 1) + ": " + post);
            
            PostComments postComments = datastore.find(PostComments.class).disableValidation().field("_id").equal(post.id).get();
            if (postComments != null) {
                for (int a = 0; a < postComments.comments.size(); a++) {
                    System.out.println("\t" + (a + 1) + ": " + postComments.comments.get(a));
                }
            }
            System.out.println();
        }
    }
    
    public static Morphia getMorphia() {
        return morphia;
    }
    
    public static Datastore getDatastore() {
        return datastore;
    }
}
