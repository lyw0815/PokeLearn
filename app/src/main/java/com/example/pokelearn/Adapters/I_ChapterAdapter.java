package com.example.pokelearn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.pokelearn.Activities.I_ChapterDetails;
import com.example.pokelearn.R;

import java.util.ArrayList;

public class I_ChapterAdapter extends RecyclerView.Adapter<I_ChapterAdapter.ChapterViewHolder> {

    Context context;
    String module;
    ArrayList<String> chapterList;
    ArrayList<String> chapterId;
    String courseId;
    String courseName;


    class ChapterViewHolder extends RecyclerView.ViewHolder{
        TextView chapter_title;
        LinearLayout chapter_combo;

        public ChapterViewHolder (View itemView){
            super((itemView));
            chapter_title = itemView.findViewById(R.id.chapter_title);
            chapter_combo = itemView.findViewById(R.id.chapter_combo);
        }
    }


    public I_ChapterAdapter(Context context, ArrayList<String> chapterList,ArrayList<String> chapterId, String courseName, String courseId, String module){
        this.context = context;
        this.chapterList = chapterList;
        this.chapterId = chapterId;
        this.courseName = courseName;
        this.courseId = courseId;
        this.module = module;
    }

    @Override
    public I_ChapterAdapter.ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_row,parent,false);
        return new I_ChapterAdapter.ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(I_ChapterAdapter.ChapterViewHolder holder, final int position){

        holder.chapter_title.setText(chapterList.get(position));

        holder.chapter_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "clicked: "+ chapterList.get(position),Toast.LENGTH_LONG).show();

                if (module.equals("details")) {
                    Intent I_ChapterDetails = new Intent(context, com.example.pokelearn.Activities.I_ChapterDetails.class);
                    I_ChapterDetails.putExtra("ChapterId", chapterId.get(position));
                    I_ChapterDetails.putExtra("CourseName", courseName);
                    I_ChapterDetails.putExtra("CourseId", courseId);
                    context.startActivity(I_ChapterDetails);
//                    Log.d("ERROR 1: ", chapterId.get(position));
                }
                else{
                    Intent I_StudentProgress = new Intent(context, com.example.pokelearn.Activities.I_StudentProgress.class);
                    I_StudentProgress.putExtra("ChapterId", chapterId.get(position));
                    I_StudentProgress.putExtra("ChapterName", chapterList.get(position));
                    I_StudentProgress.putExtra("CourseId", courseId);
                    context.startActivity(I_StudentProgress);
                }

            }
        });
    }

    @Override
    public int getItemCount(){
        return chapterList.size();
    }
}
