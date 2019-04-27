package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.pokelearn.Common;
import com.example.pokelearn.Question;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class PreQuiz extends AppCompatActivity {

    private String QuizId;
    private Button startBtn;
    private TextView quizTitle, quizQuestions;
    private DatabaseReference dbReference, dbReference2;
    private DatabaseReference questions;
    private Integer q_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.PreQuizToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        QuizId = i.getStringExtra("QuizId");

        quizTitle = (TextView) findViewById(R.id.QuizTitleShow);
        quizQuestions = (TextView) findViewById(R.id.QuizQuestionsShow);
        startBtn = (Button) findViewById(R.id.StartQuizBtn);

        dbReference = FirebaseDatabase.getInstance().getReference("Quiz");
        dbReference2 = FirebaseDatabase.getInstance().getReference("Questions");

        questions = FirebaseDatabase.getInstance().getReference("Questions");
        loadQuestion(QuizId);

        dbReference.child(QuizId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("quizTitle").getValue().toString();
                quizTitle.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbReference2.child(QuizId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    q_count++;
                }
                String questions = q_count.toString();
                quizQuestions.setText(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Quiz = new Intent(PreQuiz.this, Quiz.class);
                Quiz.putExtra("QuizId", QuizId );
                startActivity(Quiz);
                finish();
            }
        });
    }

    private void loadQuestion(String quizId) {

        if (Common.list_question.size() > 0) {
            Common.list_question.clear();
        }
        questions.child(quizId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.list_question.add(ques);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Collections.shuffle(Common.list_question);
    }
}
