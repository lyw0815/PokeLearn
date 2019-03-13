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
import com.example.pokelearn.Activities.I_ChapterList;
import com.example.pokelearn.R;

import java.util.ArrayList;

public class I_ChapterAdapter extends RecyclerView.Adapter<I_ChapterAdapter.ChapterViewHolder> {

    Context context;
    ArrayList<String> chapterList;
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


    public I_ChapterAdapter(Context context, ArrayList<String> chapterList, String courseName){
        this.context = context;
        this.chapterList = chapterList;
        this.courseName = courseName;
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

                Intent I_ChapterDetails =new Intent(context, com.example.pokelearn.Activities.I_ChapterDetails.class);
                I_ChapterDetails.putExtra("ChapterSeq", chapterList.get(position));
                I_ChapterDetails.putExtra("CourseName", courseName);
                context.startActivity(I_ChapterDetails);

            }
        });
    }

    @Override
    public int getItemCount(){
        return chapterList.size();
    }
}
