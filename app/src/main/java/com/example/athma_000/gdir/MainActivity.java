package com.example.athma_000.gdir;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

//GoogleMap mMap;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{
    public static Double beginningInputVar1;
    ImageButton magnifierButton;
    EditText beginningInput, destinationInput;
    Double beginningInputVar2 , destinationInputVar1, destinationInputVar2;

    //client-similar vars
    static final String MasterIP = "http://10.0.2.2:8080/"; //CHANGE THIS ACCORDINGLY
    static final int MasterPort = 4320;
    static Socket clientToMasterSocket = null;
    static ObjectOutputStream outToMaster = null;
    static ObjectInputStream inFromMaster = null;
    static Query q = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(); // way to instantiate GoogleMap object.


        beginningInput = (EditText)findViewById(R.id.etStart); // first EditText
        destinationInput = (EditText)findViewById(R.id.etDestination); // last EditText

        magnifierButton = (ImageButton)findViewById(R.id.buttonGo); // magnifier button

        magnifierButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.v("EditText", beginningInput.getText().toString());
                        Log.v("EditText", destinationInput.getText().toString());

                        String[] beginningString = beginningInput.getText().toString().split(",");
                        String[] destinationString = destinationInput.getText().toString().split(",");

                        beginningInputVar1 = Double.parseDouble(beginningString[0]);
                        beginningInputVar2 = Double.parseDouble(beginningString[1]);

                        destinationInputVar1 = Double.parseDouble(destinationString[0]);
                        destinationInputVar2 = Double.parseDouble(destinationString[1]);


                        Log.v("String", beginningString.toString());
                        Log.v("begDouble1", beginningInputVar1.toString());
                        Log.v("begDouble2", beginningInputVar2.toString());
                        Log.v("String", destinationString.toString());
                        Log.v("destDouble1", destinationInputVar1.toString());
                        Log.v("destDouble2", destinationInputVar2.toString());

                        Point startPoint = new Point(beginningInputVar1 ,beginningInputVar2);
                        Point destinationPoint = new Point(destinationInputVar1 ,destinationInputVar2);

                        q = createQuery(startPoint, destinationPoint);
                        new initialize().execute();
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //DO WHATEVER YOU WANT WITH GOOGLEMAP
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private static Query createQuery(Point a, Point b){
        Query query = new Query(a,b);
        return query;
    }

    private static void sendQueryToServer(Query q){
        try{
            outToMaster = new ObjectOutputStream(clientToMasterSocket.getOutputStream());
            outToMaster.writeObject(q);
            outToMaster.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public class initialize extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {

                clientToMasterSocket = new Socket(MasterIP, MasterPort);

                sendQueryToServer(q);

                Routes result = getResults();

                showResults(result);

                outToMaster.close();
                inFromMaster.close();
                clientToMasterSocket.close();

            } catch (UnknownHostException unknownHost) {
                System.err.println("You are trying to connect to an unknown host!");
            } catch(IOException ioException) {
                ioException.printStackTrace();
            }
            return null;
        }
    }


    private static Routes getResults(){
        Routes r = null;
        try {
            inFromMaster = new ObjectInputStream(clientToMasterSocket.getInputStream());
            r = (Routes) inFromMaster.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return r;
    }

    private static void showResults(Routes r){
//        Log.v("Direction",)
//       Try to swtich to Log.v after.'
        Log.v("Reach","Hello I reached getResults()");

        System.out.println(r.direction.direction);
    }
}
