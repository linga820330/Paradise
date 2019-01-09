package com.example.user.paradiseapp;

import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.os.Handler;

public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<View> viewLists;
    private Handler handler;
    private ViewPager viewPager;

    public MyPagerAdapter() {
    }

    public MyPagerAdapter(ArrayList<View> viewLists, Handler handler,ViewPager viewPager) {
        super();
        this.viewLists = viewLists;
        this.handler = handler;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {

        return viewLists.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



    /**
     * 初始化一個條目
     * @param container
     * @param position 當前需要加載條目的索引
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view =viewLists.get(position);
         container.addView(view);

       //   int relPosition = position % viewLists.size();
       // final  View view  = viewLists.get(relPosition);

       // container.addView(view);


       // view.setOnTouchListener(new View.OnTouchListener() {
       //     @Override
        //    public boolean onTouch(View v, MotionEvent event) {
         //       switch (event.getAction()){
          //          case MotionEvent.ACTION_DOWN:
       //                 handler.removeCallbacksAndMessages(null);
        //                break;
         //           case MotionEvent.ACTION_UP:
          //              handler.removeCallbacksAndMessages(null);
        //                handler.sendEmptyMessageDelayed(0,3000);
         //              break;
          //     }
           //    return false;
      //      }
       // });

        //view.setTag(relPosition);
       // view.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View v) {
          //      int position = (int) view.getTag();
      //      }
       // });

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // container.removeView(viewLists.get(position));
        container.removeView((View)object);
    }
}