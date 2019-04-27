package com.example.pokelearn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.pokelearn.R;
import java.util.ArrayList;

public class I_QuizAdapter extends RecyclerView.Adapter<I_QuizAdapter.QuizViewHolder> {
    Context context;
    ArrayList<String> quizTitleList;
    ArrayList<String> quizPinList;

    class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quiz_title, quiz_pin;
        LinearLayout quiz_combo;

        public QuizViewHolder(View itemView) {
            super((itemView));
            quiz_title = itemView.findViewById(R.id.quiz_title);
            quiz_pin = itemView.findViewById(R.id.quiz_pin);
            quiz_combo = itemView.findViewById(R.id.quiz_combo);
        }
    }
    public I_QuizAdapter(Context context, ArrayList<String> quizTitleList, ArrayList<String>quizPinList){
        this.context = context;
        this.quizTitleList = quizTitleList;
        this.quizPinList = quizPinList;
    }

    @Override
    public I_QuizAdapter.QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_row,parent,false);
        return new I_QuizAdapter.QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(I_QuizAdapter.QuizViewHolder holder, final int position){

        holder.quiz_title.setText(quizTitleList.get(position));
        holder.quiz_pin.setText(quizPinList.get(position));

//        holder.course_combo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(context, "clicked: "+ courseNameList.get(position),Toast.LENGTH_LONG).show();
//
//
//
//            }
//        });
    }

    @Override
    public int getItemCount(){
        return quizTitleList.size();
    }

}