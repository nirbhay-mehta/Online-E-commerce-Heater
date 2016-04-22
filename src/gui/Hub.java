package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

public class Hub {

	public static JFrame frame;
	public static JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hub window = new Hub();
					window.frame.setVisible(true);
					
						} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Hub() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			MongoClient mongo = new MongoClient("localhost", 27017);
		    DB db = mongo.getDB("airlinedb");
		    DBCollection collection = db.getCollection("airlineCol");
			String map = "function(){emit({ORIGIN : this.ORIGIN_CITY_NAME , UNIQUE_CARRIER : this.UNIQUE_CARRIER},{count : 1});}";
			String reduce = "function(key,values){var sum = 0;for(var i=0;i<values.length;i++){sum = sum +values[i].count; } return {count : sum};}";
			MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,"hub", MapReduceCommand.OutputType.MERGE, null);
			//MapReduceOutput out = collection.mapReduce(cmd);
		    
		    DBCollection collection1 = db.getCollection("hub");
		    //BasicDBObject searchQuery = new BasicDBObject().append("sort",new BasicDBObject("value",-1)).append("limit", 10);
		    DBCursor dbcursor = collection1.find();
		    dbcursor.sort(new BasicDBObject("value" , -1));
		    dbcursor.limit(20);
		    
		    ArrayList<String> dbOrigin = new ArrayList<String>();
		    ArrayList<String> dbCarrier = new ArrayList<String>();
		    ArrayList<Integer> dbCount = new ArrayList<Integer>();
		    Object columnNames[] = {"Airport","Carrier","No. of Flights"};
		    
		    while (dbcursor.hasNext())
		    {
		    	//System.out.println(dbcursor.next());
		    	DBObject db_ID = (DBObject) dbcursor.next().get("_id");
		    	if (db_ID != null)
		    	{
		    		String Origin = db_ID.get("ORIGIN").toString();
			    	//System.out.println(Origin);
			    	dbOrigin.add(Origin);
			    	String Carrier = db_ID.get("UNIQUE_CARRIER").toString();
			    	//System.out.println(Carrier);
			    	dbCarrier.add(Carrier);
		    	}
		    	
		    	
		    	DBObject db_value = (DBObject) dbcursor.next().get("value");
		    	if (db_value != null)
		    	{
		    		
		    		Double value = (Double) db_value.get("count");
		    		Integer value1 = value.intValue();
		    		//System.out.println(value1);
		    		dbCount.add(value1);
		    	}
		    	
		    }
		    
		    DefaultTableModel model = new DefaultTableModel(columnNames,0);
		    Object[] cell = dbOrigin.toArray();
		    Object[] cell1 = dbCarrier.toArray();
		    Object[] cell2 = dbCount.toArray();
		    for(int i = 0;i<dbCarrier.size();i++)
		    {
		    	model.addRow(new Object[] {cell[i],cell1[i],cell2[i]}); 
		    }
		       
		    
		    table = new JTable(model);
		    table.setFont(new Font("Times New Roman", Font.BOLD, 14));
		    JScrollPane scrollPane = new JScrollPane( table );
	        frame.getContentPane().add( scrollPane,"4,4,left,fill" );

		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}
}
