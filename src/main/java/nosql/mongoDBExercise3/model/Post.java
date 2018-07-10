package nosql.mongoDBExercise3.model;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "posts", noClassnameStored = true)
public class Post {
 
    @Id
    private ObjectId id;
    private String body;
    private String author;
    private ObjectId uid;
    
    public Post() {
    }
    
    //TODO: Change with Post(User user, String body) and assign author name and uid later on from User object.
    /*public Post(String author, String body, ObjectId uid) {
        this.author = author;
        this.body = body;
        this.uid = uid;
    }*/
    public Post(User user, String body) {
        this.id = new ObjectId();
        this.author = user.username;
        this.uid = user.id;
        this.body = body;
    }
    
    public ObjectId getId() {
        return id;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public ObjectId getUid() {
        return uid;
    }
    
    @Override
    public String toString() {
        return body + "\n-from: " + author +"------------\n";
    }
    
    
    
}
