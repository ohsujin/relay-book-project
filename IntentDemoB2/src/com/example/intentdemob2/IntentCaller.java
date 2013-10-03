package com.example.intentdemob2;

import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.view.*;
import android.widget.*;

public class IntentCaller extends Activity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentcaller);
        
        EditText editText = (EditText)findViewById(R.id.Phone);
        editText.setText(getMy10DigitPhoneNumber());
        
        Button button = (Button) findViewById(R.id.Send);
        button.setOnClickListener(this);            
    }

    private String getMyPhoneNumber()
    {
    	TelephonyManager mTelephonyMgr;
    	mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	
    	return mTelephonyMgr.getLine1Number();
    }
    
    private String getMy10DigitPhoneNumber()
    {
    	String s = getMyPhoneNumber();
    	return s.substring(0);
    }
    
    public void onClick(View v) {
        EditText phoneEdit = (EditText) findViewById(R.id.Phone);
        EditText passwordEdit = (EditText) findViewById(R.id.Password);
        EditText universityEdit = (EditText) findViewById(R.id.University);
        
        Intent intent = new Intent(this, IntentCallee.class);        
        intent.putExtra("phone", phoneEdit.getText());
        intent.putExtra("password", passwordEdit.getText());
        intent.putExtra("university", universityEdit.getText());
        startActivity(intent);
    }
}