package com.example.rshikkal.icafe.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.rshikkal.icafe.R;

public class AboutusActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView navigationItemNameTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        //set the toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        navigationItemNameTV = (TextView)findViewById(R.id.navigationitemname);
        navigationItemNameTV.setText("About us");
    }
}
