package com.example.pokelearn.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pokelearn.R;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Description extends Fragment {

    String desc;

    public Description() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        desc = getArguments().getString("Desc");
        Log.e("Description frag ",desc);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_description, container, false);

        TextView description = (TextView) v.findViewById(R.id.desc_field);

        description.setText(desc);


        return v;
    }
}
