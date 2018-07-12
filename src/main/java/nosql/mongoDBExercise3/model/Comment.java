package nosql.mongoDBExercise3.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(noClassnameStored = true)
public class Comment {
    
    @Id
    private ObjectId postId;
    private String author;
    private String text;
    private ObjectId userId;
    
    public Comment() {
    }
    
    public Comment(User commentingUser, String comment) {
        this.postId = new ObjectId();
        this.userId = commentingUser.id;
        this.author = commentingUser.username;
        this.text = comment;
    }
    
    public ObjectId getPostId() {
        return postId;
    }
    
    public void setPostId(ObjectId postId) {
        this.postId = postId;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public ObjectId getUserId() {
        return userId;
    }
    
    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return text;
    }
}
