package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pokelearn.Adapters.SearchChapterAdapter;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChapterList extends AppCompatActivity {

    TextView courseName;
    RecyclerView chapterLists;
    Button btnEnrol;
    DatabaseReference dbReference;
    SearchChapterAdapter SearchChapterAdapter;
    ArrayList<String> chapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.ChapterListToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle(CourseName); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseName = (TextView) findViewById(R.id.chapterListCourseName);
        btnEnrol = (Button) findViewById(R.id.btnEnrol);

        btnEnrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ENROL!!!!!
            }
        });

        chapterLists = (RecyclerView) findViewById(R.id.chapterListRecycleView);
        chapterLists.setHasFixedSize(true);
        chapterLists.setLayoutManager(new LinearLayoutManager(this));
        chapterLists.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        chapterList = new ArrayList<>();

        setAdapter(CourseName);
    }

    private void setAdapter(final String CourseName) {

        Query query = FirebaseDatabase.getInstance().getReference("Chapters").orderByChild("chapterSequence");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String chapter_title = snapshot.child("chapterTitle").getValue(String.class);
                    String chapter_course = snapshot.child("chapterCourse").getValue(String.class);
//                    Log.d("query: ", chapter_title);
                    if (chapter_course.equals(CourseName))
//                        Log.d("query: ", chapter_title);
                        chapterList.add(chapter_title);
                }
                SearchChapterAdapter = new SearchChapterAdapter(ChapterList.this, chapterList, CourseName);
                chapterLists.setAdapter(SearchChapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
