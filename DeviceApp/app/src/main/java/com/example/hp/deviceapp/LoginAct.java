package com.example.hp.deviceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
public class LoginAct extends AppCompatActivity {

    EditText let1,let2;
    Button lbtn1;
    BackTask backTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startconf();
        backTask=new BackTask(LoginAct.this);
        lbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String method="Login";

                String email=let1.getText().toString();
                String password=let2.getText().toString();

                backTask.execute(method,email,password);
               // Intent intent=new Intent(LoginAct.this,WelcomeAct.class);
                //startActivity(intent);

            }
        });
    }
    public void startconf()
    {
        let1= (EditText)findViewById(R.id.lemail);
        let2 = (EditText)findViewById(R.id.lpassword);
        lbtn1=(Button)findViewById(R.id.lbtn);

    }

}
