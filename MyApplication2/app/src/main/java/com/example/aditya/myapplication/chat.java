package com.example.aditya.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class chat extends AppCompatActivity {

    public static int SIGN_IN_REQUEST_CODE = 10;
   // private FirebaseListAdapter<ChatMessage> adapter;
    public String msg_text="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /*try{
            displayChatMessages();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyBlrC1BCczb9R77KPHxdrDFssV3KZEWX1Q")
                    .setApplicationId("1:117870858131:android:677111b16400dc4b")
                    .setDatabaseUrl("https://test-29bc7.firebaseio.com")
                    .build();
            final FirebaseApp secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, "second app");
            FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);
            secondDatabase.getReference().setValue(ServerValue.TIMESTAMP);
        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.input);

                DatabaseReference dref=FirebaseDatabase.getInstance(secondApp).getReference();

                dref.setValue(new ChatMessage(msg_text));
                input.setText("");
            }
        });
        }
                catch (Exception e) {

                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }*/
            }


    private void displayChatMessages() {
       /* try {
            ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyBlrC1BCczb9R77KPHxdrDFssV3KZEWX1Q")
                    .setApplicationId("1:117870858131:android:677111b16400dc4b")
                    .setDatabaseUrl("https://test-29bc7.firebaseio.com")
                    .build();
            final FirebaseApp secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, "second app");
            FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);
            secondDatabase.getReference().setValue(ServerValue.TIMESTAMP);
          FirebaseListOptions<ChatMessage> flistoptions=new FirebaseListOptions.Builder<ChatMessage>()
                  .setLayout(R.layout.activity_chat)
                  .build();
            adapter = new FirebaseListAdapter<ChatMessage>(flistoptions) {
                @Override
                protected void populateView(View v, ChatMessage model, int position) {
                    // Get references to the views of message.xml
                    TextView messageText = (TextView) v.findViewById(R.id.message_text);
                    TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                    TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                    // Set their text
                    messageText.setText(msg_text);
                    global_data gd = new global_data();
                    messageUser.setText(gd.name);

                    // Format the date before showing it
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                }
            };

            listOfMessages.setAdapter(adapter);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }*/
    }

}
