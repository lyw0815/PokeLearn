package com.example.pokelearn.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokelearn.Courses;
import com.example.pokelearn.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditCourse extends AppCompatActivity {
    Toolbar toolbar;
    EditText courseName;
    EditText courseDesc;
    Button updateCourseBtn;
    Button selectCoverImageBtn;
    ImageView courseCoverImage;
    ProgressBar progressBar;
    Uri pickedImgUri;
    String imageUrl;

    static int PReqCode = 1;
    static int REQUESCODE = 1;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference dbReference;
    StorageReference stReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent i = getIntent();
        final String CourseId = i.getStringExtra("CourseId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.editCourseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        dbReference = FirebaseDatabase.getInstance().getReference("Courses");
        stReference = FirebaseStorage.getInstance().getReference("CourseCoverImages");

        courseName = (EditText) findViewById(R.id.editCourseName);
        courseDesc = (EditText) findViewById(R.id.editCourseDesc);
        courseCoverImage = (ImageView) findViewById(R.id.courseCoverImage_1);
        updateCourseBtn = (Button) findViewById(R.id.editCourseBtn);
        selectCoverImageBtn = (Button) findViewById(R.id.selectCoverImageBtn_1);
        progressBar = (ProgressBar)findViewById(R.id.editCourseProgressBar);

        progressBar.setVisibility(View.INVISIBLE);
        updateCourseBtn.setVisibility(View.VISIBLE);

        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                updateCourseBtn.setVisibility(View.INVISIBLE);

                String name = courseName.getText().toString().trim();
                String desc = courseDesc.getText().toString().trim();

                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(EditCourse.this, "Update in progress", Toast.LENGTH_SHORT).show();
                }
                if (!name.isEmpty() && !desc.isEmpty()){
                    updateCourse(CourseId, name, desc, imageUrl);
                }
                if (name.isEmpty() || desc.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    updateCourseBtn.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditCourse.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectCoverImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else{
                    openGallery();
                }

            }
        });
        ////READ FROM DB
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Courses").child(CourseId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String course_name = dataSnapshot.child("courseName").getValue(String.class);
                String course_desc = dataSnapshot.child("courseDesc").getValue(String.class);
                String cover_url = dataSnapshot.child("courseCoverImgUrl").getValue(String.class);

                imageUrl = cover_url;
                courseName.setText(course_name);
                courseDesc.setText(course_desc);
                Glide.with(getApplicationContext()).load(cover_url).into(courseCoverImage);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    private void updateCourse(final String c_id, final String name, final String desc, final String i_url){

        final String instr = currentUser.getDisplayName();
        final String instrMail = currentUser.getEmail();
        final String instrId = currentUser.getUid();
        Uri uri = pickedImgUri;

        if (uri != null ){

            final StorageReference fileReference = stReference.child((System.currentTimeMillis()
                    + "." + getFileExtension(pickedImgUri)));

            uploadTask = fileReference.putFile(pickedImgUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String id = dbReference.push().getKey();
                        Courses newCourse = new Courses(id,
                                courseName.getText().toString().trim(),
                                courseDesc.getText().toString().trim(),
                                task.getResult().toString().trim(),
                                instr,
                                instrMail,
                                instrId);

                        dbReference.child(id).setValue(newCourse);
                        dbReference.child(c_id).removeValue();

                        showMessage("Course updated");
                        I_MyCourse.iMyCourse.finish();
                        Intent I_MyCourse =new Intent(getApplicationContext(), I_MyCourse.class);
                        startActivity(I_MyCourse);
                        finish();
                    } else {
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditCourse.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            String id = dbReference.push().getKey();
            Courses newCourse = new Courses(id,
                    courseName.getText().toString().trim(),
                    courseDesc.getText().toString().trim(),
                    imageUrl,
                    instr,
                    instrMail,
                    instrId);

            dbReference.child(id).setValue(newCourse);
            dbReference.child(c_id).removeValue();
            showMessage("Course updated");
            I_MyCourse.iMyCourse.finish();
            Intent I_MyCourse =new Intent(getApplicationContext(), I_MyCourse.class);
            startActivity(I_MyCourse);
            finish();
        }
    }





    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission( EditCourse.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditCourse.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(EditCourse.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(EditCourse.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedImgUri = data.getData();
            courseCoverImage.setImageURI(pickedImgUri);
            Picasso.get().load(pickedImgUri).into(courseCoverImage);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }



}
