package com.example.pokelearn.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.pokelearn.Adapters.S_CourseAdapter;
import com.example.pokelearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class S_Progress extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;

    RecyclerView courseList;
    DatabaseReference dbReference;
    DatabaseReference sCourseList;
    S_CourseAdapter SCourseAdapter;

    ArrayList<String> courseNameList;
    ArrayList<String> courseDescList;
    ArrayList<String> courseCoverImgList;
    ArrayList<String> courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s__progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sProgressToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Progress");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        courseList = (RecyclerView) findViewById(R.id.sMyProgressRecycleView);

        dbReference = FirebaseDatabase.getInstance().getReference();
        sCourseList = FirebaseDatabase.getInstance().getReference();

        courseList.setHasFixedSize(true);
        courseList.setLayoutManager(new LinearLayoutManager(this));
        courseList.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        courseNameList = new ArrayList<>();
        courseDescList = new ArrayList<>();
        courseCoverImgList = new ArrayList<>();
        courseId = new ArrayList<>();

        setAdapter();


    }

    private void setAdapter() {

        sCourseList.child("Student Course List").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    final String course_id = snapshot.child("courseId").getValue(String.class);
                    final String course_id = snapshot.getKey();

                    dbReference.child("Courses").child(course_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e("Course: ", course_id);
                            String course_name = dataSnapshot.child("courseName").getValue(String.class);
                            String course_id = dataSnapshot.child("courseId").getValue(String.class);
                            String course_desc = dataSnapshot.child("courseDesc").getValue(String.class);
                            String course_cvr = dataSnapshot.child("courseCoverImgUrl").getValue(String.class);

                            courseNameList.add(course_name);
                            courseDescList.add(course_desc);
                            courseCoverImgList.add(course_cvr);
                            courseId.add(course_id);

                            SCourseAdapter = new S_CourseAdapter(S_Progress.this, courseNameList, courseDescList, courseCoverImgList, courseId, "sProgress");
                            courseList.setAdapter(SCourseAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
