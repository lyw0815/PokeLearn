package com.example.pokelearn.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Activities.ChapterList;
import com.example.pokelearn.Activities.I_MyCourse;
import com.example.pokelearn.Adapters.SearchCourseAdapter;
import com.example.pokelearn.Courses;
import com.example.pokelearn.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private View courseView;
//    private RecyclerView courseList;
//    private DatabaseReference databaseCourses;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    RecyclerView courseList;
    DatabaseReference dbReference;
    SearchCourseAdapter SearchCourseAdapter;

    ArrayList<String> courseNameList;
    ArrayList<String> courseDescList;
    ArrayList<String> courseCoverImgList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        courseView = inflater.inflate(R.layout.fragment_home,container, false);
        /*
        courseList=(RecyclerView) courseView.findViewById(R.id.myRecycleView);
        courseList.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseCourses= FirebaseDatabase.getInstance().getReference().child("Courses");
        databaseCourses.keepSynced(true);

        return courseView;
        */

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        courseList = courseView.findViewById(R.id.myRecycleView);

        dbReference = FirebaseDatabase.getInstance().getReference();

        courseList.setHasFixedSize(true);
        courseList.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        courseNameList = new ArrayList<>();
        courseDescList = new ArrayList<>();
        courseCoverImgList = new ArrayList<>();

        setAdapter();

        return courseView;
    }

    private void setAdapter() {

        dbReference.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String cid = snapshot.getKey();
                    String course_name = snapshot.child("courseName").getValue(String.class);
                    String course_desc = snapshot.child("courseDesc").getValue(String.class);
                    String course_cvr = snapshot.child("courseCoverImgUrl").getValue(String.class);
                    String course_ins = snapshot.child("courseInstructorMail").getValue(String.class);

                    courseNameList.add(course_name);
                    courseDescList.add(course_desc);
                    courseCoverImgList.add(course_cvr);

                }
                SearchCourseAdapter = new SearchCourseAdapter(getContext(), courseNameList, courseDescList,courseCoverImgList);
                courseList.setAdapter(SearchCourseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }














/*
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Courses>()
                .setQuery(databaseCourses, Courses.class)
                .build();

         FirebaseRecyclerAdapter<Courses, CoursesViewHolder> adapter = new FirebaseRecyclerAdapter<Courses, CoursesViewHolder>(options) {
             @Override
             protected void onBindViewHolder(@NonNull CoursesViewHolder holder, int position, @NonNull Courses model) {
                 holder.course_name.setText(model.getCourseName());
                 holder.course_desc.setText(model.getCourseDesc());
                 Picasso.get().load(model.getCourseCoverImgUrl()).into(holder.course_img);


                 holder.course_combo.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Toast.makeText(getContext(), "clicked: "+ course_name.get(position),Toast.LENGTH_LONG).show();

                         Intent ChapterList =new Intent(getContext(), com.example.pokelearn.Activities.ChapterList.class);
                         ChapterList.putExtra("CourseName", courseNameList.get(position));
                         startActivity(ChapterList);

                     }
                 });
             }

             @NonNull
             @Override
             public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                 View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_row, viewGroup, false);
                 CoursesViewHolder viewHolder = new CoursesViewHolder(view);
                 return viewHolder;
             }
         };

         courseList.setAdapter(adapter);
         adapter.startListening();

    }

    public class CoursesViewHolder extends RecyclerView.ViewHolder
    { //removed static
        TextView course_name,course_desc;
        ImageView course_img;
        LinearLayout course_combo;

        public CoursesViewHolder(View itemView){
            super(itemView);
            course_name = itemView.findViewById(R.id.course_name);
            course_desc = itemView.findViewById(R.id.course_desc);
            course_img  = itemView.findViewById(R.id.course_img);
            course_combo = itemView.findViewById(R.id.course_combo);
        }

    }
*/

}
