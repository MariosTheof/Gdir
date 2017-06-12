package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class reducerActionsForWorkers implements Runnable{

	ServerSocket reducerSocket;
	Socket connection = null;
	ObjectInputStream inFromWorker;
	ObjectOutputStream outToWorker;
	Routes [] r;
	public reducerActionsForWorkers(int port) {
		try {
			
			reducerSocket = new ServerSocket(port, 10);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){

		try {
			connection = reducerSocket.accept();
			outToWorker = new ObjectOutputStream(connection.getOutputStream());
			inFromWorker = new ObjectInputStream(connection.getInputStream());

			r = (Routes[]) inFromWorker.readObject();

			outToWorker.close();
			inFromWorker.close();

			connection.close();
			reducerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
			
	}
	public Routes[] getRoutes(){
		return r;
	}
}