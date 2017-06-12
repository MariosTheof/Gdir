package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class masterActionsForReducer implements Runnable{
	
	Socket connection;
	ObjectInputStream inFromReducer;
	ObjectOutputStream outToReducer;
	Routes r[];
	public masterActionsForReducer(Socket connection){
		try{
			
			this.connection = connection;
			
			outToReducer = new ObjectOutputStream(connection.getOutputStream());
			inFromReducer = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			outToReducer.writeBoolean(true);
			outToReducer.flush();

			r = (Routes[]) inFromReducer.readObject();
			
			outToReducer.close();
			inFromReducer.close();
			
			this.connection.close();
			
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
