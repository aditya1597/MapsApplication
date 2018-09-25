package com.example.aditya.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class call_sos_services extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Button police,ambulane,firebrigade;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(call_sos_services.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sos_services);

        police=findViewById(R.id.button2_police);
        ambulane=findViewById(R.id.button5_ambulance);
        firebrigade=findViewById(R.id.button6_firebrigade);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_police_intent=new Intent(Intent.ACTION_CALL);
                call_police_intent.setData(Uri.parse("tel:9769720630"));
                if(checkLocationPermission()) {
                    startActivity(call_police_intent);
                }
                else
                {
                    checkLocationPermission();
                    startActivity(call_police_intent);
                }
            }
        });
        ambulane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_ambulance_intent=new Intent(Intent.ACTION_CALL);
                call_ambulance_intent.setData(Uri.parse("tel:9769720630"));
                if(checkLocationPermission()) {
                    startActivity(call_ambulance_intent);
                }
                else
                {
                    checkLocationPermission();
                    startActivity(call_ambulance_intent);
                }
            }
        });
        firebrigade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call_police_intent=new Intent(Intent.ACTION_CALL);
                call_police_intent.setData(Uri.parse("tel:9769720630"));
                if(checkLocationPermission()) {
                    startActivity(call_police_intent);
                }
                else
                {
                    checkLocationPermission();
                    startActivity(call_police_intent);
                }
            }
        });
    }
}
