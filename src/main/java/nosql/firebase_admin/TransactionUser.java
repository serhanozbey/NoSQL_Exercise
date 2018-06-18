package nosql.firebase_admin;

import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

import java.time.Instant;
import java.util.Objects;

@IgnoreExtraProperties
public class TransactionUser {
    
    //adding creation timestamp
    private String createdAt;
    private String name;
    private String email;
    private int counter;
    
    
    public TransactionUser() {
    }
    
    public TransactionUser(String name, String email) {
        this.name = name;
        this.email = email;
        counter = 1;
        createdAt = String.valueOf(Instant.now());
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getCounter() {
        return counter;
    }
    
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    @Override
    public String toString() {
        return "TransactionUser{" +
                "name='" + name + '\'' +
                '}';
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionUser that = (TransactionUser) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
