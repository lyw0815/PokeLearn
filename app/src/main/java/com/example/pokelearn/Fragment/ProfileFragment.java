package com.example.pokelearn.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.pokelearn.R;


public class ProfileFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView userName, userMail;
    private Button btnUpdProfile, btnChgPassword;
    private ImageView userProfilePic;


    public ProfileFragment() { }

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

        userMail = v.findViewById(R.id.userMail);
        userName = v.findViewById(R.id.userName);
        btnUpdProfile = v.findViewById(R.id.btnUpdProfile);
        btnChgPassword = v.findViewById(R.id.btnChgPassword);
        userProfilePic = v.findViewById(R.id.profilePic);

        userName.setText(user.getDisplayName());
        userMail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(userProfilePic);

        btnUpdProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UpdateProfile = new Intent(getContext(), com.example.pokelearn.Activities.UpdateProfile.class);
                startActivity(UpdateProfile);
            }
        });


        btnChgPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UpdatePassword = new Intent(getContext(), com.example.pokelearn.Activities.UpdatePassword.class);
                startActivity(UpdatePassword);
            }
        });

        return v;


    }
}
