package com.example.pokelearn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class S_ChapterAdapter extends RecyclerView.Adapter<S_ChapterAdapter.ChapterViewHolder>{

    Context context;
    ArrayList<String> chapterList;
    ArrayList<String> chapterId;
    String courseName;
    String courseId;
    String url, vid;


    class ChapterViewHolder extends RecyclerView.ViewHolder{
        TextView chapter_title;
        LinearLayout chapter_combo;

        public ChapterViewHolder (View itemView){
            super((itemView));
            chapter_title = itemView.findViewById(R.id.chapter_title);
            chapter_combo = itemView.findViewById(R.id.chapter_combo);
        }
    }


    public S_ChapterAdapter(Context context, ArrayList<String> chapterList, ArrayList<String> chapterId, String courseName, String courseId){
        this.context = context;
        this.chapterList = chapterList;
        this.chapterId = chapterId;
        this.courseName = courseName;
        this.courseId = courseId;
    }

    @Override
    public S_ChapterAdapter.ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_row,parent,false);
        return new S_ChapterAdapter.ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(S_ChapterAdapter.ChapterViewHolder holder, final int position){

        holder.chapter_title.setText(chapterList.get(position));

        holder.chapter_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Learn =new Intent(context, com.example.pokelearn.Activities.Learn.class);
                Learn.putExtra("ChapterId", chapterId.get(position));
                Learn.putExtra("CourseId", courseId);
                context.startActivity(Learn);
                Log.d("ERROR 1: ", chapterId.get(position));
            }
        });
    }

    @Override
    public int getItemCount(){
        return chapterList.size();
    }
}
