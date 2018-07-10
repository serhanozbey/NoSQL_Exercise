package nosql.mongoDBExercise3.dao;

import nosql.mongoDBExercise3.model.Comment;

import java.util.List;

public interface CommentDao {
    
    List<Comment> getAll();
    Comment get();
    void save();
    void update();
    void delete();
}
