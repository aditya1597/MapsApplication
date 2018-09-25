package com.example.aditya.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class people_finder extends AppCompatActivity {
    Button update_status,find;
    double lat,lng;
    String status="";
    RadioButton rb;
    Map<String,Object> update_status_data=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_finder);
        setTitle("People Finder");
        update_status=findViewById(R.id.button2_update);
        find=findViewById(R.id.button3_find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(people_finder.this,find_people.class);
                startActivity(i);
            }
        });
        final RadioGroup rg=findViewById(R.id.radiobutton_group);
        final  FirebaseFirestore db=FirebaseFirestore.getInstance();
        update_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference dref=db.collection("user_location_data").document(global_data.mobile_number.toString());
                dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot dsnap=task.getResult();
                            String data=dsnap.getData().toString();
                            Log.d("Data",data.toString());
                            lat=Double.parseDouble(data.substring(data.indexOf("latitude=")+9,data.indexOf(",",data.indexOf("latitude="))));
                            lng=Double.parseDouble(data.substring(data.indexOf("longitude=")+10,data.indexOf("}",data.indexOf("longitude="))));

                            rb=findViewById(rg.getCheckedRadioButtonId());
                            GeoPoint g=new GeoPoint(lat,lng);
                            update_status_data.put("user_mobile_number",global_data.mobile_number.toString());
                            update_status_data.put("user_location",g);
                            update_status_data.put("user_status",rb.getText().toString());
                            DocumentReference dref2=db.collection("user_status").document(global_data.mobile_number.toString());
                            dref2.update(update_status_data);
                            Log.d("Location:",lat+","+lng);
                        }
                    }
                });

            }
        });





        Toast.makeText(getApplicationContext(),"Status updated..",Toast.LENGTH_LONG);
    }
}
