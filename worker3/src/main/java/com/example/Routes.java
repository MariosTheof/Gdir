package com.example;

import java.io.Serializable;
public class Routes implements Serializable{
	Point start;
	Point destination;
	Directions direction;
	Routes(Point a, Point b, Directions d){
		start = a;
		destination = b;
		direction = d;
	}
	Routes(Query q, Directions d){
		start = q.startPoint;
		destination = q.endPoint;
		direction = d;
	}
	Routes(Query q){
		start = q.startPoint;
		destination = q.endPoint;
		direction = null;
	}
}
