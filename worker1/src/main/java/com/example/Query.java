package com.example;

import java.io.Serializable;
public class Query implements Serializable{
	Point startPoint;
	Point endPoint;
	Query(Point sP, Point eP){
		startPoint = sP;
		endPoint = eP;
	}
}
