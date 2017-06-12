package com.example.athma_000.gdir;

import java.io.Serializable;
public class Point implements Serializable{
	double Lat, Long;
	Point(double Latitude, double Longitude){
		this.Lat = Latitude;
		this.Long = Longitude;
	}
	public boolean equals(Point b){
		return (Math.abs(this.Lat - b.Lat) < 0.01) && (Math.abs(this.Long - b.Long) < 0.01);
	}
}