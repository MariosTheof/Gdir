package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class mapWorkerActionsForReducer implements Runnable{

	ObjectInputStream inFromReducer;
	ObjectOutputStream outToReducer;
	
	Routes routes[];
	mapWorkerActionsForReducer(Socket connection, Routes [] r){

		try {
			this.routes = r;
			outToReducer = new ObjectOutputStream(connection.getOutputStream());
			inFromReducer = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			outToReducer.writeObject(this.routes);
			outToReducer.flush();
			
			outToReducer.close();
			inFromReducer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
