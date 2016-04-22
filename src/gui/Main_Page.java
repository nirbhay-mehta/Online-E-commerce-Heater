package gui;

import connection.MongoConnection;

import java.awt.EventQueue;

import javax.swing.Box.Filler;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JFrame;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JComboBox;

import java.awt.Choice;

import com.toedter.calendar.JCalendar;

import connection.MongoConnection;
import entities.FlightResult;
import entities.MyDate;

import java.awt.Color;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JRadioButton;

public class Main_Page {

	public static JFrame mframe;
	@SuppressWarnings("rawtypes")
	public static List coll;
	public static String sSource;
	public static String sDestination;

	public static int month=MongoConnection.mydate.getMonth();
	public static int day=MongoConnection.mydate.getDay();
	public static int year=MongoConnection.mydate.getYear();
//	public static int day=1;
//	public static int year=2015;
	static MongoConnection conn = new MongoConnection();
	static JCalendar DepCalendar;
	public static  ArrayList<FlightResult> tempResult;

	// public String sSourceValue;
	// public String sDestValue;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Page window = new Main_Page();
					Main_Page.mframe.setVisible(true);
					// Connection();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main_Page() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void initialize() {
		mframe = new JFrame();
		// frame.getContentPane().setBackground(Color.CYAN);
		mframe.setBackground(new Color(240, 240, 240));
		mframe.setBounds(100, 100, 700, 500);
		mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mframe.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] { ColumnSpec.decode("6dlu"),
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						ColumnSpec.decode("60dlu"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblNewLabel = new JLabel("Source");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
		mframe.getContentPane().add(lblNewLabel, "4, 4, left, top");

		try {
			MongoClient mongo = new MongoClient("localhost", 27017);
			DB db = mongo.getDB("airlinedb");
			DBCollection collection = db.getCollection("airlinedata");
			List collOrigin = (List) collection.distinct("ORIGIN_CITY_NAME");
			@SuppressWarnings("rawtypes")
			final JComboBox SourceCombo = new JComboBox();
			

			for (int i = 0; i < collOrigin.size(); i++) {
				// System.out.println(coll.get(i));
				SourceCombo.addItem(collOrigin.get(i).toString());
			}
			sSource = SourceCombo.getSelectedItem().toString();
			System.out.println("default src:"+sSource);
			SourceCombo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					sSource = SourceCombo.getSelectedItem().toString();
				}
			});
			// SourceCombo.setSelectedIndex(0);
			SourceCombo.setFont(new Font("Times New Roman", Font.BOLD, 14));
			SourceCombo.setEditable(true);

			mframe.getContentPane().add(SourceCombo, "8, 4, left, default");

			JLabel lblDestination = new JLabel("Destination");
			lblDestination.setFont(new Font("Times New Roman", Font.BOLD, 15));
			mframe.getContentPane().add(lblDestination, "4, 8");

			List collDest = (List) collection.distinct("DEST_CITY_NAME");
			@SuppressWarnings("rawtypes")
			final JComboBox DestCombo = new JComboBox();

			for (int i = 0; i < collDest.size(); i++) {
				// System.out.println(coll.get(i));
				DestCombo.addItem(collDest.get(i).toString());
			}
			sDestination = DestCombo.getSelectedItem().toString();
			System.out.println("default dest:"+sDestination);
			DestCombo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					sDestination = DestCombo.getSelectedItem().toString();
				}
			});
			DestCombo.setEditable(true);
			DestCombo.setFont(new Font("Times New Roman", Font.BOLD, 14));
			mframe.getContentPane().add(DestCombo, "8, 8, left, default");


			
				JLabel lblDepDate = new JLabel("Dep. Date");
				lblDepDate.setFont(new Font("Times New Roman", Font.BOLD, 15));
				mframe.getContentPane().add(lblDepDate, "4, 12, left, top");
				
				DepCalendar = new JCalendar();
				Date d=new Date();
				d.setDate(1);
				d.setMonth(0);
				d.setYear(115);
				DepCalendar.setDate(d);
				DepCalendar.addPropertyChangeListener("calendar",
						new PropertyChangeListener() {

							@Override
							public void propertyChange(PropertyChangeEvent e) {
								final Calendar c = (Calendar) e.getNewValue();
								month = c.get(Calendar.MONTH);
								day = c.get(Calendar.DAY_OF_MONTH);
								year = c.get(Calendar.YEAR);
								// System.out.println(month);
								// System.out.println(day);
								// System.out.println(year);
							}
						});

				mframe.getContentPane().add(DepCalendar, "8, 12, left, top");
				tempResult=new ArrayList<FlightResult>(); 
				JButton btnSearchFlights = new JButton("Search Direct Flights");
				btnSearchFlights.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.out.println("before call to update " + month
								+ " " + day + " " + year);
						conn.updateNotes(sSource, sDestination, month, day,
								year);
//						tempResult=conn.searchDirectFlights(sSource,sDestination,month,day,year);
//						for (int i=0; i<tempResult.size();i++){
//							System.out.println(tempResult.get(i).getFlightDuration());
//						}
						SearchResults sr=new SearchResults();
						mframe.setVisible(false);
						sr.frame.setVisible(true);

					}
				});
				
				JButton btnNewButton = new JButton("Flights with one stop");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						conn.updateNotes(sSource, sDestination, month, day,
								year);
						All_Possible_Routes aa=new All_Possible_Routes();
						mframe.setVisible(false);
						aa.frame.setVisible(true);
					}
				});
				mframe.getContentPane().add(btnNewButton, "8, 14, left, default");

//				JRadioButton rdbtnOneStop = new JRadioButton("Non-Stop flights");
//				rdbtnOneStop.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent arg0) {
//
//					}
//				});
//				mframe.getContentPane().add(rdbtnOneStop,
//						"8, 13, left, default");
//
//				JRadioButton rdbtnOneStopFlights = new JRadioButton(
//						"One Stop flights");
//				mframe.getContentPane().add(rdbtnOneStopFlights, "9, 13");
				mframe.getContentPane().add(btnSearchFlights,
						"8, 16, left, default");
//
//				ButtonGroup group = new ButtonGroup();
//				group.add(rdbtnOneStop);
//				group.add(rdbtnOneStopFlights);
				
				if (MongoConnection.note != null) {
					// set default source
					System.out.println("check src: "
							+ MongoConnection.note.getSrcHistory().toString());
					SourceCombo.setSelectedItem(MongoConnection.note
							.getSrcHistory().toString());

					// set default destination
					System.out.println("check dest: "
							+ MongoConnection.note.getDestHistory().toString());
					DestCombo.setSelectedItem(MongoConnection.note.getDestHistory()
							.toString());

					// set default date
					
					Date setdate = new Date();
					// Calendar cDate = null;
					MyDate tempdate = new MyDate();
					tempdate = MongoConnection.mydate;
					//System.out.println(tempdate.getYear());
					// setdate.setDate(MongoConnection.note.getDate().getDay());
					// setdate.setMonth(MongoConnection.note.getDate().getMonth());
					// setdate.setYear(MongoConnection.note.getDate().getYear());
					// cDate.set(tempdate.getYear(), tempdate.getMonth(),
					// tempdate.getDay());
					setdate.setDate(tempdate.getDay());
					setdate.setMonth(tempdate.getMonth());
					setdate.setYear(tempdate.getYear() - 1900);
					DepCalendar.setDate(setdate);
				}
				else{
					Date d1=new Date();
					d1.setDate(1);
					d1.setMonth(0);
					d1.setYear(115);
					DepCalendar.setDate(d1);
				}
				
				mongo.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
