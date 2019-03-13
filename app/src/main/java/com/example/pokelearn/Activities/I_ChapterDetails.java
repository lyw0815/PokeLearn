package com.example.pokelearn.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pokelearn.R;

public class I_ChapterDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__chapter_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.iChapterDetailsToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("Chapter Meterials"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
