package com.client.assignment1.comp90015;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientCommunication {

	private String ip;
	private int port;
	private JSONParser parser = new JSONParser();
	
	public ClientCommunication() {
		port = 5000;
		ip = "localhost";
	}
	
	@SuppressWarnings("unchecked")
	public String query(String type, String word, ArrayList<String> defs) {
		
		try(Socket socket = new Socket(ip, port);){
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
	    	
    		JSONObject newCommand = new JSONObject();
    		
    		String def_str = "";
    		for (String def : defs)
    			def_str = def_str + def + "&&";
    		def_str = def_str.substring(0, def_str.length()-2);
    		
    		newCommand.put("query_type", type);
    		newCommand.put("word", word);
    		newCommand.put("defs", def_str);
    		
    		output.writeUTF(newCommand.toJSONString());
    		output.flush();
    		
    		JSONObject response = (JSONObject)parser.parse(input.readUTF());
    		String result = (String)response.get("response");
    		
    		socket.close();
    		
    		return result;
		    
		} catch (UnknownHostException e) {
			return "Error: IP address of the host name could not be determined";
		} catch (IOException e) {
			return "Error: Unable to connect to remote server...";
		} catch (ParseException e) {
			return "Error: unreadable response from server...";
		}
	}
	
	@SuppressWarnings("unchecked")
	public String query(String type, String word) {
		
		try(Socket socket = new Socket(ip, port);){
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
	    	
    		JSONObject newCommand = new JSONObject();
    		
    		newCommand.put("query_type", type);
    		newCommand.put("word", word);
    		
    		output.writeUTF(newCommand.toJSONString());
    		output.flush();
    		
    		JSONObject response = (JSONObject)parser.parse(input.readUTF());
    		String result = (String)response.get("response");
    		
    		socket.close();
    		
    		return result;
		    
		} catch (UnknownHostException e) {
			return "Error: IP address of the host name could not be determined";
		} catch (IOException e) {
			return "Error: Unable to connect to remote server...";
		} catch (ParseException e) {
			return "Error: unreadable response from server...";
		}
	}

}
