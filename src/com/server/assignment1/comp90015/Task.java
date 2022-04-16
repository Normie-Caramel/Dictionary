package com.server.assignment1.comp90015;

import java.io.*;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Task implements Runnable{

	private Socket socket;
	
	public Task(Socket socket) {
		this.socket = socket;
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
		response.put("reponse", result);
		output.writeUTF(response.toJSONString());		
	}
	
	private String lookup(JSONObject query){
		
		return "lookup successful";
	}
	
	private String add(JSONObject query) {
		
		return "add successful";
	}
	
	private String remove(JSONObject query) {
		
		return "remove successful";
	}
	
	private String update(JSONObject query) {
		
		return "update successful";
	}

}
