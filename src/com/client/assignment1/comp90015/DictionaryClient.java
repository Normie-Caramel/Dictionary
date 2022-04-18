package com.client.assignment1.comp90015;

import java.awt.EventQueue;

import javax.swing.JFrame;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DictionaryClient{

	private final int DEFINE_ROWS = 7;
	private JFrame frmDictionaryServer;
	private JTextField wordBar;
	private JTextArea consoleArea;
	private JTextArea meaningArea;
	private String query_type;
	private ClientCommunication cc;
	private ArrayList<JRadioButton> buttonGroup = new ArrayList<JRadioButton>();
	private ArrayList<JTextField> textGroup = new ArrayList<JTextField>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryClient window = new DictionaryClient();
					window.frmDictionaryServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DictionaryClient() {
		initialize();
		this.query_type = "lookup";
		this.cc = new ClientCommunication();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDictionaryServer = new JFrame();
		frmDictionaryServer.setTitle("Dictionary Client");
		frmDictionaryServer.setBounds(100, 100, 640, 480);
		frmDictionaryServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryServer.getContentPane().setLayout(null);
		
		wordBar = new JTextField();
		wordBar.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		wordBar.setBounds(58, 10, 397, 27);
		frmDictionaryServer.getContentPane().add(wordBar);
		wordBar.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switch(getType()) {
					case "lookup": lookupOrRemove(); break;
					case "add": addOrUpdate(); break;
					case "remove": lookupOrRemove(); break;
					case "update": addOrUpdate(); break;
					default: break;
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnNewButton.setBounds(474, 9, 124, 23);
		frmDictionaryServer.getContentPane().add(btnNewButton);
		
		JRadioButton lookupButton = new JRadioButton("Look up");
		lookupButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exclude(lookupButton);
				setType("lookup");
				readMode();
				clear();
			}
		});
		lookupButton.setSelected(true);
		lookupButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lookupButton.setBounds(480, 60, 120, 23);
		frmDictionaryServer.getContentPane().add(lookupButton);
		buttonGroup.add(lookupButton);
		
		JRadioButton addButton = new JRadioButton("Add");
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exclude(addButton);
				setType("add");
				writeMode();
				clear();
			}
		});
		addButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		addButton.setBounds(480, 100, 120, 23);
		frmDictionaryServer.getContentPane().add(addButton);
		buttonGroup.add(addButton);
		
		JRadioButton removeButton = new JRadioButton("Remove");
		removeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exclude(removeButton);
				setType("remove");
				readMode();
				clear();
			}
		});
		removeButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		removeButton.setBounds(480, 140, 120, 25);
		frmDictionaryServer.getContentPane().add(removeButton);
		buttonGroup.add(removeButton);
		
		JRadioButton updateButton = new JRadioButton("Update");
		updateButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exclude(updateButton);
				setType("update");
				writeMode();
				clear();
			}
		});
		updateButton.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		updateButton.setBounds(480, 180, 120, 23);
		frmDictionaryServer.getContentPane().add(updateButton);
		buttonGroup.add(updateButton);
		
		JLabel wordLabel = new JLabel("Word :");
		wordLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		wordLabel.setBounds(10, 16, 45, 15);
		frmDictionaryServer.getContentPane().add(wordLabel);
		
		JLabel consoleLabel = new JLabel("Console : ");
		consoleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		consoleLabel.setBounds(10, 299, 73, 15);
		frmDictionaryServer.getContentPane().add(consoleLabel);
		
		JLabel meaningButton = new JLabel("Meaning :");
		meaningButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		meaningButton.setBounds(10, 47, 445, 15);
		frmDictionaryServer.getContentPane().add(meaningButton);
		
		consoleArea = new JTextArea();
		consoleArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		consoleArea.setBounds(10, 324, 445, 95);
		frmDictionaryServer.getContentPane().add(consoleArea);
		consoleArea.setEditable(false);
		
		meaningArea = new JTextArea();
		meaningArea.setBounds(10, 72, 445, 217);
		frmDictionaryServer.getContentPane().add(meaningArea);
		meaningArea.setEditable(false);
		
		for (int i=0; i<DEFINE_ROWS; i++) {
			JTextField tf = new JTextField();
			int y = 72 + i * 30;
			tf.setBounds(10, y, 445, 25);
			frmDictionaryServer.getContentPane().add(tf);
			tf.setColumns(10);
			tf.setVisible(false);
			textGroup.add(tf);
		}
	}
	
	private void exclude(JRadioButton button) {
		for (JRadioButton b : buttonGroup)
			b.setSelected(false);
		button.setSelected(true);
	}
	
	private void setType(String s) {
		this.query_type = s;
	}
	
	private String getType() {
		return this.query_type;
	}
	
	private void readMode() {
		meaningArea.setVisible(true);
		for (JTextField tf : this.textGroup) {
			tf.setVisible(false);
			tf.setEnabled(false);
		}
	}
	
	private void writeMode() {
		meaningArea.setVisible(false);
		for (JTextField tf : this.textGroup) {
			tf.setVisible(true);
			tf.setEnabled(true);
		}
	}
	
	private void lookupOrRemove() {
		String word = wordBar.getText();
		if (word.trim() != "" && word.length() > 0) {
			String result = cc.query(this.query_type, word);
			if (result.startsWith("Error:")) {
				consoleArea.setText(result);
				meaningArea.setText(null);
			}
			else {
				consoleArea.setText("query successed!");
				meaningArea.setText(result);
			}
		} else {
			consoleArea.setText("Error: please enter a valid word (not all blanks)");
			meaningArea.setText("");
		}
	}
	
	private void addOrUpdate() {
		String word = wordBar.getText();
		ArrayList<String> defs = new ArrayList<String>();
		for (JTextField tf : this.textGroup) {
			String text = tf.getText();
			if (text.trim() != "" && text.length() > 0)
				defs.add(text);
		}
		if (word.trim() != "" && word.length() > 0 && defs.size() != 0) {
			String result = cc.query(this.query_type, word, defs);
			consoleArea.setText(result);
			if (!result.startsWith("Error:")) {
				for (JTextField tf : this.textGroup)
					tf.setText("");
			}
		} else {
			consoleArea.setText("Error: please enter valid word and defination (not all blanks)");
		}
	}
	
	private void clear() {
		this.consoleArea.setText("");
		this.meaningArea.setText("");
		this.wordBar.setText("");
		for (JTextField tf : this.textGroup)
			tf.setText("");
	}
}
