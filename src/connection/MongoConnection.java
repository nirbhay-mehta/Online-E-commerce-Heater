package connection;

import entities.FourColumnResult;
import entities.MyDate;
import entities.Notes;
import entities.FlightResult;
import entities.TwoColumnResult;
import gui.LoginPage;
import gui.Main_Page;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class MongoConnection {

	MongoClient mongo;
	DB db;
	public static int c = 0;
	public static Notes note;
	public static MyDate mydate;
	public FlightResult flightResult;
	public ArrayList<FlightResult> resultList;
	public ArrayList<TwoColumnResult> busyResult;
	public ArrayList<TwoColumnResult> delayCarrier;
	public ArrayList<TwoColumnResult> delayAirport;
	public ArrayList<TwoColumnResult> cancelAirport;
	public ArrayList<TwoColumnResult> cancelCarrier;

	public ArrayList<String> dbOrigin ;
    public ArrayList<String> dbCarrier ;
    public ArrayList<Integer> dbCount;
	// LoginPage lp=new LoginPage();

	public MongoConnection() {

		try {
			mongo = new MongoClient("localhost", 27017);
			db = mongo.getDB("airlinedb");
			// DBCollection collection = db.getCollection("airlineCol");
			// System.out.println("count" + collection.getCount());

			/*
			 * DBObject group = new BasicDBObject("$group", new BasicDBObject(
			 * "_id", "$ORIGIN_CITY_NAME")).append("delay", new
			 * BasicDBObject("$avg", "DEP_DELAY_NEW")); DBObject sort = new
			 * BasicDBObject("$sort", new BasicDBObject( "delay", -1)); DBObject
			 * limit = new BasicDBObject("$limit", 10);
			 * System.out.println("before");
			 * 
			 * @SuppressWarnings("deprecation") AggregationOutput output =
			 * collection.aggregate(group, sort, limit);
			 * System.out.println("after");
			 */

			// BasicDBObject searchQuery = new BasicDBObject("ORIGIN","JFK");

			// DBCursor cursor = collection.find(searchQuery);
			// while (cursor.hasNext()) {
			// System.out.println(cursor.next());
			// }

			// for (DBObject result : output.results()) {
			// System.out.println(result);
			// }

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// }

	public void insertLoginInfo(String pUserName, String pPassword) {
		DBCollection coll = db.getCollection("customer_info");

		BasicDBObject doc = new BasicDBObject("username", pUserName).append(
				"password", pPassword);
		System.out.println(doc);
		coll.insert(doc);

	}

	public boolean checkExistingUsername(String pUserName) {
		return true;
	}

	public boolean validatePassword(String pUserName, String pPassword) {
		return true;
	}

	public void updateNotes(String pSource, String pDestination, int month,
			int day, int year) {
		c = c + 1;
		System.out.println("in main  " + month + "  " + day + "  " + year);
		DBCollection notesCollection = db.getCollection("customer_info");
		// DBCollection currentDoc = db.getCollection("customer_info");

		DBObject query = new BasicDBObject("username", LoginPage.userName);

		DBObject doc = new BasicDBObject().append(
				"notes",
				new BasicDBObject("source", pSource).append("destination",
						pDestination).append(
						"date",
						new BasicDBObject("month", month).append("day", day)
								.append("year", year)));

		System.out.println(LoginPage.userName);
		System.out.println(query.toString());
		System.out.println(doc);
		DBObject update = new BasicDBObject("$set", doc);

		notesCollection.update(query, update);

	}

	public void getCustomerInfoNotes(String username) {
		note = new Notes();
		mydate = new MyDate();
		// System.out.println();
		DBCollection collection = db.getCollection("customer_info");
		BasicDBObject query = new BasicDBObject("username", username);
		DBObject dbNote = collection.findOne(query);
		DBObject notes = (DBObject) dbNote.get("notes");
		DBObject dateNote = (DBObject) notes.get("date");
		note.setSrcHistory(notes.get("source").toString());
		note.setDestHistory(notes.get("destination").toString());
		mydate.setDay(Integer.parseInt(dateNote.get("day").toString()));
		mydate.setMonth(Integer.parseInt(dateNote.get("month").toString()));
		mydate.setYear(Integer.parseInt(dateNote.get("year").toString()));
		System.out.println(mydate.getMonth() + " " + mydate.getDay() + " "
				+ mydate.getYear());
		// System.out.println(note.getSrcHistory());
		// System.out.println(note.getDestHistory());

	}

	public void searchDirectFlights(String source, String destination,
			int month, int day, int year) {

		resultList = new ArrayList<FlightResult>();

		DBCollection collection = db.getCollection("airlineCol");
		BasicDBObject searchquery = new BasicDBObject("ORIGIN_CITY_NAME",
				source).append("DEST_CITY_NAME", destination)
				.append("MONTH", month + 1).append("DAY_OF_MONTH", day)
				.append("YEAR", year);
		DBCursor directFl = collection.find(searchquery);
		//directFl.sort(new BasicDBObject("PRICE",-1));
		for (int i = 0; i < directFl.count(); i++) {
			flightResult = new FlightResult();
			DBObject temp = directFl.next();
			// System.out.println(((Number)temp.get("AIRLINE_ID")).intValue());

			flightResult.setAirlineId((((Number) temp.get("AIRLINE_ID"))
					.intValue()));
			flightResult.setArrTime(((Number) (temp.get("ARR_TIME")))
					.intValue());
			flightResult.setDepTime(((Number) (temp.get("DEP_TIME")))
					.intValue());
			flightResult.setFlightDuration(((Number) (temp.get("AIR_TIME")))
					.intValue());
			flightResult.setCarrier(temp.get("CARRIER").toString());
			flightResult.setDestAirport(temp.get("DEST").toString());
			flightResult.setDestCity(temp.get("DEST_CITY_NAME").toString());
			flightResult.setFlightDate(temp.get("FL_DATE").toString());
			flightResult.setOriginAirport(temp.get("ORIGIN").toString());
			flightResult.setOriginCity(temp.get("ORIGIN_CITY_NAME").toString());
			flightResult.setPrice(((Number) (temp.get("PRICE"))).intValue());

			resultList.add(i, flightResult);

			System.out.println(flightResult.getArrTime());

		}
		for (int i = 0; i < resultList.size(); i++) {
			System.out.println("inside resultlist : "
					+ resultList.get(i).getArrTime());

		}

		System.out.println(resultList.size());
		// return resultList;

	}

	@SuppressWarnings("deprecation")
	public void getBusiestAirports() {
		busyResult = new ArrayList<TwoColumnResult>();
		DBCollection collection = db.getCollection("airlineCol");
		System.out.println("count" + collection.getCount());

		TwoColumnResult tempObj;
		DBObject group = new BasicDBObject("$group", new BasicDBObject("_id",
				"$ORIGIN_CITY_NAME").append("flew",
				new BasicDBObject("$sum", 1)));
		DBObject sort = new BasicDBObject("$sort",
				new BasicDBObject("flew", -1));
		DBObject limit = new BasicDBObject("$limit", 10);

		AggregationOutput output = collection.aggregate(group, sort, limit);

		for (DBObject result : output.results()) {
			// System.out.println(result.toString());
			tempObj = new TwoColumnResult();
			tempObj.setName(result.get("_id").toString());
			tempObj.setCount(((Number) result.get("flew")).intValue());
			busyResult.add(tempObj);
		}

		for (int i = 0; i < busyResult.size(); i++) {
			System.out.println(busyResult.get(i).getName() + "  "
					+ busyResult.get(i).getCount());
		}

	}

	public void getTopDelayedCarrier() {

		delayCarrier = new ArrayList<TwoColumnResult>();
		DBCollection collection = db.getCollection("airlineCol");
		// System.out.println("count" + collection.getCount());

		TwoColumnResult tempObj;
		DBObject group = new BasicDBObject("$group", new BasicDBObject("_id",
				"$CARRIER").append("delay", new BasicDBObject("$avg",
				"$DEP_DELAY_NEW")));
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("delay",
				-1));
		DBObject limit = new BasicDBObject("$limit", 10);

		AggregationOutput output = collection.aggregate(group, sort, limit);

		for (DBObject result : output.results()) {

			tempObj = new TwoColumnResult();
			tempObj.setName(result.get("_id").toString());
			tempObj.setCount(((Number) result.get("delay")).intValue());
			delayCarrier.add(tempObj);
		}

		for (int i = 0; i < delayCarrier.size(); i++) {
			System.out.println(delayCarrier.get(i).getName() + "  "
					+ delayCarrier.get(i).getCount());
		}
	}

	public void getTopDelayAirport() {

		delayAirport = new ArrayList<TwoColumnResult>();
		DBCollection collection = db.getCollection("airlineCol");
		// System.out.println("count" + collection.getCount());

		TwoColumnResult tempObj;
		DBObject group = new BasicDBObject("$group", new BasicDBObject("_id",
				"$ORIGIN").append("delay", new BasicDBObject("$avg",
				"$DEP_DELAY_NEW")));
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("delay",
				-1));
		DBObject limit = new BasicDBObject("$limit", 10);

		AggregationOutput output = collection.aggregate(group, sort, limit);

		for (DBObject result : output.results()) {

			tempObj = new TwoColumnResult();
			tempObj.setName(result.get("_id").toString());
			tempObj.setCount(((Number) result.get("delay")).intValue());
			delayAirport.add(tempObj);
		}

		for (int i = 0; i < delayAirport.size(); i++) {
			System.out.println(delayAirport.get(i).getName() + "  "
					+ delayAirport.get(i).getCount());
		}
	}

	public void getMostCancelledFlightsInAirport() {

		cancelAirport = new ArrayList<TwoColumnResult>();
		DBCollection collection = db.getCollection("airlineCol");
		System.out.println("count" + collection.getCount());

		TwoColumnResult tempObj;	
/*		DBObject group = new BasicDBObject("$group", new BasicDBObject("_id","$ORIGIN_CITY_NAME"))
				.append("cancelled_flights",
						new BasicDBObject("$sum", "CANCELLED"))
				.append("total_flights", new BasicDBObject("$sum", 1))
				.append("avg_cancelled",
						new BasicDBObject("$avg", "CANCELLED"));
		
		DBObject match = new BasicDBObject("$match", new BasicDBObject("CANCELLED",1));
		DBObject fields=new BasicDBObject("cancelled_flights",1);
		fields.put("total_flights", 1);
		fields.put("percentage_cancelled",new BasicDBObject("$multiply", new Object[] { "$avg_cancelled",100 })) ;
//		DBObject project = new BasicDBObject("$project", new BasicDBObject(
//				"cancelled_flights", 1)).append("total_flights", 1).append(
//				"percentage_cancelled",
//				new BasicDBObject("$multiply", new Object[] { "$avg_cancelled",
//						100 }));
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("cancelled_flights", -1));
		

		DBObject limit = new BasicDBObject("$limit", 10);
		DBObject project1 = new BasicDBObject("$project", fields );
		AggregationOutput output = collection.aggregate(group,sort,limit);*/

		
//		//DBObject match = new BasicDBObject("$match", new BasicDBObject("CANCELLED",1));
//	    DBObject group = new BasicDBObject("$group",new BasicDBObject("_id", "$ORIGIN_CITY_NAME")).append("count", new BasicDBObject("$sum", "CANCELLED"));
//	    //DBObject group = new BasicDBObject("$group", new BasicDBObject("_id",).append("Origin", "$Origin").append("count", new BasicDBObject("$sum", 1)));
//	    DBObject sort = new BasicDBObject("$sort", new BasicDBObject("count", -1));
//	    DBObject limit = new BasicDBObject("$limit", 10);
		
		DBObject group = new BasicDBObject("$group", new BasicDBObject("_id",
				"$ORIGIN_CITY_NAME").append("cancel", new BasicDBObject("$sum",
				"$CANCELLED")));
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("cancel",
				-1));
		DBObject limit = new BasicDBObject("$limit", 10);

		

		//
	    
	    
	    
	    @SuppressWarnings("deprecation")
		AggregationOutput output = collection.aggregate(group,sort,limit);
		for (DBObject result : output.results()) {

			tempObj = new TwoColumnResult();
			tempObj.setName(result.get("_id").toString());
			tempObj.setCount(((Number)result.get("cancel")).intValue());
//			tempObj.setOriginCityName(result.get("_id").toString());
//			tempObj.setCancelledFlights(((Number) result.get("cancelled_flights")).intValue());
//			//tempObj.setTotalFlights(((Number)result.get("total_flights")).intValue());
//			tempObj.setTotalFlights(10);
//			//tempObj.setPercentage(((Number)result.get("percentage_cancelled")).intValue());
//			tempObj.setPercentage(55);
//			cancelAirport.add(tempObj);
			cancelAirport.add(tempObj);
			
			
		}

		for (int i = 0; i < cancelAirport.size(); i++) {
			System.out.println(cancelAirport.get(i).getName() + "  "
					+ cancelAirport.get(i).getCount());
		}
	}
	
	public void getMostCancelledFlightsByCarrier() {

		cancelCarrier = new ArrayList<TwoColumnResult>();
		DBCollection collection = db.getCollection("airlineCol");
		System.out.println("count" + collection.getCount());

		TwoColumnResult tempObj;	

		
		DBObject group = new BasicDBObject("$group", new BasicDBObject("_id",
				"$CARRIER").append("cancel", new BasicDBObject("$sum",
				"$CANCELLED")));
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("cancel",
				-1));
		DBObject limit = new BasicDBObject("$limit", 10);


	    @SuppressWarnings("deprecation")
		AggregationOutput output = collection.aggregate(group,sort,limit);
		for (DBObject result : output.results()) {

			tempObj = new TwoColumnResult();
			tempObj.setName(result.get("_id").toString());
			tempObj.setCount(((Number)result.get("cancel")).intValue());
			cancelCarrier.add(tempObj);
	
		}

		for (int i = 0; i < cancelCarrier.size(); i++) {
			System.out.println(cancelCarrier.get(i).getName() + "  "
					+ cancelCarrier.get(i).getCount());
		}
	}
	
	public void getHubDetails(){
	    DBCollection collection = db.getCollection("airlineCol");
		String map = "function(){emit({ORIGIN : this.ORIGIN_CITY_NAME , UNIQUE_CARRIER : this.UNIQUE_CARRIER},{count : 1});}";
		String reduce = "function(key,values){var sum = 0;for(var i=0;i<values.length;i++){sum = sum +values[i].count; } return {count : sum};}";
		MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,"hub", MapReduceCommand.OutputType.MERGE, null);
		MapReduceOutput out = collection.mapReduce(cmd);
	    
	    DBCollection collection1 = db.getCollection("hub");
	    //BasicDBObject searchQuery = new BasicDBObject().append("sort",new BasicDBObject("value",-1)).append("limit", 10);
	    DBCursor dbcursor = collection1.find();
	    dbcursor.sort(new BasicDBObject("value" , -1));
	    dbcursor.limit(20);
	    
	    dbOrigin = new ArrayList<String>();
	    dbCarrier = new ArrayList<String>();
	    dbCount = new ArrayList<Integer>();
	   
	    
	    while (dbcursor.hasNext())
	    {
	    	//System.out.println(dbcursor.next());
	    	DBObject db_ID = (DBObject) dbcursor.next().get("_id");
	    	if (db_ID != null)
	    	{
	    		String Origin = db_ID.get("ORIGIN").toString();
		    	System.out.println(Origin);
		    	dbOrigin.add(Origin);
		    	String Carrier = db_ID.get("UNIQUE_CARRIER").toString();
		    	System.out.println(Carrier);
		    	dbCarrier.add(Carrier);
	    	}
	    	else{
	    		dbOrigin.add("");
	    		dbCarrier.add("");
	    	}
	    	
	    	
	    	DBObject db_value = (DBObject) dbcursor.next().get("value");
	    	if (db_value != null)
	    	{
    		
	    		Double value = (Double) db_value.get("count");
	    		Integer value1 = value.intValue();
	    		System.out.println(value1);
	    		dbCount.add(value1);
	    	}
	    	else{
	    		dbCount.add(0);
	    	}
	    	
	    }
	}
	
}