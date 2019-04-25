package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pokelearn.Quiz;
import com.example.pokelearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateQuiz extends AppCompatActivity {

    private Button createQuizBtn;
    private EditText quizTitle, quizId;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbReference;
    private String quiz_title, quiz_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.iCreateQuizToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        dbReference = FirebaseDatabase.getInstance().getReference("Quiz");

        quizTitle = (EditText) findViewById(R.id.quizTitleInput);
        quizId = (EditText) findViewById(R.id.quizIdInput);
        createQuizBtn = (Button) findViewById(R.id.createQuizBtn);

        createQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quiz_title = quizTitle.getText().toString().trim();
                quiz_id = quizId.getText().toString().trim();

                if (!quiz_title.isEmpty() && !quiz_id.isEmpty())
                {
                    Quiz quiz = new Quiz(quiz_title, quiz_id, currentUser.getUid(), "");
                    dbReference.child(quiz_id).setValue(quiz);

                    Intent CreateQuizQuestion = new Intent(CreateQuiz.this, CreateQuizQuestion.class);
                    CreateQuizQuestion.putExtra("QuizId", quiz_id );
                    CreateQuizQuestion.putExtra("QuizTitle", quiz_title );
                    startActivity(CreateQuizQuestion);
                    finish();
                }
                else
                {
                    Toast.makeText(CreateQuiz.this, "Please fill up all fields to create the quiz", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
