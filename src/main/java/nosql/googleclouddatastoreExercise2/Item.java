package nosql.googleclouddatastoreExercise2;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Item {
    
    //For now, just a POJO that we want to store in every entry.
    
    /*To make a table with Items, study:
    * https://github.com/objectify/objectify/wiki/Entities#relationships*/
    @Id Long id;
    String name;
    int quantity;
    int cost;
    
    
    public Item() {
    }
    
    public Item(String name, int quantity, int cost) {
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
    }
    
    @Override
    public String toString() {
        return "Item{" +
                "userId=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", cost=" + cost +
                '}';
    }
}
