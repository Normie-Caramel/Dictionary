package com.server.assignment1.comp90015;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class DictionaryServer {
	
	private static final int SERVER_PORT = 4200;
	private static final int THREAD_NUM = 10;
	private static boolean running = true;

	public static void main(String[] args) {
		// set up thread pool
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);
		
		// keep listening from clients
		try(ServerSocket ss = new ServerSocket(SERVER_PORT)) {
			
			System.out.println("Server is waiting on port: " + SERVER_PORT + "...");
			
			while(true) {
				
				// once connection established, pass the mission to a thread
				Socket socket = ss.accept();
				System.out.println("connected from " + socket.getRemoteSocketAddress());
				executor.submit(new Task(socket));
				
				if(!running) break;
			}
			
			ss.close();
			executor.shutdown();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
