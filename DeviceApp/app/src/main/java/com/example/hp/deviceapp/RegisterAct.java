package com.example.hp.deviceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterAct extends AppCompatActivity {

    BackTask backTask;
    EditText et1,et2,et3,et4,et5;
    Button rbtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        startconf();
        backTask=new BackTask(RegisterAct.this);

        rbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String method="Reg";

                String fname=et1.getText().toString();
                String lname=et2.getText().toString();
                String email=et3.getText().toString();
                String password=et4.getText().toString();
                String cpwd=et5.getText().toString();
                String msg = fname+","+lname+","+email+","+password+",";
                Toast.makeText(RegisterAct.this, msg, Toast.LENGTH_SHORT).show();
                backTask.execute(method,fname,lname,email,password);

            }
        });
    }
    public  void startconf()
    {
        et1 = (EditText)findViewById(R.id.fname);
        et2 = (EditText)findViewById(R.id.lname);
        et3 = (EditText)findViewById(R.id.email);
        et4= (EditText)findViewById(R.id.password);
        et5= (EditText)findViewById(R.id.cpassword);
        rbtn1=(Button)findViewById(R.id.rbtn1);

    }
    public void showmsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
    public boolean checkValid(String m4,String m5)
    {
        if(!m4.equals(m5))
            return true;

        return false;
    }

    private boolean isValidEmail(String e) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(e);
        return matcher.matches();
    }
}
