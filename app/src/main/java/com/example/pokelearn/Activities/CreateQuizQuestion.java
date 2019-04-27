package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pokelearn.Question;
import com.example.pokelearn.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_SHORT;

public class CreateQuizQuestion extends AppCompatActivity {

    private Button publishBtn,addQuestionBtn;
    private EditText question, answer, option1, option2, option3, option4;
    private String quizId, quizTitle, ques, ans, opt1, opt2, opt3, opt4;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz_question);

        Intent i = getIntent();
        quizId = i.getStringExtra("QuizId");
        Intent j = getIntent();
        quizTitle = j.getStringExtra("QuizTitle");

        Toolbar toolbar = (Toolbar) findViewById(R.id.iCreateQuizQuestionToolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Create Question");
        getSupportActionBar().setTitle(quizTitle);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbReference = FirebaseDatabase.getInstance().getReference("Questions");

        question = (EditText) findViewById(R.id.questionInput);
        answer = (EditText) findViewById(R.id.answerInput);
        option1 = (EditText) findViewById(R.id.option1Input);
        option2 = (EditText) findViewById(R.id.option2Input);
        option3 = (EditText) findViewById(R.id.option3Input);
        option4 = (EditText) findViewById(R.id.option4Input);
        addQuestionBtn = (Button) findViewById(R.id.fab_addquestion);
        publishBtn = (Button) findViewById(R.id.publishQuizBtn);

        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ques = question.getText().toString().trim();
                ans = answer.getText().toString().toUpperCase().trim();
                opt1 = option1.getText().toString().trim();
                opt2 = option2.getText().toString().trim();
                opt3 = option3.getText().toString().trim();
                opt4 = option4.getText().toString().trim();

                if(!ques.isEmpty() && !ans.isEmpty() && !opt1.isEmpty() && !opt2.isEmpty() && !opt3.isEmpty() && !opt4.isEmpty()) {

                    if (ans.equals("A") ||ans.equals("B") || ans.equals("C") || ans.equals("D") ){
                        saveQuestion();
                        Intent CreateQuizQuestion = new Intent(getApplicationContext(), CreateQuizQuestion.class);
                        CreateQuizQuestion.putExtra("QuizId", quizId );
                        CreateQuizQuestion.putExtra("QuizTitle", quizTitle);
                        startActivity(CreateQuizQuestion);
                        finish();
                    }
                    else{
                        Toast.makeText(CreateQuizQuestion.this, "Please enter A, B, C or D for correct answer field", LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(CreateQuizQuestion.this, "Please do not leave blank", LENGTH_SHORT).show();
                }

            }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ques = question.getText().toString().trim();
                ans = answer.getText().toString().toUpperCase().trim();
                opt1 = option1.getText().toString().trim();
                opt2 = option2.getText().toString().trim();
                opt3 = option3.getText().toString().trim();
                opt4 = option4.getText().toString().trim();

                if(!ques.isEmpty() && !ans.isEmpty() && !opt1.isEmpty() && !opt2.isEmpty() && !opt3.isEmpty() && !opt4.isEmpty()) {
                    if (ans.equals("A") ||ans.equals("B") || ans.equals("C") || ans.equals("D") ){
                        saveQuestion();
                        Toast.makeText(CreateQuizQuestion.this, "Quiz created successfully", LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(CreateQuizQuestion.this, "Please enter A, B, C or D for correct answer field", LENGTH_SHORT).show();
                    }
                }
                else if (ques.isEmpty() && ans.isEmpty() && opt1.isEmpty() && opt2.isEmpty() && opt3.isEmpty() && opt4.isEmpty()){
                    Toast.makeText(CreateQuizQuestion.this, "Quiz created successfully", LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(CreateQuizQuestion.this, "Please do not leave blank", LENGTH_SHORT).show();
                }

            }
        });

    }

    public void saveQuestion(){
        String id = dbReference.push().getKey();
        Question quiz_question = new Question(quizId, ques, opt1, opt2, opt3, opt4, ans);
        dbReference.child(quizId).child(id).setValue(quiz_question);
    }
}
