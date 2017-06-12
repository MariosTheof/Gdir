package com.example;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Scanner;

import us.monoid.web.Resty;

public class mapWorkerActionsForMaster implements Runnable{
	
	static final String REDUCERIP = "127.0.0.1";//CHANGE THIS ACCORDINGLY
	static final int PORTFORREDUCER = 4332;
	
	private static String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?";
	private static String ARGS = "origin=%s&destination=%s&sensor=true";
	private static String LOCATION_ARG = "%s,%s";
	private static String ENCODING = "UTF-8";
	
	ObjectInputStream inFromMaster;
	ObjectOutputStream outToMaster;
	
	Query q = null;
	Routes [] r;
	Directions dfromgoogle;
	Routes rfromgoogle;
	public mapWorkerActionsForMaster(Socket connection) {
		try {
			outToMaster = new ObjectOutputStream(connection.getOutputStream());
			inFromMaster = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			q = (Query) inFromMaster.readObject();

			r = map(q);

			Socket connectiontoreducer = new Socket(REDUCERIP, PORTFORREDUCER);
			Thread reducerThread = new Thread(new mapWorkerActionsForReducer(connectiontoreducer, r));
			reducerThread.start();

			outToMaster.writeBoolean(true);
			outToMaster.flush();
			
			Boolean b = inFromMaster.readBoolean();
			if (b == true){
				dfromgoogle = askGoogleDirectionsAPI(q);
				rfromgoogle = new Routes(q, dfromgoogle);
				outToMaster.writeObject(rfromgoogle);
				outToMaster.flush();
			}
			
			outToMaster.close();
			inFromMaster.close();
			

			connectiontoreducer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	private Routes[] map(Query q){
		Routes[] routes = new Routes[100];
		try{
			Scanner read = new Scanner(new File("Worker2Data.txt"), "UTF-8");
			read.useDelimiter(",,");
			Point a = new Point(0,0);
			Point b = new Point(0,0);
			String alat, along, blat, blong, json;
			int counter = 0;
			
	    	while(read.hasNext()){
	    		alat = read.next();
	    		along = read.next();
	    		blat = read.next();
	    		blong = read.next();
	    		json = read.next();

	    		a.Lat = Double.parseDouble(alat);
	    		a.Long = Double.parseDouble(along);
	    		b.Lat = Double.parseDouble(blat);
	    		b.Long = Double.parseDouble(blong);
	    		
	    		if(q.startPoint.equals(a) && q.endPoint.equals(b)){
	    			Directions jsond = new Directions(json);
	    			routes[counter] = new Routes(a, b, jsond);
	    			counter++;
	    		}
	    		
	    	}

	    	read.close();
	    	
	    	if(counter == 0){
	    		return new Routes[0];
	    	}else{
	    		Routes[] finalroutes = new Routes[counter];
	    		for(int i = 0; i < counter; i++){
	    			finalroutes[i] = routes[i];
	    		}
	    		return finalroutes;
	    	}
	    	
		} catch (IOException e) {
			
		}
		return new Routes[0];
	}
	public Directions askGoogleDirectionsAPI(Query q) {

		try{
			String start = String.format(LOCATION_ARG, q.startPoint.Lat, q.startPoint.Long);
			String end = String.format(LOCATION_ARG, q.endPoint.Lat, q.endPoint.Long);
			String args = String.format(ARGS, encode(start), encode(end));
			String url = BASE_URL + args;

			return new Directions(new Resty().text(url).toString());

		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private String encode(String arg) throws UnsupportedEncodingException {
        return URLEncoder.encode(arg, ENCODING);
    }
}
