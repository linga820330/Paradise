package com.example.user.paradiseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Modify_MemberDataActivity extends AppCompatActivity {

    private EditText et_mef_phone;
    private Spinner spinner1,spinner2;
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

    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    private RadioGroup radioGroup;
    private Button bt_mdf_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__member_data);

    }
}
