package com.example.intentdemob2;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class IntentCaller extends Activity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentcaller);
        
        Button button = (Button) findViewById(R.id.Send);
        button.setOnClickListener(this);            
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