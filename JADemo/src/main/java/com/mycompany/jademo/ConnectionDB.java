package com.mycompany.jademo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
/**
 *
 * @author Nano
 */
public class ConnectionDB {
     public static Mongo mongo;
     public static DB db;
     public static DBCollection table;
     public static DBCursor cursor;
     
     public static void conn(){
         mongo = new Mongo("localhost", 27017);
         db = mongo.getDB("nanoTez"); 
     } 
     
     public static DBCollection getColl(String collName){
         table = db.getCollection(collName);
         return table;
     } 
     
      public static DBCursor getCollFind(String collName){
         cursor = db.getCollection(collName).find();
         return cursor;
     }
      
      public static DBCollection connIsExists(String collName){
         conn();
        
        if (!db.collectionExists(collName)) {
            db.createCollection(collName, null);
        }
        table = db.getCollection(collName);
         table.drop(); 
        return table;
      }
     
     //public static void main(String[] args) { }  
}
