package com.example.user.paradiseapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;

public class BackgroundTask extends AsyncTask<String,Integer,String> {
    SharedPreferences preferences,keepaccount;
    SharedPreferences.Editor editor;
    AlertDialog alertDialog;
    Context context;
    String result_account;
    String loginAccount,loginPassword;


    private ProgressDialog progressDialog;

    BackgroundTask(Context ctx){
        this.context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        preferences = context.getSharedPreferences("MYPREFS", MODE_PRIVATE);

        editor = preferences.edit();
        editor.putString("flag","0");
        editor.commit();


        String urltest = "http://59.126.54.198/sample/insert.php";
        String urlRegistration = "http://59.126.54.198/sample/register_test.php";
        String urlLogin  = "http://59.126.54.198/sample/login_test.php";
        String urlCheckRegister = "http://59.126.54.198/checkregister.php";
        String task = params[0];

        if(task.equals("test")){

            //  String regName = params[1];
            String testAccount = params[1];

            try {
                URL url = new URL(urltest);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData =
                        URLEncoder.encode("post_regAccount","UTF-8")+"="+URLEncoder.encode(testAccount,"UTF-8");
                bufferedWriter.write(myData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                //get response from the database
                //取得php回傳值
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8"); //"iso-8859-1"
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String dataResponse = "";
                String inputLine = "";
                while((inputLine = bufferedReader.readLine()) != null){
                    dataResponse += inputLine;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                progressDialog.dismiss();
                return "register";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (task.equals("checkregister")){

            String checkAccount= params[1];

                try {
                    URL url = new URL(urlCheckRegister);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    String checkData = URLEncoder.encode("post_regAccount","UTF-8")+"="+URLEncoder.encode(checkAccount,"UTF-8");
                    bufferedWriter.write(checkData);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String dataResponse = "";
                    String inputLine = "";
                    while((inputLine = bufferedReader.readLine()) != null){
                        dataResponse += inputLine;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();



                    //SharedPreferences
                    editor.putString("flag","checkregister");
                    editor.commit();
                    return  dataResponse;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }




        if(task.equals("register")){


            String regName = params[1];
            String regAccount = params[2];
            String regPassword = params[3];
            String regPasswordcheck = params[4];
            String regSex = params[5];
            String regPhone = params[6];
            String regArea = params[7];
            String regLocation = params[8];


            try {
                URL url = new URL(urlRegistration);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData =
                        URLEncoder.encode("post_regName","UTF-8")+"="+URLEncoder.encode(regName,"UTF-8")+"&"+
                        URLEncoder.encode("post_regAccount","UTF-8")+"="+URLEncoder.encode(regAccount,"UTF-8")+"&"
                        +URLEncoder.encode("post_regPassword","UTF-8")+"="+URLEncoder.encode(regPassword,"UTF-8")+"&"
                        +URLEncoder.encode("post_regPasswordcheck","UTF-8")+"="+URLEncoder.encode(regPasswordcheck,"UTF-8")+"&"
                        +URLEncoder.encode("post_regSex","UTF-8")+"="+URLEncoder.encode(regSex,"UTF-8")+"&"
                        +URLEncoder.encode("post_regPhone","UTF-8")+"="+URLEncoder.encode(regPhone,"UTF-8")+"&"
                        +URLEncoder.encode("post_regArea","UTF-8")+"="+URLEncoder.encode(regArea,"UTF-8")+"&"
                        +URLEncoder.encode("post_regLocation","UTF-8")+"="+URLEncoder.encode(regLocation,"UTF-8");
                bufferedWriter.write(myData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                //get response from the database
                //取得php回傳值
              InputStream inputStream = httpURLConnection.getInputStream();

              //test
               InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8"); //"iso-8859-1"
              BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
               String dataResponse = "";
              String inputLine = "";
               while((inputLine = bufferedReader.readLine()) != null){
                   dataResponse += inputLine;
               }
              bufferedReader.close();
               inputStream.close();
               httpURLConnection.disconnect();
//

                editor.putString("flag","register");
                editor.commit();
                return dataResponse;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(task.equals("login")){
          //  int progress =0;
          //  while(progress<=100){
             //  try {
               //     Thread.sleep(100);//為了模擬檔案傳輸速度, 所以故意設定0.05秒就睡一下,
             //   } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
             //       e.printStackTrace();
             //  }
            //    publishProgress(Integer.valueOf(progress));
            //    progress++;
          //  }

             loginAccount = params[1];
            loginPassword = params[2];
            try {

                URL url = new URL(urlLogin);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                //send the email and password to the database
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData = URLEncoder.encode("post_loginAccount","UTF-8")+"="+URLEncoder.encode(loginAccount,"UTF-8")+"&"
                        +URLEncoder.encode("post_loginPassword","UTF-8")+"="+URLEncoder.encode(loginPassword,"UTF-8");
                bufferedWriter.write(myData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //get response from the database
                //取得php回傳值
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8"); //"iso-8859-1"
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String dataResponse = "";
                String inputLine = "";
                while((inputLine = bufferedReader.readLine()) != null){
                    dataResponse += inputLine;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                //SharedPreferences
                editor.putString("flag","login");
                editor.commit();
                return  dataResponse;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
      alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("登入狀態");

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        //progressDialog.setCancelable(false);
       // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  // 跑100%
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //圓圈圈

        progressDialog.setCancelable(true); //返回鍵可關閉
        progressDialog.show();



    }

    //This method willbe called when doInBackground completes... and it will return the completion string which
    //will display this toast.
    @Override
    protected void onPostExecute(String result) {
        //String result 就是doInBackground return 的值

        String flag = preferences.getString("flag","0"); //若資料不在　回傳０

        if(flag.equals("register")) {

            //Toast.makeText(context,"恭喜您已成功成為秘境之商會員",Toast.LENGTH_LONG).show();
           if(result.equals("Registered_successful")) {
               Toast.makeText(context,"註冊成功", Toast.LENGTH_LONG).show();
               progressDialog.dismiss();
               Intent intent = new Intent(context,LoginActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//設置不要刷新將要跳到的Activity
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 把上一頁Activity清空

               context.startActivity(intent);
           }
        }
        if(flag.equals("login")) {
            if(result.equals("Account_error")){
                progressDialog.dismiss();
                alertDialog.setMessage("帳號輸入錯誤");
                alertDialog.show();

            }else if (result.equals("Password_errorLogin_unsuccessful")){
                progressDialog.dismiss();
                alertDialog.setMessage("密碼輸入錯誤");
                alertDialog.show();
            }else if (result.equals("Login_unsuccessful")) {
                progressDialog.dismiss();
                alertDialog.setMessage("帳號密碼輸入錯誤");
                alertDialog.show();
            }


            if (result.equals("Login_successful")) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                Toast.makeText(context,"登入成功!",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

                //將登入成功的帳號存入keepaccount  之後再用此資料判斷自動登入、會員卡權限等等
                keepaccount=context.getSharedPreferences("KEEPACCOUNT",MODE_PRIVATE);
                keepaccount.edit()
                        .putString("keepAccount",loginAccount)
                        .commit();

            }

        }
        if (flag.equals("checkregister")){
            if (result.equals("Can_Registered")){
                Toast.makeText(context,"此Email可以使用",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }else if (result.equals("Already_Registered")){
                Toast.makeText(context,"此Email已被使用，請做更換後再嘗試驗證",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);

    }

    public void display(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
