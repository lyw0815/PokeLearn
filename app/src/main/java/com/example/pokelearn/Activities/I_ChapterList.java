package com.example.pokelearn.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    FloatingActionButton fab_more, fab_edit, fab_delete, fab_add;
    Animation FabOpen, FabClose, FabClockwise, Fabanticlockwise;
    boolean isOpen = false;
    Dialog deleteDialog;
    Button deleteYes, deleteNo;
    ImageView closePopupImg;
    TextView titlePopUp, messagePopUp;
    boolean del_flag;

    TextView courseName;
    RecyclerView chapterLists;
    I_ChapterAdapter IChapterAdapter;
    ArrayList<String> chapterList;
    ArrayList<String> chapterIds;
    public static Activity iChapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__chapter_list);

        iChapterList = this;

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.iChapterListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CourseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab_more = (FloatingActionButton)findViewById(R.id.fab_more_1);
        fab_edit = (FloatingActionButton)findViewById(R.id.fab_edit_1);
        fab_delete = (FloatingActionButton)findViewById(R.id.fab_delete_1);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        Fabanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        chapterLists = (RecyclerView) findViewById(R.id.i_courseListRecycleView);
        chapterLists.setHasFixedSize(true);
        chapterLists.setLayoutManager(new LinearLayoutManager(this));
        chapterLists.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        chapterList = new ArrayList<>();
        chapterIds = new ArrayList<>();

        setAdapter(CourseName, CourseId);

        deleteDialog = new Dialog(this);
        del_flag = false;

        courseName = (TextView) findViewById(R.id.i_chapListCourseName);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    fab_add.startAnimation(FabClose);
                    fab_edit.startAnimation(FabClose);
                    fab_delete.startAnimation(FabClose);
                    fab_more.startAnimation(Fabanticlockwise);
                    fab_delete.setClickable(false);
                    fab_edit.setClickable(false);
                    isOpen = false;
                }
                else{
                    fab_add.startAnimation(FabOpen);
                    fab_edit.startAnimation(FabOpen);
                    fab_delete.startAnimation(FabOpen);
                    fab_more.startAnimation(FabClockwise);
                    fab_edit.setClickable(true);
                    fab_delete.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent CreateChapter =new Intent(getApplicationContext(), CreateChapter.class);
                CreateChapter.putExtra("CourseName", CourseName );
                CreateChapter.putExtra("CourseId", CourseId );
                startActivity(CreateChapter);
                finish();
            }
        });

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditCourse = new Intent(getApplicationContext(), EditCourse.class);
                EditCourse.putExtra("CourseId", CourseId );
                startActivity(EditCourse);
                finish();
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDeletePopup(CourseId, CourseName);
            }
        });

    }

    public void ShowDeletePopup(final String course_id, final String course_name){
        deleteDialog.setContentView(R.layout.pop_up_delete_course);
        closePopupImg = (ImageView) deleteDialog.findViewById(R.id.closePopUpImg_1);
        deleteYes = (Button) deleteDialog.findViewById(R.id.btnDeleteYes_1);
        deleteNo = (Button) deleteDialog.findViewById(R.id.btnDeleteNo_1);
        titlePopUp = (TextView) deleteDialog.findViewById(R.id.titlePopUp_1);
        messagePopUp = (TextView) deleteDialog.findViewById(R.id.messagePopUp_1);

        closePopupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();

        deleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse(course_id, course_name);
            }
        });

        deleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

    }

    private void deleteCourse(String CourseId, String CourseName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Chapters").child(CourseId);
        ref.removeValue();
        DatabaseReference ref_2 = database.getReference().child("Courses").child(CourseId);
        ref_2.removeValue();
        Toast.makeText(this, "Course deleted successfully", Toast.LENGTH_SHORT).show();
        del_flag = true;
        deleteDialog.dismiss();

        I_MyCourse.iMyCourse.finish();
        Intent I_MyCourse =new Intent(getApplicationContext(), I_MyCourse.class);
        startActivity(I_MyCourse);
        finish();
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
                IChapterAdapter = new I_ChapterAdapter(I_ChapterList.this, chapterList, chapterIds, CourseName, CourseId, "details");
                chapterLists.setAdapter(IChapterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
