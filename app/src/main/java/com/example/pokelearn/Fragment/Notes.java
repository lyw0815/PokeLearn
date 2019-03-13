package com.example.pokelearn.Fragment;

import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.pokelearn.Activities.Learn;
import com.example.pokelearn.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Notes extends Fragment {

    String pdfUrl;

    private FragmentActivity myContext;



    public Notes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pdfUrl = getArguments().getString("Url");

        Toast.makeText(getContext(), "IN Notes"+ pdfUrl,Toast.LENGTH_SHORT).show();

        View v = inflater.inflate(R.layout.fragment_notes, container, false);

//        String data = getArguments().getString("bundle");

//        Log.d("ERR///", data);


        WebView webView = v.findViewById(R.id.pdf_web_view);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

//        pdfUrl = "https://firebasestorage.googleapis.com/v0/b/pokelearn-9cf23.appspot.com/o/LearningMaterials%2F1552311681163.pdf?alt=media&token=ffb120cd-cb43-4adb-b7c7-0a29c1c233b6";

//        try {
//            pdfUrl= URLEncoder.encode(pdfUrl,"UTF-8");
//            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdfUrl);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        return v;
    }





}
