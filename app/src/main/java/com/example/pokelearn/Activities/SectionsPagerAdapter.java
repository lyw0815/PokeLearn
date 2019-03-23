package com.example.pokelearn.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pokelearn.Fragment.Chats;
import com.example.pokelearn.Fragment.Friends;
import com.example.pokelearn.Fragment.Requests;

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Requests requestsFragment = new Requests();
                return requestsFragment;
            case 1:
                Chats chats = new Chats();
                return chats;
            case 2:
                Friends friends = new Friends();
                return friends;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "REQUESTS";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";
            default:
                return null;
        }
    }
}
