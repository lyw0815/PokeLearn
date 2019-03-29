package com.example.pokelearn.Activities;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
        setContentView(R.layout.activity_s__my_course);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.sMyCourseToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("My Courses"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseList = (RecyclerView) findViewById(R.id.sMyCourseRecycleView);

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

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String course_id = snapshot.child("courseId").getValue(String.class);
                    dbReference.child("Courses").child(course_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e("Course: ", course_id);
                            String course_name = dataSnapshot.child("courseName").getValue(String.class);
                            String course_id = dataSnapshot.child("courseId").getValue(String.class);
                            String course_desc = dataSnapshot.child("courseDesc").getValue(String.class);
                            String course_cvr = dataSnapshot.child("courseCoverImgUrl").getValue(String.class);

                            Log.e("Course name: ", course_name);
                            Log.e("Course desc: ", course_desc);

                            courseNameList.add(course_name);
                            courseDescList.add(course_desc);
                            courseCoverImgList.add(course_cvr);
                            courseId.add(course_id);

                            Log.e("LIST", courseNameList.toString());
                            SCourseAdapter = new S_CourseAdapter(S_MyCourse.this, courseNameList, courseDescList,courseCoverImgList, courseId);
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

//        dbReference.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//
//                    String course_name = snapshot.child("courseName").getValue(String.class);
//                    String course_id = snapshot.child("courseId").getValue(String.class);
//                    String course_desc = snapshot.child("courseDesc").getValue(String.class);
//                    String course_cvr = snapshot.child("courseCoverImgUrl").getValue(String.class);
//
//
//                    courseNameList.add(course_name);
//                    courseDescList.add(course_desc);
//                    courseCoverImgList.add(course_cvr);
//                    courseId.add(course_id);
//
//                }
//                SCourseAdapter = new S_CourseAdapter(S_MyCourse.this, courseNameList, courseDescList,courseCoverImgList, courseId);
//                courseList.setAdapter(SCourseAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

}
