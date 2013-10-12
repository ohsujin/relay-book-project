package com.example.intentdemob2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import com.relay.image.ImageDownloader;

import relay.book.saveInform.GetPhone_Num;
import android.app.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.telephony.TelephonyManager;
import android.widget.*;

public class Read extends Activity{
	
	String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";
	Bitmap bmImg;
	 URL myFileUrl = null;
	 
	 private static final String URL = "http://14.63.212.134:8080/MyRelayServer/Image/";
	static String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/send.jsp";
	
	String filename = null;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.read);
	    
	    	getInform();
	    	
	    	System.out.println("파일 이름 : "+filename);
	    	
		    ImageView img = (ImageView)findViewById(R.id.Img); // 이미지
			ImageDownloader.download(URL+filename, img);
			
		    
			String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/send.jsp";
//			String URL = "http://14.63.212.134/MyRelayServer/JSONServer.jsp";
//			String URL = "http://121.156.253.22/hello.py";
//			String URL =  "http://api.androidhive.info/contacts/";
			
			
			
	}
	
	void getInform(){

		  String Ph_num = getMy10DigitPhoneNumber();
		DefaultHttpClient client = new DefaultHttpClient();
		
		try {
			/* 체크할 id와 pwd값 서버로 전송 */
			HttpPost post = new HttpPost(URL_book_inform+"?phone="+Ph_num);

			/* 지연시간 최대 5초 */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}
			
			System.out.println(result);
			
			JSONObject json = new JSONObject(result);	
			JSONObject rece = json.getJSONObject("Book_inform");
			
			filename = rece.getString("filename");
    
	    TextView Title_Title = (TextView)findViewById(R.id.Title_Title);
	    TextView Title = (TextView)findViewById(R.id.Title); // 책 제목
	    Title.setText(rece.getString("title"));
	
	    TextView SubjectTitle = (TextView)findViewById(R.id.Subject_Title);
	    TextView Subject = (TextView)findViewById(R.id.Subject); // 과목명
	    Subject.setText(rece.getString("subject"));
	    
	    TextView PriceTitle = (TextView)findViewById(R.id.Price_Title);
	    TextView Price = (TextView)findViewById(R.id.Price); // 가격
	    Price.setText(rece.getString("price"));
	    
	    TextView PublisherTitle = (TextView)findViewById(R.id.Publisher_Title);
	    TextView Publisher = (TextView)findViewById(R.id.Publisher); // 출판사
	    Publisher.setText(rece.getString("publisher"));
	    
	    TextView WriterTitle = (TextView)findViewById(R.id.Writer_Title);
	    TextView Writer = (TextView)findViewById(R.id.Writer); //가격
	    Writer.setText(rece.getString("writer"));
	    
    
	} catch (Exception e) {
		e.printStackTrace();
		client.getConnectionManager().shutdown();	// 연결 지연 종료		
	}
		
	}
	
	/* 전화번호 불러오기 */
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
	
}
