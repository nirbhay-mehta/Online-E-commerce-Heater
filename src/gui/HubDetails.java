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

import connection.MongoConnection;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
public class HubDetails {


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
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HubDetails window = new HubDetails();
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
	public HubDetails() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		 Object columnNames[] = {"Airport","Carrier","Number of Flights"};
		    DefaultTableModel model = new DefaultTableModel(columnNames,0);
		    if (conn.dbOrigin == null){
		    	System.out.println("origin is NULLLLL");
		    }
		    Object[] cell = conn.dbOrigin.toArray();
		    Object[] cell1 = conn.dbCarrier.toArray();
		    Object[] cell2 = conn.dbCount.toArray();
		    for(int i = 0;i<conn.dbCarrier.size();i++)
		    {
		    	model.addRow(new Object[] {cell[i],cell1[i],cell2[i]}); 
		    }
		       
		    
		    table = new JTable(model);
		    table.setFont(new Font("Times New Roman", Font.BOLD, 14));
			panel_2 = new JPanel();
			frame.getContentPane().add(panel_2);

			panel_3 = new JPanel();
			panel_3.setBorder(new TitledBorder(null,
					"Airports which are Hub for a carrier",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_2.add(panel_3);
			table = new JTable(model);

			table.setModel(model);
			table.setFont(new Font("Times New Roman", Font.BOLD, 14));
			JScrollPane scrollPane = new JScrollPane(table);
			panel_3.add(scrollPane);

			panel_1 = new JPanel();
			scrollPane.setColumnHeaderView(panel_1);

		    
	       // frame.getContentPane().add( scrollPane,"4,4,left,fill" );
	}

}
