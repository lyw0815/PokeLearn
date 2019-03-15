package com.example.pokelearn.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Chapters;
import com.example.pokelearn.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class CreateChapter extends AppCompatActivity {

    EditText chapterTitle, chapterSeq, chapterDesc, youTubeUrl;
    Button btnPublishChapter;
    Button btnBack;
    Button btnChooseFile;
    Uri pdfUri;
    ProgressDialog progressDialog;
    private StorageTask uploadTask;

    static int PReqCode = 1;
    static int REQUESCODE = 1;

    FirebaseStorage storage;
    StorageReference stReference;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chapter);

        Intent i = getIntent();
        final String CourseName = i.getStringExtra("CourseName");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");

        Toast.makeText(CreateChapter.this, CourseId, Toast.LENGTH_SHORT).show();
        Log.d("ERROR 2: ", CourseName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.createChapterToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Chapter");

        storage = FirebaseStorage.getInstance();

        chapterTitle = (EditText) findViewById(R.id.chapterTitle);
        chapterSeq =  (EditText) findViewById(R.id.chapterSeq);
        chapterDesc = (EditText) findViewById(R.id.chapterDesc);
        youTubeUrl = (EditText) findViewById(R.id.youTubeUrl);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnChooseFile = (Button) findViewById(R.id.btnChooseFile);
        btnPublishChapter = (Button) findViewById(R.id.btnPublishChapter);


        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CreateChapter.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(CreateChapter.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I_ChapterList =new Intent(getApplicationContext(), I_ChapterList.class);
                I_ChapterList.putExtra("CourseName", CourseName );
                I_ChapterList.putExtra("CourseId", CourseId );
                startActivity(I_ChapterList);
                finish();
            }
        });

        btnPublishChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri != null)
                    PublishChapter(CourseName, CourseId);
                else
                    Toast.makeText(CreateChapter.this, "Select a File", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PublishChapter(final String CourseName, final String CourseId) {
        final String title = chapterTitle.getText().toString().trim();

        String seq = chapterSeq.getText().toString().trim();
        final Double sequence = Double.parseDouble(seq);

        final String desc = chapterDesc.getText().toString().trim();

        String youtubeUrl = youTubeUrl.getText().toString().trim();
        final String youtubeVideoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("/") +1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        stReference = FirebaseStorage.getInstance().getReference("LearningMaterials");

        final StorageReference fileReference = stReference.child((System.currentTimeMillis()
                + "." + getFileExtension(pdfUri)));

        uploadTask = fileReference.putFile(pdfUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        })


                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        //final String url = task.getResult().toString().trim();
                        dbReference = FirebaseDatabase.getInstance().getReference("Chapters").child(CourseId);
                        if (task.isSuccessful()) {
                            String id = dbReference.push().getKey();
                            Chapters newChapter = new Chapters(id,
                                    CourseName,
                                    CourseId,
                                    title,
                                    sequence,
                                    desc,
                                    task.getResult().toString().trim(),
                                    youtubeVideoId);

                            dbReference.child(id).setValue(newChapter);
                            showMessage("Course created");
                            Toast.makeText(CreateChapter.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();

                            Intent IChapterList =new Intent(getApplicationContext(), I_ChapterList.class);
                            IChapterList.putExtra("CourseName", CourseName );
                            IChapterList.putExtra("CourseId", CourseId );
                            Log.d("ERROR", CourseName);
                            startActivity(IChapterList);
                            finish();
                        }

                        else
                            Toast.makeText(CreateChapter.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateChapter.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null)
       {
           pdfUri = data.getData();

           String uriString = pdfUri.toString();
           File myFile = new File(uriString);
           String path = myFile.getAbsolutePath();
           String displayName = null;

           if (uriString.startsWith("content://")) {
               Cursor cursor = null;
               try {
                   cursor = getApplication().getContentResolver().query(pdfUri, null, null, null, null);
                   if (cursor != null && cursor.moveToFirst()) {
                       displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                       final TextView file_name = (TextView) findViewById(R.id.fileNameTxtView);
                        file_name.setText(displayName);
                   }
               } finally {
                   cursor.close();
               }
           } else if (uriString.startsWith("file://")) {
               displayName = myFile.getName();
           }

       }
       else
           Toast.makeText(CreateChapter.this, "Please select a file", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        } else
            Toast.makeText(CreateChapter.this, "Please provide permission..", Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
