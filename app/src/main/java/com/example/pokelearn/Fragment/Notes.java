package com.example.pokelearn.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.example.pokelearn.R;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Notes extends Fragment {

    String pdfUrl;

    public Notes() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pdfUrl = getArguments().getString("Url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        WebView webView = v.findViewById(R.id.pdf_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        if (pdfUrl !=null) {
            try {
                pdfUrl = URLEncoder.encode(pdfUrl, "UTF-8");
                webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdfUrl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return v;
    }
}
