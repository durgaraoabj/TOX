package com.springmvc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketSysmax {

	// static ServerSocket variable
	public static ServerSocket server;
	public static String ipAddress = "localhost";
	// socket server port on which it will listen
	public static int port = 8002;

	public static void main(String[] args) {
		runSysmexServier();
		

	}

	public static void runSysmexServier() {
		try {
			/*
			 * //create the socket server object with ip InetAddress
			 * inetAddress=InetAddress.getByName("10.1.23.172"); server = new
			 * ServerSocket(); //Binding the SocketAddress with inetAddress and port
			 * SocketAddress endPoint=new InetSocketAddress(inetAddress, port); //bind()
			 * method the ServerSocket to the specified socket address
			 * server.bind(endPoint);
			 */
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			server = new ServerSocket();
			// Binding the SocketAddress with inetAddress and port
			SocketAddress endPoint = new InetSocketAddress(inetAddress, port);
			// bind() method the ServerSocket to the specified socket address
			server.bind(endPoint);
			/*
			 * //create the socket server object with out ip server = new
			 * ServerSocket(port);
			 */

			// keep listens indefinitely until receives 'exit' call or program terminates
			while (true) {
				System.out.println("Waiting for the client request");
				// creating socket and waiting for client connection
				Socket socket = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				System.out.println(in);
//	            while(in.ready()) {
//			    	System.out.println(in.readLine());
//			    }
//	            String line =  in.lines().collect(Collectors.joining());
				String line = in.readLine();
				int length = 0;
				while (!line.equals("")) {
					length = length + line.length();
					line = in.readLine();
					System.out.println(line);
				}
				System.out.println(line);
				System.out.println(in);

				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				// write object to Socket
				oos.writeObject("Hi Sysmex we have received Data successfully. status code : " + 200);
				// close resources
				in.close();
				oos.close();
				socket.close();
				// terminate the server if client sends exit request

			}

		} catch (IOException ioe) {
			// TODO: handle exception
			ioe.printStackTrace();
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}finally {
			try {
			server.close();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
