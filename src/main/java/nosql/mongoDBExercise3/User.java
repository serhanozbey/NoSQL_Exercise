package nosql.mongoDBExercise3;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "user", noClassnameStored = true)
public class User {

    @Id
    protected String id;
    protected String username;
    protected String email;
    
    public User() {
    }
    
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
