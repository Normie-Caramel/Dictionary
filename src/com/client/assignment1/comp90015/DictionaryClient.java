package com.client.assignment1.comp90015;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

public class DictionaryClient {

	private static String ip = "localhost";
	private static int port = 4200;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		try(Socket socket = new Socket(ip, port);){
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
	    	
    		JSONObject newCommand = new JSONObject();
    		
    		newCommand.put("query_type", "add");
    		
    		output.writeUTF(newCommand.toJSONString());
    		output.flush();
    		
    		String result = input.readUTF();
    		System.out.println("Received from server: "+result);
    		
    		socket.close();
    		System.out.println("disconnected...");
		    
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Unable to connect to remote server...");
		}
	}

}
