package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import connection.MongoConnection;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginPage {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	public static String userName;
	public static String password;
	MongoConnection conn = new MongoConnection();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage window = new LoginPage();
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
	public LoginPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
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
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblUsername = new JLabel("Username");
		frame.getContentPane().add(lblUsername, "6, 6");

		textField = new JTextField();
		frame.getContentPane().add(textField, "10, 6, 1, 2, left, default");
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		frame.getContentPane().add(lblPassword, "6, 10");

		passwordField = new JPasswordField();
		frame.getContentPane().add(passwordField, "10, 10, fill, center");

		JButton btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				userName = textField.getText().toString();
				System.out.println(userName);
				conn.getCustomerInfoNotes(userName);
				// if(!userName.equals("") || userName!=null){
				password = String.valueOf(passwordField.getPassword());
				Main_Page mp = new Main_Page();
				frame.setVisible(false); // Hide current frame
				mp.mframe.setVisible(true);

				// conn.getCustomerInfoNotes(userName);
			}
		});
		frame.getContentPane().add(btnLogIn, "6, 16");

		JButton btnNewUserSign = new JButton("New User? Sign in");
		btnNewUserSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userName = textField.getText().toString();
				password = String.valueOf(passwordField.getPassword());

				System.out.println(userName);
				System.out.println(passwordField.getPassword());
				conn.insertLoginInfo(userName, password);
				frame.setVisible(false); // Hide current frame
				Main_Page mp = new Main_Page();
				frame.setVisible(false); // Hide current frame
				mp.mframe.setVisible(true);
			}

		});
		frame.getContentPane().add(btnNewUserSign, "10, 16, left, default");
	}

}
