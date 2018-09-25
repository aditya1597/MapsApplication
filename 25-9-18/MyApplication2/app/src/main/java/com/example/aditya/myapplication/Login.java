package com.example.aditya.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    FirebaseFirestore db;
    EditText username;EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (global_data.disp_logo != 1) {
            Intent i = new Intent(Login.this, FullscreenActivity.class);
            startActivity(i);
            global_data.disp_logo=1;
        } else {
            setContentView(R.layout.activity_login);
            Button register = findViewById(R.id.button4);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent register = new Intent(Login.this, register.class);
                    startActivity(register);
                }
            });
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            db = FirebaseFirestore.getInstance();

            Button login = findViewById(R.id.button3);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference login_ref = db.collection("user_login_data").document(username.getText().toString());
                    login_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String data = document.getData().toString();
                                    if (data.contains(password.getText().toString())) {
                                        DocumentReference personal_data = db.collection("user_personal_data").document(username.getText().toString());
                                        personal_data.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot pdata = task.getResult();
                                                if (pdata.exists()) {
                                                    String info = pdata.getData().toString();
                                                    int index_name = info.indexOf("name=") + 5;
                                                    int last_index = info.indexOf(",", index_name);
                                                    global_data gd = new global_data();
                                                    gd.name = info.substring(index_name, last_index);
                                                    gd.volunteer = info.substring(info.indexOf("volunteer=") + 10, info.indexOf(",", info.indexOf("volunteer=")));
                                                    gd.doctor = info.substring(info.indexOf("doctor=") + 7, info.indexOf(",", info.indexOf("doctor=")));
                                                    Log.d("MSG", gd.doctor);

                                                }
                                            }
                                        });

                                        global_data gd = new global_data();
                                        gd.mobile_number = username.getText().toString();

                                        Intent login = new Intent(Login.this, user_home.class);
                                        startActivity(login);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    Log.d("MSG", "No such document");
                                }
                            } else {
                                Log.d("MSG", "get failed with ", task.getException());
                            }
                        }
                    });

                }
            });
        }
    }
}
