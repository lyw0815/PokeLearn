package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Adapters.SearchChapterAdapter;
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

public class ChapterList extends AppCompatActivity {

    TextView courseName;
    RecyclerView chapterLists;
    Button btnEnrol;
    SearchChapterAdapter SearchChapterAdapter;
    ArrayList<String> chapterList;
    ArrayList<String> chapterIds;
    private DatabaseReference mSCourseDatabase;
    private DatabaseReference mEnrolDatabase;
    private FirebaseUser mCurrent_user;
    public Boolean enrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.ChapterListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CourseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseName = (TextView) findViewById(R.id.chapterListCourseName);
        btnEnrol = (Button) findViewById(R.id.btnEnrol);

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = mCurrent_user.getUid();
        enrol = false;
        mSCourseDatabase = FirebaseDatabase.getInstance().getReference().child("Student Course List").child(user_id);
        mEnrolDatabase = FirebaseDatabase.getInstance().getReference().child("Course Enrolment").child(CourseId);

        mEnrolDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    btnEnrol.setText("Unenrol from this course");
                    btnEnrol.setBackground(getResources().getDrawable(R.drawable.signup_btn_style));
                    btnEnrol.setTextColor(getResources().getColor(R.color.colorPrimary));
                    enrol = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnEnrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ENROL!!!!!
                if (enrol == false) {
                    mEnrolDatabase.child(user_id).child("studentId").setValue(user_id);
                    mSCourseDatabase.child(CourseId).child("courseId").setValue(CourseId);
                    Toast.makeText(ChapterList.this, "Yay, you have enrolled in this course!", Toast.LENGTH_SHORT).show();
                    btnEnrol.setText("Unenrol from this course");
                    btnEnrol.setBackground(getResources().getDrawable(R.drawable.signup_btn_style));
                    btnEnrol.setTextColor(getResources().getColor(R.color.colorPrimary));
                    enrol = true;
                }
                else {
                    mEnrolDatabase.child(user_id).removeValue();
                    mSCourseDatabase.child(CourseId).removeValue();
                    Toast.makeText(ChapterList.this, "You have unenrolled from this course", Toast.LENGTH_SHORT).show();
                    btnEnrol.setText("Enrol Now !");
                    btnEnrol.setBackground(getResources().getDrawable(R.drawable.reg_btn_style));
                    btnEnrol.setTextColor(getResources().getColor(R.color.backgroundcolor));
                    enrol = false;
                }

            }
        });

        chapterLists = (RecyclerView) findViewById(R.id.chapterListRecycleView);
        chapterLists.setHasFixedSize(true);
        chapterLists.setLayoutManager(new LinearLayoutManager(this));
        chapterLists.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        chapterList = new ArrayList<>();

        setAdapter(CourseName, CourseId);
    }

    private void setAdapter(final String CourseName, final String CourseId) {

        Query query = FirebaseDatabase.getInstance().getReference("Chapters").child(CourseId).orderByChild("chapterSequence");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String chapter_title = snapshot.child("chapterTitle").getValue(String.class);
                    String chapter_id = snapshot.child("chapterId").getValue(String.class);

                        chapterList.add(chapter_title);
            }
                SearchChapterAdapter = new SearchChapterAdapter(ChapterList.this, chapterList, CourseName);
                chapterLists.setAdapter(SearchChapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
