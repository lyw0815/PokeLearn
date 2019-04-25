package com.example.pokelearn.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.example.pokelearn.Adapters.SearchFriendAdapter;
import com.example.pokelearn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class SearchFriend extends AppCompatActivity {

    EditText searchFriend_edit_text;
    RecyclerView searchFriend_recycle_view;
    DatabaseReference dbReference;
    ArrayList<String> friendIdList;
    ArrayList<String> friendNameList;
    ArrayList<String> friendEmailList;
    ArrayList<String> friendImgList;
    SearchFriendAdapter SearchFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.searchFriendToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchFriend_edit_text = (EditText) findViewById(R.id.searchFriend_editText);
        searchFriend_recycle_view = (RecyclerView) findViewById(R.id.searchFriend_recycleView);

        dbReference = FirebaseDatabase.getInstance().getReference();

        friendIdList = new ArrayList<>();
        friendNameList = new ArrayList<>();
        friendEmailList = new ArrayList<>();
        friendImgList = new ArrayList<>();

        searchFriend_recycle_view.setHasFixedSize(true);
        searchFriend_recycle_view.setLayoutManager(new LinearLayoutManager(this));
        searchFriend_recycle_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        searchFriend_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    friendIdList.clear();;
                    friendNameList.clear();
                    friendEmailList.clear();
                    friendImgList.clear();
                    searchFriend_recycle_view.removeAllViews();
                }
            }
        });
    }

    private void setAdapter(final String searchedString) {

        dbReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                friendIdList.clear();
                friendNameList.clear();
                friendEmailList.clear();
                friendImgList.clear();
                searchFriend_recycle_view.removeAllViews();

                int counter = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String friend_name = snapshot.child("userName").getValue(String.class);
                    String friend_id = snapshot.child("userId").getValue(String.class);
                    String friend_email = snapshot.child("userEmail").getValue(String.class);
                    String friend_img = snapshot.child("userImgUrl").getValue(String.class);

                    if (friend_name.toLowerCase().contains(searchedString)) {
                        friendIdList.add(friend_id);
                        friendNameList.add(friend_name);
                        friendEmailList.add(friend_email);
                        friendImgList.add(friend_img);
                        counter++;
                    }
                    if (counter == 15) { break;}
                }
                SearchFriendAdapter = new SearchFriendAdapter(SearchFriend.this, friendIdList, friendNameList, friendEmailList, friendImgList);
                searchFriend_recycle_view.setAdapter(SearchFriendAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
