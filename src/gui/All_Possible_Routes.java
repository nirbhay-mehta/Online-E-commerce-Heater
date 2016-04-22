package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JFrame;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gui.Main_Page;

public class All_Possible_Routes {

	public static JFrame frame;
	public static JTable table;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					All_Possible_Routes window = new All_Possible_Routes();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public All_Possible_Routes() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unused")
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		try
		{
			
			int m=Main_Page.month+1;
			MongoClient mongo = new MongoClient("localhost", 27017);
		    DB db = mongo.getDB("airlinedb");
		    DBCollection collection = db.getCollection("airlineCol");
//		    String map = "function(){if(this.ORIGIN_CITY_NAME==\""+ Main_Page.sSource +"\" && this.FL_DATE==\""+m+"/"+Main_Page.day+"/"+Main_Page.year+"\"){emit({UNIQUE_CARRIER : this.UNIQUE_CARRIER,AIRLINE_ID : this.AIRLINE_ID,ORIGIN : this.ORIGIN_CITY_NAME, DEPARTURE_TIME : this.DEP_TIME , DESTINATION: this.DEST_CITY_NAME,DESTINATION : this.DEST_CITY_NAME,ARRIVAL_TIME : this.ARR_TIME,DURATION:this.AIR_TIME},1);}}";
//			String reduce = "function(key,values){var count_way=[]; for(var i=0;i<values.length;i++){" +
//							"if(count_way[i]){count_way[i]+=values[i] ;} else {count_way[i]=values[i]; }} return Array.sum(values);}";
		    String map = "function(){if(this.ORIGIN_CITY_NAME==\""+ Main_Page.sSource +"\" && this.FL_DATE==\""+m+"/"+Main_Page.day+"/"+Main_Page.year+"\"){emit({UNIQUE_CARRIER : this.UNIQUE_CARRIER,AIRLINE_ID : this.AIRLINE_ID,ORIGIN : this.ORIGIN_CITY_NAME, DEPARTURE_TIME : this.DEP_TIME , DESTINATION: this.DEST_CITY_NAME,DESTINATION : this.DEST_CITY_NAME,ARRIVAL_TIME : this.ARR_TIME,DURATION:this.AIR_TIME},1);}}";
		    String reduce = "function(key,values){var count_way=[]; for(var i=0;i<values.length;i++){" +
				"if(count_way[i]){count_way[i]+=values[i] ;} else {count_way[i]=values[i]; }} return Array.sum(values);}";
		    		System.out.println(map);
		    		System.out.println(reduce);
		    MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,"allpossibleroutes1", MapReduceCommand.OutputType.MERGE, null);
			MapReduceOutput out = collection.mapReduce(cmd);
			
			DBCollection collection_temp = db.getCollection("allpossibleroutes1");
			DBCursor dbcursor1 = collection_temp.find();
			//System.out.println(dbcursor1.count());
			ArrayList<String> dbOrigin = new ArrayList<String>();
		    ArrayList<String> dbCarrier = new ArrayList<String>();
		    ArrayList<String> dbDest = new ArrayList<String>();
		    Object columnNames[] = {"Origin","Intermediate Stop","Destination"};
		    
			while (dbcursor1.hasNext())
			{
				DBObject db_ID = (DBObject) dbcursor1.next().get("_id");
				String Origin = db_ID.get("ORIGIN").toString();
				dbOrigin.add(Origin);
				String Carrier = db_ID.get("UNIQUE_CARRIER").toString();
				dbCarrier.add(Carrier);
				String Destination = db_ID.get("DESTINATION").toString();
				dbDest.add(Destination);
			}
			
			String map1 = "function(){if(this.DEST_CITY_NAME==\""+ Main_Page.sDestination +"\" && this.FL_DATE==\""+m+"/"+Main_Page.day+"/"+Main_Page.year+"\"){emit({UNIQUE_CARRIER : this.UNIQUE_CARRIER,AIRLINE_ID : this.AIRLINE_ID,ORIGIN : this.ORIGIN_CITY_NAME, DEPARTURE_TIME : this.DEP_TIME , DESTINATION: this.DEST_CITY_NAME,DESTINATION : this.DEST_CITY_NAME,ARRIVAL_TIME : this.ARR_TIME,DURATION:this.AIR_TIME},1);}}";
			String reduce1 = "function(key,values){var count_way=[]; for(var i=0;i<values.length;i++){" +
					"if(count_way[i]){count_way[i]+=values[i] ;} else {count_way[i]=values[i]; }} return Array.sum(values);}";
			MapReduceCommand cmd1 = new MapReduceCommand(collection, map1, reduce1,"allpossibleroutes2", MapReduceCommand.OutputType.MERGE, null);
			MapReduceOutput out1 = collection.mapReduce(cmd1);
			DBCollection collection_temp1 = db.getCollection("allpossibleroutes2");
			DBCursor dbcursor2 = collection_temp1.find();
			//System.out.println(dbcursor2.count());
			
			ArrayList<String> dbOrigin1 = new ArrayList<String>();
		    ArrayList<String> dbCarrier1 = new ArrayList<String>();
		    ArrayList<String> dbDest1 = new ArrayList<String>();
		    
		    while (dbcursor2.hasNext())
			{
				DBObject db_ID1 = (DBObject) dbcursor2.next().get("_id");
				String Origin1 = db_ID1.get("ORIGIN").toString();
				dbOrigin1.add(Origin1);
				if (db_ID1.get("UNIQUE_CARRIER") != null)
				{
					String Carrier1 = db_ID1.get("UNIQUE_CARRIER").toString();
					dbCarrier1.add(Carrier1);
				}
				else 
				{
					String Carrier1 = null;
					dbCarrier1.add(Carrier1);
				}
					
				
				//String Carrier1 = null;
				
				String Destination1 = db_ID1.get("DESTINATION").toString();
				dbDest1.add(Destination1);
			}
			
		    dbDest.retainAll(dbOrigin1);
		    //System.out.println("Before" + dbDest);
		    LinkedHashSet<String> lhs = new LinkedHashSet<String>();
		    
		    lhs.addAll(dbDest);
		    dbDest.clear();
		    dbDest.addAll(lhs);
		    //System.out.println("After" + dbDest);
		     //Main_Page.sSource = "Chicago, IL";
		     //Main_Page.sDestination = "Los Angeles, CA";
		    
			DefaultTableModel model = new DefaultTableModel(columnNames,0);
		    //Object[] cell = "Chicago, IL";
		    Object[] cell1 = dbDest.toArray();
		    //Object[] cell2 = dbDest1.toArray();
		    for(int i1 = 0;i1<dbDest.size();i1++)
		    {
		    	model.addRow(new Object[] {Main_Page.sSource,cell1[i1],Main_Page.sDestination}); 
		    }
		    
		table = new JTable(model);
		table.setFont(new Font("Times New Roman", Font.BOLD, 14));
		JScrollPane scrollPane = new JScrollPane( table );
        frame.getContentPane().add( scrollPane,"4,4,left,fill" );
	
		}
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
