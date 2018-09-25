package com.example.aditya.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class safety_instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_instructions);

        TextView safety_instructions=findViewById(R.id.textView2);
        safety_instructions.setText("Assemble disaster supplies:\n" +
                "\n" +
                "Drinking water – Fill clean containers.\n" +
                "Food that requires no refrigeration or cooking.\n" +
                "Cash.\n" +
                "Medications and first aid supplies.\n" +
                "Clothing, toiletries.\n" +
                "Battery-powered radio.\n" +
                "Flashlights.\n" +
                "Extra batteries.\n" +
                "Important documents: insurance papers, medical records, bank account numbers.");
    }
}
