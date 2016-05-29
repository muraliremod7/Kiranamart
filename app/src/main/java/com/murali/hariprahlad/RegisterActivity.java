package com.murali.hariprahlad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.murali.hariprahlad.walletbaba.R;

import java.util.jar.Attributes;

public class RegisterActivity extends AppCompatActivity {
    EditText Name,Phone,Email,StreetOne,StreetTwo,Landmark,Postalcode;
    AutoCompleteTextView State;
    Spinner Location;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    TextView clicktologin;
    private ArrayAdapter<String> adapter;
    String item[];
    String ReState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name = (EditText)findViewById(R.id.registername);
        Phone = (EditText)findViewById(R.id.registerphone);
        Email = (EditText)findViewById(R.id.registeremail);
        StreetOne = (EditText)findViewById(R.id.registerstreetone);
        StreetTwo = (EditText)findViewById(R.id.registerstreettwo);
        Landmark = (EditText)findViewById(R.id.registerlandmark);
        Postalcode = (EditText)findViewById(R.id.registerpostalcode);
        State = (AutoCompleteTextView)findViewById(R.id.registerstate);
        Location = (Spinner)findViewById(R.id.registerlocation);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        clicktologin = (TextView)findViewById(R.id.registerhere);
        item = getResources().getStringArray(R.array.Locations);
        RelativeLayout loginrelative = (RelativeLayout)findViewById(R.id.registerrelative);
        loginrelative.setAlpha(0.9f);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
        State.setThreshold(1);
        //Set adapter to AutoCompleteTextView
        State.setAdapter(adapter);
        State.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                ReState = (String)parent.getItemAtPosition(position);
                //TODO Do something with the selected text
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
        String ReEmail = Email.getText().toString();
        String ReStreetOne = StreetOne.getText().toString().replace(" ","%20");
        String ReStreetTwo = StreetTwo.getText().toString().replace(" ","%20");
        String ReLandmark = Landmark.getText().toString().replace(" ","%20");
        String RePostalcode = Postalcode.getText().toString();
        String ReArea = Location.getSelectedItem().toString();
        String ReSex = radioSexButton.getText().toString();
        if (ReName == null || RePhone == null || ReEmail == null || ReStreetOne == null || ReStreetTwo == null || ReLandmark == null || RePostalcode==null) {
            Name.setError("Enter This Field");
            Phone.setError("Enter This Field");
            Email.setError("Enter This Field");
            StreetOne.setError("Enter This Field");
            StreetTwo.setError("Enter This Field");
            Landmark.setError("Enter This Field");
            Postalcode.setError("Enter This Field");
        }

    }
}
