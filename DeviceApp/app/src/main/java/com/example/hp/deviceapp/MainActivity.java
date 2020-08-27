


package com.example.hp.deviceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
public class MainActivity extends AppCompatActivity {

    public void login(View view)
    {
        startActivity(new Intent(MainActivity.this,LoginAct.class));
    }
    public void register(View view)
    {
        startActivity(new Intent(MainActivity.this,RegisterAct.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
