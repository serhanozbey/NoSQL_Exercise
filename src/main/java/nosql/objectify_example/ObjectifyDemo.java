package nosql.objectify_example;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.datastore.*;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ObjectifyDemo {
    
    static Datastore datastore;
    static KeyFactory keyFactory;
    
    //at this example, we are also storing Item as Item kind. And referencing the same one from Person object. Relational.
    
    public static void main(String[] args) throws Exception {
        
        
        setupDatastore();
        keyFactory = datastore.newKeyFactory().setKind("Person");
        
        Key key1 = datastore.allocateId(keyFactory.newKey());
    
    
        Item pen = new Item("Apple Pen", 5, 25);
        
        ObjectifyService.init(new ObjectifyFactory(datastore));
        ObjectifyService.register(Person.class);
        ObjectifyService.register(Item.class);
        ObjectifyService.begin();
        ofy().save().entity(pen).now();
        ofy().save().entity(new Person("Serhan", 25, pen)).now();
    
        System.out.println(ofy().load().type(Person.class).id(6293031984562176L).now());
        
    }
    
    /**
     * Builds a datastore connection and returns Datastore object.
     * @throws IOException
     */
    public static void setupDatastore() throws IOException {
        Path path = Paths.get("./src/resources/First Application-d0e1da1820bd.json");
        datastore = DatastoreOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(path.toString())))
                .setProjectId("first-application-202308")
                .build()
                .getService();
        System.out.println(datastore);
    }
    
}
