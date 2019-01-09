package com.example.user.paradiseapp;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RmberFragment extends Fragment {

    private List<ImageView> imageViewList = new ArrayList<>();
    private List<String> imageUrl_List = new ArrayList<>();
    private ViewPager viewPager_rmber;
    private View view;
    private LinearLayout dot_rmber;
    private int previousPosition = 0,newPosition;
    private RequestQueue requestQueue;
    private static String imageUrl = "http://59.126.54.198/MeSunApp/Model/ChamberIntroduction.php";
    private int count=0; //判斷imageslist增加次數
    private ProgressDialog  progressDialog;
    public RmberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.fragment_rmber, container, false);


       getUrl();//取得圖片Url

        return view;
    }

    private void getUrl() {
        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, imageUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //拆JSON
                    JSONObject arr = response.getJSONObject("Content");
                    JSONObject arrr = arr.getJSONObject("ImageData");
                    final JSONArray arrrr = arrr.getJSONArray("會員制度");
                    for (int i = 0; i < arrrr.length(); i++) {
                        //從JSONArray抓圖片url 到imageurl_list
                        String s = arrrr.getString(i);
                        imageUrl_List.add(s);

                      //  imageViewList = new ArrayList<>();//建立imageslist來存放下載的view
                      //  ImageRequest imageRequest = new ImageRequest(s, new Response.Listener<Bitmap>() {
                      //      @Override
                      //      public void onResponse(Bitmap response) {
                      //          ImageView iv = new ImageView(getActivity());
                      //          iv.setImageBitmap(response);//設置圖片
                      //          imageViewList.add(iv);
//
                      //          count++; //計算圖片load次數
                      //          if (count==arrrr.length()){ //次數達到url陣列數量時 開始執行Viewpagr
                      //              setView();//初始化View，設置適配器(須先把圖片下載好 才能開始配置viewpager)
                      //              progressDialog.dismiss();
                      //          }
                      //      }
                      //  }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                      //      @Override
                      //      public void onErrorResponse(VolleyError error) {
                      //          progressDialog.dismiss();
                      //      }
                      //  });
                      //  requestQueue.add(imageRequest); //加入請求佇列 完成
                    }
                    //透過url load圖片
                    loadImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
        //initialize the progress dialog and show it 初始化progressDialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("載入中");
        progressDialog.show();
    }

    private void loadImage() {
        for(int i=0;i<imageUrl_List.size();i++){
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext())
                    .load(imageUrl_List.get(i))
                    .into(imageView);
            imageViewList.add(imageView);
        }
        setView();
        progressDialog.dismiss();
    }


    public void setView(){
        viewPager_rmber = (ViewPager) view.findViewById(R.id.viewpager_rmber);
        dot_rmber = (LinearLayout) view.findViewById(R.id.dot_rmber);
        //小點點
        for (int i=0;i<imageViewList.size();i++){
            //生成對應數量的點點
            dot_rmber.addView(getLayoutInflater().inflate(R.layout.dot,null));
        }
        //預設第一頁小點點
        dot_rmber.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.ic_dot_selected);

        //設置Viewpager配置器
        ViewPagerAdapter2 viewPagerAdapter = new ViewPagerAdapter2(imageViewList,viewPager_rmber);
        viewPagerAdapter.notifyDataSetChanged();
        viewPager_rmber.setAdapter(viewPagerAdapter);
        viewPager_rmber.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
             newPosition = position;
             for (int i=0;i<imageViewList.size();i++) {
                 if (i == position) {
                     dot_rmber.getChildAt(newPosition).findViewById(R.id.v_dot)
                             .setBackgroundResource(R.drawable.ic_dot_selected);
                 } else {
                     dot_rmber.getChildAt(previousPosition).findViewById(R.id.v_dot)
                             .setBackgroundResource(R.drawable.ic_dot_normal);
                 }
             }
                previousPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private static class ViewPagerAdapter2 extends PagerAdapter {


        List<ImageView> imageslist;
        ViewPager viewpager;

        public ViewPagerAdapter2(List<ImageView> imageslist, ViewPager viewpager_club) {
            this.imageslist = imageslist;
            this.viewpager = viewpager_club;
        }

        @Override
        public int getCount() {
            return imageslist.size();//Integer.MAX_VALUE;//返回一個無限大的值，可以 無限循環
        }



        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            /**
             * 判斷是否使用緩存, 如果返回的是true, 使用緩存. 不去調用instantiateItem方法創建一個新的對象
             */
            return view == o;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 把position對應位置的ImageView添加到ViewPager中
            ImageView iv = imageslist.get(position % imageslist.size());
            viewpager.addView(iv);
            // 把當前添加ImageView返回回去.

            return iv;
        }
        /**
         * 銷燬一個條目
         * position 就是當前需要被銷燬的條目的索引
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 把ImageView從ViewPager中移除掉
            viewpager.removeView(imageslist.get(position % imageslist.size()));

        }

        @Override
        public int getItemPosition(@NonNull Object object) {

            return POSITION_NONE;
        }
    }

}
