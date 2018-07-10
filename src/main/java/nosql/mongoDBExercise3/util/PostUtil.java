package nosql.mongoDBExercise3.util;

import com.mongodb.DBObject;
import nosql.mongoDBExercise3.PostComments;
import nosql.mongoDBExercise3.UserPost;
import nosql.mongoDBExercise3.model.Post;
import nosql.mongoDBExercise3.model.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;
import java.util.Scanner;

public class PostUtil {
    
    //POST
    
    //TODO: To be removed, this should be done within PostDao- savePost.
    public static void saveUserPost(User user, Post post) {
        Datastore datastore = MorphiaUtil.getInstance().getDatastore();
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
        Datastore datastore = MorphiaUtil.getInstance().getDatastore();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter post");
        Post post = new Post(user, scanner.nextLine());
        datastore.save(post);
        saveUserPost(user, post);
        return post;
    }
    
    public static void printAllPosts() {
        Datastore datastore = MorphiaUtil.getInstance().getDatastore();
        List<DBObject> list = datastore.getDB().getCollection("posts").find().toArray();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + ": " + MorphiaUtil.getInstance().getMorphia().fromDBObject(datastore, Post.class, list.get(i)));
        }
    }
    
    public static List<DBObject> getAllPosts() {
        return MorphiaUtil.getInstance().getDatastore().getDB().getCollection("posts").find().toArray();
    }
    
    public static void printAllPostsAndComments() {
        Datastore datastore = MorphiaUtil.getInstance().getDatastore();
        List<DBObject> postsList = datastore.getDB().getCollection("posts").find().toArray();
        for (int i = 0; i < postsList.size(); i++) {
            Post post = MorphiaUtil.getInstance().getMorphia().fromDBObject(datastore, Post.class, postsList.get(i));
            System.out.println((i + 1) + ": " + post);
            
            PostComments postComments = datastore.find(PostComments.class).disableValidation().field("_id").equal(post.getId()).get();
            if (postComments != null) {
                for (int a = 0; a < postComments.getComments().size(); a++) {
                    System.out.println("\t" + (a + 1) + ": " + postComments.getComments().get(a));
                }
            }
            System.out.println();
        }
    }
}
