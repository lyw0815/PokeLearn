package com.example.pokelearn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.pokelearn.R;

import java.util.ArrayList;

public class S_CourseAdapter extends RecyclerView.Adapter<S_CourseAdapter.CourseViewHolder>  {

    Context context;
    String module;
    ArrayList<String> courseNameList;
    ArrayList<String> courseDescList;
    ArrayList<String> courseCoverImgList;
    ArrayList<String> courseId;

    class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView course_name,course_desc;
        ImageView course_img;
        LinearLayout course_combo;

        public CourseViewHolder (View itemView){
            super((itemView));
            course_name = itemView.findViewById(R.id.course_name);
            course_desc = itemView.findViewById(R.id.course_desc);
            course_img  = itemView.findViewById(R.id.course_img);
            course_combo = itemView.findViewById(R.id.course_combo);
        }
    }


    public S_CourseAdapter(Context context, ArrayList<String> courseNameList, ArrayList<String>courseDescList,
                           ArrayList<String>courseCoverImgList, ArrayList<String>courseId, String module){
        this.context = context;
        this.courseNameList = courseNameList;
        this.courseDescList = courseDescList;
        this.courseCoverImgList = courseCoverImgList;
        this.courseId = courseId;
        this.module = module;
    }

    @Override
    public S_CourseAdapter.CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.course_row,parent,false);
        return new S_CourseAdapter.CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(S_CourseAdapter.CourseViewHolder holder, final int position){

        holder.course_name.setText(courseNameList.get(position));
        holder.course_desc.setText(courseDescList.get(position));
        Glide.with(context).asBitmap().load(courseCoverImgList.get(position)).into(holder.course_img);

        holder.course_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(module.equals("myCourse")) {
                    Intent S_ChapterList = new Intent(context, com.example.pokelearn.Activities.S_ChapterList.class);
                    S_ChapterList.putExtra("CourseId", courseId.get(position));
                    S_ChapterList.putExtra("CourseName", courseNameList.get(position));
                    context.startActivity(S_ChapterList);
                }
                else{
                    Intent S_CourseProgress = new Intent(context, com.example.pokelearn.Activities.S_CourseProgress.class);
                    S_CourseProgress.putExtra("CourseId", courseId.get(position));
                    S_CourseProgress.putExtra("CourseName", courseNameList.get(position));
                    context.startActivity(S_CourseProgress);
                }

            }
        });
    }

    @Override
    public int getItemCount(){
        return courseNameList.size();
    }
}
