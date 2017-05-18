package com.example.athma_000.gdir;

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

//GoogleMap mMap;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    ImageButton magnifierButton;
    EditText beginningInput, destinationInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(); // way to instantiate GoogleMap object.

        magnifierButton = (ImageButton)findViewById(R.id.buttonGo); // magnifier button
        beginningInput= (EditText)findViewById(R.id.etStart); // first EditText
        destinationInput = (EditText)findViewById(R.id.etDestination); // last EditText

        magnifierButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.v("EditText", beginningInput.getText().toString());
                        Log.v("EditText", destinationInput.getText().toString());
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

}
