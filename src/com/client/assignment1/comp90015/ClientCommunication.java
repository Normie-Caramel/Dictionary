package com.client.assignment1.comp90015;

/***
 * Name: Jie Yang
 * Student ID: 1290106
 * E-mail: jieyang3@student.unimelb.edu.au
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientCommunication {

	private String ip;
	private int port;
	private JSONParser parser = new JSONParser();
	
	/***
	 * Instantiated with default values, which can be updated per query
	 */
	public ClientCommunication() {
		port = 5000;
		ip = "localhost";
	}
	
	/***
	 * Establish a connection to server, execute 'add' and 'update' queries
	 * @param type: query type, either add or update
	 * @param word: word to add or update
	 * @param defs: meannings to add or update
	 * @return response from server indicating whether successful or not
	 */
	@SuppressWarnings("unchecked")
	public String query(String type, String word, ArrayList<String> defs) {
		
		try(Socket socket = new Socket(ip, port);){
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
	    	
		    // translate the query into JSON
    		JSONObject newCommand = new JSONObject();
    		JSONArray jsonDefs = new JSONArray();
    		for (String def: defs)
    			jsonDefs.add(def);
    		
    		newCommand.put("query_type", type);
    		newCommand.put("word", word.toLowerCase());
    		newCommand.put("defs", jsonDefs);
    		
    		// send message
    		output.writeUTF(newCommand.toJSONString());
    		output.flush();
    		
    		// receive and resolve message
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
	
	/***
	 * Establish a connection to server, execute 'look up' and 'remove' queries
	 * @param type: query type, either lookup or remove
	 * @param word: word to look up or remove
	 * @return meanings of the word if query succeed, an error message otherwise
	 */
	@SuppressWarnings("unchecked")
	public String query(String type, String word) {
		
		try(Socket socket = new Socket(ip, port);){
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
	    	
		    // translate the query into JSON
    		JSONObject newCommand = new JSONObject();
    		
    		newCommand.put("query_type", type);
    		newCommand.put("word", word);
    		
    		// send message
    		output.writeUTF(newCommand.toJSONString());
    		output.flush();
    		
    		// receive and resolve message
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
	
	/***
	 * Update the server ip that to connect
	 * @param ip: target server ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/***
	 * Update the server port that to connect
	 * @param port: target server port
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
