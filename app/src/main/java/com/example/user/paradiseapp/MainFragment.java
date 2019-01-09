package com.example.user.paradiseapp;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment  {




    public MainFragment() {
        // Required empty public constructor
    }

    private ViewPager vpager_one;
    private ArrayList<View> aList;
    private LayoutInflater li;
    private MyPagerAdapter mAdapter;
    private int prePosition = 0;
    private WebView main_webview;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout; //刷新webview
    private SimpleDraweeView draweeView;

    private boolean isDragging = false;

/*
    //自動跳下一個畫面
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = vpager_one.getCurrentItem() ;// 自動跳下個畫面 :  int item = vpager_one.getCurrentItem()+1;
            vpager_one.setCurrentItem(item);
            handler.sendEmptyMessageDelayed(0, 3000);
            }
    };
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        // Inflate the layout for this fragment
           View v = inflater.inflate(R.layout.fragment_main, container, false);
    /*輪播
           vpager_one = (ViewPager) v.findViewById(R.id.viewpager);
        aList = new ArrayList<View>();
        li = getLayoutInflater();
        aList.add(li.inflate(R.layout.view_one, null, false));
        aList.add(li.inflate(R.layout.view_two, null, false));
        aList.add(li.inflate(R.layout.view_three, null, false));
        mAdapter = new MyPagerAdapter(aList, handler, vpager_one);
        vpager_one.setAdapter(mAdapter);
        vpager_one.addOnPageChangeListener(new MyOnPagerChangeListener());
       // int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % aList.size();
       // vpager_one.setCurrentItem(item);
        handler.sendEmptyMessageDelayed(0, 3000);
        */
    /*
    網頁刷新
    progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
    main_webview = (WebView)v.findViewById(R.id.main_webview);
    swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_contain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Loadwebview();
            }
        });
        Loadwebview();
        */

        //Fresco
   // draweeView = (SimpleDraweeView) v.findViewById(R.id.drawee);
   //     Uri uri = Uri.parse("");
   //    draweeView.setImageURI(uri);
            return v;

        }

        private void Loadwebview(){
            main_webview.setVisibility(View.INVISIBLE);
            main_webview.loadUrl("http://192.168.31.116/wordpress/");//""https://www.google.com.tw/webhp?hl=zh-TW&sa=X&ved=0ahUKEwiV4fXG2dDeAhVFQd4KHRqDB6YQPAgH
           // swipeRefreshLayout.setRefreshing(true);
            main_webview.setWebChromeClient(new WebChromeClient());
            main_webview.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                    main_webview.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);  //關掉圈圈
                }

            //        @Override
            //        public void onReceivedError (WebView view, WebResourceRequest
            //        request, WebResourceError error){
            //        super.onReceivedError(view, request, error);
            //        Toast.makeText(getActivity(), "網路連線發生問題" + error, Toast.LENGTH_LONG).show();

            //    }

            });

            WebSettings webSettings = main_webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBuiltInZoomControls(true);// support zoom
            webSettings.setUseWideViewPort(true);//填滿螢幕
            webSettings.setLoadWithOverviewMode(true);

        }

        //按下返回鍵  webview 回上一頁  若已無法回上一頁 則回傳false到MainActivity
  //  public boolean onmyKeyDown(int keyCode, KeyEvent event) {
  //      if (keyCode == event.KEYCODE_BACK && main_webview.canGoBack()) {
  //          main_webview.goBack();
  //          return true;
  //      }
  //      return main_webview.canGoBack();
  //  }



/*輪播
        class MyOnPagerChangeListener implements ViewPager.OnPageChangeListener {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                int relPosition = position % aList.size();
                prePosition = relPosition;



            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                    isDragging = false;
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(0, 3000);
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    isDragging = true;
                    handler.removeCallbacksAndMessages(null);
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                }


            }

        }
        */

    }


