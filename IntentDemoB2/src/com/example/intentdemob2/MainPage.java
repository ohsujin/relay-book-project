package com.example.intentdemob2;

import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.view.*;
import android.widget.*;

public class MainPage extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
      
        Button btn1 = (Button)findViewById(R.id.Buy);
		btn1.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainPage.this, Write.class);
				startActivity(intent);
			}
		});
		
		Button btn2 = (Button)findViewById(R.id.Sell);
		btn2.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainPage.this, Write.class);
				
				startActivity(intent);
				
				
			}
		});
		
		
		
		
		
	}
	

}
