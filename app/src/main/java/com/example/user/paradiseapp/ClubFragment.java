package com.example.user.paradiseapp;


import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubFragment extends Fragment {


    public ClubFragment() {
        // Required empty public constructor
    }

    // 輪播功能 https://hk.saowen.com/a/405081a0b1fb6034e4628a63dc197124b111d0ddedc4ec9e4f7f27fdbecfa417
    private List<ImageView> imageslist;
    private ViewPager viewpager_club;
    //private TextView mTvPagerTitle; //標題
    //private String[] mImageTitles;//標題集合
    private int previousPosition = 0, newPosition; //前一個被選中的position
    //private boolean isStop = false;//線程是否停止
    //private static int PAGER_TIME = 5000;//間隔時間
    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4, R.id.pager_image5};
    private List<String> imageurl_list = new ArrayList<>();
    private String downloadurl;
    private View view;
    private int count=0; //判斷imageslist增加次數
    // @BindView(R.id.dot_club)
    private LinearLayout dot_club;
    private static String imageUrl = "http://59.126.54.198/MeSunApp/Model/ChamberIntroduction.php";
    private RequestQueue requestQueue;
    private ProgressDialog  progressDialog;
    private ViewPagerAdapter viewPagerAdapter;
    //private PhotoView photoView;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_club, container, false);

        //autoPlayView();//開啟線程，自動播放

        //可放大圖片
        // photoView = (PhotoView) view.findViewById(R.id.photoview);
        // photoView.setImageResource(R.drawable.clubbg_compressor);
       // int size = imageslist.size();



        imageslist = new ArrayList<>();//建立imageslist來存放下載的view
        loadImage();//下載圖片
       // initData();//初始化數據

        return view;
    }



    //下載圖片
    private void loadImage() {
        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, imageUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //拆JSON
                    JSONObject arr = response.getJSONObject("Content");
                    JSONObject arrr = arr.getJSONObject("ImageData");
                    final JSONArray arrrr = arrr.getJSONArray("商會介紹");
                    for (int i = 0; i < arrrr.length(); i++) {
                        //從JSONArray抓圖片url 到imageurl_list
                        String s = arrrr.getString(i);
                        imageurl_list.add(s);
                    }
                    //透過url load圖片
                    //loadImage2();
                    loadImage3();
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


    private void loadImage3() {
        //解決用Imagerequest下載圖片順序會錯亂的問題
        for (int j=0;j<imageurl_list.size();j++ ) {
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext())
                    .load(imageurl_list.get(j))
                    .into(imageView);
            imageslist.add(imageView);
        }
        initView();
        progressDialog.dismiss();
    }

    private void loadImage2() {

            //因為response的順序不會依照Imagerequestu的順序依序出來，所以只好用此愚蠢的方法，不過感覺也可以用Networkimadeview
            ImageRequest imageRequest = new ImageRequest(imageurl_list.get(0), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    ImageView iv = new ImageView(getContext());
                    iv.setImageBitmap(response);//設置圖片
                    imageslist.add(iv);

                    ImageRequest imageRequest = new ImageRequest(imageurl_list.get(1), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            ImageView iv = new ImageView(getContext());
                            iv.setImageBitmap(response);//設置圖片
                            imageslist.add(iv);

                            ImageRequest imageRequest = new ImageRequest(imageurl_list.get(2), new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    ImageView iv = new ImageView(getContext());
                                    iv.setImageBitmap(response);//設置圖片
                                    imageslist.add(iv);

                                    ImageRequest imageRequest = new ImageRequest(imageurl_list.get(3), new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap response) {
                                            ImageView iv = new ImageView(getContext());
                                            iv.setImageBitmap(response);//設置圖片
                                            imageslist.add(iv);

                                            ImageRequest imageRequest = new ImageRequest(imageurl_list.get(4), new Response.Listener<Bitmap>() {
                                                @Override
                                                public void onResponse(Bitmap response) {
                                                    ImageView iv = new ImageView(getContext());
                                                    iv.setImageBitmap(response);//設置圖片

                                                    imageslist.add(iv);

                                                    initView();//初始化View，設置適配器(須先把圖片下載好 才能開始配置viewpager)
                                                    progressDialog.dismiss();

                                                }
                                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    progressDialog.dismiss();
                                                }
                                            });
                                            requestQueue.add(imageRequest); //加入請求佇列 完成

                                        }
                                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                        }
                                    });
                                    requestQueue.add(imageRequest); //加入請求佇列 完成

                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                }
                            });
                            requestQueue.add(imageRequest); //加入請求佇列 完成

                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                        }
                    });
                    requestQueue.add(imageRequest); //加入請求佇列 完成
                   // count++; //計算圖片load次數
                   // if (count == imageurl_list.size()) { //次數達到url陣列數量時 開始執行Viewpagr
                        //initView();//初始化View，設置適配器(須先把圖片下載好 才能開始配置viewpager)
                        //progressDialog.dismiss();
                  //  }
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            });
            requestQueue.add(imageRequest); //加入請求佇列 完成
        }



    //初始化數據
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void initData() {

        //mTvPagerTitle = (TextView) view.findViewById(R.id.tv_pager_title);


        //初始化標題列表和圖片
        // mImageTitles = new String[]{"1", "2","3","4","5"};
        // mTvPagerTitle.setText(mImageTitles[0]);
      //  int[] imageRess = new int[]{R.drawable.club_1, R.drawable.club_2, R.drawable.club_3, R.drawable.club_4, R.drawable.club_5};

        //(從專案)添加圖片到圖片列表裏
      //  imageslist = new ArrayList<>();
      //  ImageView iv;
      //  for (int i = 0; i < imageRess.length; i++) {
      //      iv = new ImageView(getActivity());
      //      iv.setBackgroundResource(imageRess[i]);//設置圖片
      //      iv.setId(imgae_ids[i]);//順便給圖片設置id
      //      iv.setOnClickListener(new pagerImageOnClick());//設置圖片點擊事件
      //      imageslist.add(iv);

      //  }

    }

    //設置輪播View
    public void initView() {
        viewpager_club = (ViewPager) view.findViewById(R.id.viewpager_club);
        dot_club = (LinearLayout) view.findViewById(R.id.dot_club);

        //小點點
        for (int i = 0; i < imageslist.size(); i++) {
            //生成對應圖片數量的點點數量(佈局,結果提供)
            dot_club.addView(getLayoutInflater().inflate(R.layout.dot, null));
        }
        //預設第一頁小點點  //getChildAt(0) 取得第0個layout.dot,把當中的v_dot改成dot_selected
        dot_club.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.ic_dot_selected);
        //Viewpager配置

    viewPagerAdapter = new ViewPagerAdapter(imageslist, viewpager_club);
    viewpager_club.setAdapter(viewPagerAdapter);
    //viewPagerAdapter.notifyDataSetChanged(); //告知PagerAdapter更新

        viewpager_club.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                newPosition = position;

                // 切換當前的title
                // mTvPagerTitle.setText(mImageTitles[position]);

                //小點點切換
                for (int i = 0; i < imageurl_list.size(); i++) {
                    if (i == position) {
                        //將當前切換的小點點設為drawable.ic_dot_selected
                        dot_club.getChildAt(newPosition).findViewById(R.id.v_dot)
                                .setBackgroundResource(R.drawable.ic_dot_selected);
                    } else {
                        //將原本的小點點改為drawable.ic_dot_normal
                        dot_club.getChildAt(previousPosition).findViewById(R.id.v_dot)
                                .setBackgroundResource(R.drawable.ic_dot_normal);
                    }
                }
                // 把當前的索引賦值給前一個索引變量, 方便下一次再切換.
                previousPosition = newPosition;
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    //ViewPager配置器
        private static class ViewPagerAdapter extends PagerAdapter {

        List<ImageView> Imageslist;
        ViewPager viewpager;

        public ViewPagerAdapter(List<ImageView> imageslist, ViewPager viewpager_club) {
            this.Imageslist = imageslist;
            this.viewpager = viewpager_club;
        }

      //  @Override
      //  public int getItemPosition(@NonNull Object object) {
//
      //      return POSITION_NONE; //不回傳現在的頁面position，讓viewpager重新去載入每個頁面的view
      //  }

        @Override
        public int getCount() {
            return Imageslist.size();//Integer.MAX_VALUE;//返回一個無限大的值，可以 無限循環
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
            ImageView iv = Imageslist.get(position );
            //viewpager.addView(iv);
            // 把當前添加ImageView返回回去.
            container.addView(iv);
            return iv;
        }

        /**
         * 銷燬一個條目
         * position 就是當前需要被銷燬的條目的索引
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 把ImageView從ViewPager中移除掉
           //viewpager.removeView(Imageslist.get(position));
            container.removeView(Imageslist.get(position)); //(View)object
        }


        @Override
        public void notifyDataSetChanged() {
            // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新

            super.notifyDataSetChanged();
      }

    }


    //圖片點擊事件
    private class pagerImageOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pager_image1:
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(getContext(), "圖片2被點擊", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(getContext(), "圖片3被點擊", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image4:
                    Toast.makeText(getContext(), "圖片4被點擊", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image5:
                    Toast.makeText(getContext(), "圖片5被點擊", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}
