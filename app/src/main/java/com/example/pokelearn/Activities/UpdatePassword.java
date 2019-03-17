package com.example.pokelearn.Activities;

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

import com.example.pokelearn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatePassword extends AppCompatActivity {
    private TextView password, password2;
    private Button btnUpdPassword;
    private ProgressBar updProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.updatePasswordToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        password = (TextView) findViewById(R.id.pwd);
        password2 = (TextView) findViewById(R.id.pwd2);
        btnUpdPassword = (Button) findViewById(R.id.btnChangePwd);
        updProgress = (ProgressBar) findViewById(R.id.updProgressPwd);

        btnUpdPassword.setVisibility(View.VISIBLE);
        updProgress.setVisibility(View.INVISIBLE);

        btnUpdPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = password.getText().toString().trim();
                String pwd2 = password2.getText().toString().trim();

                if (pwd != null && pwd2 != null && pwd.equals(pwd2) && pwd.length()>5) {
                    updPassword(pwd, pwd2);
                    Log.e("Update", pwd);
                }
                else if (pwd.isEmpty() || pwd2.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please fill up password field", Toast.LENGTH_LONG).show();
                }
                else if (pwd.length()<6){
                    Toast.makeText(getApplicationContext(),"Password length must be at least 6", Toast.LENGTH_LONG).show();
                }
                else if (pwd != pwd2){
                    Toast.makeText(getApplicationContext(),"Password does not match with retype password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void updPassword(String password,String password2) {
            btnUpdPassword.setVisibility(View.INVISIBLE);
            updProgress.setVisibility(View.VISIBLE);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Password Updated", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
    }
}
