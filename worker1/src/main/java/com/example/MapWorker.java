package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MapWorker extends Worker{
    final int WORKERID;
    int portformaster;
    ServerSocket workerSocket;
    Socket connection = null;
    MapWorker(int id, int p){
        this.WORKERID = id;
        this.portformaster = p;
    }
    public static void main(String args[]){
        new MapWorker(1, 4321).initialize();
    }
    @Override
    public void initialize() {
        waitForTasksThread();
    }

    @Override
    public void waitForTasksThread() {
        try {
            while (true) {
                workerSocket = new ServerSocket(this.portformaster, 10);

                connection = workerSocket.accept();

                Thread masterThread = new Thread(new mapWorkerActionsForMaster(connection));
                masterThread.start();
                masterThread.join();

                connection.close();
                workerSocket.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                workerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


    }

}