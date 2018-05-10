package nosql.mongoDBExercise3;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "user", noClassnameStored = true)
public class User {

    @Id
    protected ObjectId id;
    protected String username;
    protected String email;
    
    public User() {
    }
    
    public User(String username, String email) {
        this.id = new ObjectId();
        this.username = username;
        this.email = email;
    }
}
