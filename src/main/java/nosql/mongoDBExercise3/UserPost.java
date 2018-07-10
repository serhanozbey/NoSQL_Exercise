package nosql.mongoDBExercise3;

import nosql.mongoDBExercise3.model.Post;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "user-posts",noClassnameStored = true)
public class UserPost {
//TODO: To be removed.
    @Id
    private ObjectId userId;
    @Embedded
    private List<Post> posts;
    
    public UserPost() {
    }
    
    public UserPost(Post post) {
        posts = new ArrayList<>();
        this.userId = post.getUid();
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
    
    public ObjectId getUserId() {
        return userId;
    }
    
    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
    
    public List<Post> getPosts() {
        return posts;
    }
    
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
