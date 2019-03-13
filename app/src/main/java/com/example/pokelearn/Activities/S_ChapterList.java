package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Adapters.S_ChapterAdapter;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class S_ChapterList extends AppCompatActivity {

    TextView courseName;
    RecyclerView chapterLists;
    DatabaseReference dbReference;
    S_ChapterAdapter SChapterAdapter;
    ArrayList<String> chapterList;
    ArrayList<String> chapterIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s__chapter_list);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");

        Toast.makeText(getApplicationContext(), "clicked: "+ CourseId,Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.sChapterListToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle(CourseName); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseName = (TextView) findViewById(R.id.s_chapListCourseName);

        chapterLists = (RecyclerView) findViewById(R.id.s_courseListRecycleView);
        chapterLists.setHasFixedSize(true);
        chapterLists.setLayoutManager(new LinearLayoutManager(this));
        chapterLists.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        chapterList = new ArrayList<>();
        chapterIds = new ArrayList<>();

        setAdapter(CourseName, CourseId);

    }

    private void setAdapter(final String CourseName, final String CourseId) {

//        Toast.makeText(getApplicationContext(), "query "+ CourseId,Toast.LENGTH_SHORT).show();

        Query query = FirebaseDatabase.getInstance().getReference("Chapters").child(CourseId).orderByChild("chapterSequence");

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String chapter_title = snapshot.child("chapterTitle").getValue(String.class);
                    String chapter_course = snapshot.child("chapterCourse").getValue(String.class);
                    String chapter_id = snapshot.child("chapterId").getValue(String.class);

//                    if (chapter_course.equals(CourseName))
//                    if (chapter_course.equals(CourseName))
                        chapterList.add(chapter_title);
                        chapterIds.add(chapter_id);
                }
                SChapterAdapter = new S_ChapterAdapter(S_ChapterList.this, chapterList, chapterIds, CourseName, CourseId);
                chapterLists.setAdapter(SChapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
