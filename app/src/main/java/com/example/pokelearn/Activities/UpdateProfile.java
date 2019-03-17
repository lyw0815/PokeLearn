package com.example.pokelearn.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokelearn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateProfile extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView userName;
    private EditText newUserName;
    private Button btnUpdProfile, btnChoosePic;
    private ImageView userProfilePic;
    private ProgressBar updProgress;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.updateProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = (TextView) findViewById(R.id.textView13);
        newUserName = (EditText) findViewById(R.id.upd_name);
        btnUpdProfile = (Button) findViewById(R.id.btnUpdProfile);
        userProfilePic = (ImageView)findViewById(R.id.profilePic);
        btnChoosePic = (Button) findViewById(R.id.btnChoosePic);
        updProgress = (ProgressBar) findViewById(R.id.updProgressProfile);

        btnUpdProfile.setVisibility(View.VISIBLE);
        updProgress.setVisibility(View.INVISIBLE);

        userName.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(userProfilePic);

        btnChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22){ checkAndRequestForPermission(); }
                else{ openGallery(); }
            }
        });

        btnUpdProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = newUserName.getText().toString();

                if(!username.isEmpty() && pickedImgUri == null) {
                    updProfileName(username);
                }
                else if(pickedImgUri != null  && username.isEmpty()){
                    updProfileImg(pickedImgUri);
                }
                else if(!username.isEmpty() && pickedImgUri != null) {
                    updProfile(username,pickedImgUri);
                }
                else{
                    Toast.makeText(getApplicationContext(),"No changes made", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }


    private void updProfile(final String name, final Uri uri){
        btnUpdProfile.setVisibility(View.INVISIBLE);
        updProgress.setVisibility(View.VISIBLE);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("UsersPhoto");
        final StorageReference imageFilePath = mStorage.child(uri.getLastPathSegment());
        imageFilePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(uri)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        }
                                    });
                        }
                        });
                    }
        });
    }

    private void updProfileImg(final Uri uri) {
        btnUpdProfile.setVisibility(View.INVISIBLE);
        updProgress.setVisibility(View.VISIBLE);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("UsersPhoto");
        final StorageReference imageFilePath = mStorage.child(uri.getLastPathSegment());
        imageFilePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Profile picture updated", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updProfileName(String name){
        btnUpdProfile.setVisibility(View.INVISIBLE);
        updProgress.setVisibility(View.VISIBLE);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Username updated", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
    }

    private void openGallery() {
        //open gallery intent and wait for user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission( UpdateProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(UpdateProfile.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(UpdateProfile.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            //user has successfully picked an image
            //save reference to a Uri variable
            pickedImgUri = data.getData();
            userProfilePic.setImageURI(pickedImgUri);
        }
    }
}
