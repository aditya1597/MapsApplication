package com.example.aditya.myapplication;

import android.icu.util.Freezable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class send_user_request extends AppCompatActivity {
    Button send_req,track_req;
    EditText req_data;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    Map<String,Object> volt_location=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_user_request);
        setTitle("Request to Volunteers");
        send_req=findViewById(R.id.button2_userreq);
        track_req=findViewById(R.id.button5_userreq);
        req_data=findViewById(R.id.editText_userreq);
        final Map<String, Object> mob_number_volt=new HashMap<>();

        send_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             db.collection("user_personal_data").whereEqualTo("volunteer",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     if(task.isSuccessful())
                     {
                         for (DocumentSnapshot document : task.getResult()) {
                             Log.d("MSG", document.getId() + " => " + document.getData());
                             mob_number_volt.put(document.getId().toString(),document.getId());

                         }
                     }
                     else {
                         Log.d("MSG", "Error getting documents: ", task.getException());
                     }
                 }

             });
                Iterator mobnum=mob_number_volt.keySet().iterator();
                while (mobnum.hasNext()) {
                    String key=(String)mobnum.next();

                    db.collection("user_location_data").whereEqualTo("user_mobile_number", key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("MSG", document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d("MSG", "Error getting documents: ", task.getException());
                            }

                        }
                    });
                }

            }
        });
    }
}
