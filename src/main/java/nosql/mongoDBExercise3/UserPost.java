package nosql.mongoDBExercise3;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "user-posts",noClassnameStored = true)
public class UserPost {
//TODO: To be removed.
    @Id
    protected ObjectId userId;
    @Embedded
    protected List<Post> posts;
    
    public UserPost() {
    }
    
    public UserPost(Post post) {
        posts = new ArrayList<>();
        this.userId = post.uid;
        this.posts.add(post);
    }
    
    public void addPost(Post post){
        posts.add(post);
    }
    
    @Override
    public String toString() {
        return "UserPost{" +
                "userId=" + userId +
                ", posts=" + posts +
                '}';
    }
}
