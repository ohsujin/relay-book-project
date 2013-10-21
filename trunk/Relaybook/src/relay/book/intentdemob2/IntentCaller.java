package relay.book.intentdemob2;


import java.io.File;

import org.json.*;



import relay.book.Option.PhoneNum;
import relay.book.http.Send_Recv.HttpHost;
import relay.book.saveInform.SchoolList;


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
        
        /* 이미지 저장 폴더 생성 */
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Relaybook/");
	     if(! path.isDirectory()) {
	             path.mkdirs();
	     }
	     
        EditText editText = (EditText)findViewById(R.id.Phone);
        editText.setText(getMy10DigitPhoneNumber());
        
        PhoneNum.setPhoneNum(getMy10DigitPhoneNumber()); //PhoneNum class에 핸드폰 번호 저
        int i=1;
        while(i<4){
        	
	        /* 서버를 통해 가입유무 확인 */
	        String regi_Chk =  http.ChkRegister(getMy10DigitPhoneNumber()).trim();
	     
	        
	        if(regi_Chk.equalsIgnoreCase("join")){  // 서버에서 join을 반환하면 가입된것이므로 로그인 화면을 건너 뛰게 해준다.
	        	System.out.println("가입됨: "+regi_Chk);
	
	        	Intent myIntent = new Intent(IntentCaller.this, Tab.class);
	        	IntentCaller.this.startActivity(myIntent); //새로운 액티비티 이동
	        	
	        	//finish(); //이전 액티비티 종료
	        	break;
	        }else if(regi_Chk.equalsIgnoreCase("unjoin")){
	        	System.out.println("가입안됨 : "+regi_Chk);
	        	break;
	        }else{
	        	
	        		Toast.makeText(getApplicationContext(), "서버와의 통신을 실패했습니다. 재접속 시도 "+i+"번", Toast.LENGTH_SHORT).show();
		        	System.out.println("접속실패 : "+regi_Chk);
		        	 try {
		 				Thread.sleep(1000);
		 			} catch (InterruptedException e) {
		 				// TODO Auto-generated catch block
		 				e.printStackTrace();
		 			}
		        	 i++;
	        	
	        }
	      
	        if(i == 4){
	        	 Toast.makeText(getApplicationContext(), "서버와 통신 할 수 없습니다. 네트워크 상태를 점검해주세요.", Toast.LENGTH_LONG).show();
	        	 finish();
	        }
	        
	        
        /**/
        }  

	        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.University);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,sch_list.COUNTRIES);
	        textView.setAdapter(adapter);
        
	        Button button = (Button) findViewById(R.id.Send);
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
        
        
        
        Intent intent = new Intent(this, Tab.class);        
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