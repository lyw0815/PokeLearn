package com.example.pokelearn.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.pokelearn.R;

import java.util.ArrayList;

public class SearchChapterAdapter extends RecyclerView.Adapter<SearchChapterAdapter.ChapterViewHolder> {
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


    public SearchChapterAdapter(Context context, ArrayList<String> chapterList, String courseName){
        this.context = context;
        this.chapterList = chapterList;
        this.courseName = courseName;
    }

    @Override
    public SearchChapterAdapter.ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_row,parent,false);
        return new SearchChapterAdapter.ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchChapterAdapter.ChapterViewHolder holder, final int position){

        holder.chapter_title.setText(chapterList.get(position));

    }

    @Override
    public int getItemCount(){
        return chapterList.size();
    }
}
