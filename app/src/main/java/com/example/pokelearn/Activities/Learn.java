package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class Learn extends AppCompatActivity  {

    private String url, vid, desc;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Intent i = getIntent();
        final String ChapterId = i.getStringExtra("ChapterId");
        Intent j = getIntent();
        final String CourseId = j.getStringExtra("CourseId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.learnToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                                          Log.d("url",url);
                                          Log.e("desc",desc);
                                                  getSupportActionBar().setTitle(chapter_title);
                                                  callTab();
                                      }
                                      @Override
                                      public void onCancelled(@NonNull DatabaseError databaseError) { }
                                  });
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
        getMenuInflater().inflate(R.menu.menu_learn, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_learn, container, false);
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
        public int getCount() {
            return 3;
        }

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
