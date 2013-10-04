package com.example.intentdemob2;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Choice extends Activity implements View.OnClickListener {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
		
        Button buy = (Button) findViewById(R.id.Buy);
        buy.setOnClickListener(this);
        
        Button sell = (Button) findViewById(R.id.Sell);
        sell.setOnClickListener(this);
	}
	
	public void onClick(View v) {
    	Intent intent = new Intent(this, Choice.class);
    	startActivity(intent);
    	
        finish();
    }
	
}
