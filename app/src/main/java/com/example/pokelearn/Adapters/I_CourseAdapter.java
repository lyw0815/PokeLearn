package com.example.pokelearn.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokelearn.Activities.I_ChapterList;
import com.example.pokelearn.R;

import java.util.ArrayList;

public class I_CourseAdapter extends RecyclerView.Adapter<I_CourseAdapter.CourseViewHolder> {
    Context context;
    ArrayList<String> courseNameList;
    ArrayList<String> courseIdList;
    ArrayList<String> courseDescList;
    ArrayList<String> courseCoverImgList;

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


    public I_CourseAdapter(Context context, ArrayList<String> courseNameList, ArrayList<String>courseIdList, ArrayList<String>courseDescList, ArrayList<String>courseCoverImgList){
        this.context = context;
        this.courseNameList = courseNameList;
        this.courseIdList = courseIdList;
        this.courseDescList = courseDescList;
        this.courseCoverImgList = courseCoverImgList;
    }

    @Override
    public I_CourseAdapter.CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.course_row,parent,false);
        return new I_CourseAdapter.CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, final int position){

        holder.course_name.setText(courseNameList.get(position));
        holder.course_desc.setText(courseDescList.get(position));
        Glide.with(context).asBitmap().load(courseCoverImgList.get(position)).into(holder.course_img);

        holder.course_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "clicked: "+ courseNameList.get(position),Toast.LENGTH_LONG).show();

                Intent I_ChapterList =new Intent(context, I_ChapterList.class);
                I_ChapterList.putExtra("CourseName", courseNameList.get(position));
                I_ChapterList.putExtra("CourseId", courseIdList.get(position));
                context.startActivity(I_ChapterList);

            }
        });
    }

    @Override
    public int getItemCount(){
        return courseNameList.size();
    }

}
