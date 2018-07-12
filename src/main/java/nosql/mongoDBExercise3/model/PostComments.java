package nosql.mongoDBExercise3.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "post-comments",noClassnameStored = true)
public class PostComments {
    @Id
    private ObjectId postId;
    private String postBody;
    @Embedded
    private List<Comment> comments;
    
    public PostComments() {
    }
    
    public PostComments(Post postToComment, Comment comment) {
        comments = new ArrayList<>();
        this.postId = postToComment.getId();
        this.postBody = postToComment.getBody();
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
    
    public ObjectId getPostId() {
        return postId;
    }
    
    public void setPostId(ObjectId postId) {
        this.postId = postId;
    }
    
    public String getPostBody() {
        return postBody;
    }
    
    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
