package com.example.pokelearn.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.example.pokelearn.Adapters.I_CourseAdapter;
import com.example.pokelearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class I_Progress extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;

    RecyclerView courseList;
    DatabaseReference dbReference;
    DatabaseReference ICourseList;
    I_CourseAdapter ICourseAdapter;

    ArrayList<String> courseNameList;
    ArrayList<String> courseDescList;
    ArrayList<String> courseCoverImgList;
    ArrayList<String> courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__my_course);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.i_StudentProgressToolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.iMyCourseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Students' Progress");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

//        courseList = (RecyclerView) findViewById(R.id.sMyProgressRecycleView);
        courseList = (RecyclerView) findViewById(R.id.iMyCourseRecycleView);
        dbReference = FirebaseDatabase.getInstance().getReference();
        ICourseList = FirebaseDatabase.getInstance().getReference();

        courseList.setHasFixedSize(true);
        courseList.setLayoutManager(new LinearLayoutManager(this));
        courseList.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        courseNameList = new ArrayList<>();
        courseDescList = new ArrayList<>();
        courseCoverImgList = new ArrayList<>();
        courseId = new ArrayList<>();

        setAdapter(currentUser.getEmail());
    }
    private void setAdapter(final String email) {

        dbReference.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String cid = snapshot.getKey();
                    String course_name = snapshot.child("courseName").getValue(String.class);
                    String course_id = snapshot.child("courseId").getValue(String.class);
                    String course_desc = snapshot.child("courseDesc").getValue(String.class);
                    String course_cvr = snapshot.child("courseCoverImgUrl").getValue(String.class);
                    String course_ins = snapshot.child("courseInstructorMail").getValue(String.class);

                    if (course_ins.equals(email)){
                        courseNameList.add(course_name);
                        courseId.add(course_id);
                        courseDescList.add(course_desc);
                        courseCoverImgList.add(course_cvr);
                        counter++;
                    }

                    if (counter == 15){
                        break;
                    }
                }
                ICourseAdapter = new I_CourseAdapter(I_Progress.this, courseNameList, courseId, courseDescList,courseCoverImgList,"myProgress");
                courseList.setAdapter(ICourseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}




