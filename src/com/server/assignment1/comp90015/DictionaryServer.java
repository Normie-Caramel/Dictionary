package com.server.assignment1.comp90015;

/***
 * Name: Jie Yang
 * Student ID: 1290106
 * E-mail: jieyang3@student.unimelb.edu.au
 */

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.JTextPane;
import javax.swing.JTextArea;

public class DictionaryServer {

	private JFrame frmDictionaryserver;
	private int serverPort = 5000;
	private int threadNum = 5;
	private String dictPath = System.getProperty("user.dir") + File.separator + "dict.txt";
	private String logPath = System.getProperty("user.dir") + File.separator + "log.txt";
	private Server server;
	private Thread serverThread;
	private JTextField portText;
	private JTextField pathText;
	private JSpinner spinner;
	private JTextPane consolePane;
	private JTextPane reportPane;
	private JTextArea consolePane2;
	private ArrayList<JLabel> initLabels = new ArrayList<JLabel>();
	private ArrayList<JButton> initButtons = new ArrayList<JButton>();
	private ArrayList<JButton> opButtons = new ArrayList<JButton>();

	/***
	 * Server GUI entry
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryServer window = new DictionaryServer();
					window.frmDictionaryserver.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/***
	 * Initialize the program
	 */
	public DictionaryServer() {
		initialize();
	}

	/***
	 * Initialize the componenets in GUI board
	 */
	private void initialize() {
		
		// GUI frame
		frmDictionaryserver = new JFrame();
		frmDictionaryserver.setTitle("DictionaryServer");
		frmDictionaryserver.getContentPane().setFont(new Font("Times New Roman", Font.BOLD, 12));
		frmDictionaryserver.setBounds(100, 100, 640, 480);
		frmDictionaryserver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryserver.getContentPane().setLayout(null);
		
		// Start button in the initial window
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (init())
					start();
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton.setBounds(56, 304, 174, 29);
		frmDictionaryserver.getContentPane().add(btnNewButton);
		initButtons.add(btnNewButton);
		
		// Exit button in the initial window
		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton_1.setBounds(380, 304, 174, 29);
		frmDictionaryserver.getContentPane().add(btnNewButton_1);
		initButtons.add(btnNewButton_1);
		
		// Label indicators in the initial window
		JLabel lblNewLabel = new JLabel("SERVER INIT");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 32));
		lblNewLabel.setBounds(207, 40, 204, 46);
		frmDictionaryserver.getContentPane().add(lblNewLabel);
		initLabels.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Port : ");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(56, 101, 54, 15);
		frmDictionaryserver.getContentPane().add(lblNewLabel_2);
		initLabels.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Thread Number :");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(56, 171, 129, 15);
		frmDictionaryserver.getContentPane().add(lblNewLabel_1);
		initLabels.add(lblNewLabel_1);
		
		JLabel lblNewLabel_3 = new JLabel("Dictionary Path :");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(56, 235, 129, 15);
		frmDictionaryserver.getContentPane().add(lblNewLabel_3);
		initLabels.add(lblNewLabel_3);
		
		// The text field to enter the port number in initial window
		portText = new JTextField();
		portText.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		portText.setText("5000");
		portText.setBounds(56, 127, 498, 34);
		frmDictionaryserver.getContentPane().add(portText);
		portText.setColumns(10);
		
		// The text field to enter the dictionary path in initial window
		pathText = new JTextField();
		pathText.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		pathText.setBounds(56, 260, 498, 34);
		pathText.setText(this.dictPath);
		frmDictionaryserver.getContentPane().add(pathText);
		pathText.setColumns(10);		

		// Thread thread number spinner in the initial window
		spinner = new JSpinner();
		spinner.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		spinner.setModel(new SpinnerNumberModel(5, 1, 10, 1));
		spinner.setBounds(56, 196, 54, 29);
		frmDictionaryserver.getContentPane().add(spinner);
		
		// The text pane to display errors in initialization
		consolePane = new JTextPane();
		consolePane.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		consolePane.setBounds(56, 346, 498, 64);
		frmDictionaryserver.getContentPane().add(consolePane);
		
		// The text pane to show statistic report in operation window
		reportPane = new JTextPane();
		reportPane.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		reportPane.setBounds(27, 22, 428, 228);
		frmDictionaryserver.getContentPane().add(reportPane);
		reportPane.setVisible(false);
		reportPane.setEditable(false);
		
		// The text area to show the information redirected from system console in operation window
		consolePane2 = new JTextArea();
		consolePane2.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		consolePane2.setBounds(27, 260, 428, 156);
		frmDictionaryserver.getContentPane().add(consolePane2);
		consolePane2.setEditable(false);
		consolePane2.setVisible(false);
		
		// Refresh button in operation window
		JButton btnNewButton_2 = new JButton("Refresh");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showReport();
			}
		});
		btnNewButton_2.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton_2.setBounds(475, 22, 115, 34);
		frmDictionaryserver.getContentPane().add(btnNewButton_2);
		opButtons.add(btnNewButton_2);
		btnNewButton_2.setVisible(false);
		
		// Save button in operation window
		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveFile();
			}
		});
		btnNewButton_3.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton_3.setBounds(475, 78, 115, 34);
		frmDictionaryserver.getContentPane().add(btnNewButton_3);
		opButtons.add(btnNewButton_3);
		btnNewButton_3.setVisible(false);
		
		// Exit button in operation window
		JButton btnNewButton_4 = new JButton("Exit");
		btnNewButton_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				safeExit();
			}
		});
		btnNewButton_4.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnNewButton_4.setBounds(475, 134, 115, 34);
		frmDictionaryserver.getContentPane().add(btnNewButton_4);
		opButtons.add(btnNewButton_4);
		btnNewButton_4.setVisible(false);
	}
	
	/***
	 * Start the server service, scheduled saving and console redirection
	 */
	private void start() {
		try {
			// switch GUI component form initial to operation and redirect terminal info
			closeInitGUI();
			redirectSystemStreams();
			openOperaGUI();
			
			// start server service
			this.server = new Server(serverPort, threadNum, dictPath);
			this.serverThread = new Thread(server);
			this.serverThread.start();
			
			// wait for dictionary reading the data from file
			Thread.sleep(300);
			showReport();
			
			// start the scheduled saving timer
			startSaveSchedule();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * Make components in initial window invisible
	 */
	private void closeInitGUI() {
		this.portText.setVisible(false);
		this.pathText.setVisible(false);
		this.spinner.setVisible(false);
		this.consolePane.setVisible(false);
		for (JButton b : this.initButtons)
			b.setVisible(false);
		for (JLabel l : this.initLabels)
			l.setVisible(false);
	}
	
	/***
	 * Make components in operation window visible (which invisible by default)
	 */
	private void openOperaGUI() {
		for (JButton b : this.opButtons)
			b.setVisible(true);
		this.reportPane.setVisible(true);
		this.consolePane2.setVisible(true);
	}
	
	/***
	 * Create a timer thread to save the dictionary per minute
	 */
	private void startSaveSchedule() {
		Timer saveTimer = new Timer();
		saveTimer.schedule(new TimerTask() {
			public void run() {
				saveFile();
			}
		}, 60000, 60000);
	}
	
	/***
	 * Check the validiation of the initialization information
	 * @return true if info entered is valid, false otherwise
	 */
	private boolean init() {
		String warn = "";
		if (!setPort())
			warn = warn + "Invalid port number, please check again!\n";
		if (!setThreadNum())
			warn = warn + "Invalid thread number, please check again!\n";
		if (!setPath())
			warn = warn + "Target path does not exist, please check again!";
		this.consolePane.setText(warn);
		return setPort() && setThreadNum() && setPath();
	}
	
	/***
	 * Check the format and range of port number provided
	 * @return true if port number is an integer from 0 to 65536, false otherwise
	 */
	private boolean setPort() {
		try {
			String portStr = this.portText.getText();
			int port = Integer.parseInt(portStr);
			if (port < 0 || port > 65536)
				return false;
			this.serverPort = port;
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/***
	 * Check the range of thread number
	 * @return true if given number is from 1 to 10, false otherwise
	 */
	private boolean setThreadNum() {
		int num = (int) spinner.getValue();
		if (num > 0 && num <= 10) {
			this.threadNum = num;
			return true;
		}
		return false;
	}
	
	/***
	 * Check if the dictionary can be found in given path
	 * @return true if dictionary is found, false otherwise
	 */
	private boolean setPath() {
		String path = this.pathText.getText();
		File dict = new File(path);
		if (dict.exists()) {
			this.dictPath = path;
			return true;
		}
		return false;
	}
	
	/***
	 * Opposite to enforced exit, this method will exit the program with generating
	 * a log file, saving the memory data and close the thread pool (wait until current
	 * task finished)
	 */
	private void safeExit() {
		extractLog();
		this.serverThread.interrupt();
		this.server.close();
		System.exit(0);
	}
	
	/***
	 * Save the dictionary data from memeory into original file, and show result
	 */
	private void saveFile() {
		if(this.server.save())
			System.out.println("save succeed!");
	}
	
	/***
	 * Collect the newest report from dictionary and show it on display board
	 */
	private void showReport() {
		String report = this.server.report();
		this.reportPane.setText(report);
	}
	
	/***
	 * Create a new thread listening on terminal and write the info on GUI console
	 * @param text
	 */
	private void consoleUpdate(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				appendConsole(text);
			}
		});
	}
	
	/***
	 * Append new message to current log in GUI console
	 * @param text
	 */
	private void appendConsole(String text) {
		consolePane2.append(text);
	}
	
	/***
	 * Redirect the info in terminal to GUI console by overwritten write method
	 * Referenced from Billwaa's Blog: https://billwaa.wordpress.com/2011/11/14/java-gui-console-output/#comment-100
	 */
	private void redirectSystemStreams() {
		 OutputStream out = new OutputStream() {
		    @Override
		    public void write(int b) throws IOException {
		      consoleUpdate(String.valueOf((char) b));
		    }
		 
		    @Override
		    public void write(byte[] b, int off, int len) throws IOException {
		      consoleUpdate(new String(b, off, len));
		    }
		 
		    @Override
		    public void write(byte[] b) throws IOException {
		      write(b, 0, b.length);
		    }
		 };
		 
		 System.setOut(new PrintStream(out, true));
		 System.setErr(new PrintStream(out, true));
	}
	
	/***
	 * Write the log info into file in default path
	 */
	private void extractLog() {
		try {			
			PrintWriter outStream = new PrintWriter(new FileOutputStream(logPath,true));
			// get time stamp
			Long timeStamp = System.currentTimeMillis();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ts_str = format.format(timeStamp);
			// write the log into default file
			outStream.print(consolePane2.getText());
			outStream.print(ts_str + "\n" + "\n");
			outStream.close();
		} catch(FileNotFoundException e) {
			// since default path is working path, it is relatively safe to save
			System.err.println("ERROR: could not create log, target directory does not exist.");
		}
	}
}
