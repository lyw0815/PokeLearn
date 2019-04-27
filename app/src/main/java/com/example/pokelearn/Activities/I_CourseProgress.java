package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.pokelearn.Adapters.I_ChapterAdapter;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class I_CourseProgress extends AppCompatActivity {

    TextView courseName;
    RecyclerView chapterLists;
    I_ChapterAdapter IChapterAdapter;
    ArrayList<String> chapterList;
    ArrayList<String> chapterIds;
    FloatingActionButton fab_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__chapter_list);

        fab_more = (FloatingActionButton)findViewById(R.id.fab_more_1);
        fab_more.hide();

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.iChapterListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CourseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chapterLists = (RecyclerView) findViewById(R.id.i_courseListRecycleView);
        chapterLists.setHasFixedSize(true);
        chapterLists.setLayoutManager(new LinearLayoutManager(this));
        chapterLists.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        chapterList = new ArrayList<>();
        chapterIds = new ArrayList<>();

        setAdapter(CourseName, CourseId);

    }

    private void setAdapter(final String CourseName, final String CourseId) {

        Query query = FirebaseDatabase.getInstance().getReference("Chapters").child(CourseId).orderByChild("chapterSequence");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String chapter_title = snapshot.child("chapterTitle").getValue(String.class);
                    String chapter_course = snapshot.child("chapterCourse").getValue(String.class);
                    String chapter_id = snapshot.child("chapterId").getValue(String.class);

                    chapterList.add(chapter_title);
                    chapterIds.add(chapter_id);
                }
                IChapterAdapter = new I_ChapterAdapter(I_CourseProgress.this, chapterList, chapterIds, CourseName, CourseId, "progress");
                chapterLists.setAdapter(IChapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
