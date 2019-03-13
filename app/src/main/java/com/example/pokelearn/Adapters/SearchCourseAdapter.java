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
import com.bumptech.glide.Glide;
import com.example.pokelearn.Activities.ChapterList;
import com.example.pokelearn.R;
import java.util.ArrayList;

public class SearchCourseAdapter extends RecyclerView.Adapter<SearchCourseAdapter.CourseViewHolder> {
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


    public SearchCourseAdapter(Context context, ArrayList<String> courseNameList,ArrayList<String> courseIdList, ArrayList<String>courseDescList, ArrayList<String>courseCoverImgList){
        this.context = context;
        this.courseNameList = courseNameList;
        this.courseIdList = courseIdList;
        this.courseDescList = courseDescList;
        this.courseCoverImgList = courseCoverImgList;
    }

    @Override
    public SearchCourseAdapter.CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.course_row,parent,false);
        return new SearchCourseAdapter.CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, final int position){

        holder.course_name.setText(courseNameList.get(position));
        holder.course_desc.setText(courseDescList.get(position));
        Glide.with(context).asBitmap().load(courseCoverImgList.get(position)).into(holder.course_img);

        holder.course_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ChapterList =new Intent(context, ChapterList.class);
                ChapterList.putExtra("CourseName", courseNameList.get(position));
                ChapterList.putExtra("CourseId", courseIdList.get(position));
                context.startActivity(ChapterList);
            }
        });
    }

    @Override
    public int getItemCount(){
        return courseNameList.size();
    }

}
