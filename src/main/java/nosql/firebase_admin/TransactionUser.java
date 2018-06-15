package nosql.firebase_admin;

import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TransactionUser {
    
    
    private String name;
    private String email;
    private int counter;
    
    
    public TransactionUser() {
    }
    
    public TransactionUser(String name, String email) {
        this.name = name;
        this.email = email;
        counter = 1;
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
        return name;
    }
}
