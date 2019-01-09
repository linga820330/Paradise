package com.example.user.paradiseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ACT_DetailActivity extends AppCompatActivity {

    private Button bt_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__detail);
        bt_signup = (Button)findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACT_DetailActivity.this,ACT_signUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
