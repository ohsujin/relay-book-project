package com.example.intentdemob2;


import org.json.*;

import relay.book.saveInform.SchoolList;

import com.Relaybook.Option.PhoneNum;
import com.http.Send_Recv.HttpHost;

import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;

import android.view.*;

import android.widget.*;

public class IntentCaller extends Activity implements View.OnClickListener {

	HttpHost http = new HttpHost();
	private  EditText passwordEdit;


	SchoolList sch_list = new SchoolList(); //학교명단이 많아 따로 클래스로 분류
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentcaller);
        
        startActivity(new Intent(this, SplashActivity.class));
        
        EditText editText = (EditText)findViewById(R.id.Phone);
        editText.setText(getMy10DigitPhoneNumber());
        
        PhoneNum.setPhoneNum(getMy10DigitPhoneNumber()); //PhoneNum class에 핸드폰 번호 저
        

        /* 서버를 통해 가입유무 확인 */
        String regi_Chk =  http.ChkRegister(getMy10DigitPhoneNumber());
        
        System.out.println("regi_Chk : "+regi_Chk);
        System.out.println(regi_Chk);
        
        
        if(regi_Chk.equalsIgnoreCase(" join")){  // 서버에서 join을 반환하면 가입된것이므로 로그인 화면을 건너 뛰게 해준다.
        	System.out.println("가입됨: "+regi_Chk);
        	finish(); //이전 액티비티 종료
        	

        	Intent myIntent = new Intent(IntentCaller.this, MainPage.class);
        	IntentCaller.this.startActivity(myIntent); //새로운 액티비티 이동

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
    
    public String getMyPhoneNumber()
    {
    	TelephonyManager mTelephonyMgr;
    	mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	
    	return mTelephonyMgr.getLine1Number();
    }
    
    
    
    public String getMy10DigitPhoneNumber()
    {
    	String s = getMyPhoneNumber();
    	//return s.substring(0);
    	return s.replace("-", "").replace("+82", "0");
    	
    }
    

    public void onClick(View v) {
        EditText phoneEdit = (EditText) findViewById(R.id.Phone);
        passwordEdit = (EditText) findViewById(R.id.Password);
        AutoCompleteTextView universityEdit = (AutoCompleteTextView) findViewById(R.id.University);
        
        
        
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