package com.example;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Master{
    Integer port;
    static final String WORKER1IP = "127.0.0.1";//CHANGE THIS ACCORDINGLY
    static final String WORKER2IP = "127.0.0.1";//CHANGE THIS ACCORDINGLY
    static final String WORKER3IP = "127.0.0.1";//CHANGE THIS ACCORDINGLY
    static final String REDUCERIP = "127.0.0.1";//CHANGE THIS ACCORDINGLY
    static BigInteger WHArray[] = {md5hash(WORKER1IP + "4321"), md5hash(WORKER2IP + "4322"), md5hash(WORKER3IP + "4323")};


    Master(int P){
        this.port = P;
    }
    public static void main(String args[]) {
        Master.hashorder();
        new Master(4320).initialize();

    }

    ServerSocket masterSocket;
    Socket connection = null;
    static Routes cache[] = new Routes[100];
    static int oldestCachedRoute = 0;
    public void initialize() {
        try {
            while (true) {
                masterSocket = new ServerSocket(this.port, 10);

                connection = masterSocket.accept();

                Thread clientThread = new Thread(new masterActionsForClient(connection));
                clientThread.start();
                try {
                    clientThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                connection.close();
                masterSocket.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public TApair initialize(int WorkerID, Query q){
        try {
            if(WorkerID == 1){
                connection = new Socket(WORKER1IP, port);
            }
            else if(WorkerID == 2){
                connection = new Socket(WORKER2IP, port);
            }
            else if(WorkerID == 3){
                connection = new Socket(WORKER3IP, port);
            }
            masterActionsForWorkers mAFW = new masterActionsForWorkers(connection, q);
            Thread workerThread = new Thread(mAFW);
            workerThread.start();
            return new TApair(workerThread, mAFW);

        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    public TApair initialize(boolean b){
        try {
            connection = new Socket(REDUCERIP, port);

            masterActionsForReducer mAFR = new masterActionsForReducer(connection);
            Thread reducerThread = new Thread(mAFR);
            reducerThread.start();
            return new TApair(reducerThread, mAFR);
        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BigInteger md5hash(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(), 0, s.length());
        return new BigInteger(1, m.digest());
    }

    private static void hashorder(){
        int[] order = new int[3];
        BigInteger max = Master.WHArray[0];
        BigInteger min = Master.WHArray[0];
        int maxi = 0;
        int mini = 0;
        for(int i = 0; i < 2; i++){
            if(max.compareTo(Master.WHArray[i + 1]) == -1){
                max = Master.WHArray[i + 1];
                maxi = i + 1;
            }
        }
        order[0] = maxi;
        for(int i = 0; i < 2; i++){
            if(min.compareTo(Master.WHArray[i + 1]) == 1){
                min = Master.WHArray[i + 1];
                mini = i + 1;
            }
        }
        order[2] = mini;
        order[1] = 3 - order[0] - order[2];
        BigInteger temp[] = new BigInteger[3];
        for(int i = 0; i < 3; i++){
            temp[i] = WHArray[order[i]];
        }
        WHArray = temp;
    }
}
