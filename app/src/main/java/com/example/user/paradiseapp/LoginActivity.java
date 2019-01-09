package com.example.user.paradiseapp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener  {

    private EditText et_account,et_password;
    private Button bt_login,bt_register,bt_google;
    private RequestQueue requestQueue;
    private StringRequest request;
    private String stringAccount,stringPassword,googleAccount;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private static final int SIDN_IN_CODE = 777;
    private CheckBox checkBox_password; //checkBox_account,
    private SharedPreferences rememberAccount,keepaccount;
    private final String url = "http://59.126.54.198/MeSunApp/Model/login.php";
    private String result;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rememberAccount = getSharedPreferences("ACCOUNT",MODE_PRIVATE);
        keepaccount =getSharedPreferences("KEEPACCOUNT",MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        et_account=(EditText)findViewById(R.id.et_account);
        et_password=(EditText)findViewById(R.id.et_password);

        CHECKBOX();

        bt_login=(Button)findViewById(R.id.bt_login);
        bt_register=(Button)findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringAccount = et_account.getText().toString().trim();
                stringPassword = et_password.getText().toString().trim();
                rememberAccount.edit() //儲存帳號 ，用於checkbox
                        .putString("Account",stringAccount)
                        .putString("Password",stringPassword)
                       .commit();

                PostforVolley();
             //PostforAsync
            //    String task = "login";
            //    //String task = "test";
            //   BackgroundTask backgroundTask = new BackgroundTask(LoginActivity.this);
            //    backgroundTask.execute(task,stringAccount,stringPassword);
           }

        });

        googlesign();

    }

    //會員登入API
    private void PostforVolley() {

        //置入要POST的資料
        HashMap data = new HashMap();
        data.put("Account",stringAccount);
        data.put("Password",stringPassword);

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(data), //POST格式設為JSON
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject1 = response.getJSONObject("Content");
                    //JSONObject jsonObject2 = jsonObject1.getJSONObject("Message");
                     result = jsonObject1.getString("Message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //將登入成功的帳號存入keepaccount  之後再用此資料判斷自動登入、會員卡權限等等
                keepaccount.edit()
                        .putString("keepAccount",stringAccount)
                        .commit();

                progressDialog.dismiss();
                //如果API回傳值為登入成功 進入MainActivity
                if(result.equals("登入成功")){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 把上一頁Activity清空
                    startActivity(intent);
                }
                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("載入中");
        progressDialog.show();
    }

    //Google快速登入
    public void googlesign(){
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

       googleApiClient = new GoogleApiClient.Builder(this)
               .enableAutoManage(this,0,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API ,gso)
                .build();

       //GOOGLE客製化按鈕
        bt_google = (Button)findViewById(R.id.bt_google1);
        bt_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, SIDN_IN_CODE);
            }
        });

        //GOOGLE內建按鈕
      //  signInButton = (SignInButton) findViewById(R.id.bt_google);
       // signInButton.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
       //         Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
       //         startActivityForResult(intent,SIDN_IN_CODE);
       //     }
     //   });
}

    //Google快速登入回傳結果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIDN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //取得GOOGLE 帳號
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            googleAccount = googleSignInAccount.getEmail();
            Toast.makeText(this,googleAccount,Toast.LENGTH_LONG).show();
            keepaccount.edit()
                    .putString("keepAccount",googleAccount)
                    .commit();

            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //消除前面的Activity
            startActivity(intent);
        }else {
            Toast.makeText(this,"not_login",Toast.LENGTH_LONG);
        }
    }

    //前往會員註冊
    public void register(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //記住我 checkbox
   private void CHECKBOX(){
   //    checkBox_account=(CheckBox)findViewById(R.id.checkBox_account);
   //
   //    //帳號
   //    //記錄是否有勾選
   //    if(rememberAccount.getBoolean("isCheck_account",false)){
   //        checkBox_account.setChecked(true);
   //    }else {
   //        checkBox_account.setChecked(false);
   //    }
//
   //    checkBox_account.setOnClickListener(new View.OnClickListener() {
   //        @Override
   //        public void onClick(View v) {
   //            if(checkBox_account.isChecked()) {
   //                rememberAccount.edit()
   //                        .putBoolean("isCheck_account", true)
   //                        .commit();
   //            }else {
   //                rememberAccount.edit()
   //                        .putBoolean("isCheck_account", false)
   //                        .commit();
   //            }
   //        }
   //    });
   //    //如果上次登入時有勾選，就顯示rememberAccount紀錄的帳號
   //    if(rememberAccount.getBoolean("isCheck_account",false)) {
   //        et_account.setText(rememberAccount.getString("Account", ""));
   //    }
//
       //帳號、密碼
       checkBox_password=(CheckBox)findViewById(R.id.checkBox_password);
       //判斷 checkBox之前是否有勾選
       if(rememberAccount.getBoolean("isCheck_password",false)){ //若"isCheck_password"記錄為true，checkBox顯示為已勾選狀態，若為空值（第一次進入）則取得false
           checkBox_password.setChecked(true);
       }else {
           checkBox_password.setChecked(false);
       }

       checkBox_password.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(checkBox_password.isChecked()) {//checkBox若打勾，"isCheck_password"寫入true
                   rememberAccount.edit()
                           .putBoolean("isCheck_password", true)
                           .commit();
               }else {                              //反之，"isCheck_password"寫入false
                   rememberAccount.edit()
                           .putBoolean("isCheck_password", false)
                           .commit();
               }
           }
       });
       //如果上次登入時有勾選，就顯示rememberAccount紀錄的帳號、密碼
       if(rememberAccount.getBoolean("isCheck_password",false)) {
           et_account.setText(rememberAccount.getString("Account", ""));
           et_password.setText(rememberAccount.getString("Password", ""));
       }

   }

}
