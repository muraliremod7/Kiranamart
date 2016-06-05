package com.murali.hariprahlad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.murali.hariprahlad.walletbaba.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import model.SpinnerListRow;

public class RegisterActivity extends AppCompatActivity {
    EditText Name,Phone,Email,AlPhone,StreetOne,StreetTwo,City,Postalcode,PINNUMBER;
    String ReLocationID,ReState;
    AutoCompleteTextView State;
    Spinner Location;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    TextView clicktologin;
    private ArrayAdapter<String> adapter;
    String[] locationName,locationid,locationname;
    ArrayList<String> locationID = new ArrayList<>();
    ArrayList<String> locationNAME = new ArrayList<>();
    public String newUserId;
    SharedPreferences.Editor registerid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        PINNUMBER = (EditText)findViewById(R.id.newpinnumber);
        Name = (EditText)findViewById(R.id.registername);
        Phone = (EditText)findViewById(R.id.registerphone);
        AlPhone = (EditText)findViewById(R.id.registeralternatephone);
        Email = (EditText)findViewById(R.id.registeremail);
        StreetOne = (EditText)findViewById(R.id.registerstreetone);
        StreetTwo = (EditText)findViewById(R.id.registerstreettwo);
        City = (EditText)findViewById(R.id.registerlandmark);
        Postalcode = (EditText)findViewById(R.id.registerpostalcode);
        State = (AutoCompleteTextView)findViewById(R.id.registerstate);
        Location = (Spinner)findViewById(R.id.registerlocation);
        getlocations();
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        clicktologin = (TextView)findViewById(R.id.registerhere);

        locationName = getResources().getStringArray(R.array.Locations);
        RelativeLayout loginrelative = (RelativeLayout)findViewById(R.id.registerrelative);
        loginrelative.setAlpha(0.9f);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locationName);
        State.setThreshold(1);
        //Set adapter to AutoCompleteTextView
        State.setAdapter(adapter);
        State.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                ReState = (String)parent.getItemAtPosition(position);
                //TODO Do something with the selected text
            }
        });
        findViewById(R.id.closelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View forgotLayout = findViewById(R.id.pingeneratelayout);
                //forgotLayout.setAnimation(AnimationUtils.makeInChildBottomAnimation(getApplicationContext()));
                forgotLayout.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.registerlayout).setVisibility(View.VISIBLE);
                    }
                }, 200);
            }
        });
    }

    private void getlocations() {
        Ion.with(getApplicationContext())
                .load("POST","http://walletbaba.com/sources/getLocations")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    if(e!=null){

                    }else {
                        try {
                            JSONObject j = new JSONObject(result);
                            int status = j.getInt("status");
                            if (status == 1) {
                                JSONArray jsonObject = j.getJSONArray("locations");
                                for(int i =0;i<jsonObject.length();i++){
                                    JSONObject json = jsonObject.getJSONObject(i);
                                    String locationid = json.getString("id");
                                    locationID.add(locationid);
                                    String locationname = json.getString("locationName");
                                    locationNAME.add(locationname);
                                }
                                locationid = locationID.toArray(new String[locationID.size()]);
                                locationname = locationNAME.toArray(new String[locationNAME.size()]);
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        SpinnerListRow sp = new SpinnerListRow(RegisterActivity.this,locationid,locationname);
                        sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        Location.setAdapter(sp);
                        Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                ReLocationID = ((TextView)view.findViewById(R.id.locationId)).getText().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    }
                });
    }

    public void ClickToLogin(View view){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }
    public void Register(View view){
        String ReName = Name.getText().toString().replace(" ","%20");
        String RePhone = Phone.getText().toString();
        String ReAlPhone = AlPhone.getText().toString();
        String ReEmail = Email.getText().toString();
        String ReStreetOne = StreetOne.getText().toString().replace(" ","%20");
        String ReStreetTwo = StreetTwo.getText().toString().replace(" ","%20");
        String ReCity = City.getText().toString().replace(" ","%20");
        String RePostalcode = Postalcode.getText().toString();

        if (ReName == null || RePhone == null || ReEmail == null || ReStreetOne == null || ReStreetTwo == null || RePostalcode==null) {
            Name.setError("Enter This Field");
            Phone.setError("Enter This Field");
            Email.setError("Enter This Field");
            StreetOne.setError("Enter This Field");
            StreetTwo.setError("Enter This Field");
            City.setError("Enter This Field");
            Postalcode.setError("Enter This Field");
        }
        else {
            createuser(ReName,RePhone,ReAlPhone,ReEmail,ReStreetOne,ReStreetTwo,ReCity,ReLocationID,ReState,RePostalcode);
        }

    }
    private void createuser(String reName, String rePhone, final String reAlPhone, String reEmail, String reStreetOne, String reStreetTwo, String reCity, String reLocationID, String reState, String rePostalcode) {
        Ion.with(getApplicationContext())
                .load("POST","http://walletbaba.com/sources/createUser?phone="+rePhone+"&name="+reName+"&street1="+reStreetOne+"&street2="+reStreetTwo+"&city="+reCity+"&state="+reState+"&locationId="+reLocationID+"&postalCode="+rePostalcode+"&email="+reEmail+"&altphone="+reAlPhone)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    if(e!=null){

                    }
                        else {
                        try{
                            JSONObject jsonObject = new JSONObject(result);
                            int status = jsonObject.getInt("status");
                            if(status == 1){
                                Toast.makeText(RegisterActivity.this,"Register Successfully Completed",Toast.LENGTH_LONG).show();

                                JSONObject j = jsonObject.getJSONObject("user");
                                newUserId = j.getString("id");
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                registerid = settings.edit();
                                registerid.putString("teamid", newUserId);
                                registerid.commit();

                                View forgotLayout = findViewById(R.id.pingeneratelayout);
                                forgotLayout.setAnimation(AnimationUtils.makeInChildBottomAnimation(getApplicationContext()));
                                forgotLayout.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                findViewById(R.id.registerlayout).setVisibility(View.GONE);
                                            }
                                        }, 500);
                            }
                            else {
                                JSONArray array = jsonObject.getJSONArray("errors");
                                    JSONObject j = array.getJSONObject(0);
                                String error = j.getString("message");
                                Toast.makeText(RegisterActivity.this,error,Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    }
                });
    }

    public void createpinNumber(View V){

        String PinNumber = PINNUMBER.getText().toString();
        createPin(newUserId,PinNumber);
    }

    private void createPin(String newUserId, String pinNumber) {
        Ion.with(getApplicationContext())
                .load("POST","http://walletbaba.com/sources/createPIN?id="+newUserId+"&pin="+pinNumber)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    if(e!=null){

                    }
                        else {
                        try{
                        JSONObject jsonObject = new JSONObject(result);
                            int status = jsonObject.getInt("status");
                            if(status==1){
                                Toast.makeText(RegisterActivity.this,"Pin Successfully Created",Toast.LENGTH_LONG).show();
                                Intent in = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(in);
                                finish();
                            }
                            else {
                                JSONArray array = jsonObject.getJSONArray("errors");
                                JSONObject j = array.getJSONObject(0);
                                String error = j.getString("message");
                                Toast.makeText(RegisterActivity.this,error,Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception ex){

                        }
                    }
                    }
                });
    }
}
