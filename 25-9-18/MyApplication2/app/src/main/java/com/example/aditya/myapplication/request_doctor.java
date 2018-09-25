package com.example.aditya.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class request_doctor extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_doctor);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.drawable.doctor);
        db.collection("doctor_location_data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (DocumentSnapshot document : task.getResult()) {
                        //Log.d("MSG", document.getId() + " => " + document.getData());
                        String data=document.getData().get("user_location").toString();
                        double lat,lng;
                        Log.d("MSG",document.getData().get("user_location").toString());
                        lat=Double.parseDouble(data.substring(data.indexOf("latitude=")+9,data.indexOf(",",data.indexOf("latitude="))));
                        lng=Double.parseDouble(data.substring(data.indexOf("longitude=")+10,data.indexOf("}",data.indexOf("longitude="))));
                        MarkerOptions mopt=new MarkerOptions();
                        mopt.position(new LatLng(lat,lng));
                        mopt.icon(icon);
                        mopt.title("Contact Number:"+document.getId().toString());
                        mMap.addMarker(mopt);
                    }
                }
                else {
                    Log.d("MSG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
