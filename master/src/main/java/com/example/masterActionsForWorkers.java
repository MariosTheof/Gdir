package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class masterActionsForWorkers implements Runnable{
	
	Socket connection;
	ObjectInputStream inFromWorker;
	ObjectOutputStream outToWorker;
	Query q = null;
	Routes r = null;
	Boolean workerDone = false;
	Boolean threadlock = false;
	Boolean startAPI = false;
	public masterActionsForWorkers(Socket connection, Query query) {
		try {
			this.connection = connection;
			q = query;
			outToWorker = new ObjectOutputStream(connection.getOutputStream());
			inFromWorker = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {			
			
			outToWorker.writeObject(q);
			outToWorker.flush();		
			workerDone = inFromWorker.readBoolean();

			synchronized(threadlock){
				while(!threadlock){
					threadlock.wait();
				}
			}
			outToWorker.writeBoolean(startAPI);
			outToWorker.flush();
			if(startAPI == true){
				r = (Routes) inFromWorker.readObject();
			}
			
			outToWorker.close();
			inFromWorker.close();
			
			this.connection.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	public Query getQuery(){
		return q;
	}
	public Routes getAPIRoutes(){
		return r;
	}
}
