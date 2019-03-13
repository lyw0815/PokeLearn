package com.example.pokelearn.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.pokelearn.Adapters.I_CourseAdapter;
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

public class S_MyCourse extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    RecyclerView courseList;
    DatabaseReference dbReference;
    S_CourseAdapter SCourseAdapter;

    ArrayList<String> courseNameList;
    ArrayList<String> courseDescList;
    ArrayList<String> courseCoverImgList;
    ArrayList<String> courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s__my_course);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.sMyCourseToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("My Courses"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseList = (RecyclerView) findViewById(R.id.sMyCourseRecycleView);

        dbReference = FirebaseDatabase.getInstance().getReference();

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
//                    String cid = snapshot.getKey();
                    String course_name = snapshot.child("courseName").getValue(String.class);
                    String course_id = snapshot.child("courseId").getValue(String.class);
                    String course_desc = snapshot.child("courseDesc").getValue(String.class);
                    String course_cvr = snapshot.child("courseCoverImgUrl").getValue(String.class);
//                    String course_ins = snapshot.child("courseInstructorMail").getValue(String.class);

//                    if (course_ins.equals(email)){
                        courseNameList.add(course_name);
                        courseDescList.add(course_desc);
                        courseCoverImgList.add(course_cvr);
                        courseId.add(course_id);
//                        counter++;
//                    }

//                    if (counter == 15){
//                        break;
//                    }
                }
                SCourseAdapter = new S_CourseAdapter(S_MyCourse.this, courseNameList, courseDescList,courseCoverImgList, courseId);
                courseList.setAdapter(SCourseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
