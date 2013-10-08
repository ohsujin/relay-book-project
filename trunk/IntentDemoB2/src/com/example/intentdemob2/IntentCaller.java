package com.example.intentdemob2;

import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.protocol.*;
import org.apache.http.util.*;
import org.json.*;

import relay.book.saveInform.SchoolList;

import com.http.Send_Recv.HttpHost;

import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class IntentCaller extends Activity implements View.OnClickListener {

	HttpHost http = new HttpHost();
	private  EditText passwordEdit;

	SchoolList sch_list = new SchoolList(); //학교명단이 많아 따로 클래스로 분류
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentcaller);
        
        EditText editText = (EditText)findViewById(R.id.Phone);
        editText.setText(getMy10DigitPhoneNumber());


        /* 서버를 통해 가입유무 확인 */
        String regi_Chk = http.ChkRegister(getMy10DigitPhoneNumber());
        
        
        if(regi_Chk.equals("join")){  // 서버에서 join을 반환하면 가입된것이므로 로그인 화면을 건너 뛰게 해준다.
        	System.out.println("가입됨: "+regi_Chk );
        	finish(); //이전 액티비티 종료
        	

        	Intent myIntent = new Intent(IntentCaller.this, MainPage.class);
        	IntentCaller.this.startActivity(myIntent); //새로운 액티비티 이동
        	
        	//passwordEdit = (EditText) findViewById(R.id.Password);
        	//passwordEdit.setText("가입됨");

        }else{
        	System.out.println("가입안됨 : "+regi_Chk);
        }
        
        /**/
       
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.University);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,sch_list.COUNTRIES);
        textView.setAdapter(adapter);
        

        ImageButton button = (ImageButton) findViewById(R.id.Send);
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
        passwordEdit = (EditText) findViewById(R.id.Password);
        AutoCompleteTextView universityEdit = (AutoCompleteTextView) findViewById(R.id.University);
        
        
        // 키보드 보이기 --->> 추후 삭제 요
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(universityEdit, InputMethodManager.SHOW_FORCED);
        /* --------------------------------------------------------*/
        
        Intent intent = new Intent(this, MainPage.class);        
        intent.putExtra("phone", phoneEdit.getText());
        intent.putExtra("password", passwordEdit.getText());
        intent.putExtra("university", universityEdit.getText());
        
        startActivity(intent);
        finish();
        
        /* 서버로 넘기기 */
        JSONObject regi = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject listData = new JSONObject();
		
		try {
			
			regi.put("phone", phoneEdit.getText().toString());
			regi.put("school", universityEdit.getText().toString() );
			regi.put("passwd",passwordEdit.getText().toString());
			
			listData.put("Register", regi);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		http.HttpPostData(listData.toString());

	}
       
}