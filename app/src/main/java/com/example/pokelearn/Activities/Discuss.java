package com.example.pokelearn.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pokelearn.R;

public class Discuss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sDiscussToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("Discuss"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
