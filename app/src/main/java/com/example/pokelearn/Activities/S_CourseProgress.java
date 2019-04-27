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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokelearn.Progress;
import com.example.pokelearn.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class S_CourseProgress extends AppCompatActivity {

    private RecyclerView chapterLists;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    private Query mStudentDatabase;
    private DatabaseReference mStudentDb;
    private DatabaseReference mCourseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s__course_progress);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.sCourseProgressToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CourseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Student Course List").child(mCurrent_user_id).child(CourseId).orderByChild("chapterSequence");
        mStudentDatabase.keepSynced(true);
        mCourseDatabase = FirebaseDatabase.getInstance().getReference().child("Chapters").child(CourseId);
        mCourseDatabase.keepSynced(true);
        mStudentDb = FirebaseDatabase.getInstance().getReference().child("Student Course List").child(mCurrent_user_id).child(CourseId);
        mStudentDb.keepSynced(true);

        chapterLists = (RecyclerView) findViewById(R.id.sCourseProgressRecycleView);
        chapterLists.setHasFixedSize(true);
        chapterLists.setLayoutManager(new LinearLayoutManager(this));
        chapterLists.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        FirebaseRecyclerAdapter<Progress, S_CourseProgress.ProgressViewHolder> chapterRecyclerViewAdapter = new FirebaseRecyclerAdapter<Progress, S_CourseProgress.ProgressViewHolder>(

                Progress.class,
                R.layout.progress_row,
                S_CourseProgress.ProgressViewHolder.class,
                mStudentDatabase

        ) {
            @Override
            protected void populateViewHolder(final S_CourseProgress.ProgressViewHolder progressViewHolder, Progress progress, int i) {

                final String list_chapter_id = getRef(i).getKey();

                mCourseDatabase.child(list_chapter_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String c_id = dataSnapshot.child("chapterId").getValue().toString();
                        final String c_title = dataSnapshot.child("chapterTitle").getValue().toString();

                        mStudentDb.child(c_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild("progressChapter")) {
                                    String sProgress = dataSnapshot.child("progressChapter").getValue().toString();
                                    progressViewHolder.setChapterProgress(sProgress);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        progressViewHolder.setChapterTitle(c_title);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        chapterLists.setAdapter(chapterRecyclerViewAdapter);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setChapterTitle(String title){
            TextView chapterTitleView = (TextView) mView.findViewById(R.id.progress_chapter_title);
            chapterTitleView.setText(title);
        }

        public void setChapterProgress(String progress_status) {
            ImageView progressView = (ImageView) mView.findViewById(R.id.progressDone);
            if(progress_status.equals("true")){
                progressView.setVisibility(View.VISIBLE);
            } else {
                progressView.setVisibility(View.INVISIBLE);
            }

        }


    }
}


