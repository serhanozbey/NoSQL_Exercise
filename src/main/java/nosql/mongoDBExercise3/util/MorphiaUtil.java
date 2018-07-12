package nosql.mongoDBExercise3.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorphiaUtil {
    
    private final MongoClientURI mongoURI = new MongoClientURI("..");
    private final Morphia morphia = new Morphia();
    private final Datastore datastore = morphia.mapPackage("nosql.mongoDBExercise3.model").createDatastore(new MongoClient(mongoURI), "exercise3");
    
    
    //constructor start- double checked lazy initialization for multi-threading
    private volatile static MorphiaUtil morphiaUtil;
    
    private MorphiaUtil(){}
    
    public static MorphiaUtil getInstance() {
        if (morphiaUtil == null) {
            synchronized (MorphiaUtil.class) {
                if (morphiaUtil == null) {
                    morphiaUtil = new MorphiaUtil();
                }
            }
        }
        return morphiaUtil;
    }
    //constructor end
    
    public MongoClientURI getMongoURI() {
        return mongoURI;
    }
    
    public Morphia getMorphia() {
        return morphia;
    }
    
    public Datastore getDatastore() {
        return datastore;
    }
}
