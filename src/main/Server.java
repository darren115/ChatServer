package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.text.BadLocationException;

public class Server {
	
	private UserInterface UI;

	int port = 5000;
	ServerSocket sSocket = null;
	ExecutorService executor = null;
	int clientCount = 0;
	
	Map<String, ClientThread> userMap;

	public Server() {
		executor = Executors.newFixedThreadPool(5);
		
		userMap = new HashMap<>();
		
		
		ServerThread server = new ServerThread(this);
		
		UI = new UserInterface(this);
		
		executor.execute(server);

		try {
			sSocket = new ServerSocket(port);
			System.out.println("Server socket started on port " + port);
			System.out.println("To end server type 'FINISHED'");
			while (true) {
				Socket socket = sSocket.accept();
				
				ClientThread ct = new ClientThread(socket, this);
				executor.execute(ct);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void addClient(String username, ClientThread thread) throws BadLocationException {
		if (userMap.containsKey(username)) {
			System.out.println("User already exists need to handle differently");
		}else {
			broadcastAll("User " + username + " has connected");
			userMap.put(username, thread);
			clientCount++;
			UI.appendList(username);
		}
	}
	
	
	
	public Map<String, ClientThread> getClients(){
		return userMap;
	}
	
	public boolean messageClient(String username, String message) throws BadLocationException {
		UI.appendMessage(message);
		ClientThread client = userMap.get(username);
		if(client == null) return false;
		
		client.sendMessage(message);
		return true;
	}
	
	public void closeClient(String client) throws BadLocationException {
		clientCount--;
		userMap.remove(client);
		UI.removeList(client);
		broadcastAll("User " + client + " has disconnected");
	}
	
	public void broadcast(String message, ClientThread client) throws BadLocationException {
		UI.appendMessage(client.username + ": " + message);
		for( ClientThread ct: userMap.values()) {
			if(!ct.equals(client))
				ct.sendMessage(client.username + ": " + message);
		}
	}
	
	public void broadcastAll(String message) throws BadLocationException {
		UI.appendMessage(message);
		for( ClientThread st: userMap.values()) {
			st.sendMessage(message);
		}
	}

}
