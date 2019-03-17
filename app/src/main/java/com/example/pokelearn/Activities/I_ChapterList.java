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

import com.example.pokelearn.Adapters.I_ChapterAdapter;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class I_ChapterList extends AppCompatActivity {

    TextView courseName;
    Button btnAddChapter;
    RecyclerView chapterLists;
    I_ChapterAdapter IChapterAdapter;
    ArrayList<String> chapterList;
    ArrayList<String> chapterIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__chapter_list);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = i.getStringExtra("CourseId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.iChapterListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CourseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseName = (TextView) findViewById(R.id.i_chapListCourseName);
        btnAddChapter = (Button) findViewById(R.id.btnAddChapter);


        btnAddChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent CreateChapter =new Intent(getApplicationContext(), CreateChapter.class);
                CreateChapter.putExtra("CourseName", CourseName );
                CreateChapter.putExtra("CourseId", CourseId );
                startActivity(CreateChapter);
                finish();
            }
        });


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
                IChapterAdapter = new I_ChapterAdapter(I_ChapterList.this, chapterList, chapterIds, CourseName, CourseId);
                chapterLists.setAdapter(IChapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
