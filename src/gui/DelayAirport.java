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
import entities.TwoColumnResult;

public class DelayAirport {

	public JFrame frame;
	private JTable table;
	private JPanel panel;
	MongoConnection conn=new MongoConnection();
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
					DelayAirport window = new DelayAirport();
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
	public DelayAirport() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		
		Object columNames[] = { "Airport Name"," Total number of delayed Flights"};
		
		DefaultTableModel model = new DefaultTableModel(columNames,0);
		conn.getTopDelayAirport();
//		if(conn.busyResult.isEmpty()){
//			System.out.println("empty true");
//		}
		System.out.println("inside delay airports results page  count:"+ conn.delayAirport.size());

		for (int i = 0; i < conn.delayAirport.size(); i++) {
			TwoColumnResult temp = conn.delayAirport.get(i);

			model.addRow(new Object[] { temp.getName(),temp.getCount()});
			
		}
		
		panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		
		panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Top 10 Airports with highest number of delayed flights", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(panel_3);
		table = new JTable(model);
		
		table.setModel(model);
		table.setFont(new Font("Times New Roman", Font.BOLD, 14));
	    JScrollPane scrollPane = new JScrollPane( table );
	    panel_3.add(scrollPane);
		
		panel_1 = new JPanel();
		scrollPane.setColumnHeaderView(panel_1);
		
	}

}
