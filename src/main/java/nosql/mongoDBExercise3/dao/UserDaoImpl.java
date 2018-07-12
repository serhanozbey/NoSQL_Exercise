package nosql.mongoDBExercise3.dao;

import nosql.mongoDBExercise3.model.User;
import nosql.mongoDBExercise3.util.MorphiaUtil;
import org.mongodb.morphia.Datastore;

public class UserDaoImpl implements UserDao {
    
    private Datastore datastore;
    
    public UserDaoImpl() {
        this.datastore = MorphiaUtil.getInstance().getDatastore();
    }
    
    @Override
    public User get(String username) {
        return datastore.find(User.class).field("username").equal(username).get();
    }
    
    @Override
    public void add(User user) {
        datastore.save(user);
    }
    
    @Override
    public void update(User user) {
        //TODO: user preferences edit.
    }
    
    @Override
    public void delete(User user) {
        datastore.delete(user);
    }
}
