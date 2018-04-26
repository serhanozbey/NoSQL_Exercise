package com.serhan.nosql;

//Creating first app connecting to google cloud database.

/*https://stackoverflow.com/questions/20015464/can-i-use-googles-datastore-for-a-desktop-application*/

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

import java.io.FileInputStream;

/*  1. Sign up and enable API
 https://cloud.google.com/datastore/docs/activate#service_account
 */
public class App {
    
    /*https://stackoverflow.com/questions/20015464/can-i-use-googles-datastore-for-a-desktop-application*/
    
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    
    public static void main(String[] args) throws Exception{
        Datastore datastore = DatastoreOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("C:\\Users\\PLSEOZB\\Desktop\\JAVA_WORKS\\nosqllibs\\src\\main\\resources\\First Application-d0e1da1820bd.json")))
                .setProjectId("nosql-libs")
                .build()
                .getService();
        System.out.println(datastore);
        
        
    }
    
}
