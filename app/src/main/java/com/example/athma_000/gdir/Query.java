package com.example.athma_000.gdir;

import java.io.Serializable;
public class Query implements Serializable{
	Point startPoint;
	Point endPoint;
	Query(Point sP, Point eP){
		startPoint = sP;
		endPoint = eP;
	}
}
