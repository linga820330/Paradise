package com.example.user.paradiseapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sumimakito.awesomeqr.option.background.BlendBackground;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.BitSet;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class CardActivity extends AppCompatActivity {


   // private ImageView ig_qrcode;
    private ImageButton ig_qrcode;
    private String account;
    private static final String TAG = "BrightnessActivity";
    private static final int MY_PERMISSIONS_REQUEST_SETTING = 200;
    private int brightness = 0;
    private Bitmap originalQrBitmap;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        try {
            brightness = Settings.System.getInt(getContentResolver(), //取得目前螢幕亮度
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

     //   Context context = getApplicationContext();
//
     //   // Check whether has the write settings permission or not.
     //   boolean settingsCanWrite = Settings.System.canWrite(context);
//
     //   if(!settingsCanWrite) {
     //       // If do not have write settings permission then open the Can modify system settings panel.
     //       Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
     //       startActivity(intent);
     //   }

        account = getSharedPreferences("KEEPACCOUNT", MODE_PRIVATE)
                .getString("keepAccount", "0");
        ig_qrcode = (ImageButton) findViewById(R.id.ig_qrcode);
        //點擊QRcode調整螢幕亮度
        ig_qrcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               final Context context = getApplicationContext();
//
               // Check whether has the write settings permission or not.
               boolean settingsCanWrite = hasWriteSettingsPermission(context);
//
               // If do not have then open the Can modify system settings panel.
               if(!settingsCanWrite) {
                   changeWriteSettingsPermission(context);
               }else {

                   changeScreenBrightness(context, 255); // 螢幕調到最亮
               }
                Handler handler = new Handler();
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       changeScreenBrightness(context,brightness); //三秒後要做的事: 調回原本亮度
                   }
               },4000);//延遲三秒
              // int brightness = 0;
              // try {
              //     brightness = Settings.System.getInt(getContentResolver(),
              //             Settings.System.SCREEN_BRIGHTNESS);
              // } catch (Settings.SettingNotFoundException e) {
              //     e.printStackTrace();
              // }
              //  String msg = "目前亮度 - " + brightness;
              //  Log.d(CardActivity.TAG, msg);
              //  brightness += 30;
//
              //  Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, brightness);
//
              //  msg = "調整後亮度 - " + brightness;
              //  Log.d(CardActivity.TAG, msg);
            }
        });


        //QRcode產生
        // BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
      //  try {
           // Bitmap bitmap = barcodeEncoder.encodeBitmap(account, BarcodeFormat.QR_CODE, 700, 700);

       // } catch (WriterException e) {
      //      e.printStackTrace();
       // }
        //透明ＱRcode //https://blog.csdn.net/lidiwo/article/details/74554934
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lo_go); //QRcode中間的小圖
        originalQrBitmap = QRCodeEncoder.syncEncodeQRCode(account, //條碼內容
                BGAQRCodeUtil.dp2px(this, 200),//條碼大小
                ContextCompat.getColor(this,R.color.black ),//條碼顏色
                ContextCompat.getColor(this,R.color.transparent ),//背景顏色
                bitmap);//中間小圖

        ig_qrcode.setImageBitmap(toRoundCornerImage(originalQrBitmap,50));
    }
    //調整螢幕亮度
    // Check whether this app has android write settings permission.
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasWriteSettingsPermission(Context context)
    {
        boolean ret = true;
        // Get the result from below code.
        ret = Settings.System.canWrite(context);
        return ret;
    }
    //取得設定螢幕亮度權限
    // Start can modify system settings panel to let user change the write settings permission.
    private void changeWriteSettingsPermission(Context context)
    {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        context.startActivity(intent);
    }

    //調整螢幕亮度
    // This function only take effect in real physical android device,
    // it can not take effect in android emulator.
    private void changeScreenBrightness(Context context, int screenBrightnessValue)
    {
        // Change the screen brightness change mode to manual. //
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue);

    }
    //設定圖片圓角
    public static Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        // 抗锯齿
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
