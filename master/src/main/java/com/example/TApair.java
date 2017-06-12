package com.example;

public class TApair {
	Thread thread;
	masterActionsForWorkers actionsw;
	masterActionsForReducer actionsr;
	TApair(Thread t, masterActionsForWorkers a){
		this.thread = t;
		this.actionsw = a;
	}
	TApair(Thread t, masterActionsForReducer a){
		this.thread = t;
		this.actionsr = a;
	}
	
	
}
