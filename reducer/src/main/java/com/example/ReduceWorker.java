package com.example;

public class ReduceWorker extends Worker{
    int port;
    ReduceWorker(int p){
        this.port = p;
    }

    public static void main(String args[]){
        new ReduceWorker(4324).initialize();
    }
    @Override
    public void initialize() {
        waitForTasksThread();
    }
    @Override
    public void waitForTasksThread() {

        while (true) {
            Thread masterThread = new Thread(new reducerActionsForMaster(this.port));
            masterThread.start();
            try {
                masterThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}





