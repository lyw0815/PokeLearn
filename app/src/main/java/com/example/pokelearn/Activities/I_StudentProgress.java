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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Progress;
import com.example.pokelearn.R;
import com.example.pokelearn.Student;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class I_StudentProgress extends AppCompatActivity {

    private RecyclerView chapterLists;
    private TextView complete_count, total_count;
    private Query mStudentDatabase;
    private DatabaseReference mUserDb;
    private DatabaseReference mProgressDatabase;
    private Integer c_count = 0;
    private Integer t_count = 0;
    private String ChapterName, CourseId, ChapterId, complete, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__student_progress);

        Intent i = getIntent();
        ChapterName = i.getStringExtra("ChapterName");
        Intent j = getIntent();
        CourseId = j.getStringExtra("CourseId");
        Intent k = getIntent();
        ChapterId = k.getStringExtra("ChapterId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.iStudentProgressToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ChapterName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        complete_count = (TextView) findViewById(R.id.complete_count);
        total_count = (TextView) findViewById(R.id.total_count);

        chapterLists = (RecyclerView) findViewById(R.id.iStudentProgressRecycleView);
        chapterLists.setHasFixedSize(true);
        chapterLists.setLayoutManager(new LinearLayoutManager(this));
        chapterLists.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Course Enrolment").child(CourseId);
        mStudentDatabase.keepSynced(true);
        mProgressDatabase = FirebaseDatabase.getInstance().getReference().child("Student Course List");
        mProgressDatabase.keepSynced(true);
        mUserDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDb.keepSynced(true);

        FirebaseRecyclerAdapter<Student, I_StudentProgress.ProgressViewHolder> chapterRecyclerViewAdapter = new FirebaseRecyclerAdapter<Student, I_StudentProgress.ProgressViewHolder>(
                Student.class,
                R.layout.progress_row,
                I_StudentProgress.ProgressViewHolder.class,
                mStudentDatabase
        ) {
            @Override
            protected void populateViewHolder(final I_StudentProgress.ProgressViewHolder progressViewHolder, Student progress, int i) {

                final String list_student_id = getRef(i).getKey();
                t_count = t_count + 1;

                mProgressDatabase.child(list_student_id).child(CourseId).child(ChapterId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("progressChapter")) {
                            String sProgress = dataSnapshot.child("progressChapter").getValue().toString();
                            progressViewHolder.setChapterProgress(sProgress);
                            if(sProgress.equals("true")){
                                c_count = c_count + 1;
                            }
                        }

                        mUserDb.child(list_student_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String studentName = dataSnapshot.child("userName").getValue().toString();
                                progressViewHolder.setName(studentName);
                                complete = c_count.toString();
                                total = t_count.toString();
                                complete_count.setText(complete);
                                total_count.setText(total);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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

        public void setName(String name){
            TextView nameView = (TextView) mView.findViewById(R.id.progress_chapter_title);
            nameView.setText(name);
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