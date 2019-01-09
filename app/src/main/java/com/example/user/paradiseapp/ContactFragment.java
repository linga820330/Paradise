package com.example.user.paradiseapp;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {


    public ContactFragment() {
        // Required empty public constructor
    }

    //private Button bt_email,bt_skype; //bt_phone
    private ImageButton ib_email,ib_skype;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        ib_email = (ImageButton) view.findViewById(R.id.ib_email);
        ib_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email();
            }
        });

        ib_skype = (ImageButton) view.findViewById(R.id.ib_skype);
        ib_skype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Skype();
            }
        });


      //          //叫出手機的skype　方法二
      //          //String user_name = "";
      //          //initiateSkypeUri(getActivity(), "skype:" + user_name + "?call=true");

        return view;
    }



    //開啟手機EmailAPP
    private void Email() {
        String[] recipients = new String[]{"paradise@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    //開起手機SkypeAPP
    private void Skype() {
        Uri uri = Uri.parse("https://join.skype.com/invite/HiDceOZvStYo");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    //開起手機撥打電話
    private void Phone(){
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"0422557019")));
    }


    //SkypeUri 方法二
        public void initiateSkypeUri(Context myContext, String mySkypeUri) {
// Make sure the Skype for Android client is installed
            if (!isSkypeClientInstalled(myContext)) {
                goToMarket(myContext);
                return;
            }

// Create the Intent from our Skype URI
            Uri skypeUri = Uri.parse(mySkypeUri);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);
// Restrict the Intent to being handled by the Skype for Android client
// only
            myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.MainActivity"));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
// Initiate the Intent. It should never fail since we've already established the
// presence of its handler (although there is an extremely minute window where that
// handler can go away...)
            myContext.startActivity(myIntent);
            return;
        }
       //確認 Skype是否有被安裝在 Device  
        //Determine if a Skype client is installed Determine whether the Skype for 
        //Android client is installed on this device.

        public boolean isSkypeClientInstalled(Context myContext) {
            PackageManager myPackageMgr = myContext.getPackageManager();
            try
            {
                myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
            }
            catch (PackageManager.NameNotFoundException e) {
                return (false); }
            return (true);
        }

        //如果偵測到 Device沒有安裝 Skype, 那麼就連接到 Google play安裝
        //If a Skype client is not installed Install the Skype client through the
        //market: URI scheme.

        public void goToMarket(Context myContext) {
            Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(myIntent);
            return;
        }
    }


