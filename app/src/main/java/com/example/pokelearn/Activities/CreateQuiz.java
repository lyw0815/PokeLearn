package com.example.pokelearn.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pokelearn.R;

public class CreateQuiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.iCreateQuizToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("Create Quiz"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
