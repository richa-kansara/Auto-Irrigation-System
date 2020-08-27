package com.example.hp.deviceapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public class BackTask extends AsyncTask<String,Void,String>{

    Context ctx;
    public BackTask(Context ctx) {
        this.ctx=ctx;
    }

    AlertDialog alertDialog;
    @Override
    protected String doInBackground(String... params) {
        String Login_Url="";

        String method=params[0];
        Log.d("hi",method);

        if(method.equals("Reg"))
        {
            Login_Url="http://itsouls.com/ais/androidreg.php";
            Log.d("One",Login_Url);
            String fname=params[1];
            Log.d("FirstName",params[1]);
            String lname=params[2];
            Log.d("LastName",params[2]);
            String email=params[3];
            Log.d("Email",params[3]);
            String password=params[4];
            Log.d("Password",params[4]);


            String response="";
            try
            {

                URL url=new URL(Login_Url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                Log.d("Rubiya1","Success");
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                Log.d("Rubiya2","Connected");
                HashMap<String, String> postDataParams;
                postDataParams=new HashMap<String, String>();
                postDataParams.put("n1", fname);
                postDataParams.put("n2", lname);
                postDataParams.put("n3", email);
                postDataParams.put("n4", password);

                // httpURLConnection.setDoInput(true);

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
                Log.d("Rubiya3","All Done");
                return response;
            }
            catch (MalformedURLException ex)
            {
                Log.d("Malformed",ex.toString());
                response = "MalformURLException : "+ex.getMessage().toString();
                ex.printStackTrace();
                return response;
            } catch (ProtocolException e) {
                response = "Protocol : "+e.getMessage().toString();
                Log.d("ProtocolEx",e.toString());
                e.printStackTrace();
                return response;
            } catch (IOException e) {
                Log.d("IOException",e.toString());
                response = "IO Exception"+e.getMessage().toString();
                e.printStackTrace();
                return response;
            }
        }
        else if(method.equals("Login"))
        {
            Login_Url="http://itsouls.com/ais/androidlogin.php";
            String email=params[1];
            String password=params[2];
            String response="";
            try
            {
                URL url=new URL(Login_Url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                HashMap<String, String> postDataParams;
                postDataParams=new HashMap<String, String>();
                postDataParams.put("n1", email);
                postDataParams.put("n2", password);
                // httpURLConnection.setDoInput(true);

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
                response = "MalformURLException : "+ex.getMessage().toString();
                ex.printStackTrace();
                return response;
            } catch (ProtocolException e) {
                response = "Protocol : "+e.getMessage().toString();
                e.printStackTrace();
                return response;
            } catch (IOException e) {
                response = "IO Exception"+e.getMessage().toString();
                e.printStackTrace();
                return response;
            }

        }
        return null;
    }
    @Override
    protected void onPostExecute(String s) {

        Toast.makeText(ctx,s,Toast.LENGTH_LONG).show();

        if(s.startsWith("Login"))
        {
            String p[] = s.split(",");

            Toast.makeText(ctx,p[0],Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ctx,WelcomeAct.class);
            Toast.makeText(ctx,"value : "+p[1],Toast.LENGTH_LONG).show();
            intent.putExtra("id",p[1]);
            ctx.startActivity(intent);
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

}
