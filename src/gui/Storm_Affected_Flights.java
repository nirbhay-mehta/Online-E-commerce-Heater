package gui;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import java.net.UnknownHostException;

public class Storm_Affected_Flights 
{
	
	public static void main(String[] args) 
	{	  
	    try 
	    {
	    MongoClient mongo = new MongoClient("localhost", 27017);
	    DB db = mongo.getDB("airlinedb");
	    DBCollection collection = db.getCollection("airlineCol");
	    
	    DBObject Origin_State = new BasicDBObject("ORIGIN_STATE_ABR", "NY");
	    DBObject Dest_State = new BasicDBObject("DEST_STATE_ABR", "NY");
	    BasicDBList or = new BasicDBList();
	    or.add(Origin_State);
	    or.add(Dest_State);
	    DBObject match = new BasicDBObject("$match", new BasicDBObject("CANCELLATION_CODE","B").append("MONTH", 1).append("$or", or));
	    DBObject group = new BasicDBObject("$group",new BasicDBObject("_id","$DAY_OF_MONTH").append("sum", new BasicDBObject("$sum", 1)));
	    DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id", 1));
	    
	    @SuppressWarnings("deprecation")
		AggregationOutput output = collection.aggregate(match,group,sort);
	    
	    for (DBObject result : output.results()) 
	    {
	    	 System.out.println(result);
	    }
	    }

	    catch (UnknownHostException e) {
	        e.printStackTrace();
	        } catch (MongoException e) {
	        e.printStackTrace();
	}
}
}
