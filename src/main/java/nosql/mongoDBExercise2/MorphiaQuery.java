package nosql.mongoDBExercise2;

import nosql.mongoDBExercise.MorphiaExercise;

import java.util.Iterator;

public class MorphiaQuery {
    
    public static void main(String[] args) {
        
        MorphiaExercise.setupMorphia();
        
        Iterator it = MorphiaExercise.getDatastore().getDB().getCollection("addressEntries").find().iterator();
        
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
        
        
    }
    
}
