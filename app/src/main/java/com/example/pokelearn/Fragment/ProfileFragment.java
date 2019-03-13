package com.example.pokelearn.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokelearn.Activities.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.pokelearn.R;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.support.constraint.Constraints.TAG;


public class ProfileFragment extends Fragment {

    static int PReqCode = 1;
    static int REQUESCODE = 1;

    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText updUserName, updPassword, updPassword2;
    private TextView userName, userMail;
    private Button btnUpd, btnChoose;
    private ProgressBar updProgress;
    private ImageView userProfilePic;
    Uri pickedImgUri;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = v.findViewById(R.id.userName);
        userMail = v.findViewById(R.id.userMail);
        updUserName = v.findViewById(R.id.updUserName);
        updPassword = v.findViewById(R.id.updPassword);
        updPassword2 = v.findViewById(R.id.updPassword2);
        btnUpd = v.findViewById(R.id.btnUpd);
        btnChoose = v.findViewById(R.id.btnChoosePic);
        updProgress = v.findViewById(R.id.updProgress);
        userProfilePic = v.findViewById(R.id.userProfilePic);


        userName.setText(user.getDisplayName());
        userMail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(userProfilePic);

        return v;

//        btnChoose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= 22) {
//                    checkAndRequestForPermission();
//                } else {
//                    openGallery();
//                }
//            }
//        });


//        btnUpd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updProgress.setVisibility(View.VISIBLE);
//                btnUpd.setVisibility(View.INVISIBLE);
//
//                final String uName = userName.getText().toString();
//                final String uPassword = updPassword.getText().toString();
//                final String uPassword2 = updPassword2.getText().toString();
//
//                if( uName.isEmpty() ){
//                    showMessage("Please fill in username");
//                    btnUpd.setVisibility(View.VISIBLE);
//                    updProgress.setVisibility(View.INVISIBLE);
//                }
//
//                if (!uPassword.equals(uPassword2)) {
//                    showMessage("Please re-enter password");
//                    btnUpd.setVisibility(View.VISIBLE);
//                    updProgress.setVisibility(View.INVISIBLE);
//                }
//                if (!uName.equals(user.getDisplayName()) || !photoUrl.equals(pickedImgUri)) {
//
//                    updateProfile();
//                }
//                if (uPassword2.equals(uPassword) && !uPassword.isEmpty()){
//
//                    updatePassword(user);
//                }
//            }
//        });
//
//
//        return inflater.inflate(R.layout.fragment_profile, container, false);
//    }
//
//    public void updateProfile() {
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName("Jane Q. User")
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User profile updated.");
//                        }
//                    }
//                });
//    }
//
//    private void updatePassword(FirebaseUser user) {
//        String newPassword = uPassword;
//
//        user.updatePassword(newPassword)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User password updated.");
//                        }
//                    }
//                });
//    }
//
//    private void showMessage(String message) {
//
//        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//    }

//        private void openGallery () {
        //open gallery intent and wait for user to pick an image

//            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            galleryIntent.setType("image/*");
//            startActivityForResult(galleryIntent, REQUESCODE);
//        }
//
//        private void checkAndRequestForPermission () {
//
//            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    Toast.makeText(getContext(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
//                } else {
//                    ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            PReqCode);
//                }
//            } else
//                openGallery();
//
//        }
//
//        @Override
//        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
//            super.onActivityResult(requestCode, resultCode, data);
//
//            if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
//                //user has successfully picked an image
//                //save reference to a Uri variable
//                pickedImgUri = data.getData();
//                userProfilePic.setImageURI(pickedImgUri);
//            }
//        }


//    }
    }
}
