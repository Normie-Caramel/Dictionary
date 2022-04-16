package com.client.assignment1.comp90015;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DictionaryClient {

	private static String ip = "localhost";
	private static int port = 5000;
	private static JSONParser parser = new JSONParser();
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		try(Socket socket = new Socket(ip, port);){
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
	    	
    		JSONObject newCommand = new JSONObject();
    		
    		newCommand.put("query_type", "add");
    		newCommand.put("word", "love");
    		newCommand.put("defs", "like somebody very much.");
    		
    		output.writeUTF(newCommand.toJSONString());
    		output.flush();
    		
    		JSONObject response = (JSONObject)parser.parse(input.readUTF());
    		String result = (String)response.get("response");
    		System.out.println(result);
    		
    		socket.close();
    		System.out.println("disconnected...");
		    
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Unable to connect to remote server...");
		} catch (ParseException e) {
			System.out.println("unreadable response from server...");
		}
	}

}
