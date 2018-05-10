package nosql.mongoDBExercise3;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(noClassnameStored = true)
public class Comment {
    
    @Id
    protected ObjectId postId;
    protected String author;
    protected String text;
    protected ObjectId userId;
    
    public Comment() {
    }
    
    public Comment(User commentingUser, String comment) {
        this.postId = new ObjectId();
        this.userId = commentingUser.id;
        this.author = commentingUser.username;
        this.text = comment;
    }
}
