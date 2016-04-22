package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import connection.MongoConnection;
import entities.FourColumnResult;
import entities.TwoColumnResult;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

import entities.TwoColumnResult;

import javax.swing.JList;

public class StormAffected {

	public JFrame frame;
	private JTable table;
	private JPanel panel;
	MongoConnection conn = new MongoConnection();
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StormAffected window = new StormAffected();
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
	public StormAffected() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
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
			MongoClient mongo = new MongoClient("localhost", 27017);
		    DB db = mongo.getDB("airlinedb");
		    DBCollection collection = db.getCollection("airlineCol");
			String map = "function(){if(this.CANCELLATION_CODE==\"B\" && this.MONTH==1){if(this.ORIGIN_STATE_ABR== \"MA\" || this.ORIGIN_STATE_ABR==\"NY\" ||this.DEST_STATE_ABR==\"MA\" || this.DEST_STATE_ABR==\"NY\"){emit(this.FL_DATE , {count:1});}}}";
			String reduce = "function(key,values){var sum = 0;for(var i=0;i<values.length;i++){sum = sum +values[i].count; } return {count : sum};}";
			MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,"storm", MapReduceCommand.OutputType.MERGE, null);
			MapReduceOutput out = collection.mapReduce(cmd);
		    
			System.out.println(map);
			System.out.println(reduce);
		    DBCollection collection1 = db.getCollection("storm");
		    //BasicDBObject searchQuery = new BasicDBObject().append("sort",new BasicDBObject("value",-1)).append("limit", 10);
		    DBCursor dbcursor = collection1.find();
		    dbcursor.sort(new BasicDBObject("value" , -1));
		    dbcursor.limit(20);
		    	
		    
		    TwoColumnResult stormdata;
		    ArrayList<TwoColumnResult> stormList= new ArrayList<>();
		    Object columnNames[] = {"Date","Number of flights affected"};
		    System.out.println();
		    while (dbcursor.hasNext())
		    	
		    {
		    	//System.out.println(dbcursor.next());
		    	stormdata=new  TwoColumnResult();
		    	DBObject db_ID = (DBObject) dbcursor.next().get("_id");
		    	DBObject db_value = (DBObject) dbcursor.next().get("value");
		    	System.out.println("  string key value "+db_ID.get("FL_DATE"));
		    	System.out.println("key value  "+db_value.get("count"));
		    	if (db_ID.get("FL_DATE") != null)
		    	{
		    		System.out.println("date   "+ db_ID.toString());
					stormdata.setName(db_ID.get("FL_DATE").toString());
		    		

		    	}
		    	
		    	
		    	//DBObject db_value = (DBObject) dbcursor.next().get("value");
		    	if (db_value != null)
		    	{
		    		stormdata.setCount(((Number)db_value.get("count")).intValue());
		    	}
		    	stormList.add(stormdata);
		    }
		    
		    DefaultTableModel model = new DefaultTableModel(columnNames,0);

		    for(int i = 0;i<stormList.size();i++)
		    {
		    	model.addRow(new Object[] {stormList.get(i).getName(),stormList.get(i).getCount()}); 
		    }
		       
		    
		    table = new JTable(model);
		    table.setFont(new Font("Times New Roman", Font.BOLD, 14));
		    
		    table = new JTable(model);
		    table.setFont(new Font("Times New Roman", Font.BOLD, 14));
		    JScrollPane scrollPane = new JScrollPane( table );
	        frame.getContentPane().add( scrollPane,"4,4,left,fill" );
//			panel_2 = new JPanel();
//			frame.getContentPane().add(panel_2);
//
//			panel_3 = new JPanel();
//			panel_3.setBorder(new TitledBorder(null,
//					"Top 10 City Airports with highest number of cancelled flights",
//					TitledBorder.LEADING, TitledBorder.TOP, null, null));
//			panel_2.add(panel_3);
//			table = new JTable(model);
//
//			table.setModel(model);
//			table.setFont(new Font("Times New Roman", Font.BOLD, 14));
//			JScrollPane scrollPane1 = new JScrollPane(table);
//			panel_3.add(scrollPane1);
//
//			panel_1 = new JPanel();
//			scrollPane1.setColumnHeaderView(panel_1);

		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}
}
