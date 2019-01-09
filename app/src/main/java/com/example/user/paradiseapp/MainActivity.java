package com.example.user.paradiseapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager vpager_one;
    private ArrayList<View> aList;
    private LayoutInflater li;
    private MyPagerAdapter mAdapter;
    private int prePosition = 0;
    private Button bt_activitymessage,bt_card;

    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    private long mExitTime;

    private boolean isDragging = false;
    private String account;
    private SharedPreferences keepaccount;
    private AlertDialog logoutDialog;
    private Fragment fg;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 200;
    private RequestQueue requestQueue;
    private final String cardID_url = "http://59.126.54.198/MeSunApp/Model/GetMemberCardID.php";
    private ProgressDialog progressDialog;

    //輪播自動跳下一個畫面
    // private Handler handler = new Handler(){
    //    @Override
    //   public void handleMessage(Message msg) {
    //      super.handleMessage(msg);
    //     int item = vpager_one.getCurrentItem()+1;
    //    vpager_one.setCurrentItem(item);
    //   handler.sendEmptyMessageDelayed(0,3000); // new Message or 0

    //  }
    //};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        requestQueue = Volley.newRequestQueue(this);


        fg = new  MainFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer, fg).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


       keepaccount=getSharedPreferences("KEEPACCOUNT",MODE_PRIVATE);
       //取得登入帳號，若沒登入則取得"0"
      account= keepaccount.getString("keepAccount", "0");

        Permission();


    }

    //查看有沒有CAMERA權限
    private void Permission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                //若沒有權限 就跳出權限請求 再從onRequestPermissionsResult取回結果
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允許
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //拒絕
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
            {
            super.onBackPressed();
        }
    }

    //連續點擊兩次返回鍵退出APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //判断用户是否點擊了“返回键”
       if (keyCode == KeyEvent.KEYCODE_BACK) {
           //先判定web是否可以返回
         //  if (fg instanceof MainFragment ) {
         //     // ((MainFragment) fg).onmyKeyDown(keyCode, event);
         //     boolean key= ((MainFragment) fg).onmyKeyDown(keyCode, event);// 執行MainFragment的onmyKeyDown 並判斷webView是否可以返回，
         //       if (key){                                               // 若可以 回傳true，若不行 回傳false，並往下繼續執行 。
         //           return true;
         //       }
         //  }
               //與上次點擊返回鍵時做時間差
               if ((System.currentTimeMillis() - mExitTime) > 2000) { //MainFragment不使用此功能 因為有webview
                   //大於2000ms則認為是不小心按到，使用Toast進行提示
                   Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                   //並記錄本次點擊“返回鍵”的時刻，以便下次進行判断
                   mExitTime = System.currentTimeMillis();
               } else {
                   //小於2000m則認為是用户確實希望退出程序-調用System.exit()方法進行退出
                   System.exit(0);
               }
               return true;
           }

           return super.onKeyDown(keyCode, event);

    }

    //設置側邊欄連結
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        //主介面左邊選單功能
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Member_data)
        {//會員資料
            if(account.equals("0")){
                Toast.makeText(this,"請先登入會員",Toast.LENGTH_LONG).show();
            }else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer, new Member_dataFragment()).commit();
            }
            toolbar_title.setText("會 員 資 料");
        }
        else if (id == R.id.system)
        {//商會介紹
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer,new SystemFragment()).commit();
            toolbar_title.setText("商 會 介 紹");
        }
        else if (id == R.id.call_we)
        {//連絡我們
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer,new ContactFragment()).commit();
                   // Intent Intent = new Intent(MainActivity.this,Contact.class);
                    //startActivity(Intent);
            toolbar_title.setText("聯 絡 我 們");
        }else if (id == R.id.Main)
        {//回首頁觸發跳轉到回首頁xml
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer,new MainFragment()).commit();
            toolbar_title.setText("首 頁");
        }else if (id == R.id.activity)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer,new act_listFragment()).commit();
            //Intent intent = new Intent(MainActivity.this,Activity_message.class);
            //startActivity(intent);
            toolbar_title.setText("活 動 報 名");

        }else if (id == R.id.card)
        {
           // Toast.makeText(this,account,Toast.LENGTH_LONG).show();
            //先判斷是否已登入會員(以SharedPreferences判斷)
            if(account.equals("0")){
                //若未登入 先式Tost
                Toast.makeText(this,"請先登入會員",Toast.LENGTH_LONG).show();
            }else {
                //若以登入 則利用帳號post CardID API 取得會員的卡片等級
                HashMap data = new HashMap();
                data.put("Account", account);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, cardID_url, new JSONObject(data), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject1 = response.getJSONObject("Content");
                            String Message = jsonObject1.getString("Message");
                            int Level = jsonObject1.getInt("Level");
                            String CardID = jsonObject1.getString("CardID");
                            //將會員等級存入SharedPreferences
                            keepaccount.edit()
                                    .putString("CardLevel", String.valueOf(Level))
                                    .commit();
                            progressDialog.dismiss();
                            if(Level==0){
                                Toast.makeText(MainActivity.this, Message, Toast.LENGTH_LONG).show();
                            }else
                            {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer,new CardFragment()).commit();
                                      toolbar_title.setText("會 員 卡 片");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
                requestQueue.add(jsonObjectRequest);
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("載入中");
                progressDialog.show();
            }
          //  if(account.equals("0")){
          //      Toast.makeText(this,"請先登入會員",Toast.LENGTH_LONG).show();
          //  }else {
          //      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer,new CardFragment()).commit();
          //      toolbar_title.setText("會 員 卡 片");
          //     // Intent intent = new Intent(MainActivity.this, CardActivity.class);
          //     // startActivity(intent);
          //  }
        }
        else if(id == R.id.consultation){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_comtainer,new ConsultationFragment()).commit();
            toolbar_title.setText("諮 詢 預 約");
        }
        else if(id == R.id.login_logout){
            if(account.equals("0")){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                //客製化Dialog
                AlertDialog.Builder logoutDialog = new AlertDialog.Builder(this);
                View custom_dialog = getLayoutInflater().inflate(R.layout.custom_dialog,null);
                Button dialog_bt_p =(Button) custom_dialog.findViewById(R.id.dialog_bt_p);
                Button dialog_bt_n =(Button) custom_dialog.findViewById(R.id.dialog_bt_n);
                logoutDialog.setView(custom_dialog);
                final AlertDialog dialog = logoutDialog.create();
                dialog.show();
                dialog_bt_p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keepaccount.edit().putString("keepAccount","0").commit();
                        account = keepaccount.getString("keepAccount","1");
                        Toast.makeText(MainActivity.this,"已登出",Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                dialog_bt_n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });



            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
