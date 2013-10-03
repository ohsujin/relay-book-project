package com.example.json_test;


import net.sf.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.send.Register;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Json_MysqlTest extends Activity {

	private EditText etMessage;
	private EditText etMessage1;
	private EditText etMessage2;
	private Button btnSend;
	private TextView tvRecvData;
	private Button btn_Mysql;
	
	
	TextView result;
	

		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json__mysql_test);
		
		etMessage = (EditText) findViewById(R.id.et_message);
		etMessage1 = (EditText) findViewById(R.id.et_message1);
		etMessage2 = (EditText) findViewById(R.id.et_message2);
		btnSend = (Button) findViewById(R.id.btn_sendData);
		btn_Mysql = (Button) findViewById(R.id.btn_Mysql);
	
		tvRecvData = (TextView)	findViewById(R.id.tv_recvData);
		result = (TextView)findViewById(R.id.result);
		
		// 가입정보 저장 객체 생성
		final Register regi = new Register();

		/*	Send 버튼을 눌렀을 때 서버에 데이터를 보내고 받는다	*/
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String sMessage = etMessage1.getText().toString(); // 보내는 메시지를 받아옴
				/* JSON으로 묶기 */
				JSONObject regi = new JSONObject();
				JSONArray arr = new JSONArray();
				JSONObject listData = new JSONObject();
				
				try {
					
					regi.put("phone", etMessage1.getText().toString());
					regi.put("school", etMessage.getText().toString());
					regi.put("passwd", etMessage2.getText().toString());
					
					listData.put("Register", regi);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
//				String result = SendByHttp(listData.toString()); // 메시지를 서버에 보냄
//				String[][] parsedData = jsonParserList(result); // JSON 데이터 파싱
				
				HttpPostData(listData.toString());
				 
				tvRecvData.setText(listData.toString());	// 받은 메시지를 화면에 보여주기
			}
		});

		btn_Mysql.setOnClickListener(new Button.OnClickListener() { 
            public void onClick(View v) { 
            	String sMessage = etMessage.getText().toString(); // 보내는 메시지를 받아옴
				String result = SendByHttp("1"); // 메시지를 서버에 보냄
				String[][] parsedData = jsonParserList(result);
            	
				
				
            	jsonParser(result); 
            } 
        }); 

	}
	
	/**
	 * 서버에 데이터를 보내는 메소드
	 * @param msg
	 * @return
	 */
	private String SendByHttp(String msg) {
		if(msg == null)
			msg = "";
		
		String URL = "http://192.168.25.6:8080/MyServer/JSONServer.jsp";
//		String URL = "http://14.63.212.134/MyServer/JSONServer.jsp";
//		String URL =  "http://api.androidhive.info/contacts/";
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			/* 체크할 id와 pwd값 서버로 전송 */
			HttpPost post = new HttpPost(URL+"?phone="+msg);
			
			
			/* 지연시간 최대 5초 */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"utf-8"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}
	
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();	// 연결 지연 종료
			return ""; 
		}
		
	}
	
	//------------------------------ 
    //   Http Post로 주기
    //------------------------------ 
	public void HttpPostData(String msg){
		try{
			HttpClient client = new DefaultHttpClient();
//			   String postURL = "http://192.168.25.6:8080/MyServer/JSONServer.jsp";
//			   String postURL = "http://14.63.212.134/MyServer/JSONServer.jsp";
			   String postURL = "http://121.156.253.22/hello.py";
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

		

	/**
	 * 받은 JSON 객체를 파싱하는 메소드
	 * @param page
	 * @return
	 */
	
	private String[][] jsonParserList(String pRecvServerPage) {
		
		Log.i("서버에서 받은 전체 내용 : ", pRecvServerPage);
		
		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("List");


			// 받아온 pRecvServerPage를 분석하는 부분
			String[] jsonName = {"msg1", "msg2", "msg3"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);
				
				for(int j = 0; j < jsonName.length; j++) {
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}
						
			// 분해 된 데이터를 확인하기 위한 부분
			for(int i=0; i<parseredData.length; i++){
				Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);
				Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);
				Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][2]);
			}

			return parseredData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String jsonParser(String pRecvServerPage){
	     
	     String resultStr = "";
	     
	     try {
	      //JSON String으로 부터 JSONArray 생성. [](대괄호)
	    	 JSONObject json = new JSONObject(pRecvServerPage);
	    	
	    	 JSONArray jArr = json.getJSONArray("List");
	    	 
	    	
	   
	   for (int i = 0; i < jArr.length(); i++) {
	    //JSONArray에서 i번째 해당하는 JSONObject를 추출.
	    JSONObject jObj = jArr.getJSONObject(i);
	    
	    //각 이름("id"/"tel")에 해당하는 값을 추출.
	    resultStr += String.format("학교 : %s   전화번호 : %s	패스워드 : %s\n", 
	        jObj.getString("school"), jObj.getString("Phone"),jObj.getString("passwd"));   
	   }
	   	
	   result.setText(resultStr);
	   	 
	   	return null;
	  } catch (JSONException e) {
//	   Toast.makeText(JSONActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		  return null;
	  }
		
	
	}
	
}
