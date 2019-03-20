package com.example.pokelearn.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Fragment.Description;
import com.example.pokelearn.Fragment.Notes;
import com.example.pokelearn.Fragment.Video;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class I_ChapterDetails extends AppCompatActivity {

    private String url, vid, desc;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    FloatingActionButton fab_more, fab_edit, fab_delete;
    Animation FabOpen, FabClose, FabClockwise, Fabanticlockwise;
    Dialog deleteDialog;
    Button deleteYes, deleteNo;
    ImageView closePopupImg;
    TextView titlePopUp, messagePopUp;
    boolean isOpen = false;
    boolean del_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i__chapter_details);

        Intent i = getIntent();
        final String ChapterId = i.getStringExtra("ChapterId");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");
        Intent k = getIntent();
        final String CourseName = k.getStringExtra("CourseName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.chapterDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab_more = (FloatingActionButton)findViewById(R.id.fab_more);
        fab_edit = (FloatingActionButton)findViewById(R.id.fab_edit);
        fab_delete = (FloatingActionButton)findViewById(R.id.fab_delete);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        Fabanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        deleteDialog = new Dialog(this);
        del_flag = false;

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    fab_edit.startAnimation(FabClose);
                    fab_delete.startAnimation(FabClose);
                    fab_more.startAnimation(Fabanticlockwise);
                    fab_delete.setClickable(false);
                    fab_edit.setClickable(false);
                    isOpen = false;
                }
                else{
                    fab_edit.startAnimation(FabOpen);
                    fab_delete.startAnimation(FabOpen);
                    fab_more.startAnimation(FabClockwise);
                    fab_edit.setClickable(true);
                    fab_delete.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditChapter = new Intent(getApplicationContext(), EditChapter.class);
                EditChapter.putExtra("ChapterId", ChapterId );
                EditChapter.putExtra("CourseId", CourseId );
                EditChapter.putExtra("CourseName", CourseName );
                startActivity(EditChapter);
                finish();
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDeletePopup(ChapterId, CourseId, CourseName);
            }
        });

        if (del_flag == false) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("Chapters").child(CourseId).child(ChapterId);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String chapter_url = dataSnapshot.child("chapterMaterialUrl").getValue(String.class);
                    String chapter_vid = dataSnapshot.child("chapterYoutubeVideoId").getValue(String.class);
                    String chapter_desc = dataSnapshot.child("chapterDesc").getValue(String.class);
                    String chapter_title = dataSnapshot.child("chapterTitle").getValue(String.class);
                    url = chapter_url;
                    vid = chapter_vid;
                    desc = chapter_desc;
                    getSupportActionBar().setTitle(chapter_title);
                    callTab();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

    public void ShowDeletePopup(final String chapter_id, final String course_id, final String course_name){
        deleteDialog.setContentView(R.layout.pop_up_dialog);
        closePopupImg = (ImageView) deleteDialog.findViewById(R.id.closePopUpImg);
        deleteYes = (Button) deleteDialog.findViewById(R.id.btnDeleteYes);
        deleteNo = (Button) deleteDialog.findViewById(R.id.btnDeleteNo);
        titlePopUp = (TextView) deleteDialog.findViewById(R.id.titlePopUp);
        messagePopUp = (TextView) deleteDialog.findViewById(R.id.messagePopUp);

        closePopupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();

        deleteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChapter(chapter_id, course_id, course_name);
            }
        });

        deleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

    }

    private void deleteChapter(String ChapterId, String CourseId, String CourseName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Chapters").child(CourseId).child(ChapterId);
        ref.removeValue();
        Toast.makeText(this, "Chapter deleted successfully", Toast.LENGTH_SHORT).show();
        del_flag = true;
        deleteDialog.dismiss();
        I_ChapterList.iChapterList.finish();
        Intent I_ChapterList =new Intent(getApplicationContext(), I_ChapterList.class);
        I_ChapterList.putExtra("CourseName", CourseName );
        I_ChapterList.putExtra("CourseId", CourseId );
        startActivity(I_ChapterList);
        finish();
    }

    public void callTab() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_i__chapter_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() { }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_i__chapter_details, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0 :
                    fragment = new Description();
                    Bundle bundleDesc = new Bundle();
                    bundleDesc.putString("Desc", desc);
                    fragment.setArguments(bundleDesc);
                    break;
                case 1:
                    fragment = new Notes();
                    Bundle bundleNotes = new Bundle();
                    bundleNotes.putString("Url",url);
                    fragment.setArguments(bundleNotes);
                    break;
                case 2:
                    fragment = new Video();
                    Bundle bundleVideo = new Bundle();
                    bundleVideo.putString("Vid", vid);
                    fragment.setArguments(bundleVideo);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {return 3;}


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Description";
                case 1:
                    return "Notes";
                case 2:
                    return "Video";
            }
            return null;
        }
    }
}
