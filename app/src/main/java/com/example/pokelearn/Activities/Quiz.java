package com.example.pokelearn.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pokelearn.Common;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Quiz extends AppCompatActivity implements View.OnClickListener {

    private String QuizId, ans;
    private DatabaseReference dbReference;

    private static long INTERVAL = 1000;
    private static long TIMEOUT = 10000;
    private int progressValue = 0;
    private CountDownTimer mCountDown;
    private int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;
    private ProgressBar progressBar;
    private Button btnA, btnB, btnC, btnD;
    private TextView txtScore, txtQuestionNum, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent i = getIntent();
        QuizId = i.getStringExtra("QuizId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.quizToolbar);
        setSupportActionBar(toolbar);

        dbReference = FirebaseDatabase.getInstance().getReference("Quiz");

        dbReference.child(QuizId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("quizTitle").getValue().toString();
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestionNum = (TextView) findViewById(R.id.txtTotalQuestion);
        question_text = (TextView) findViewById(R.id.question_text);
        Collections.shuffle(Common.list_question);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mCountDown.cancel();

        if (index < totalQuestion) {
            Button clickedButton = (Button) v;

            switch (Common.list_question.get(index).getAnswer()) {
                case "A" :
                    ans = Common.list_question.get(index).getOption1(); break;
                case "B":
                    ans = Common.list_question.get(index).getOption2(); break;
                case "C":
                    ans = Common.list_question.get(index).getOption3(); break;
                case "D":
                    ans = Common.list_question.get(index).getOption4(); break;
                default:
                    break;
            }

//            if (clickedButton.getText().equals(Common.list_question.get(index).getAnswer())) {
            if (clickedButton.getText().equals(ans)) {
//                Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                score = score + 100*1/totalQuestion;
                correctAnswer++;
                showQuestion(++index);
            }
            else {
                showQuestion(++index);
            }
            txtScore.setText(String.format("%d", score));
        }
    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            question_text.setText("Q. "+Common.list_question.get(index).getQuestion());
            question_text.setVisibility(View.VISIBLE);

            btnA.setText(Common.list_question.get(index).getOption1());
            btnB.setText(Common.list_question.get(index).getOption2());
            btnC.setText(Common.list_question.get(index).getOption3());
            btnD.setText(Common.list_question.get(index).getOption4());
            mCountDown.start();
        } else {
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.list_question.size();

        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue+=12;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}
