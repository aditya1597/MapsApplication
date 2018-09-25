package com.example.aditya.myapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class register extends AppCompatActivity {
    EditText uname, uemail, umobile_number, otp, upassword, urepasswpord;
    Button uget_otp, uverify_otp, uregister;
    CheckBox udoctor,uvolunteer;
    boolean otp_verified=false;
    int otp_int;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Send SMS permission")
                        .setMessage("Application requires permission to send message")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(register.this,
                                        new String[]{Manifest.permission.SEND_SMS},
                                        MY_PERMISSIONS_REQUEST_SEND_SMS);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
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
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Send message:
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(umobile_number.getText().toString(), null, "Your OTP is:"+otp_int, null, null);
                            Toast.makeText(getApplicationContext(), "SMS Sent!",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "SMS faild, please try again later!"+e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

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
        setContentView(R.layout.activity_register);

        uname = findViewById(R.id.name);
        uemail = findViewById(R.id.email);
        umobile_number = findViewById(R.id.mobile_number);
        otp = findViewById(R.id.enter_otp);
        upassword = findViewById(R.id.password);
        urepasswpord = findViewById(R.id.repassword);
        uget_otp = findViewById(R.id.get_otp);
        uverify_otp = findViewById(R.id.verify_otp);
        uregister = findViewById(R.id.register);
        udoctor=findViewById(R.id.doctor_input);
        uvolunteer=findViewById(R.id.volunteer_input);
        otp_int=new Random().nextInt(9999);
        uget_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkLocationPermission())
                {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(umobile_number.getText().toString(), "test", "Your OTP is:"+otp_int, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS faild, please try again later!"+e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }



            }

            }
        });
        uverify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().equals(otp_int))
                {
                    otp_verified=true;
                    Toast.makeText(getApplicationContext(),"OTP Verified.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Wrong OTP.",Toast.LENGTH_LONG).show();
                }
            }
        });
        uregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference cref = db.collection("user_personal_data");
                    Map<String, Object> user_data = new HashMap<>();
                    boolean doc = false;
                    boolean volunteer = false;
                    if (udoctor.isChecked()) {
                        doc = true;
                    }
                    if (uvolunteer.isChecked()) {
                        volunteer = true;
                    }
                    user_data.put("doctor", doc);
                    user_data.put("email", uemail.getText().toString());
                    user_data.put("mobile_number", umobile_number.getText().toString());
                    user_data.put("name", uname.getText().toString());
                    user_data.put("profile_complete", "ture");
                    user_data.put("volunteer", volunteer);
                    cref.document(umobile_number.getText().toString()).set(user_data);
                    CollectionReference login_ref = db.collection("user_login_data");
                    Map<String, Object> data_ref = new HashMap<>();
                    data_ref.put("password", upassword.getText().toString());
                    data_ref.put("username", umobile_number.getText().toString());
                    login_ref.document(umobile_number.getText().toString()).set(data_ref);
                    Intent i = new Intent(register.this, Login.class);
                    startActivity(i);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendmessage() {
                //SmsManager sms=SmsManager.getDefault();
                //sms.sendTextMessage("8879024273",null,"Hi",null,null);


    }


}
