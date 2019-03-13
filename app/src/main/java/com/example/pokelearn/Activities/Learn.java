package com.example.pokelearn.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.pokelearn.Chapters;
import com.example.pokelearn.Fragment.Notes;
import com.example.pokelearn.Fragment.Video;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Learn extends AppCompatActivity  {

    private String url, vid;
//    public String ChapterId, CourseId;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Intent i = getIntent();
        final String ChapterId = i.getStringExtra("ChapterId");
//       ChapterId = i.getStringExtra("ChapterId");
        Intent j = getIntent();
        final String CourseId = i.getStringExtra("CourseId");
//        CourseId = i.getStringExtra("CourseId");
////
////        Intent i = getIntent();
////        url = i.getStringExtra("Url");
////        Intent j = getIntent();
////        vid = j.getStringExtra("Vid");
//
//
        Toolbar toolbar = (Toolbar) findViewById(R.id.learnToolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        getSupportActionBar().setTitle("Learn"); // setting a title for this Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(getApplicationContext(), "query LEARN"+ ChapterId,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "query LEARN courseid"+ CourseId,Toast.LENGTH_SHORT).show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Chapters").child(CourseId).child(ChapterId);

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                                          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                              String chapter_title = dataSnapshot.child("chapterTitle").getValue(Chapters.class).getChapterTitle();
                                              String chapter_url = dataSnapshot.child("chapterMaterialUrl").getValue(String.class);
                                              String chapter_vid = dataSnapshot.child("chapterYoutubeVideoId").getValue(String.class);
                                              String chapter_id = dataSnapshot.child("chapterId").getValue(String.class);

                                              Toast.makeText(getApplicationContext(), "IN "+ chapter_vid,Toast.LENGTH_SHORT).show();
//                                              if (chapter_id.equals(ChapterId)) {
                                                  url = chapter_url;
                                                  vid = chapter_vid;
                                                  callTab();
//                                              }
//                                          }
                                      }

                                      @Override
                                      public void onCancelled(@NonNull DatabaseError databaseError) {

                                      }
                                  });


//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    public void callTab() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_learn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         *
         */
        public String url, vid;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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

            //////////// FROM ON CREATE

            Intent i = getActivity().getIntent();
            final String ChapterId = i.getStringExtra("ChapterId");
//       ChapterId = i.getStringExtra("ChapterId");
            Intent j = getActivity().getIntent();
            final String CourseId = i.getStringExtra("CourseId");
//        CourseId = i.getStringExtra("CourseId");
//
//        Intent i = getIntent();
//        url = i.getStringExtra("Url");
//        Intent j = getIntent();
//        vid = j.getStringExtra("Vid");


            Toast.makeText(getContext(), "query LEARN"+ ChapterId,Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "query LEARN courseid"+ CourseId,Toast.LENGTH_SHORT).show();

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child(CourseId).child(ChapterId);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                                          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                              String chapter_title = dataSnapshot.child("chapterTitle").getValue(Chapters.class).getChapterTitle();
                    String chapter_url = dataSnapshot.child("chapterMaterialUrl").getValue(String.class);
                    String chapter_vid = dataSnapshot.child("chapterYoutubeVideoId").getValue(String.class);
                    String chapter_id = dataSnapshot.child("chapterId").getValue(String.class);

                    Toast.makeText(getContext(), "IN "+ chapter_url,Toast.LENGTH_SHORT).show();
//                                              if (chapter_id.equals(ChapterId)) {
                    url = chapter_url;
                    vid = chapter_vid;
//                                              }
//                                          }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            ///////END ONCREATE




            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new Notes();
                    Bundle bundleNotes = new Bundle();
                    bundleNotes.putString("Url",url);
                    fragment.setArguments(bundleNotes);
                    break;
                case 1:
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
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Notes";
                case 1:
                    return "Video";
            }
            return null;
        }


    }
}
