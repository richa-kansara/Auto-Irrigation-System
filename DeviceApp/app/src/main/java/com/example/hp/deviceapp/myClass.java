package com.example.hp.deviceapp;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;



public class myClass extends AsyncTask<String,Void,String> {


    String u1,u2;
    Context ctx;

    public myClass(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {

        u1 = "http://10.0.2.2:8080/users/adddata.php";
        u2="http://10.0.2.2:8080/users/login.php";
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {
        String method = params[0];

        if(method.equals("signup")) {

            String fname = params[1];
            String lname = params[2];
            String email = params[3];
            String password = params[4];

            try {
                URL url = new URL(u1);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);


                String data = URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(fname, "UTF-8");
                data += "&" + URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(lname, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


                Log.d("Data are : ", data);


                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                bw.write(data);

                bw.close();

                InputStream IS = httpURLConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(IS, "UTF-8"));

                IS.close();
                br.close();

                httpURLConnection.disconnect();

                return "User Registered";

            } catch (MalformedURLException ex) {

            } catch (Exception ex) {

            }
        }
        else if(method.equals("signin"))
        {
            String fname = params[1];
            String lname = params[2];
            Log.d("k",fname);
            try {
                URL url = new URL(u2);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);


                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(fname, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(lname, "UTF-8");

                Log.d("val",data);



                OutputStream OS = httpURLConnection.getOutputStream();

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                bw.write(data);

                bw.close();

                InputStream IS = httpURLConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(IS, "UTF-8"));

                String line = "";
                StringBuilder st = new StringBuilder();
                while((line=br.readLine())!=null)
                {
                    st.append(line);
                }

                IS.close();
                br.close();

                httpURLConnection.disconnect();
                Log.d("value",st.toString());
                return st.toString();

            } catch (MalformedURLException ex) {

            } catch (Exception ex) {

            }



        }


        return null;
    }
    @Override
    protected void onPostExecute(String s) {

        showMsg(s);

        if(s.equals("User Registered"))
        {
            showMsg("Registration Completed Successfully");
            Intent i1 = new Intent(ctx,LoginAct.class);
            ctx.startActivity(i1);
        }
        else if(s.equals("Login Successfully"))
        {

            Intent i1 = new Intent(ctx,WelcomeAct.class);
            ctx.startActivity(i1);
        }
        super.onPostExecute(s);
    }

    public void showMsg(String s)
    {
        Toast.makeText(ctx,s,Toast.LENGTH_LONG).show();
    }
}
