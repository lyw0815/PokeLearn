package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Adapters.S_ChapterAdapter;
import com.example.pokelearn.Progress;
import com.example.pokelearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    S_ChapterAdapter SChapterAdapter;
    ArrayList<String> chapterList;
    ArrayList<String> chapterIds;
    DatabaseReference mProgressDatabase;
    FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s__chapter_list);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");
//        Toast.makeText(getApplicationContext(), "clicked: "+ CourseId,Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.sChapterListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CourseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDatabase = FirebaseDatabase.getInstance().getReference("Student Course List");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        Query query = FirebaseDatabase.getInstance().getReference("Chapters").child(CourseId).orderByChild("chapterSequence");

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String chapter_title = snapshot.child("chapterTitle").getValue(String.class);
                    String chapter_course = snapshot.child("chapterCourse").getValue(String.class);
                    final String chapter_id = snapshot.child("chapterId").getValue(String.class);
                    final Double chapter_sequence = snapshot.child("chapterSequence").getValue(Double.class);

                        chapterList.add(chapter_title);
                        chapterIds.add(chapter_id);


                        ///// progress
//                    mProgressDatabase.child(mCurrentUser.getUid()).child(CourseId).child(chapter_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    mProgressDatabase.child(mCurrentUser.getUid()).child(CourseId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(chapter_id)) {
                                Progress newProgress = new Progress(chapter_id, "", chapter_sequence);
                                mProgressDatabase.child(mCurrentUser.getUid()).child(CourseId).child(chapter_id).setValue(newProgress);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                       ///// end progress

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
