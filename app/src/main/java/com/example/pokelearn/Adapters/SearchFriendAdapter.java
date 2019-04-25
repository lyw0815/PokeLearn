package com.example.pokelearn.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.pokelearn.R;
import java.util.ArrayList;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder> {

    Context context;
    ArrayList<String> friendIdList;
    ArrayList<String> friendNameList;
    ArrayList<String> friendEmailList;
    ArrayList<String> friendImgList;

    class SearchFriendViewHolder extends RecyclerView.ViewHolder{
        TextView friend_name,friend_email;
        ImageView friend_img;
        RelativeLayout friend_combo;

        public SearchFriendViewHolder (View itemView){
            super((itemView));
            friend_name = itemView.findViewById(R.id.user_single_name);
            friend_email = itemView.findViewById(R.id.user_single_email);
            friend_img  = itemView.findViewById(R.id.user_single_img);
            friend_combo = itemView.findViewById(R.id.friend_combo);
        }
    }

    public SearchFriendAdapter(Context context, ArrayList<String> friendIdList, ArrayList<String> friendNameList, ArrayList<String> friendEmailList, ArrayList<String> friendImgList){
        this.context = context;
        this.friendIdList = friendIdList;
        this.friendNameList = friendNameList;
        this.friendEmailList = friendEmailList;
        this.friendImgList = friendImgList;
    }

    @Override
    public SearchFriendAdapter.SearchFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.users_single_layout,parent,false);
        return new SearchFriendAdapter.SearchFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchFriendAdapter.SearchFriendViewHolder holder, final int position){

        holder.friend_name.setText(friendNameList.get(position));
        holder.friend_email.setText(friendEmailList.get(position));
        Glide.with(context).asBitmap().load(friendImgList.get(position)).into(holder.friend_img);

        holder.friend_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Profile =new Intent(context, com.example.pokelearn.Activities.Profile.class);
                Profile.putExtra("user_id", friendIdList.get(position));
                context.startActivity(Profile);
            }
        });
    }

    @Override
    public int getItemCount(){
        return friendNameList.size();
    }


}
