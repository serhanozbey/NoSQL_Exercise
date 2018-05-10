package nosql.mongoDBExercise2;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexDirection;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(value = "addressEntries", noClassnameStored = true)
public class UserEntry {
    
    @Id
    protected ObjectId id;
    //FIXME: unique not working
    @Indexed(options = @IndexOptions(unique = true))
    protected String name;
    private String address;
    @Reference(idOnly = true)
    private List<UserEntry> addressBook;
    
    public UserEntry() {
        //for db engines
    }
    
    public UserEntry(String name, String address) {
        this.id = new ObjectId();
        this.name = name;
        this.address = address;
        this.addressBook = new ArrayList<>();
        System.out.println("UserEntry{" +
                "userId=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", addressBook=" + addressBook +
                '}');
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
    
    public List<UserEntry> getAddressBook() {
        return addressBook;
    }
    
    public void addAddressBook(UserEntry userEntry) {
        if (!userEntry.equals(this)&&!addressBook.contains(userEntry)) {
            addressBook.add(userEntry);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserEntry userEntry = (UserEntry) o;
    
        return name.equals(userEntry.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        
        return "UserEntry{" +
                "userId=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", addressBook=" + addressBook +
                '}';
    }
}
