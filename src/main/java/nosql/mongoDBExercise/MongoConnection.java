package nosql.mongoDBExercise;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;


public class MongoConnection {
    
    public static void main(String[] args) throws UnknownHostException {
        MongoClientURI mongoURI = new MongoClientURI("mongodb://localhost:27017");
        
        MongoClient mongoClient = new MongoClient(mongoURI);
        MongoDatabase db = mongoClient.getDatabase("exercise1");
        
        MongoCollection<Document> users = db.getCollection("user");
        
        User user = new User("Deniz Can", "ul.Dmochowskiego 6/6 Warszawa");
        
        //System.out.println(users.find().first().toString());
        
        BasicDBObject obj = new BasicDBObject("name", "Serhan Ozbey");
        users.insertOne(new Document("User", user));
    
        Iterator it = users.find().iterator();
    
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
        
    }
    
    
}
    

