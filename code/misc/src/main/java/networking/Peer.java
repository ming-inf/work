package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Peer {
	static AtomicInteger port = new AtomicInteger(16000);
	static AtomicBoolean listening = new AtomicBoolean(true);
	static final LinkedBlockingQueue<String> connectQ = new LinkedBlockingQueue<>();
	static final LinkedBlockingQueue<String> messageQ = new LinkedBlockingQueue<>();
	static final LinkedBlockingQueue<String> disconnectQ = new LinkedBlockingQueue<>();
	static final List<PrintWriter> peers = new ArrayList<>();

	public static void main(String[] args) throws InterruptedException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		new Thread() {
			@Override
			public void run() {
				try {
					String command;
					while ((command = stdIn.readLine()) != null) {
						if (command.startsWith("connect")) {
							connectQ.add(command);
						} else if ("q".equals(command)) {
							break;
						} else {
							messageQ.add(command);
						}
					}
					disconnectQ.add("");
					listening.set(false);
				} catch (IOException e) {
				}
				System.out.println("Closed");
			}
		}.start();

		int portNumber = port.getAndIncrement();
		while (portNumber < 16003) {
			try (ServerSocket listenerSocket = new ServerSocket(portNumber)) {
				System.out.println("listening on port " + portNumber);
				startListening(listenerSocket).start();

				connected(listenerSocket);

				listenerSocket.close();
				break;
			} catch (Exception e) {
				System.err.println("Could not listen on port " + portNumber);
				System.out.println(e);
				portNumber = port.getAndIncrement();
			}
		}
	}

	private static void connected(ServerSocket listenerSocket) throws IOException, InterruptedException {
		AtomicBoolean connectAgain = new AtomicBoolean(true);
		String userInput;
		while (connectAgain.get()) {
			userInput = connectQ.take();
			System.out.println(userInput);

			String[] parsedInput = userInput.split(" ");
			String command = parsedInput[0];
			String hostName = parsedInput[1];
			int peerPortNumber = Integer.parseInt(parsedInput[2]);

			if (!"connect".equals(command)) {
				break;
			}

			try (Socket echoSocket = new Socket(hostName, peerPortNumber);
					PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()))) {
				new Thread() {
					@Override
					public void run() {
						try {
							String inputLine;
							while ((inputLine = in.readLine()) != null) {
								System.out.println(echoSocket.getInetAddress() + ":" + echoSocket.getPort() + " " + inputLine);
							}
						} catch (IOException e) {
						}
					}
				}.start();

				final AtomicBoolean stayConnected = new AtomicBoolean(true);
				new Thread() {
					@Override
					public void run() {
						try {
							disconnectQ.take();
							stayConnected.set(false);
							connectAgain.set(false);
							messageQ.add(""); // add poison pill
						} catch (InterruptedException e) {
						}
					}
				}.start();

				while (stayConnected.get()) {
					userInput = messageQ.take();
					if (userInput.isEmpty()) { // it's poison
						break;
					} else if ("ping".equals(userInput)) {
						out.println("ping");
					} else {
						out.println(userInput);
					}
				}
				echoSocket.shutdownInput();
				echoSocket.shutdownOutput();
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host " + hostName);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);
			}
		}
		listening.set(false);
	}

	private static Thread startListening(ServerSocket listenerSocket) {
		return new Thread() {
			@Override
			public void run() {
				while (listening.get()) {
					try {
						Socket clientSocket = listenerSocket.accept();
						Thread thread = new Thread() {
							@Override
							public void run() {
								try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
										BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
									peers.add(out);
									new Thread() {
										@Override
										public void run() {
											try {
												String message;
												while ((message = messageQ.take()) != null) {
													out.println(message);
												}
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									}.start();

									String inputLine;
									while ((inputLine = in.readLine()) != null) {
										String outputLine;
										if ("ping".equals(inputLine)) {
											outputLine = "pong";
										} else {
											outputLine = inputLine;
										}
										out.println(outputLine);
										System.out.println(outputLine);
									}
									peers.remove(out);
									clientSocket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						};
						thread.setDaemon(false);
						thread.start();
					} catch (IOException e1) {
						listening.set(false);
					}
				}
			}
		};
	}
}
