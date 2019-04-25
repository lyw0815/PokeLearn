package com.example.pokelearn.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.example.pokelearn.Adapters.I_QuizAdapter;
import com.example.pokelearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class I_MyQuiz extends AppCompatActivity {

    private RecyclerView quizList;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    private Query dbReference;
    private ArrayList<String> quizTitleList;
    private ArrayList<String> quizPinList;
    private I_QuizAdapter IQuizAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__my_quiz);

        toolbar = (Toolbar) findViewById(R.id.iMyQuizToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        dbReference = FirebaseDatabase.getInstance().getReference("Quiz");

        quizList = (RecyclerView) findViewById(R.id.i_myQuizRecycleView);
        quizList.setHasFixedSize(true);
        quizList.setLayoutManager(new LinearLayoutManager(this));
        quizList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        quizTitleList = new ArrayList<>();
        quizPinList = new ArrayList<>();

        setAdapter(mCurrent_user_id);
    }

        private void setAdapter(final String uid) {

            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int counter = 0;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                        String title = snapshot.child("quizTitle").getValue(String.class);
                        String pin = snapshot.child("quizId").getValue(String.class);
                        String user = snapshot.child("quizCreator").getValue(String.class);

                        if (user.equals(uid)){
                            quizTitleList.add(title);
                            quizPinList.add(pin);
                            counter++;
                        }

                        if (counter == 15){
                            break;
                        }
                    }
                    IQuizAdapter = new I_QuizAdapter(I_MyQuiz.this, quizTitleList, quizPinList);
                    quizList.setAdapter(IQuizAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
