package com.example.user.paradiseapp;

//import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar; //materialdatetimepicker用的Calendar
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog; //另外加入的第三方SDK

public class Consultation2Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button bt_date;
    private TextView tv_date,tv_time;
    private EditText et_content;
    private Spinner spinner_time;
    private ArrayAdapter<String> adapter_Time;
    private String[] time = {"13:00-14:00","14:00-15:00","15:00-16:00","16:00-17:00"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation2);
      //  tv_time = (TextView)  findViewById(R.id.tv_time);

        bt_date = (Button)findViewById(R.id.bt_date);
        bt_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        setTimeSpinner();

        setEdittext();

    }

    //設置時間的Spinner
    private void setTimeSpinner() {
        spinner_time = (Spinner)findViewById(R.id.spinner_time);
        adapter_Time = new ArrayAdapter<String>(this,R.layout.spinner,time);
        adapter_Time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time.setAdapter(adapter_Time);

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // tv_time.setText(time[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //設置諮詢內容Edittext
    private void setEdittext() {
        et_content = (EditText)findViewById(R.id.et_content);
        //設置Edittext換行
        et_content.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);//設置EditText的顯示方式為多行文本输入
        et_content.setGravity(Gravity.TOP);//文本顯示的位置在EditText的最上方
        et_content.setSingleLine(false);//改變默認的單行模式
        et_content.setHorizontallyScrolling(false);//水平滾動設置為False
    }

    //設置日期選擇的Dialog
    private void selectDate() {
        //套用多功能DatePickerDialog函式庫  https://github.com/wdullaer/MaterialDateTimePicker
        //https://stackoverflow.com/questions/44067870/setselecteabledays-material-datetime-picker-android
        //日期宣告
        Calendar calendar = Calendar.getInstance();
        Calendar date1 = Calendar.getInstance();
        date1.set(Calendar.YEAR,2018);
        date1.set(Calendar.MONTH,12-1); //不知為什麼顯示時會自動加一 所以在這先減一
        date1.set(Calendar.DAY_OF_MONTH,15);
        Calendar date2 = Calendar.getInstance();
        date2.set(Calendar.YEAR,2018);
        date2.set(Calendar.MONTH,12-1);
        date2.set(Calendar.DAY_OF_MONTH,16);

        Calendar[] selectdate = new Calendar[2]; //日期陣列
        selectdate[0] = date1;
        selectdate[1] = date2;

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                Consultation2Activity.this,
                //現在的日期
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setSelectableDays(selectdate); //設定可選擇的日期
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    //DatePickerDialog複寫的方法
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    //選擇日期完　按下確定後的動作
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String dateTime = String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_date.setText(dateTime);
    }
}
