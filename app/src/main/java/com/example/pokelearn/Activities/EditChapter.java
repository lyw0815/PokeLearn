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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Chapters;
import com.example.pokelearn.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class EditChapter extends AppCompatActivity {
    String chap_title, chap_seq, chap_desc, chap_url, chap_vid;

    EditText cTitle, cSeq, cDesc, cYouTubeUrl;
    Button btnUpdateChapter;
    Button btnBack;
    Button btnChooseFile;
    Uri pdfUri;
    ProgressDialog progressDialog;
    ProgressBar updateChapProgress;
    private StorageTask uploadTask;

    static int PReqCode = 1;
    static int REQUESCODE = 1;

    FirebaseStorage storage;
    StorageReference stReference;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chapter);

        Intent i = getIntent();
        final String ChapterId = i.getStringExtra("ChapterId");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");
        Intent k = getIntent();
        final String CourseName = k.getStringExtra("CourseName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.editChapterToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Chapter");

        storage = FirebaseStorage.getInstance();

        cTitle = (EditText) findViewById(R.id.editChapterTitle);
        cSeq =  (EditText) findViewById(R.id.editChapterSeq);
        cDesc = (EditText) findViewById(R.id.editChapterDesc);
        cYouTubeUrl = (EditText) findViewById(R.id.editYouTubeUrl);
        btnBack = (Button) findViewById(R.id.btnBack_1);
        btnChooseFile = (Button) findViewById(R.id.btnChooseFile_1);
        btnUpdateChapter = (Button) findViewById(R.id.btnUpdateChapter);
        updateChapProgress = (ProgressBar) findViewById(R.id.updateChapProgress);

        btnUpdateChapter.setVisibility(View.VISIBLE);
        updateChapProgress.setVisibility(View.INVISIBLE);


        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(EditChapter.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(EditChapter.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I_ChapterDetails =new Intent(getApplicationContext(), I_ChapterDetails.class);
                I_ChapterDetails.putExtra("ChapterId", ChapterId );
                I_ChapterDetails.putExtra("CourseId", CourseId );
                I_ChapterDetails.putExtra("CourseName", CourseName);
                startActivity(I_ChapterDetails);
                finish();
            }
        });

        btnUpdateChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = cTitle.getText().toString().trim();
                String seq = cSeq.getText().toString().trim();
                String desc = cDesc.getText().toString().trim();
                String youtubeUrl = cYouTubeUrl.getText().toString().trim();
                String youtubeVideoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("/") +1);

                if (!title.isEmpty() && !seq.isEmpty() && !desc.isEmpty()) {
                    Double sequence = Double.parseDouble(seq);
                    updateChapter(CourseId, CourseName, ChapterId, title, sequence, desc, chap_url, youtubeVideoId);
                }
                else
                    Toast.makeText(EditChapter.this, "Please fill up all fields", Toast.LENGTH_SHORT).show();
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Chapters").child(CourseId).child(ChapterId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String chapter_title = dataSnapshot.child("chapterTitle").getValue(String.class);
                Double chapter_sequence = dataSnapshot.child("chapterSequence").getValue(Double.class);
                String chapter_desc = dataSnapshot.child("chapterDesc").getValue(String.class);
                String chapter_url = dataSnapshot.child("chapterMaterialUrl").getValue(String.class);
                String chapter_vid = dataSnapshot.child("chapterYoutubeVideoId").getValue(String.class);

                chap_title = chapter_title;
                chap_seq = String.valueOf(chapter_sequence);
                chap_desc = chapter_desc;
                chap_url = chapter_url;
                chap_vid = "https://youtu.be/" + chapter_vid;


                cTitle.setText(chap_title);
                cSeq.setText(chap_seq);
                cDesc.setText(chapter_desc);
                cYouTubeUrl.setText(chap_vid);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    public void updateChapter(final String course_id, final String course_name, final String c_id, final String c_title, final Double c_seq, final String c_desc, final String c_url, final String c_vid) {

        btnUpdateChapter.setVisibility(View.INVISIBLE);
        updateChapProgress.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Updating Chapter...");
        progressDialog.setMessage("Please wait while we updating the chapter content");
//        progressDialog.setProgress(0);
        progressDialog.show();

        stReference = FirebaseStorage.getInstance().getReference("LearningMaterials");

        if (pdfUri != null) {
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
                            dbReference = FirebaseDatabase.getInstance().getReference("Chapters").child(course_id);
                            if (task.isSuccessful()) {
                                String id = dbReference.push().getKey();
                                Chapters newChapter = new Chapters(id,
                                        course_name,
                                        course_id,
                                        c_title,
                                        c_seq,
                                        c_desc,
                                        task.getResult().toString().trim(),
                                        c_vid);


                                dbReference.child(id).setValue(newChapter);

                                Toast.makeText(EditChapter.this, "Chapter updated", Toast.LENGTH_SHORT).show();

                                /////DELETE HERE////////////

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference().child("Chapters").child(course_id).child(c_id);
                                ref.removeValue();

//                                Intent IChapterDetails = new Intent(getApplicationContext(), I_ChapterDetails.class);
//                                IChapterDetails.putExtra("CourseId", course_id);
//                                IChapterDetails.putExtra("ChapterId", id);
//                                IChapterDetails.putExtra("CourseName", course_name);
//                                startActivity(IChapterDetails);
                                I_ChapterList.iChapterList.finish();
                                Intent IChapterList = new Intent(getApplicationContext(), I_ChapterList.class);
                                IChapterList.putExtra("CourseId", course_id);
                                IChapterList.putExtra("CourseName", course_name);
                                startActivity(IChapterList);
                                finish();
                            } else
                                Toast.makeText(EditChapter.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditChapter.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else
        {   dbReference = FirebaseDatabase.getInstance().getReference("Chapters").child(course_id);
            String id = dbReference.push().getKey();
            Chapters newChapter = new Chapters(id,
                    course_name,
                    course_id,
                    c_title,
                    c_seq,
                    c_desc,
                    chap_url,
                    c_vid);


            dbReference.child(id).setValue(newChapter);

            Toast.makeText(EditChapter.this, "Chapter updated", Toast.LENGTH_SHORT).show();

            /////DELETE HERE////////////

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("Chapters").child(course_id).child(c_id);
            ref.removeValue();

//            Intent IChapterDetails = new Intent(getApplicationContext(), I_ChapterDetails.class);
//            IChapterDetails.putExtra("CourseId", course_id);
//            IChapterDetails.putExtra("ChapterId", id);
//            IChapterDetails.putExtra("CourseName", course_name);
//            startActivity(IChapterDetails);
            I_ChapterList.iChapterList.finish();
            Intent IChapterList = new Intent(getApplicationContext(), I_ChapterList.class);
            IChapterList.putExtra("CourseId", course_id);
            IChapterList.putExtra("CourseName", course_name);
            startActivity(IChapterList);
            finish();
        }
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
                        final TextView file_name = (TextView) findViewById(R.id.editFileNameTxtView);
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
            Toast.makeText(EditChapter.this, "Please select a file", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        { selectPdf(); }
        else
            Toast.makeText(EditChapter.this, "Please provide permission..", Toast.LENGTH_SHORT).show();
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

