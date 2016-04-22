package gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import connection.MongoConnection;
import entities.FlightResult;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.border.TitledBorder;

public class SearchResults {

	public static JFrame frame;
	private JTable table;
	MongoConnection conn=new MongoConnection();
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblDelayStatistics;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchResults window = new SearchResults();
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
	public SearchResults() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Object columNames[] = { "Flight Date", "Carrier", "From",
				"Origin City", "To", "Destination City","Departure Time","Arrival Time", "Flight Duration",
				"Price" };
		
//		String scolumNames[] = new String[]{ "Flight Date", "Carrier", "From",
//				"Origin City", "To", "Destination City", "Flight Duration",
//				"Price" };
		DefaultTableModel model = new DefaultTableModel(columNames,0);
		//model.setColumnIdentifiers(scolumNames);
		

//		model.addColumn("Flight Date");
//		model.addColumn("Carrier");
//		model.addColumn("From");
//		model.addColumn("Origin City");
//		model.addColumn("To");
//		model.addColumn("Destination City");
//		model.addColumn("Flight Duration");
//		model.addColumn("Price");

		table = new JTable(model);
		table.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//JTable resultTable = new JTable(model);
		table.setModel(model);
		conn.searchDirectFlights(Main_Page.sSource,Main_Page.sDestination,Main_Page.month,Main_Page.day,Main_Page.year);
		//System.out.println("inside search results page result count:"+ conn.resultList.size());

		for (int i = 0; i < conn.resultList.size(); i++) {
			FlightResult temp = conn.resultList.get(i);
//			model.setValueAt(temp.getFlightDate(), i, 1);
//			model.setValueAt(temp.getCarrier(), i, 2);
			model.addRow(new Object[] { temp.getFlightDate(),
					temp.getCarrier(), temp.getOriginAirport(),
					temp.getOriginCity(), temp.getDestAirport(),
					temp.getDestCity(),temp.getDepTime(),temp.getArrTime(),temp.getFlightDuration(),
					temp.getPrice() });
			System.out.println(temp.getFlightDate());
			
		}
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("684px"),},
			new RowSpec[] {
				RowSpec.decode("361px"),
				RowSpec.decode("100px"),}));
		table.setFont(new Font("Times New Roman", Font.BOLD, 14));
	    JScrollPane scrollPane = new JScrollPane( table );
	    //scrollPane.setPreferredSize(new Dimension(1000,500));
		frame.getContentPane().add(scrollPane, "1, 1");
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(1000,100));
		frame.getContentPane().add(panel, "1, 2, fill, top");
		
		lblNewLabel = new JLabel("     ");
		
		lblDelayStatistics = new JLabel("Delay Statistics:");
		
		JButton btnTopCarrier = new JButton("Top 10 Carrier");
		btnTopCarrier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conn.getTopDelayedCarrier();
				DelayedCarrier dc=new DelayedCarrier();
				dc.frame.setVisible(true);
			}
		});
		
		JButton btnTopAirports = new JButton("Top 10 Airports");
		btnTopAirports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conn.getTopDelayAirport();
				DelayAirport dc=new DelayAirport();
				dc.frame.setVisible(true);
			}
		});
		
		JLabel lblCancellationRecord = new JLabel("Cancellation Record:");
		
		JButton btnTopCarrier_2 = new JButton("Top 10 Carrier");
		btnTopCarrier_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelCarrier cc=new cancelCarrier();
				cc.frame.setVisible(true);
			}
		});
		
		JButton btnTopAirports_2 = new JButton("Top 10 Airports");
		btnTopAirports_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelAirport ca=new CancelAirport();
				ca.frame.setVisible(true);
			}
		});
		
		JButton btnBusiestAirports = new JButton("Busiest Airports");
		btnBusiestAirports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conn.getMostCancelledFlightsInAirport();
				System.out.println("inside call to busy airport");
				BusyAirports ba=new BusyAirports();
				ba.frame.setVisible(true);
			}
		});
		
		JButton btnTop = new JButton("Hub Details");
		btnTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				conn.getHubDetails();
				Hub hd=new Hub();
				hd.frame.setVisible(true);
			}
		});
		
		JButton btnStormAffectedFlights = new JButton("Storm Affected Flights");
		btnStormAffectedFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StormAffected sa=new StormAffected();
				sa.frame.setVisible(true);
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap(40, Short.MAX_VALUE)
							.addComponent(lblDelayStatistics)
							.addGap(102))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(30)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnTopAirports)
								.addComponent(btnTopCarrier))
							.addGap(82)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnTopAirports_2, 0, 0, Short.MAX_VALUE)
						.addComponent(btnTopCarrier_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblCancellationRecord, Alignment.TRAILING))
					.addGap(36)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnBusiestAirports)
							.addGap(38)
							.addComponent(btnStormAffectedFlights))
						.addComponent(btnTop))
					.addGap(93)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 684, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDelayStatistics)
								.addComponent(lblCancellationRecord))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(btnTopCarrier)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnTopAirports))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(btnTopCarrier_2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnTopAirports_2))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(33)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnBusiestAirports)
								.addComponent(btnStormAffectedFlights))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnTop)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		//panel.setLayout(new GridLayout(1, 0, 0, 0));
		
	}
}
