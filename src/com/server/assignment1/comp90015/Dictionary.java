package com.server.assignment1.comp90015;

/***
 * Name: Jie Yang
 * Student ID: 1290106
 * E-mail: jieyang3@student.unimelb.edu.au
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Dictionary {
	
	private final int WORD_INDEX = 0;
	private final int DEF_INDEX = 1;
	private String path;
	private int query_count = 0;
	private int failure_count = 0;
	private int lookup_count = 0;
	private int add_count = 0;
	private int remove_count = 0;
	private int update_count = 0;
	private HashMap<String, String[]> dict;
	
	/***
	 * Initialize dictionary by given path
	 * @param path: dictionary file location
	 */
	public Dictionary(String path) {
		this.path = path;
		this.dict = new HashMap<String, String[]>();
	}
	
	/***
	 * Read and format the data from dictionary file
	 */
	public void init() {
		try {
			Scanner inputStream = new Scanner(new FileInputStream(this.path));
			// data is saved as plain txt, follow the format: word--meaning1&&meaning2&&...
			while(inputStream.hasNextLine()) {
				String[] entry = inputStream.nextLine().split("--", -1);
				String[] defs = entry[DEF_INDEX].split("&&",-1);
				dict.put(entry[WORD_INDEX], defs);
			}
			System.out.println("" + dict.size() + " words have been loaded.");
			inputStream.close();
			
		} catch (FileNotFoundException e) {
			// This error has been checked before (in GUI initialization), just make it safe.
			System.err.println("Error: could not find dictionary file.");
		}
	}
	
	/***
	 * Write the data from memeory into original file following the predetermined format
	 * @return true if writing succeed, false otherwise
	 */
	public boolean write() {
		try {
			PrintWriter outStream = new PrintWriter(new FileOutputStream(path, false));
			String dict_log = "";
			// data is saved as plain txt, follow the format: word--meaning1&&meaning2&&...
			for (HashMap.Entry<String, String[]> entry : dict.entrySet()) {
				String word = entry.getKey();
				String[] defs = entry.getValue();
				String def_str = "";
	    		for (String def : defs)
	    			def_str = def_str + def + "&&";
	    		def_str = def_str.substring(0, def_str.length()-2);
	    		dict_log = dict_log + word + "--" + def_str + "\n";
			}
			outStream.print(dict_log.trim());
			outStream.close();
			return true;
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: failed to save, target directory does not exist.");
			return false;
		}
	}
	
	/***
	 * Execute look up query
	 * @param word: word to look up
	 * @return meanings in well formatted string if entry exists, error message otherwise
	 */
	public String lookup(String word) {
		this.query_count++;
		if (this.dict.containsKey(word)) {
			// format the meanings to make it appropriate to display
			String response = "";
			String[] defs = dict.get(word);
			int def_count = 1;
			for (String def : defs) {
				response = response + "def" + def_count + ". " + def + "\n";
				def_count++;
			}
			this.lookup_count++;
			return response.trim();
		} else {
			this.failure_count++;
			return "Error: The word you are looking for was not found.";
		}
	}
	
	/***
	 * Execute add query
	 * @param word: word whose meanings to add
	 * @param defs: meanings to add
	 * @return a prompt indicating whether succeed or not, if the word exits, query will fail
	 */
	public String add(String word, String[] defs) {
		this.query_count++;
		if (!this.dict.containsKey(word)) {
			this.dict.put(word, defs);
			this.add_count++;
			return "Successfully add the word: " + word;
		} else {
			this.failure_count++;
			return "Error: target word exists, fail to add duplicate word.";
		}
		
	}
	
	/***
	 * Execute remove query
	 * @param word: word to remove
	 * @return the meaning of the word that has been removed if succeed, an error message if word
	 * does not exist.
	 */
	public String remove(String word) {
		this.query_count++;
		if (this.dict.containsKey(word)) {
			// format the meanings to make it appropriate to display
			String response = "Removed Definations: \n";
			String[] defs = dict.get(word);
			int def_count = 1;
			for (String def : defs) {
				response = response + "def" + def_count + ". " + def + "\n";
				def_count++;
			}
			this.dict.remove(word);
			this.remove_count++;
			return response;
		} else {
			this.failure_count++;
			return "Error: target word does not exist, fail to remove empty word.";
		}
	}
	
	/***
	 * Execute update query
	 * @param word: word whose meanings to update
	 * @param defs: meanings to update
	 * @return a prompt indicating whether succeed or not, if the word does not exit, query will fail
	 */
	public String update(String word, String[] defs) {
		this.query_count++;
		if (this.dict.containsKey(word)) {
			this.dict.put(word, defs);
			this.update_count++;
			return "Successfully update the word: " + word;
		} else {
			this.failure_count++;
			return "Error: target word does not exist, please add it first.";
		}
	}
	
	/***
	 * Get the number of words in the dictionary
	 * @return words count
	 */
	public int getWordNum() {
		return this.dict.size();
	}
	
	/***
	 * Get the number of query applied in this run
	 * @return query count
	 */
	public int getQueryNum() {
		return this.query_count;
	}
	
	/***
	 * Get the number of add query applied in this run
	 * @return add query count
	 */
	public int getAddNum() {
		return this.add_count;
	}
	
	/***
	 * Get the number of remove query applied in this run
	 * @return remove query count
	 */
	public int getRemoveNum() {
		return this.remove_count;
	}
	
	/***
	 * Get the number of update query applied in this run
	 * @return update query count
	 */
	public int getUpdateNum() {
		return this.update_count;
	}
	
	/***
	 * Get the number of look up query applied in this run
	 * @return look up query count
	 */
	public int getLookupNum() {
		return this.lookup_count;
	}
	
	/***
	 * Get the number of failed query in this run
	 * @return failed query count
	 */
	public int getFailureNum() {
		return this.failure_count;
	}
}
