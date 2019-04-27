package com.example.pokelearn.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.pokelearn.R;

public class Done extends AppCompatActivity {
    private Button btnTryAgain, btnQuit;
    private TextView txtResultScore, getTxtResultQuestion;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        txtResultScore = findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = findViewById(R.id.txtTotalQuestion);
        progressBar = findViewById(R.id.doneProgressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        btnQuit = findViewById(R.id.btnQuit);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Quiz = new Intent(Done.this, Quiz.class);
                Quiz.putExtra("QuizId", "1");
                startActivity(Quiz);
                finish();
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d", score));
            getTxtResultQuestion.setText(String.format("PASSED : %d/%d", correctAnswer, totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);
            progressBar.getProgressDrawable().setColorFilter(
                    Color.CYAN, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}
