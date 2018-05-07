package nosql.mongoDBExercise2;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "addressEntries", noClassnameStored = true)
public class UserEntry {

    @Id
    private ObjectId id;
    private String name;
    private String address;
    @Reference(idOnly = true)
    private List<UserEntry> addressBook;

    public UserEntry() {
        //for db engines
    }

    public UserEntry(String name, String address) {
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

    public List<UserEntry> getAddressBook() {
        return addressBook;
    }

    public void addAddressBook(UserEntry userEntry) {
        addressBook.add(userEntry);
    }

    @Override
    public String toString() {
        return "UserEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", addressBook=" + addressBook +
                '}';
    }
}
