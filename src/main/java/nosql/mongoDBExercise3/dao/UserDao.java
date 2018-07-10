package nosql.mongoDBExercise3.dao;

import nosql.mongoDBExercise3.model.User;

public interface UserDao {
    
    User get(String username);
    void add(User user);
    void update(User user);
    void delete(User user);
    
}
