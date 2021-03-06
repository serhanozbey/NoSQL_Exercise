package nosql.googleclouddatastoreExercise2;

import com.googlecode.objectify.annotation.*;

@Entity
public class Person {
    
    @Id Long id;
    String name;
    int age;
    @Load
    Item item;
    @Ignore
    int irrelevant;
    
    public Person() {
    }
    
    public Person(String name, int age, Item item) {
        this.name = name;
        this.age = age;
        this.item = item;
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "userId=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", item=" + item +
                '}';
    }
}
