package com.murali.hariprahlad;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.murali.hariprahlad.walletbaba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.AlertDialogManager;
import services.ConnectionDetector;
import services.SessionManager;

public class LoginActivity extends AppCompatActivity {
    EditText PhoneNumber,PinNumber;
    Button LoginButton;
    TextView clicktoreg;
    private ProgressDialog pDialog;
    private String Phone,Pin;
    SessionManager session;
    SharedPreferences.Editor editor;
    ConnectionDetector connection;
    AlertDialogManager alert = new AlertDialogManager();
    public static String registerationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PhoneNumber = (EditText)findViewById(R.id.phonenumber);
        PinNumber = (EditText)findViewById(R.id.pinnumber);
        LoginButton = (Button)findViewById(R.id.loginbutton);
        clicktoreg = (TextView)findViewById(R.id.registerhere);
        session = new SessionManager(getApplicationContext());
        connection = new ConnectionDetector(getApplicationContext());
        RelativeLayout loginrelative = (RelativeLayout)findViewById(R.id.loginrelative);
        loginrelative.setAlpha(0.9f);
        if (!connection.isNetworkOn(getApplicationContext())) {
            // Internet Connection is Present
            // make HTTP requests
            alert.showAlertDialog(LoginActivity.this,"Sorry Theire Is A Network Problem",false);
        }
    }
    public void ClickToRegister(View V){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
    public void Login(View view){
         Phone = PhoneNumber.getText().toString().replace(" ","%20");
         Pin = PinNumber.getText().toString().replace(" ","%20");
        if(Phone.equals("")){
            PhoneNumber.setError("Enter Phone Number");
        }
        else if(Phone.length()<10){
            PhoneNumber.setError("Enter 10 digit Phone Number");
        }
        else if(Pin.equals("")){
            PinNumber.setError("Enter Pin Number");
        }
        else if(Pin.length()<4||Pin.length()>4){
            PinNumber.setError("Enter 4 digit Pin Number");
        }
        else{
            login(Phone,Pin);

        }
    }
    public void timerDelayRemoveDialog(long time, final AlertDialog d){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);

    }
    private void login(String phoneNumber, String pinNumber) {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Please Wait....");
        pDialog.setCancelable(false);
        pDialog.show();
        timerDelayRemoveDialog(10*1000,pDialog);
        Ion.with(getApplicationContext())
                .load("POST","http://walletbaba.com/sources/login?phone="+phoneNumber+"&pin="+pinNumber)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(e!=null){
                        }
                        else {
                            try{
                                JSONObject j = new JSONObject(result);
                                int status = j.getInt("status");
                                if(status==1){
                                    if(pDialog.isShowing())
                                        pDialog.dismiss();
                                    if (!connection.isNetworkOn(getApplicationContext())) {
                                        // Internet Connection is Present
                                        // make HTTP requests
                                        alert.showAlertDialog(LoginActivity.this,"Sorry Theire Is A Network Problem",false);
                                    }
                                    session.createLoginSession(Phone,Pin);
                                    JSONObject object = j.getJSONObject("user");
                                    registerationId = object.getString("id");
                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    editor = settings.edit();
                                    editor.putString("teamid", registerationId);
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    // Add new Flag to start new Activity
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }else {
                                    JSONArray array = j.getJSONArray("errors");
                                    JSONObject json = array.getJSONObject(0);
                                    String error = json.getString("message");
                                    if(pDialog.isShowing())
                                        pDialog.dismiss();
                                    alert.showAlertDialog(LoginActivity.this,error,false);
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                })
                .show();

    }

}
