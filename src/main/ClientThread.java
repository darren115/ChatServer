package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.text.BadLocationException;

public class ClientThread implements Runnable {

	Socket socket;
	int id;
	Server server = null;
	BufferedReader input;
	PrintStream output;
	String s = "";

	Scanner scanner = new Scanner(System.in);

	String username = "client";

	boolean running = true;

	public ClientThread(Socket client, Server server) {
		this.socket = client;
		this.server = server;

		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintStream(socket.getOutputStream());

			s = input.readLine();
			if ("connected".equals(s.substring(s.length() - 9, s.length()))) {
				username = (s.substring(0, s.length() - 9).trim());
				server.addClient(username, this);

			} else {
				closeConnections();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			while (running) {
				s = input.readLine();

				if (s.equalsIgnoreCase("finished")) {
					running = false;
					break;
				}

				if (s.charAt(0) == '@') {
					String[] split = s.split("\s");

					if (server.messageClient(split[0].substring(1), username + ":" + s.substring(split[0].length()))) {

					} else {
						server.messageClient(username, "User " + split[0].substring(1) + " not found");
					}
				} else {

					System.out.print(username + " : " + s + "\n");
					server.broadcast(s, this);
				}

			}
			closeConnections();

		} catch (IOException e) {
//			e.printStackTrace();
			closeConnections();

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void closeConnections() {
		try {
			server.closeClient(username);
			running = false;
			input.close();
			output.close();
//			socket.close();
		} catch (IOException | BadLocationException e) {

		}
	}

	public void sendMessage(String message) {
		output.println(message);
	}

}
