package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.text.BadLocationException;

public class ServerThread implements Runnable {

	BufferedReader reader;
	Scanner scanner;

	Server server;

	ArrayList<ClientThread> clients;

	private boolean running = true;

	String s;

	public ServerThread(Server server) {
		this.server = server;
		scanner = new Scanner(System.in);
	}

	@Override
	public void run() {

		while (running) {
			s = scanner.nextLine();

			if (s.equalsIgnoreCase("finished")) {
				running = false;
				break;
			}
			try {
				server.broadcastAll(s);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//				System.out.print("Client " + id + " : " + s + "\n");
//				broadcast(s);

		}

	}

}
