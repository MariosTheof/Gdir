package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class reducerActionsForMaster implements Runnable{

	ServerSocket reducerSocket;
	Socket connection = null;
	ObjectInputStream inFromMaster;
	ObjectOutputStream outToMaster;
	Boolean mastersignal = false;
	int portsforworkers[] = {4331, 4332, 4333};
	Routes [] r;
	reducerActionsForWorkers rAFW1;
	reducerActionsForWorkers rAFW2;
	reducerActionsForWorkers rAFW3;
	public reducerActionsForMaster(int port) {
		try {
			
			reducerSocket = new ServerSocket(port, 10);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){

		rAFW1 = new reducerActionsForWorkers(portsforworkers[0]);
		Thread worker1Thread = new Thread(rAFW1);
		worker1Thread.start();
		TApair taw1 = new TApair (worker1Thread, rAFW1);
		
		rAFW2 = new reducerActionsForWorkers(portsforworkers[1]);
		Thread worker2Thread = new Thread(rAFW2);
		worker2Thread.start();
		TApair taw2 = new TApair (worker2Thread, rAFW2);
		
		rAFW3 = new reducerActionsForWorkers(portsforworkers[2]);
		Thread worker3Thread = new Thread(rAFW3);
		worker3Thread.start();
		TApair taw3 = new TApair (worker3Thread, rAFW3);
		
		try {
			connection = reducerSocket.accept();
			outToMaster = new ObjectOutputStream(connection.getOutputStream());
			inFromMaster = new ObjectInputStream(connection.getInputStream());
			
			mastersignal = inFromMaster.readBoolean();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			taw1.thread.join();
			taw2.thread.join();
			taw3.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		r = reduce(taw1.actionsrw.getRoutes(), taw2.actionsrw.getRoutes(), taw3.actionsrw.getRoutes());

		try {
			outToMaster.writeObject(r);
			outToMaster.flush();
						
			outToMaster.close();
			inFromMaster.close();

			connection.close();
			reducerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private Routes[] reduce(Routes [] r1, Routes [] r2, Routes [] r3){
		if(r1 == null && r2 == null && r3 == null){
			return null;
		}
		if(r1.length == 0 && r2.length == 0 && r3.length == 0){
			return null;
		}
		r = new Routes [r1.length + r2.length + r3.length];
		for (int i = 0; i < r1.length + r2.length + r3.length; i++){
			if(i < r1.length){
				r[i] = r1[i];
			}else if(i < r1.length + r2.length){
				r[i] = r2[i - r1.length];
			}else if(i < r1.length + r2.length + r3.length){
				r[i] = r3[i - r1.length - r2.length];				
			}
		}
		return r;
	}
}