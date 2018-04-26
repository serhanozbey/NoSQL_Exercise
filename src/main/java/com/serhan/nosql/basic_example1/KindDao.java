package com.serhan.nosql.basic_example1;


/*https://stackoverflow.com/questions/20015464/can-i-use-googles-datastore-for-a-desktop-application*/

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class KindDao {
    
    /*https://stackoverflow.com/questions/20015464/can-i-use-googles-datastore-for-a-desktop-application*/
    static Datastore datastore;
    static KeyFactory keyFactory;
    
    public static void main(String[] args) throws Exception {
        /**/
        datastore = DatastoreOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("C:\\Users\\PLSEOZB\\Desktop\\JAVA_WORKS\\nosqllibs\\src\\main\\resources\\First Application-d0e1da1820bd.json")))
                .setProjectId("first-application-202308")
                .build()
                .getService();
        System.out.println(datastore);
        
        
        keyFactory = datastore.newKeyFactory().setKind("Task");
        Key key1 = addTask("Jumping");
        Key key2 = addTask("Climbing");
    
        Iterator<Entity> it = listTasks();
        while (it.hasNext()) {
            Entity e = it.next();
            markDone(e.getKey().getId());
            System.out.println(e);
        }
        
        /*it = listTasks();
        while (it.hasNext()) {
            Entity e = it.next();
            System.out.println(e);
            deleteTask(e.getKey().getId());
        }*/
    }
    
    static Key addTask(String description) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity task = Entity.newBuilder(key)
                .set("description", StringValue.newBuilder(description).setExcludeFromIndexes(true).build())
                .set("created", Timestamp.now())
                .set("done", false)
                .build();
        datastore.put(task);
        return key;
    }
    
    static void deleteTask(long id) {
        datastore.delete(keyFactory.newKey(id));
    }
    
    static Iterator<Entity> listTasks() {
        Query<Entity> query =
                Query.newEntityQueryBuilder().setKind("Task").setOrderBy(StructuredQuery.OrderBy.asc("created")).build();
        return datastore.run(query);
    }
    
    static boolean markDone(long id) {
        Transaction transaction = datastore.newTransaction();
        try {
            Entity task = transaction.get(keyFactory.newKey(id));
            if (task != null) {
                transaction.put(Entity.newBuilder(task).set("done", true).build());
            }
            transaction.commit();
            return task != null;
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }
}
