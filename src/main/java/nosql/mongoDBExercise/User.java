package nosql.mongoDBExercise;




import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;


@Entity(value = "user",noClassnameStored = true)
public class User{
    
    @Id
    private ObjectId id;
    private String name;
    private String address;
    //private BSONTimestamp timestamp;
    @Embedded
    private List<User> addressBook;
    
    public User() {
    }
    
    public User(String name, String address) {
        this.name = name;
        this.address = address;
        this.addressBook = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public List<User> getAddressBook() {
        return addressBook;
    }
    
    public void addAddressBook(User user) {
        addressBook.add(user);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", addressBook=" + addressBook +
                '}';
    }
}
