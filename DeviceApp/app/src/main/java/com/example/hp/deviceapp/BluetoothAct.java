package com.example.hp.deviceapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BluetoothAct extends AppCompatActivity {

    BluetoothAdapter myBlue;
    public void btOn(View v) {
        if (myBlue.isEnabled()) {
            showMsg("Device Already On");
        } else {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(i);
            showMsg("Device Start");
        }
    }

    public void btOff(View v) {
        myBlue.disable();
    }

    public void back(View v) {
        startActivity(new Intent(BluetoothAct.this, WelcomeAct.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        myBlue = BluetoothAdapter.getDefaultAdapter();
    }
    public void showMsg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
