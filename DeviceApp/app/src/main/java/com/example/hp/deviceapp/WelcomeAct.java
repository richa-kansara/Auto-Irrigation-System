package com.example.hp.deviceapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import android.os.Handler;
public class WelcomeAct extends AppCompatActivity {

    Button sensor1, sensor2, pair1;
    Switch bluet;
    ListView lv;
    TextView sensText1,sensText2;
    int flag1 = 0;
    int flag2 = 0;
    final int handlerState = 0;
    String uid="";
    String finaltemp[] = new String[5];
    String finalsv1[] = new String[5];

    private ArrayAdapter<String> mArrayAdapter;

    //private static String address = "9C:02:98:B1:90:B3";

    private static String address = "20:14:05:22:31:24";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket btSocket = null;

    private OutputStream outStream = null;

    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;

    BluetoothAdapter mBtAdapter;

    Handler bluetoothIn;

    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        uid = getIntent().getStringExtra("id");
        // this is for configuration ...
        Config();

        //bluetooth instance create

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter.isEnabled()) {
            bluet.setChecked(true);
        }
        // now we will apply for bluetooth ...
        // switch button programming
        bluet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bluet.isChecked()) {
                    if (mBtAdapter.isEnabled()) {
                        Toast.makeText(getApplicationContext(), "Bluetooth is On", Toast.LENGTH_LONG).show();

                    } else {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                        if (mBtAdapter.isEnabled()) {
                            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            startActivity(i);
                        }
                    }
                } else {
                    mBtAdapter.disable();
                }
            }
        });


        // pair button click
        pair1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBtAdapter.isEnabled()) {
                    //start

                    mBtAdapter = BluetoothAdapter.getDefaultAdapter();

                    Set<BluetoothDevice> pairedDevice = mBtAdapter.getBondedDevices();

                    // Set<BluetoothDevice> pairedDevice = mBtAdapter.getRemoteDevice(address);

                    lv = (ListView) findViewById(R.id.list1);

                    final ArrayList pairedDevicesArrayList = new ArrayList();
                    final ArrayList pairedAddress = new ArrayList();
                    if (pairedDevice.size() > 0) {
                        //for(BluetoothDevice bluetoothDevice : pairedDevice)
                        for (BluetoothDevice bluetoothDevice : pairedDevice) {

                            pairedDevicesArrayList.add(bluetoothDevice.getName());
                            pairedAddress.add(bluetoothDevice.getAddress());
                            showmsg(""+bluetoothDevice.getAddress());
                            //arrayListBluetoothDevices.add(bluetoothDevice);
                            // in newer version
                            // 9C:02:98:B1:90:B3
                            //pairedDevicesArrayList.add(bluetoothDevice.getName());
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(WelcomeAct.this, android.R.layout.simple_list_item_1, pairedDevicesArrayList);

                        lv.setAdapter(arrayAdapter);
                    }
                    //end

                    // listview Click Generated
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //showMsg("ListItem clickedx");

                            //BluetoothDevice bdDevice = arrayListBluetoothDevices.get(position);

                            String bdev = pairedAddress.get(position).toString();

                            //showMsg(bdev);

                            address = bdev;

                            BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
                            try {
                                //showMsg("Get Remote");

                                btSocket = creatBluetoothSocket(device);

                                showMsg("Connected...");

                            } catch (IOException ex) {
                                showMsg("Fatal Error");
                            }


                            mBtAdapter.cancelDiscovery();

                            try {
                                btSocket.connect();
                            } catch (Exception ex) {
                                try {
                                    btSocket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            try {
                                outStream = btSocket.getOutputStream();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }
                    });
                } else {
                    showMsg("Plz Turn On Bluetooth");
                }

            }
        });

        // sensor1 button click
        sensor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMsg("Sensor 1 Button Click");
                flag1 = 1;
                if (mBtAdapter.isEnabled()) {
                    final InputStream mmInStream;
                    final OutputStream mmOutStream;
                    byte[] buffer = new byte[256];
                    int bytes;

                    try {

                        mmInStream = btSocket.getInputStream();

                        bytes = mmInStream.read(buffer);            //read bytes from input buffer

                        String readMessage = new String(buffer, 0, bytes);

                        showmsg("On Button click : "+readMessage);

                        // Send the obtained bytes to the UI Activity via handler
                        bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    } catch (IOException e) {
                        //break;
                        showMsg(e.getMessage());
                    }


                } else {
                    showMsg("Plz Turn On Bluetooth");
                }

            }
        });

        // sensor2 button click
        sensor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg("Sensor2 Button Click");
                flag2 = 1;
                if (mBtAdapter.isEnabled()) {
                    final InputStream mmInStream;
                    final OutputStream mmOutStream;
                    byte[] buffer = new byte[256];
                    int bytes;

                    try {
                        mmInStream = btSocket.getInputStream();
                        bytes = mmInStream.read(buffer);            //read bytes from input buffer

                        String readMessage = new String(buffer, 0, bytes);

                        showMsg("On Button click : "+readMessage);

                        // Send the obtained bytes to the UI Activity via handler
                        bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    } catch (IOException e) {
                        //break;
                           showMsg(e.getMessage());
                    }


                } else {
                    showMsg("Plz Turn On Bluetooth");
                }
            }
        });

        // Handler Event
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want

                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread

                    //showMsg("Brijesh : "+data);
                    String data = readMessage;
                    //showMsg("Brijesh : "+data);
                    //showMsg(data);
                    if (flag1 == 1) {

                        int tid = data.indexOf("Sensor1");

                        if (tid != -1) {
                            String m = data.substring(tid, tid + 14);

                            showMsg("Sens M : " + m);
                            String krunal[] = m.split("=");

                            showMsg(krunal[1]);
                            sensText1.setText(krunal[1]);
                            flag1 = 0;

                            // send data into database
                            // call here execute method sensor1

                            // obj.execute(userid,krunal[1],sensor1);
                            new SetData().execute(uid, krunal[1], "Sensor 1");
                        }
                    }

                    if (flag2 == 1) {
                        int tid = data.indexOf("Sensor2");

                        if (tid != -1) {
                            String m = data.substring(tid, tid + 14);

                            //showMsg(m);
                            String krunal[] = m.split("=");

                            showMsg(krunal[1]);
                            sensText2.setText(krunal[1]);
                            flag2 = 0;
                            // send data into database for sensor 2
                            // call here execute method...for sensor2
                            // obj.execute(userid,krunal[1],sensor2);
                            new SetData().execute(uid, krunal[1], "Sensor 2");

                        }
                    }


                }
            }
        };
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //ShowMessage("Hello");
            String action = intent.getAction();
            String n = "Hello";
            ArrayList list1 = new ArrayList();


            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                n = n + device.getName();
                list1.add(device.getName());

            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(WelcomeAct.this, android.R.layout.simple_list_item_1, list1);
            showMsg(n);
            lv.setAdapter(arrayAdapter);
        }
    };

    private BluetoothSocket creatBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                showMsg("Connection");
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception ex) {
                ex.printStackTrace();
                showMsg("Could not connect Socket");
            }

        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }


    public void showMsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public void Config() {
        bluet = (Switch) findViewById(R.id.bluet);
        sensor1 = (Button) findViewById(R.id.sensor1);
        sensor2 = (Button) findViewById(R.id.sensor2);
        pair1 = (Button) findViewById(R.id.pair1);
        lv = (ListView) findViewById(R.id.list1);
        sensText1 = (TextView)findViewById(R.id.sensText1);
        sensText2 = (TextView)findViewById(R.id.sensText2);
    }

    private void sendData(String message) {

        byte[] buffer = message.getBytes();

        try {
            outStream.write(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            // Keep looping to listen for received messages
            //while (true) {
            try {
                bytes = mmInStream.read(buffer);            //read bytes from input buffer
                String readMessage = new String(buffer, 0, bytes);
                // Send the obtained bytes to the UI Activity via handler
                // bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
            } catch (IOException e) {
                //break;
            }
            //}
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }

    public class SetData extends AsyncTask<String,Void,String>
    {

        String u1;

        @Override
        protected void onPreExecute() {
            u1="http://itsouls.com/ais/insertrecord.php";
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String response="";
            String reading=params[1];
            String sensor=params[2];
            try {

                URL url=new URL(u1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                HashMap<String, String> postDataParams;
                postDataParams=new HashMap<String, String>();
                postDataParams.put("n1",uid);
                postDataParams.put("n2",reading);
                postDataParams.put("n3",sensor);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    //Log.d("Output",br.toString());
                    while ((line = br.readLine()) != null) {
                        response += line;
                        Log.d("output lines", line);
                    }
                } else {
                    response = "";
                }
                httpURLConnection.disconnect();
                return response;
            }
            catch (MalformedURLException ex)
            {

            }
            catch (Exception ex)
            {

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void showmsg(String msg)
    {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}
