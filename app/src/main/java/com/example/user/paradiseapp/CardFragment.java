package com.example.user.paradiseapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static android.content.Context.MODE_PRIVATE;
import static com.example.user.paradiseapp.CardActivity.toRoundCornerImage;


public class CardFragment extends Fragment {

    private ImageButton ib_qrcode;
    private String account,Level;
    private static final String TAG = "BrightnessActivity";
    private static final int MY_PERMISSIONS_REQUEST_SETTING = 200;
    private int brightness = 0;
    private Bitmap originalQrBitmap;
    private View view;
    private int[] CardImage = {R.drawable.card_1,R.drawable.card_2,R.drawable.card_3,R.drawable.card_4};
    private ImageView iv_card;
    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_card, container, false);

       setQRcode();
       setCard();

        return view;
    }


    //設置會員CRcode
    private void setQRcode() {
        try {
            brightness = Settings.System.getInt(getContext().getContentResolver(), //取得目前螢幕亮度
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        account = getContext().getSharedPreferences("KEEPACCOUNT", MODE_PRIVATE)
                .getString("keepAccount", "0");
        ib_qrcode = (ImageButton) view.findViewById(R.id.ib_qrcode);
        //點擊QRcode調整螢幕亮度
        ib_qrcode.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                final Context context = getContext();
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
            }
        });
        //透明ＱRcode //https://blog.csdn.net/lidiwo/article/details/74554934
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lo_go); //QRcode中間的小圖
        originalQrBitmap = QRCodeEncoder.syncEncodeQRCode(account, //條碼內容
                BGAQRCodeUtil.dp2px(getContext(), 200),//條碼大小
                ContextCompat.getColor(getContext(),R.color.black ),//條碼顏色
                ContextCompat.getColor(getContext(),R.color.transparent ),//背景顏色
                bitmap);//中間小圖

        ib_qrcode.setImageBitmap(toRoundCornerImage(originalQrBitmap,50));
    }

    //設置會員卡片
    private void setCard() {
     Level = getContext().getSharedPreferences("KEEPACCOUNT",MODE_PRIVATE)
             .getString("CardLevel","0");
        iv_card = (ImageView)view.findViewById(R.id.iv_card);
     //依照取得的會員等級來顯示會員卡片等級
     switch (Integer.valueOf(Level)){
         case 1:
             iv_card.setImageResource(CardImage[0]);
             break;
         case 2:
             iv_card.setImageResource(CardImage[1]);
             break;
         case 3:
             iv_card.setImageResource(CardImage[2]);
             break;
         case 4:
             iv_card.setImageResource(CardImage[3]);
             break;

     }
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
