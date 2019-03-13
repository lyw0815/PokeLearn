package com.example.pokelearn.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
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

import com.example.pokelearn.Courses;
import com.example.pokelearn.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CreateCourse extends AppCompatActivity {

    Toolbar toolbar;

    EditText courseName;
    EditText courseDesc;
    Button createCourseBtn;
    Button selectCoverImageBtn;
    ImageView courseCoverImage;
    ProgressBar progressBar;
    Uri pickedImgUri;

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
        setContentView(R.layout.activity_create_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.iCreateCourseToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("Create Course"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        dbReference = FirebaseDatabase.getInstance().getReference("Courses");
        stReference = FirebaseStorage.getInstance().getReference("CourseCoverImages");

        courseName = (EditText) findViewById(R.id.courseName);
        courseDesc = (EditText) findViewById(R.id.courseDesc);
        courseCoverImage = (ImageView) findViewById(R.id.courseCoverImage);
        createCourseBtn = (Button) findViewById(R.id.createCourseBtn);
        selectCoverImageBtn = (Button) findViewById(R.id.selectCoverImageBtn);
        progressBar = (ProgressBar)findViewById(R.id.createCourseProgressBar);

        progressBar.setVisibility(View.INVISIBLE);
        createCourseBtn.setVisibility(View.VISIBLE);

        createCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                createCourseBtn.setVisibility(View.INVISIBLE);
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(CreateCourse.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    createCourse();
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
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission( CreateCourse.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateCourse.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(CreateCourse.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(CreateCourse.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();

    }

    private void openGallery() {
        //open gallery intent and wait for user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            //user has successfully picked an image
            //save reference to a Uri variable
            pickedImgUri = data.getData();
            courseCoverImage.setImageURI(pickedImgUri);

            Picasso.get().load(pickedImgUri).into(courseCoverImage);
        }
    }

    private void createCourse(){
        String name = courseName.getText().toString().trim();
        String desc = courseDesc.getText().toString().trim();
        Uri uri = pickedImgUri;
        final String instr = currentUser.getDisplayName();
        final String instrMail = currentUser.getEmail();
        final String instrId = currentUser.getUid();


        if (!name.isEmpty() && !desc.isEmpty() && ( uri != null )){

            final StorageReference fileReference = stReference.child((System.currentTimeMillis()
                    + "." + getFileExtension(pickedImgUri)));

            uploadTask = fileReference.putFile(pickedImgUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL

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
                        showMessage("Course created");
                        finish();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreateCourse.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else if (name.isEmpty() && desc.isEmpty()){
            Toast.makeText(this,"Please fill up the fields", Toast.LENGTH_LONG).show();
        }else if (name.isEmpty()){
            Toast.makeText(this,"Please enter course name", Toast.LENGTH_LONG).show();
        }else if (desc.isEmpty()) {
            Toast.makeText(this,"Please enter course description", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"Please select a cover image for this course", Toast.LENGTH_LONG).show();
        }
    }

    //get file extension from image
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
