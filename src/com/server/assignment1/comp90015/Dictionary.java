package com.server.assignment1.comp90015;

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
	private int word_count = 0;
	private int query_count = 0;
	private int failure_count = 0;
	private int lookup_count = 0;
	private int add_count = 0;
	private int remove_count = 0;
	private int update_count = 0;
	private HashMap<String, String[]> dict;
	
	
	public Dictionary(String path) {
		this.path = path;
		this.dict = new HashMap<String, String[]>();
	}
	
	public void init() {
		try {
			Scanner inputStream = new Scanner(new FileInputStream(this.path));
			while(inputStream.hasNextLine()) {
				String[] entry = inputStream.nextLine().split("--", -1);
				String[] defs = entry[DEF_INDEX].split("&&",-1);
				dict.put(entry[WORD_INDEX], defs);
				this.word_count++;
			}
			System.out.println("" + this.word_count + " words have been loaded.");
			inputStream.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("Error: could not find dictionary file.");
			System.exit(1);
		}
	}
	
	public void write() {
		try {
			PrintWriter outStream = new PrintWriter(new FileOutputStream(path, false));
			String dict_log = "";
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
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: failed to save, target directory does not exist.");
		}
	}
	
	public String lookup(String word) {
		this.query_count++;
		if (this.dict.containsKey(word)) {
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
	
	public String add(String word, String defs_str) {
		this.query_count++;
		if (!this.dict.containsKey(word)) {
			String[] defs = defs_str.split("&&");
			this.dict.put(word, defs);
			this.add_count++;
			return "Successfully add the word: " + word;
		} else {
			this.failure_count++;
			return "Error: target word exists, fail to add duplicate word.";
		}
		
	}
	
	public String remove(String word) {
		this.query_count++;
		if (this.dict.containsKey(word)) {
			this.dict.remove(word);
			this.remove_count++;
			return "Successfully remove the word: " + word;
		} else {
			this.failure_count++;
			return "Error: target word does not exist, fail to remove empty word.";
		}
	}
	
	public String update(String word, String defs_str) {
		this.query_count++;
		if (this.dict.containsKey(word)) {
			String[] defs = defs_str.split("&&");
			this.dict.put(word, defs);
			this.update_count++;
			return "Successfully update the word: " + word;
		} else {
			this.failure_count++;
			return "Error: target word does not exist, please add it first.";
		}
	}
	
	public int getWordNum() {
		return this.dict.size();
	}
	
	public int getQueryNum() {
		return this.query_count;
	}
	
	public int getAddNum() {
		return this.add_count;
	}
	
	public int getRemoveNum() {
		return this.remove_count;
	}
	
	public int getUpdateNum() {
		return this.update_count;
	}
	
	public int getLookupNum() {
		return this.lookup_count;
	}
	
	public int getFailureNum() {
		return this.failure_count;
	}
}
