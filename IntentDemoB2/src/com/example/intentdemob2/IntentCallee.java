package com.example.intentdemob2;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class IntentCallee extends Activity implements View.OnClickListener {   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentcallee);
        
        Button button = (Button) findViewById(R.id.Close);
        button.setOnClickListener(this);
        
        Button button2 = (Button) findViewById(R.id.ListView);
        button2.setOnClickListener(this);
        
        this.setData();
      
    }

    public void setData() {
        Intent intent = getIntent();
        
        String phone = intent.getExtras().get("phone").toString();
        String password = intent.getExtras().get("password").toString();        
        String university = intent.getExtras().get("university").toString();        
        
        
        TextView phoneText = (TextView) findViewById(R.id.Phone);        
        phoneText.setText(phone);
        
        TextView passwordText = (TextView) findViewById(R.id.Password);
        passwordText.setText(password);
        
        TextView universityText = (TextView) findViewById(R.id.University);
        universityText.setText(university);
        
    }

    public void onClick(View v) {
    	Intent intent = new Intent(this, ListViewTest.class);
    	startActivity(intent);
    	
        finish();
    }
}
