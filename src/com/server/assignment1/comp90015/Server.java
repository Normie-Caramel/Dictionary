package com.server.assignment1.comp90015;

/***
 * Name: Jie Yang
 * Student ID: 1290106
 * E-mail: jieyang3@student.unimelb.edu.au
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
	
	private int serverPort;
	private String dict_path;
	private Dictionary dict;
	private ExecutorService executor;

	/***
	 * Initialize the server instance
	 * @param port: the port server listen on
	 * @param threadNum: number of thread server maintains
	 * @param dict_path: the path of dictionary that server refers to
	 */
	public Server(int port, int threadNum, String dict_path) {
		this.serverPort = port;
		this.dict_path = dict_path;
		this.dict = new Dictionary(this.dict_path);
		this.executor = Executors.newFixedThreadPool(threadNum);
		this.dict.init();
	}
	
	/***
	 * Listen on port and assign tasks when assigened a thread to run
	 */
	public void run() {
		
		try(ServerSocket ss = new ServerSocket(serverPort)) {	
			System.out.println("Server is listening on port: " + serverPort + "...");
			while(true) {
				Socket socket = ss.accept();
				System.out.println("connected from " + socket.getRemoteSocketAddress());
				executor.submit(new Task(socket, dict));
			}
		} catch (IOException e) {
			// Server should restart automatically when crash on stream
			System.err.println("data stream failure, reboot in progress...");
			run();
		}
	}
	
	/***
	 * Save data and close thread pool before closed, make data and queries safe
	 */
	public void close() {
		dict.write();
		executor.shutdown();
	}
	
	/***
	 * Save data in memory into original file
	 * @return true if data is saved successfully, false otherwise
	 */
	public boolean save() {
		return dict.write();
	}
	
	/***
	 * Generate report prompting query statistic features
	 * @return report contents
	 */
	public String report() {
		int word_num = dict.getWordNum();
		int query_num = dict.getQueryNum();
		int failure_num = dict.getFailureNum();
		int lookup_num = dict.getLookupNum();
		int add_num = dict.getAddNum();
		int remove_num = dict.getRemoveNum();
		int update_num = dict.getUpdateNum();
		String report = "Current words number: " + word_num + "\n"
				+ "Query number: " + query_num + "\n"
				+ "Look-up query number: " + lookup_num + "\n"
				+ "Add query number: " + add_num + "\n"
				+ "Remove query number: " + remove_num + "\n"
				+ "Update query number: " + update_num + "\n"
				+ "Failed query number: " + failure_num;
		return report;
	}
}
