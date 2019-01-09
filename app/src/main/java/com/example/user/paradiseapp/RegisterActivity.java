package com.example.user.paradiseapp;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.user.paradiseapp.ValidateActivity.getFileByUri;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_account,et_name,et_password,et_passwordcheck,et_phone,et_enName,et_email,et_address,et_introducer;
    private Button bt_register; //bt_checkregister 帳號認證
    private Spinner spinner_Country,spinner_Area,spinner_year,spinner_month,spinner_day,spinner_carrer;
    private String[] area_1 = new String[] {"台灣","中國","新加坡","馬來西亞","香港","澳門"};
    private String[] tw = new String[] {"台北","桃園","台中","台南","高雄"};
    private String[][] area_2 = new String[][] {{"台北","桃園","台中","台南","高雄"},{"北京","上海","福建"},{"中區","東北區","西北區","東南區","西南區"},
            {"玻璃市","吉打","檳城","霹靂","雪蘭莪","森美蘭","馬六甲","柔佛","彭亨","登嘉樓","吉蘭丹","砂拉越","沙巴","吉隆坡","納閩","布城"},
            {"香港","九龍","新界"},{"澳門"}};
    private String[] area_1_en = new String[] {"Taiwan","China","Singapore","Malaysia","Hong Kong","Macau"};
    private String[][] area_2_en = new String[][] {{"Taipei","Taoyuan","Taichung","Tainan","Kaohsiung"},{"Beijing","Shanghai","Fujian"},
            {"Central Singapore Community","North East Community","North West Community"," South East Communityl","South West Community"},
            {"Perlis","Kedah","Penang","Perak","Selangor","Negeri Sembilan","Malacca","Johor","Pahang ","Terengganu","Kelantan","Sarawak","Sabah","Kuala Lumpur","Labuan","Putrajaya"},
            {"Hong Kong","Kowloon","New Territories"},{"Macau"}};
    private int area_1_position;
    private int area_2_position;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private ArrayList<String> dataMonth = new ArrayList<String>();
    private ArrayList<String> dataDay = new ArrayList<String>();

    private ArrayAdapter<String> adapter_Country;
    private ArrayAdapter<String> adapter_Area;
    private ArrayAdapter<String> adapter_year;
    private ArrayAdapter<String> adapter_month;
    private ArrayAdapter<String> adapter_day;

    private String name,account=null,password,passwordcheck,phone,Country,Area,enName,email,address,introducer,Year,Month,Day,
            bitmap_Front_encode="",bitmap_Back_encode="",bitmap_Bns_encode="";
    private int gender;
    private int Level;
    private RadioGroup radioGroup_gender,radioGroup_card;
    public boolean Account_check = false;
    private String result = null;
    private String check = "nocheck";
    BackgroundTask backgroundTask;

    //照片上傳
    private static final String TAG =" " ;
    private ImageButton ib_identity_front, ib_identity_back, ib_businesscard;
    private static final int REQUEST_CAMERA_FRONT = 10, REQUEST_PICKPICTURE_FRONT = 1,
            REQUEST_CAMERA_BACK = 20,REQUEST_PICKPICTURE_BACK = 2,
            REQUEST_CAMERA_BNS = 30,REQUEST_PICKPICTURE_BNS = 3,MY_PERMISSIONS_REQUEST_CAMERA=200;
    private DisplayMetrics mPhone;
    private Uri CameraUri;
    private File photoFile = null,galleryFile = null;
    String mCurrentPhotoPath;
    private Uri photoURI = null;
    private String encoded_string, image_name="test.jpg";
    private Bitmap bitmap,bitmap_Front=null,bitmap_Back=null,bitmap_Bns=null;
    private File file;
    private Uri file_uri;
    private String downloadurl = "http://59.126.54.198/backup/xxxxxxxxx/sample/images/test.jpg";
    private RequestQueue requestQueue;
    private static final String RegisterUrl = "http://59.126.54.198/MeSunApp/Model/Register.php";
    ImageView iv_test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_account = (EditText)findViewById(R.id.et_account);
        et_name = (EditText)findViewById(R.id.et_name);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_password = (EditText)findViewById(R.id.et_password);
        et_passwordcheck = (EditText)findViewById(R.id.et_passwordcheck);
        et_enName = (EditText)findViewById(R.id.et_enName);
        et_email = (EditText)findViewById(R.id.et_email);
        et_introducer = (EditText)findViewById(R.id.et_introducer);
        et_address = (EditText)findViewById(R.id.et_address);
        radioGroup_gender = (RadioGroup)findViewById(R.id.radioGroup_gender);
        radioGroup_card = (RadioGroup)findViewById(R.id.radioGroup_card);
        iv_test = (ImageView)findViewById(R.id.iv_test);

        //選擇地區
        setlocation();
        //選擇生日
        setBirthday();
        //選擇照片
        selectphoto();

        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得註冊資料
                getRegisterData();

                //註冊過濾
                Boolean Pass = PassRegister();

                //如果入資料皆齊全、符合格式，就開始執行註冊
                if(Pass){
                register();}
            }
        });

        //帳號驗證
       // bt_checkregister = (Button) findViewById(R.id.bt_checkregister);
       // bt_checkregister.setOnClickListener(new View.OnClickListener() {
//
       //     @Override
       //     public void onClick(View v) {
       //         String checkAccount = et_account.getText().toString().trim();
//
       //         if (isEmailValid(checkAccount)) {
//
       //             String task = "checkregister";
//
       //             backgroundTask = new BackgroundTask(RegisterActivity.this);
       //             backgroundTask.execute(task,checkAccount);
//
       //             check = "check";
       //         }else {
       //             Toast.makeText(RegisterActivity.this,"Email格式不正確，請重新確認後再認證一次",Toast.LENGTH_LONG).show();
       //             return;
       //         }
       //         }
//
       // });

    }

    //選擇生日
    private void setBirthday() {
        spinner_year = (Spinner)findViewById(R.id.spinner_year);
        spinner_month = (Spinner)findViewById(R.id.spinner_month);
        spinner_day = (Spinner)findViewById(R.id.spinner_day);

        // 年份設定為當年的前70年
        Calendar cal = Calendar.getInstance();//取得日期
        for (int i = 0; i < 70; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) - 70 + i));
        }
        adapter_year = new ArrayAdapter<String>(this, R.layout.spinner, dataYear);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_year);
        spinner_year.setSelection(35);// 默認選中今年前35年

        // 12個月
        for (int i = 1; i <= 12; i++) {
            dataMonth.add("" + (i < 10 ? "0" + i : i)); //i<10時 數字前面加０ // ?代表if ,前面(i<10)是判斷式 , :代表else
        }
        adapter_month = new ArrayAdapter<String>(this, R.layout.spinner, dataMonth);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter_month);

        adapter_day = new ArrayAdapter<String>(this, R.layout.spinner, dataDay);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapter_day);

        //依照所選取的Month來決定可選擇的Ｄay
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dataDay.clear();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.valueOf(spinner_year.getSelectedItem().toString())); //取得前面所選擇的年份
                cal.set(Calendar.MONTH, arg2); //取得所選擇的月份
                int dayofm = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //取得當月的最大日期
                for (int i = 1; i <= dayofm; i++) {
                    dataDay.add("" + (i < 10 ? "0" + i : i));
                }
                adapter_day.notifyDataSetChanged(); //重製adapter_day
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    //選擇地區
    private void setlocation() {
        //adapter_area = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,area);
        adapter_Country = new ArrayAdapter<String>(this,R.layout.spinner,area_1);
        adapter_Country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Country = (Spinner)findViewById(R.id.spinner_Country);
        spinner_Country.setAdapter(adapter_Country);

        //adapter_location = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tw);
        adapter_Area = new ArrayAdapter<String>(this,R.layout.spinner,tw);
        adapter_Area.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Area = (Spinner)findViewById(R.id.spinner_Area);
        spinner_Area.setAdapter(adapter_Area);
        spinner_Country.setOnItemSelectedListener(selectedListener);
    }

    //依選擇的Country顯示不同的Area
    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int pos = spinner_Country.getSelectedItemPosition();
            adapter_Area = new ArrayAdapter<String>(RegisterActivity.this,R.layout.spinner,area_2[pos]);
            adapter_Area.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Area.setAdapter(adapter_Area);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //判斷是否為Email格式
    public static boolean isEmailValid(String email) {
       String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //選擇照片
    private void selectphoto() {
        //取得手機資訊
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        ib_identity_front = (ImageButton) findViewById(R.id.ib_identity_front);
        ib_identity_back = (ImageButton) findViewById(R.id.ib_identity_back);
        ib_businesscard = (ImageButton) findViewById(R.id.ib_businesscard);
        ib_identity_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicture("front");
            }
        });
        ib_identity_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicture("back");
            }
        });
        ib_businesscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicture("bns");
            }
        });

    }

    //取得照片
    private void takepicture(final String choice) {

        final CharSequence item[] = {"拍照", "從相簿中選取", "取消"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //設置DialogTitle
        String title="";
        switch (choice){
            case "front":
                title = "上傳身分證正面照片";
                break;
            case "back":
                title = "上傳身分證反面照片";
                break;
            case "bns":
                title = "上傳名片照片";
                break;
        }

        builder.setTitle(title);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (item[which].equals("拍照")) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        try {
                            photoFile = createImageFile();
                            Log.i("Mayank",photoFile.getAbsolutePath());

                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(getBaseContext(),
                                        "com.example.user.paradiseapp.fileprovider"/* 由這行來決定Manifest裡使用哪個provider，所以須和欲使用的provider的authorities一致*/
                                        , photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                switch (choice) {
                                    case "front":
                                        startActivityForResult(takePictureIntent, REQUEST_CAMERA_FRONT);
                                        break;
                                    case "back":
                                        startActivityForResult(takePictureIntent, REQUEST_CAMERA_BACK);
                                        break;
                                    case "bns":
                                        startActivityForResult(takePictureIntent,REQUEST_CAMERA_BNS);
                                        break;
                                }
                            }
                        } catch (Exception ex) {
                        }
                    }

                } else if (item[which].equals("從相簿中選取")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    switch (choice) {
                        case "front":
                            startActivityForResult(intent,REQUEST_PICKPICTURE_FRONT);
                            break;
                        case "back":
                            startActivityForResult(intent,REQUEST_PICKPICTURE_BACK);
                            break;
                        case "bns":
                            startActivityForResult(intent,REQUEST_PICKPICTURE_BNS);
                            break;
                    }

                } else if (item[which].equals("取消")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //取得照片結果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_CAMERA_FRONT:
                    bitmap_Front = getResizedBitmap(photoFile.getAbsolutePath(),mPhone.widthPixels);
                    ib_identity_front.setImageBitmap(bitmap_Front);// mPhone.widthPixels取得手機的螢幕寬度
                    break;
                case REQUEST_PICKPICTURE_FRONT:
                    Uri selectimageuri_front = data.getData();
                    galleryFile = getFileByUri(selectimageuri_front,this);
                    bitmap_Front = getResizedBitmap(galleryFile.getAbsolutePath(),mPhone.widthPixels);
                    ib_identity_front.setImageBitmap(bitmap_Front);
                    break;
                case REQUEST_CAMERA_BACK:
                    bitmap_Back = getResizedBitmap(photoFile.getAbsolutePath(),mPhone.widthPixels);
                    ib_identity_back.setImageBitmap(bitmap_Back);
                    break;
                case REQUEST_PICKPICTURE_BACK:
                    Uri selectimageuri_back = data.getData();
                    galleryFile = getFileByUri(selectimageuri_back,this);
                    bitmap_Back = getResizedBitmap(galleryFile.getAbsolutePath(),mPhone.widthPixels);
                    ib_identity_back.setImageBitmap(bitmap_Back);
                    break;
                case REQUEST_CAMERA_BNS:
                    bitmap_Bns = getResizedBitmap(photoFile.getAbsolutePath(),mPhone.widthPixels);
                    ib_businesscard.setImageBitmap(bitmap_Bns);
                    break;
                case REQUEST_PICKPICTURE_BNS:
                    Uri selectimageuri_bns = data.getData();
                    galleryFile = getFileByUri(selectimageuri_bns,this);
                    bitmap_Bns = getResizedBitmap(galleryFile.getAbsolutePath(),mPhone.widthPixels);
                    ib_businesscard.setImageBitmap(bitmap_Bns);
                   // new Encode_image().execute(); 上傳照片的範例
                    break;
            }

        }
    }

    //建立File
    private File createImageFile() throws IOException {
        File imageFile = null;
        String storagePath;
        File storageDir;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            //文件路徑是PICTURE目錄下的/camerademo目錄
            storagePath = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getAbsolutePath() + File.separator + "camerademo";
            storageDir = new File(storagePath);
            storageDir.mkdirs();
            imageFile = File.createTempFile(timeStamp, ".jpg", storageDir);
            Log.e(TAG, imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;

    }

    //Bitmap壓縮、方向轉正
    private Bitmap getResizedBitmap(String imagePath,int phone) {

        //取得圖片訊息
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }

        int degree=0;
        if (exif != null) {
            // 讀取圖片中相機方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 計算旋轉角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        }
        int postrotate=0;
        if(degree!=0){
            postrotate=degree;
        }


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只讀取寬度和高度
        BitmapFactory.decodeFile(imagePath, options);
        int width = options.outWidth, height = options.outHeight;


        // 求出要縮小的 scale 值，必需是2的次方，ex: 1,2,4,8,16...
        int scale = 1;//預設值

        // 使用 scale 值產生縮小的圖檔
        BitmapFactory.Options scaledOptions = new BitmapFactory.Options();
        scaledOptions.inSampleSize = scale;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(imagePath, scaledOptions);

        //以螢幕的寬度phone作等比例縮放 會產生正方形的圖
        float resizew = ((float) phone/width);
        float resizeh = ((float) phone/width);//高度按照寬度縮放的比例做等比例縮放

        Matrix matrix = new Matrix(); // 產生縮圖需要的參數 matrix
        matrix.postScale(resizew,resizeh);
        matrix.postRotate(postrotate);//使圖片角度轉至0

        // 產生縮小後的圖 (Bitmap)
        return Bitmap.createBitmap(scaledBitmap, 0, 0, width, height, matrix, true);
    }

    //Uri轉File 超猛
    public static File getFileByUri(Uri uri,Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    //使用volley照片上傳MYSQL , https://www.youtube.com/watch?v=3tEiiUOLemA
    private class Encode_image extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) { //背後執行

            //Bitmap轉base64碼
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //quality是指壓縮率，100表示不壓縮，30表示壓縮70%
            bitmap.recycle();//回收Bitmap的空間

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            makeRequest();
        }
    }

    //上傳照片
    private void makeRequest() {

        StringRequest request = new StringRequest(Request.Method.POST, "http://59.126.54.198/backup/xxxxxxxxx/sample/upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"error"+error,Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encoded_string);
                map.put("image_name",image_name);

                return map;
            }
        };
        requestQueue.add(request); //將request加入 requestQueue佇列
    }

    //送出註冊資料
    private void register (){

        //串接API for Volley
        HashMap data = new HashMap();
        data.put("Account",account);
        data.put("Password",password);
        data.put("CheckPW",passwordcheck);
        data.put("Name",name);
        data.put("NameEN",enName);
        data.put("Gender",gender);
        data.put("Birthday",Year+"/"+Month+"/"+Day);
        data.put("Career","賣肝業");
        data.put("Email",email);
        data.put("Tel",phone);
        data.put("Country",Country);
        data.put("Area",Area);
        data.put("Address",address);
        data.put("Level",Level);
        data.put("Referrer",introducer);
        data.put("IdentityImg01",bitmap_Front_encode);
        data.put("IdentityImg02",bitmap_Back_encode);
        data.put("SalaryProveImg",bitmap_Bns_encode);
        data.put("BusinessImg",bitmap_Bns_encode);

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RegisterUrl, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("Content");
                    String s = jsonObject.getString("Message");
                    Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"發生錯誤",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    //取得註冊資料
    private void getRegisterData() {
        account = et_account.getText().toString().trim();
        name = et_name.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        password = et_password.getText().toString().trim();
        passwordcheck = et_passwordcheck.getText().toString().trim();
        enName = et_enName.getText().toString().trim();
        email = et_email.getText().toString().trim();
        address = et_address.getText().toString().trim();
        introducer = et_introducer.getText().toString().trim();


        switch (radioGroup_gender.getCheckedRadioButtonId())
        {
            case R.id.radioButton_male:
                // Toast.makeText(this,"male",Toast.LENGTH_LONG).show();
                gender=0;
                break;
            case R.id.radioButton_female:
                // Toast.makeText(this,"female",Toast.LENGTH_LONG).show();
                gender=1;
                break;
        }

        switch (radioGroup_card.getCheckedRadioButtonId())
        {
            case R.id.radioButton_general:
                Level=0;
                break;
            case R.id.radioButton_silver:
                Level=1;
                break;
            case R.id.radioButton_gold:
                Level=2;
                break;
        }
        //取得地區字串(中文)
        Country = String.valueOf(spinner_Country.getSelectedItem());
        Area = String.valueOf(spinner_Area.getSelectedItem());

        //取得地區字串(英文)
        // area_1_position = spinner1.getSelectedItemPosition();
        // area_2_position = spinner2.getSelectedItemPosition();
        // area = area_1_en[area_1_position];
        // location = area_2_en[area_1_position][area_2_position];

        //取得生日
        Year = String.valueOf(spinner_year.getSelectedItem());
        Month = String.valueOf(spinner_month.getSelectedItem());
        Day = String.valueOf(spinner_day.getSelectedItem());

        //取得圖片轉base64碼
        if(bitmap_Front==null){ //若未選擇照片則跳出
            Toast.makeText(RegisterActivity.this,"請上傳身分證正面照片",Toast.LENGTH_LONG).show();
            return;
        }else { bitmap_Front_encode = getBitmapforBase64(bitmap_Front); }

        if(bitmap_Back==null){
            Toast.makeText(RegisterActivity.this,"請上傳身分證反面照片",Toast.LENGTH_LONG).show();
            return;
        }else { bitmap_Back_encode = getBitmapforBase64(bitmap_Back);}

        if(bitmap_Bns==null){
            Toast.makeText(RegisterActivity.this,"請上傳名片照片",Toast.LENGTH_LONG).show();
            return;
        }else { bitmap_Bns_encode = getBitmapforBase64(bitmap_Bns);}
    }

    //註冊前先過濾 若有不符合規定則返回
    private Boolean PassRegister() {
        String regex= "^.*[(/) | (\\\\) | (:) | (\\*) | (\\?) | (\") | (<) | (>)|@|#|%].*$"; //特殊字元
        //帳號未填
        if(account.equals("") ){
            Toast.makeText(RegisterActivity.this,"請輸入帳號",Toast.LENGTH_LONG).show();
            return false;
        }
        //密碼未填
        else if(password.equals("")){
            Toast.makeText(RegisterActivity.this,"請輸入密碼",Toast.LENGTH_LONG).show();
            return false;
        }
        //密碼含特殊符號
        else if(password.matches(regex) ){
            Toast.makeText(RegisterActivity.this,"密碼中包含特殊符號，請輸入英文和數字",Toast.LENGTH_LONG).show();
            return false;
        }
        //密碼和密碼確認不同
        else if (!(password.equals(passwordcheck))){
            Toast.makeText(RegisterActivity.this,"密碼確認輸入與密碼不同",Toast.LENGTH_LONG).show();
            return false;
        }
        //姓名未填
        else if(name.equals("")){
            Toast.makeText(RegisterActivity.this,"請輸入真實姓名",Toast.LENGTH_LONG).show();
            return false;
        }
        //英文姓名未填
        else if(enName.equals("")){
            Toast.makeText(RegisterActivity.this,"請輸入英文姓名",Toast.LENGTH_LONG).show();
            return false;
        }
        //Email未填
        else if(email.equals("")){
            Toast.makeText(RegisterActivity.this,"請輸入Email",Toast.LENGTH_LONG).show();
            return false;
        }
        //手機未填
        else if(phone.equals("")){
            Toast.makeText(RegisterActivity.this,"請輸入手機號碼",Toast.LENGTH_LONG).show();
            return false;
        }
        //地址未填
        else if(address.equals("")){
            Toast.makeText(RegisterActivity.this,"請輸入地址",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //圖片轉base64碼
    private String getBitmapforBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //quality是指壓縮率，100表示不壓縮，30表示壓縮70%

        byte[] array = stream.toByteArray();
        String encoded = Base64.encodeToString(array, 0);
        return encoded;
    }


}

//串接API for Async
//String task = "register";
//backgroundTask = new BackgroundTask(RegisterActivity.this);
//backgroundTask.execute(task,name, account, password, passwordcheck,gender,phone,Country,Area);