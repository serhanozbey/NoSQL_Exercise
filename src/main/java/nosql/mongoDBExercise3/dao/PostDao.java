package nosql.mongoDBExercise3.dao;

import nosql.mongoDBExercise3.model.Post;

import java.util.List;

public interface PostDao {
    
    
    List<Post> getAll();
    void get();
    void add();
    void update();
    void delete();
    
}
