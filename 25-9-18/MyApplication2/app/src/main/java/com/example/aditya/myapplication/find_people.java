package com.example.aditya.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class find_people extends AppCompatActivity implements OnMapReadyCallback {
    EditText input;
    Button find;
    double lat,lng;
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        input=findViewById(R.id.editText_findpeople);
        find=findViewById(R.id.button2_findpeople);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                DocumentReference dref=db.collection("user_status").document(global_data.mobile_number.toString());
                dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snap=task.getResult();
                        String status=snap.getData().toString();
                        String user_status=status.substring(status.indexOf("=")+1,status.indexOf(","));
                        lat=Double.parseDouble(status.substring(status.indexOf("latitude=")+9,status.indexOf(",",status.indexOf("latitude="))));
                        lng=Double.parseDouble(status.substring(status.indexOf("longitude=")+10,status.indexOf("}",status.indexOf("longitude="))));
                        Log.d("Status",user_status+","+lat+","+lng);
                        MarkerOptions mopt=new MarkerOptions();
                        mopt.position(new LatLng(lat,lng));
                        if(user_status.contains("Safe"))
                        {
                            BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.drawable.me_safe);
                            mopt.icon(icon);
                            mopt.title(user_status);
                        }
                        else
                        {
                            BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.drawable.me_help);
                            mopt.icon(icon);
                            mopt.title(user_status);
                        }
                        mMap.addMarker(mopt);
                    }
                });

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
    }
}
