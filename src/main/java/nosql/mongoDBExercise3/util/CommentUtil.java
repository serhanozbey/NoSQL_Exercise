package nosql.mongoDBExercise3.util;

import nosql.mongoDBExercise3.PostComments;
import nosql.mongoDBExercise3.model.Comment;
import nosql.mongoDBExercise3.model.Post;
import nosql.mongoDBExercise3.model.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Scanner;

public class CommentUtil {
    
    
    
    public static void saveComment(User user, Post post) {
        Datastore datastore = MorphiaUtil.getInstance().getDatastore();
        Scanner scanner = new Scanner(System.in);
        post = datastore.find(Post.class, "id", new ObjectId(post.getId().toHexString())).get();
        System.out.println("\nEnter first comment to your post");
        Comment comment = new Comment(user, scanner.nextLine());
        
        Query<PostComments> currentPostComments = datastore.find(PostComments.class, "_id", post.getId()).disableValidation();
        
        PostComments pc;
        
        if (currentPostComments.get() != null) {
            pc = currentPostComments.get();
        } else {
            pc = new PostComments(post, comment);
            datastore.save(pc);
        }
        
        pc.addComment(comment);
        UpdateOperations<PostComments> ops2 = datastore
                .createUpdateOperations(PostComments.class)
                .addToSet("comments", comment);
        datastore.update(currentPostComments, ops2, true);
        
        System.out.println("Comment saved successfully.\n");
    }
    
}
