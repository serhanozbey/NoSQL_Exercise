package nosql.mongoDBExercise3.util;

import com.mongodb.DBObject;
import nosql.mongoDBExercise3.model.Post;
import nosql.mongoDBExercise3.model.PostComments;
import nosql.mongoDBExercise3.model.User;
import nosql.mongoDBExercise3.model.UserPost;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;
import java.util.Scanner;

public class PostUtil {
    
    //POST
    
    public static Post savePost(User user) {
        Datastore datastore = MorphiaUtil.getInstance().getDatastore();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter post");
        Post post = new Post(user, scanner.nextLine());
        datastore.save(post);
        //add or update to user-posts
        UpdateOperations<UserPost> ops = datastore
                .createUpdateOperations(UserPost.class)
                .addToSet("posts", post);
        datastore.update(datastore.find(UserPost.class), ops, true);
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
