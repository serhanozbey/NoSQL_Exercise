package nosql.mongoDBExercise3;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "posts", noClassnameStored = true)
public class Post {
 
    @Id
    protected ObjectId id;
    protected String body;
    protected String author;
    protected ObjectId uid;
    
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
}
