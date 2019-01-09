package com.example.user.paradiseapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.service.Common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ValidateActivity extends AppCompatActivity {
    private static final String TAG =" " ;
    private ImageView iv_indentity_front, iv_indentity_back, iv_businesscard,iv_download;
    private Button bt_indentity_front, bt_indentity_back, bt_businesscard,bt_download;
    private static final int REQUEST_CAMERA_FRONT = 10, REQUEST_PICKPICTURE_FRONT = 1,
                          REQUEST_CAMERA_BACK = 20,REQUEST_PICKPICTURE_BACK = 2,
                          REQUEST_CAMERA_BNS = 30,REQUEST_PICKPICTURE_BNS = 3,MY_PERMISSIONS_REQUEST_CAMERA=200;
    private DisplayMetrics mPhone;
    private Uri CameraUri;
    private File photoFile = null,galleryFile = null;
    String mCurrentPhotoPath;
    Uri photoURI = null;

    private String encoded_string, image_name="test.jpg";
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;
    private String downloadurl = "http://59.126.54.198/backup/xxxxxxxxx/sample/images/test.jpg";
    RequestQueue requestQueue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        //取得手機資訊
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        requestQueue = Volley.newRequestQueue(ValidateActivity.this); // 創請求佇列

        //查看有沒有CAMERA權限
      if ((ContextCompat.checkSelfPermission(ValidateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)||
              (ContextCompat.checkSelfPermission(ValidateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

          // Permission is not granted
          // Should we show an explanation?
          if (ActivityCompat.shouldShowRequestPermissionRationale(ValidateActivity.this, Manifest.permission.CAMERA)||
                  ActivityCompat.shouldShowRequestPermissionRationale(ValidateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
              // Show an explanation to the user *asynchronously* -- don't block
              // this thread waiting for the user's response! After the user
              // sees the explanation, try again to request the permission.
          } else {
              //若沒有權限 就跳出權限請求 再從onRequestPermissionsResult取回結果
              // No explanation needed; request the permission
              ActivityCompat.requestPermissions(ValidateActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);

              // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
              // app-defined int constant. The callback method gets the
              // result of the request.
          }
      } else {
          // Permission has already been granted
      }

        iv_indentity_front = (ImageView) findViewById(R.id.iv_indentity_front);
        iv_indentity_back = (ImageView) findViewById(R.id.iv_indentity_back);
        iv_businesscard = (ImageView) findViewById(R.id.iv_businesscard);
        bt_indentity_front = (Button) findViewById(R.id.bt_indentity_front);
        bt_indentity_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicture("front");
            }
        });
        bt_indentity_back = (Button) findViewById(R.id.bt_indentity_back);
        bt_indentity_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicture("back");
            }
        });
        bt_businesscard = (Button) findViewById(R.id.bt_businesscard);
        bt_businesscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicture("bns");
            }
        });
      //  bt_download = (Button)findViewById(R.id.bt_download);
      //  //使用url下載圖片
      //  bt_download.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
      //          iv_download = (ImageView)findViewById(R.id.iv_download);
      //          //Volley 圖片請求 https://www.youtube.com/watch?v=ZTare91T-JE&t=3s
      //          ImageRequest imageRequest = new ImageRequest(downloadurl, new Response.Listener<Bitmap>() {
      //              @Override
      //              public void onResponse(Bitmap response) {
      //              iv_download.setImageBitmap(response);
      //              }
      //          }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
      //              @Override
      //              public void onErrorResponse(VolleyError error) {
      //                 Toast.makeText(ValidateActivity.this,"error",Toast.LENGTH_LONG).show();
      //              }
      //          });
      //          requestQueue.add(imageRequest); //加入請求佇列 完成
      //      }
      //  });
//
//
    }

    private void takepicture(final String choice) {
        if (ContextCompat.checkSelfPermission(ValidateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            //使用shouldShowRequestPermissionRationale方法來跟使用者解釋更多需要使用這些權限的理由,
            //當使用者第一次看到授權畫面, 這個方法會先回傳false,
            //當使用者按下了拒絕, 而第二次再進入app的時候,
            //shouldShowRequestPermissionRationale就會回傳true,

            if (ActivityCompat.shouldShowRequestPermissionRationale(ValidateActivity.this, Manifest.permission.CAMERA)||
                    ActivityCompat.shouldShowRequestPermissionRationale(ValidateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //若按拒絕之後會再跳出來
                ActivityCompat.requestPermissions(ValidateActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                //只有第一次會跳出來
                ActivityCompat.requestPermissions(ValidateActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
            }

        }
        final CharSequence item[] = {"拍照", "從相簿中選取", "取消"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ValidateActivity.this);
        builder.setTitle("上傳照片");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (item[which].equals("拍照")) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        try {
                            photoFile = createImageFile();
                            displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                            Log.i("Mayank",photoFile.getAbsolutePath());

                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(ValidateActivity.this,
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
                            // Error occurred while creating the File
                            displayMessage(getBaseContext(),ex.getMessage().toString());
                        }

                    }else
                    {
                        displayMessage(getBaseContext(),"Nullll");
                    }



                } else if (item[which].equals("從相簿中選取")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    switch (choice) {
                        case "front":
                            startActivityForResult(intent, REQUEST_PICKPICTURE_FRONT);
                            break;

                        case "back":
                            startActivityForResult(intent, REQUEST_PICKPICTURE_BACK);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_CAMERA_FRONT:
                    iv_indentity_front.setImageBitmap(getResizedBitmap(photoFile.getAbsolutePath(),mPhone.widthPixels));// mPhone.widthPixels取得手機的螢幕寬度
                    iv_indentity_front.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_PICKPICTURE_FRONT:
                    Uri selectimageuri_front = data.getData();
                    galleryFile = getFileByUri(selectimageuri_front,this);
                    iv_indentity_front.setImageBitmap(getResizedBitmap(galleryFile.getAbsolutePath(),mPhone.widthPixels));
                    iv_indentity_front.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_CAMERA_BACK:
                    iv_indentity_back.setImageBitmap(getResizedBitmap(photoFile.getAbsolutePath(),mPhone.widthPixels));
                    iv_indentity_back.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_PICKPICTURE_BACK:
                    Uri selectimageuri_back = data.getData();
                    galleryFile = getFileByUri(selectimageuri_back,this);
                    iv_indentity_back.setImageBitmap(getResizedBitmap(galleryFile.getAbsolutePath(),mPhone.widthPixels));
                    iv_indentity_back.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_CAMERA_BNS:
                    iv_businesscard.setImageBitmap(getResizedBitmap(photoFile.getAbsolutePath(),mPhone.widthPixels));
                    iv_businesscard.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_PICKPICTURE_BNS:
                    Uri selectimageuri_bns = data.getData();
                    galleryFile = getFileByUri(selectimageuri_bns,this);
                    iv_businesscard.setImageBitmap(getResizedBitmap(galleryFile.getAbsolutePath(),mPhone.widthPixels));
                    iv_businesscard.setVisibility(View.VISIBLE);
                    bitmap = getResizedBitmap(galleryFile.getAbsolutePath(),mPhone.widthPixels);
                    new Encode_image().execute();
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
            //文件路径是PICTURE目录下的/camerademo目录
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

    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
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
       float resizeh = ((float) phone/height);

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
        protected Void doInBackground(Void... voids) {

           // bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitmap.recycle();

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            makeRequest();
        }
    }

    private void makeRequest() {

        StringRequest request = new StringRequest(Request.Method.POST, "http://59.126.54.198/backup/xxxxxxxxx/sample/upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                  Toast.makeText(ValidateActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             Toast.makeText(ValidateActivity.this,"error"+error,Toast.LENGTH_LONG).show();
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
}