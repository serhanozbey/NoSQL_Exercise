package com.serhan.nosql.objectify_example;

import com.googlecode.objectify.Ref;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", item=" + item +
                '}';
    }
}
