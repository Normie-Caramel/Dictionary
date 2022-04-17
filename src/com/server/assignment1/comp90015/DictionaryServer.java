package com.server.assignment1.comp90015;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextPane;

public class DictionaryServer {

	private JFrame frame;
	private int serverPort = 5000;
	private int threadNum = 10;
	private String dictPath = System.getProperty("user.dir") + File.separator + "dict.txt";
	private Server server;
	private Thread serverThread;
	private JTextField portText;
	private JTextField pathText;
	private JSpinner spinner;
	private JTextPane consolePane;
	private ArrayList<JLabel> initLabels = new ArrayList<JLabel>();
	private ArrayList<JButton> initButtons = new ArrayList<JButton>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryServer window = new DictionaryServer();
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
	public DictionaryServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Times New Roman", Font.BOLD, 12));
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		portText = new JTextField();
		portText.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		portText.setText("5000");
		portText.setBounds(132, 127, 376, 34);
		frame.getContentPane().add(portText);
		portText.setColumns(10);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (init())
					start();
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton.setBounds(132, 304, 174, 29);
		frame.getContentPane().add(btnNewButton);
		initButtons.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton_1.setBounds(334, 304, 174, 29);
		frame.getContentPane().add(btnNewButton_1);
		initButtons.add(btnNewButton_1);
		
		JLabel lblNewLabel = new JLabel("SERVER INIT");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 32));
		lblNewLabel.setBounds(207, 40, 204, 46);
		frame.getContentPane().add(lblNewLabel);
		initLabels.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Port : ");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(132, 102, 54, 15);
		frame.getContentPane().add(lblNewLabel_2);
		initLabels.add(lblNewLabel_2);
		
		spinner = new JSpinner();
		spinner.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		spinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
		spinner.setBounds(132, 196, 54, 29);
		frame.getContentPane().add(spinner);
		
		JLabel lblNewLabel_1 = new JLabel("Thread Number :");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(132, 171, 129, 15);
		frame.getContentPane().add(lblNewLabel_1);
		initLabels.add(lblNewLabel_1);
		
		pathText = new JTextField();
		pathText.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		pathText.setBounds(132, 260, 376, 34);
		pathText.setText(this.dictPath);
		frame.getContentPane().add(pathText);
		pathText.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Dictionary Path :");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(132, 235, 129, 15);
		frame.getContentPane().add(lblNewLabel_3);
		initLabels.add(lblNewLabel_3);
		
		consolePane = new JTextPane();
		consolePane.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		consolePane.setBounds(132, 343, 376, 64);
		frame.getContentPane().add(consolePane);
		
	}
	
	private void start() {
		this.server = new Server(serverPort, threadNum, dictPath);
		this.serverThread = new Thread(server);
		this.portText.setVisible(false);
		this.pathText.setVisible(false);
		this.spinner.setVisible(false);
		this.consolePane.setVisible(false);
		for (JButton b : this.initButtons)
			b.setVisible(false);
		for (JLabel l : this.initLabels)
			l.setVisible(false);
		this.serverThread.start();
	}
	
	private boolean init() {
		String warn = "";
		this.consolePane.setText(warn);
		if (!setPort())
			warn = warn + "Invalid prot number (should be 0 ~ 65536), please check again!\n";
		if (!setThreadNum())
			warn = warn + "Invalid thread number (should be 1 ~ 10), please check again!\n";
		if (!setPath())
			warn = warn + "Targe path does not exist, please check again!";
		this.consolePane.setText(warn);
		return setPort() && setThreadNum() && setPath();
	}
	
	private boolean setPort() {
		String portStr = this.portText.getText();
		for (char c : portStr.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		int port = Integer.parseInt(portStr);
		if (port >= 0 && port <= 65536) {
			this.serverPort = port;
			return true;
		}
		return false;
	}
	
	private boolean setThreadNum() {
		int num = (int) spinner.getValue();
		if (num > 0 && num <= 10) {
			this.threadNum = num;
			return true;
		}
		return false;
	}
	
	private boolean setPath() {
		String path = this.pathText.getText();
		File dict = new File(path);
		if (dict.exists()) {
			this.dictPath = path;
			return true;
		}
		return false;
	}
}
