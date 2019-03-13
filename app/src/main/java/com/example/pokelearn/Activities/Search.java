package com.example.pokelearn.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.pokelearn.Adapters.SearchCourseAdapter;
import com.example.pokelearn.R;
import com.example.pokelearn.Adapters.I_CourseAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    EditText search_edit_text;
    RecyclerView search_recycle_view;
    DatabaseReference dbReference;
    SearchCourseAdapter SearchCourseAdapter;

    ArrayList<String> courseNameList;
    ArrayList<String> courseDescList;
    ArrayList<String> courseCoverImgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.SearchToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("Search Courses"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search_edit_text = (EditText) findViewById(R.id.search_editText);
        search_recycle_view = (RecyclerView) findViewById(R.id.search_recycleView);

        dbReference = FirebaseDatabase.getInstance().getReference();

        search_recycle_view.setHasFixedSize(true);
        search_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        search_recycle_view.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        courseNameList = new ArrayList<>();
        courseDescList = new ArrayList<>();
        courseCoverImgList = new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }
                else {
                    courseNameList.clear();
                    courseDescList.clear();
                    courseCoverImgList.clear();
                    search_recycle_view.removeAllViews();
                }
            }
        });
    }

    private void setAdapter(final String searchedString) {

        dbReference.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                courseNameList.clear();
                courseDescList.clear();
                courseCoverImgList.clear();
                search_recycle_view.removeAllViews();

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String cid = snapshot.getKey();
                    String course_name = snapshot.child("courseName").getValue(String.class);
                    String course_desc = snapshot.child("courseDesc").getValue(String.class);
                    String course_cvr = snapshot.child("courseCoverImgUrl").getValue(String.class);

                    if (course_name.toLowerCase().contains(searchedString)){
                        courseNameList.add(course_name);
                        courseDescList.add(course_desc);
                        courseCoverImgList.add(course_cvr);
                        counter++;
                    }

                    if (counter == 15){
                        break;
                    }
                }
                SearchCourseAdapter = new SearchCourseAdapter(Search.this, courseNameList, courseDescList,courseCoverImgList);
                search_recycle_view.setAdapter(SearchCourseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
