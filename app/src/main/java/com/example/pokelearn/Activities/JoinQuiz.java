package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinQuiz extends AppCompatActivity {

    private EditText quizIdInput;
    private Button joinBtn;
    private String quizId;
    private DatabaseReference dbReferenece;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.JoinQuizToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Join Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quizIdInput = (EditText) findViewById(R.id.quizPinInput);
        joinBtn = (Button) findViewById(R.id.EnterQuizBtn);

        dbReferenece = FirebaseDatabase.getInstance().getReference("Quiz");

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quizId = quizIdInput.getText().toString().trim();

                dbReferenece.child(quizId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            joinQuiz(quizId);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Opps! This quiz pin does not exist. Make sure you enter the correct pin.", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void joinQuiz(String quiz_id){
        Intent PreQuiz = new Intent(getApplicationContext(), PreQuiz.class);
        PreQuiz.putExtra("QuizId", quiz_id );
        startActivity(PreQuiz);
        finish();
    }
}
