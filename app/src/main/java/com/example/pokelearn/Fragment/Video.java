package com.example.pokelearn.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.pokelearn.Activities.Learn;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import com.example.pokelearn.R;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
//import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener;


public class Video extends Fragment {

    String videoId;

    public Video() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        videoId = getArguments().getString("Vid");

        View v = inflater.inflate(R.layout.fragment_video, container, false);

//        String data = getArguments().getString("bundle2");




        String frameVideo = "<html><body><iframe width=\"400\" height=\"220\" src=\"https://www.youtube.com/embed/";
//        frameVideo = frameVideo + "SiQP80HIrrw";
        frameVideo = frameVideo + videoId;
        frameVideo = frameVideo + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";

        WebView displayYoutubeVideo = v.findViewById(R.id.video_web_view);


        //////////This works//////////////

//        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }
//        });
//        WebSettings webSettings = displayYoutubeVideo.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");

        //////////End of THIS WORKS ////////////





        ///////ANOTHER TRY/////////

//        displayYoutubeVideo.setWebChromeClient(new MyChrome());
//
//        displayYoutubeVideo.getSettings().setJavaScriptEnabled(true);
//        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");

        /////END OF ANOTHER TRY??????????





        ////////// LAST TRY 》》》》》》》》》》》》》》》》》》》》

        displayYoutubeVideo.setWebViewClient(new Browser_home());
        displayYoutubeVideo.setWebChromeClient(new MyChrome());
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");

        ////////　END LAST TRY ??????????

        return v;
    }


//    private class MyChrome extends WebChromeClient {
//
//        private View mCustomView;
//        private WebChromeClient.CustomViewCallback mCustomViewCallback;
//        protected FrameLayout mFullscreenContainer;
//        private int mOriginalOrientation;
//        private int mOriginalSystemUiVisibility;
//
//        MyChrome() {}
//
//        public Bitmap getDefaultVideoPoster()
//        {
//            if (mCustomView == null) {
//                return null;
//            }
//            return BitmapFactory.decodeResource(getContext().getResources(), 2130837573);
//        }
//
//        public void onHideCustomView()
//        {
//            ((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
//            this.mCustomView = null;
//            this.mOriginalOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//            getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
//            getActivity().setRequestedOrientation(this.mOriginalOrientation);
//            this.mCustomViewCallback.onCustomViewHidden();
//            this.mCustomViewCallback = null;
//        }
//
//        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
//        {
//            if (this.mCustomView != null)
//            {
//                onHideCustomView();
//                return;
//            }
//            this.mCustomView = paramView;
//            this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
////            this.mOriginalOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
////            getActivity().setRequestedOrientation(this.mOriginalOrientation);
//            this.mOriginalOrientation = getActivity().getRequestedOrientation();
//            this.mCustomViewCallback = paramCustomViewCallback;
//            ((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
//            getActivity().getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//        }
//    }



    class Browser_home extends WebViewClient {
        Browser_home() {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            getActivity().setTitle(view.getTitle());
            super.onPageFinished(view, url);
        }
    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            getActivity().setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getActivity().getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

}
