package com.example.user.paradiseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ACT_signUpActivity extends AppCompatActivity {

    private Spinner spinner_session,spinner_partner,spinner_pay;
    private ArrayAdapter<String> adapter_Session,adapter_Partner,adapter_Pay;
    private String[] Session = {"第一場","第二場"};
    private String[] Partner = {"0","1","2"};
    private String[] Pay = {"轉帳","紅利"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_sign_up);

        setSpinners();

    }

    //設置場次、攜伴人數、付費方式Spinner
    private void setSpinners() {
        spinner_session = (Spinner)findViewById(R.id.spinner_session);
        spinner_partner = (Spinner)findViewById(R.id.spinner_partner);
        spinner_pay = (Spinner)findViewById(R.id.spinner_pay);

        adapter_Session = new ArrayAdapter<String>(this,R.layout.spinner,Session);
        adapter_Session.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_session.setAdapter(adapter_Session);

        adapter_Partner = new ArrayAdapter<String>(this,R.layout.spinner,Partner);
        adapter_Partner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_partner.setAdapter(adapter_Partner);

        adapter_Pay = new ArrayAdapter<String>(this,R.layout.spinner,Pay);
        adapter_Pay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pay.setAdapter(adapter_Pay);
    }

}
