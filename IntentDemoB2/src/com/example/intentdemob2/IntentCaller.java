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

import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class IntentCaller extends Activity implements View.OnClickListener {
	static final String[] COUNTRIES = new String[]{"aa", "aaa","ascc","aacsd","bb","bfdb"};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentcaller);
        
        EditText editText = (EditText)findViewById(R.id.Phone);
        editText.setText(getMy10DigitPhoneNumber());
        
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.University);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,COUNTRIES);
        textView.setAdapter(adapter);
        
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
        AutoCompleteTextView universityEdit = (AutoCompleteTextView) findViewById(R.id.University);
        
        Intent intent = new Intent(this, IntentCallee.class);        
        intent.putExtra("phone", phoneEdit.getText());
        intent.putExtra("password", passwordEdit.getText());
        intent.putExtra("university", universityEdit.getText());
        startActivity(intent);
        
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

		
		HttpPostData(listData.toString());
		 
	
	}
    
    
  //------------------------------ 
  //   Http Post로 주기
  //------------------------------ 
	public void HttpPostData(String msg){
		try{
			HttpClient client = new DefaultHttpClient();
			   String postURL = "http://192.168.25.6:8080/MyServer/JSONServer.jsp";
//			   String postURL = "http://14.63.212.134/MyServer/JSONServer.jsp";
//			   String postURL = "http://121.156.253.22/hello.py";
			   
			   HttpPost post = new HttpPost(postURL);
			   List params = new ArrayList(); // 파라미터를 List에 담아서 보냅니다.
			   params.add(new BasicNameValuePair("regi_JSON", msg)); //파라미터 이름, 보낼 데이터 순입니다.
			   
			   UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			   post.setEntity(ent);
			   HttpResponse responsePOST = client.execute(post);
			   HttpEntity resEntity = responsePOST.getEntity();
			   
			  if (resEntity != null) {
			   Log.w("RESPONSE", EntityUtils.toString(resEntity));
			  }
			  }catch (Exception e) {
			   // TODO: handle exception
			  }
			 
	}
}