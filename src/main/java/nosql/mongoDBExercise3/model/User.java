package nosql.mongoDBExercise3.model;

import nosql.mongoDBExercise3.util.PassHasher;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

@Entity(value = "user", noClassnameStored = true)
public class User {

    @Id
    public ObjectId id;
    @Indexed( options = @IndexOptions(unique = true))
    public String username;
    public String password;
    public String email;
    
    public User() {
    }
    
    public User(String username, String password, String email) {
        this.id = new ObjectId();
        this.password = PassHasher.hashPassword(password);
        this.username = username;
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "Username: " + username + "\nEmail: " + email;
    }
}
