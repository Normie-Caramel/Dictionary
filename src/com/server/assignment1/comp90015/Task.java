package com.server.assignment1.comp90015;

/***
 * Name: Jie Yang
 * Student ID: 1290106
 * E-mail: jieyang3@student.unimelb.edu.au
 */

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Task implements Runnable{

	private Socket socket;
	private Dictionary dict;
	private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();
    
    /**
     * Initialize the task by given parameters
     * @param socket: the socket this task are to communicate with
     * @param dict: the dictionary refered to
     */
	public Task(Socket socket, Dictionary dict) {
		this.socket = socket;
		this.dict = dict;
	}
	
	/***
	 * Handle the socket and process info when assigned a thread
	 */
	@Override
	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			process(input, output);
			socket.close();
			System.out.println("Client " + socket.getRemoteSocketAddress() + " disconnected...");
			
		} catch (ParseException e) {
			System.err.println("Error: Task interrupted, information received is invalid.");
		} catch (IOException ioe) {
			System.err.println("Error: connection interrupted... Task terminated.");
		}
	}
	
	/***
	 * Unpack JSON file and handle the query according to its type
	 * @param input: data input stream from socket
	 * @param output: data output stream to socket
	 * @throws ParseException when received file is invalid
	 * @throws IOException when stream breaks
	 */
	@SuppressWarnings("unchecked")
	private void process(DataInputStream input, DataOutputStream output) throws ParseException, IOException {
		
		JSONParser parser = new JSONParser();
		
		// receive the query
		JSONObject query = (JSONObject) parser.parse(input.readUTF());
		String result = "";
		switch((String)query.get("query_type")) {
			case "lookup":
				result = lookup(query); break;
			case "add":
				result = add(query); break;
			case "remove":
				result = remove(query); break;
			case "update":
				result = update(query); break;
		}
		
		// send response
		JSONObject response = new JSONObject();
		response.put("response", result);
		output.writeUTF(response.toJSONString());		
	}
	
	/***
	 * look up specified word in dictionary
	 * @param query word
	 * @return meanings or error
	 */
	private String lookup(JSONObject query){
		// implement read lock to increase throughput
		rlock.lock();
		try {
			String word = (String)query.get("word");
			return dict.lookup(word);
		} finally {
			// whatever happens, lock need to be released
			rlock.unlock();
		}
	}
	
	/***
	 * add word and its meanings to dictionary
	 * @param query word and meanings
	 * @return prompt indicating succeed or not
	 */
	@SuppressWarnings("unchecked")
	private String add(JSONObject query) {
		wlock.lock();
		try {
			String word = (String)query.get("word");
			JSONArray jsonDefs = (JSONArray)query.get("defs");
			String[] defs = new String[jsonDefs.size()];
			jsonDefs.toArray(defs);
			return dict.add(word, defs);
		} finally {
			wlock.unlock();
		}
	}
	
	/***
	 * remove word and its meanings from dictionary
	 * @param query word
	 * @return the meaning to be removed if successful, error message otherwise
	 */
	private String remove(JSONObject query) {
		wlock.lock();
		try {
			String word = (String)query.get("word");
			return dict.remove(word);
		} finally {
			wlock.unlock();
		}
	}
	
	/***
	 * update specified word's meaning
	 * @param query word and meaning
	 * @return a prompt indicating succeed or not
	 */
	@SuppressWarnings("unchecked")
	private String update(JSONObject query) {
		wlock.lock();
		try {
			String word = (String)query.get("word");
			JSONArray jsonDefs = (JSONArray)query.get("defs");
			String[] defs = new String[jsonDefs.size()];
			jsonDefs.toArray(defs);
			return dict.update(word, defs);
		} finally {
			wlock.unlock();
		}
	}

}
