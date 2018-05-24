package nosql.mongoDBExercise3;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "post-comments",noClassnameStored = true)
public class PostComments {
//TODO: To be removed
    @Id
    protected ObjectId postId;
    public String postBody;
    @Embedded
    protected List<Comment> comments;
    
    public PostComments() {
    }
    
    public PostComments(Post postToComment, Comment comment) {
        comments = new ArrayList<>();
        this.postId = postToComment.id;
        this.postBody = postToComment.body;
        this.comments.add(comment);
    }
    
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    
    @Override
    public String toString() {
        return "PostComments{" +
                "comments=" + comments +
                '}';
    }
}
