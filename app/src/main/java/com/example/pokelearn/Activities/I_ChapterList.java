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
    DatabaseReference dbReference;
    I_ChapterAdapter IChapterAdapter;

    ArrayList<String> chapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__chapter_list);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = i.getStringExtra("CourseId");
       // courseName.setText(CourseName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.iChapterListToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle(CourseName); // setting a title for this Toolbar
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

        setAdapter(CourseName, CourseId);

    }

    private void setAdapter(final String CourseName, final String CourseId) {
//        dbReference = FirebaseDatabase.getInstance().getReference("Chapters");
        Query query = FirebaseDatabase.getInstance().getReference("Chapters").child(CourseId).orderByChild("chapterSequence");

//        dbReference.child("Chapters").addListenerForSingleValueEvent(new ValueEventListener() {
         query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    String cid = snapshot.getKey();

                    String chapter_title = snapshot.child("chapterTitle").getValue(String.class);
                    String chapter_id = snapshot.child("chapterId").getValue(String.class);

//                    if (chapter_course.equals(CourseName))
//                    if (chapter_id.equals(CourseId))
                        chapterList.add(chapter_title);
                }
                IChapterAdapter = new I_ChapterAdapter(I_ChapterList.this, chapterList, CourseName);
                chapterLists.setAdapter(IChapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
