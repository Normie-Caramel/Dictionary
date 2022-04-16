package com.server.assignment1.comp90015;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class DictionaryServer {
	
	private static final int SERVER_PORT = 5000;
	private static final int THREAD_NUM = 10;
	private static boolean running = true;
	private static String dict_path = System.getProperty("user.dir") + File.separator + "dict.txt";
	private static Dictionary dict = new Dictionary(dict_path);

	public static void main(String[] args) {
		// set up thread pool
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);
		dict.init();
		
		// keep listening from clients
		try(ServerSocket ss = new ServerSocket(SERVER_PORT)) {
			
			System.out.println("Server is waiting on port: " + SERVER_PORT + "...");
			
			while(true) {
				
				// once connection established, pass the mission to a thread
				Socket socket = ss.accept();
				System.out.println("connected from " + socket.getRemoteSocketAddress());
				executor.submit(new Task(socket, dict));
				
				if(!running) break;
			}
			
			ss.close();
			executor.shutdown();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
