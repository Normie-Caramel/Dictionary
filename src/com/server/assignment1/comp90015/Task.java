package com.server.assignment1.comp90015;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Task implements Runnable{

	private Socket socket;
	private Dictionary dict;
	private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();
	
	public Task(Socket socket, Dictionary dict) {
		this.socket = socket;
		this.dict = dict;
	}
	
	@Override
	public void run() {
		
		try {
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			process(input, output);
			socket.close();
			System.out.println("Client disconnected...");
			
		} catch (IOException|ParseException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void process(DataInputStream input, DataOutputStream output) throws ParseException, IOException {
		
		JSONParser parser = new JSONParser();
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
		JSONObject response = new JSONObject();
		response.put("response", result);
		output.writeUTF(response.toJSONString());		
	}
	
	private String lookup(JSONObject query){
		rlock.lock();
		try {
			String word = (String)query.get("word");
			return dict.query(word);
		} finally {
			rlock.unlock();
		}
	}
	
	private String add(JSONObject query) {
		wlock.lock();
		try {
			String word = (String)query.get("word");
			String defs = (String)query.get("defs");
			return dict.add(word, defs);
		} finally {
			wlock.unlock();
		}
	}
	
	private String remove(JSONObject query) {
		wlock.lock();
		try {
			String word = (String)query.get("word");
			return dict.remove(word);
		} finally {
			wlock.unlock();
		}
	}
	
	private String update(JSONObject query) {
		wlock.lock();
		try {
			String word = (String)query.get("word");
			String defs = (String)query.get("defs");
			return dict.update(word, defs);
		} finally {
			wlock.unlock();
		}
	}

}
