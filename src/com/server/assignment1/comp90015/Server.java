package com.server.assignment1.comp90015;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {
	
	private int serverPort;
	private int threadNum;
	private boolean running = true;
	private String dict_path;
	private Dictionary dict;

	public Server(int port, int threadNum, String dict_path) {
		this.serverPort = port;
		this.threadNum = threadNum;
		this.dict_path = dict_path;
		this.dict = new Dictionary(this.dict_path);
	}
	
	public void run() {
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		dict.init();
		
		try(ServerSocket ss = new ServerSocket(serverPort)) {
			
			System.out.println("Server is waiting on port: " + serverPort + "...");
			
			while(true) {
				
				Socket socket = ss.accept();
				System.out.println("connected from " + socket.getRemoteSocketAddress());
				executor.submit(new Task(socket, dict));
				
				if(!running) break;
			}
			
			dict.write();
			ss.close();
			executor.shutdown();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		this.running = false;
	}
}
