package relay.book.intentdemob2;

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

import relay.book.intentdemob2.R;

import relay.book.Option.PhoneNum;
import relay.book.image.ImageDownloader;


import android.app.*;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.telephony.TelephonyManager;
import android.view.*;
import android.widget.*;

public class Read extends Activity{
	
	String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";
	Bitmap bmImg;
	 URL myFileUrl = null;
	 
	 
	 private static final String URL = "http://14.63.212.134:8080/MyRelayServer/Image/";
	static String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/Send.jsp";
	
	String filename = null;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.read);
	    	
	    int i=1;
	    	getInform();
	    	
	    	
	    	System.out.println("파일 이름 : "+filename);
	    	
		    ImageView img1 = (ImageView)findViewById(R.id.Img1); // 이미지
		    ImageView img2 = (ImageView)findViewById(R.id.Img2); // 이미지
		    ImageView img3 = (ImageView)findViewById(R.id.Img3); // 이미지
		    
			ImageDownloader.download(URL+filename+"_1.jpg", img1);
			ImageDownloader.download(URL+filename+"_2.jpg", img2);
			ImageDownloader.download(URL+filename+"_3.jpg", img3);
			
		    
			String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/send.jsp";
//			String URL = "http://14.63.212.134/MyRelayServer/JSONServer.jsp";
//			String URL = "http://121.156.253.22/hello.py";
//			String URL =  "http://api.androidhive.info/contacts/";
			
			
			
	}
	
	void getInform(){

		  String Ph_num = PhoneNum.getPhoneNum();
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
    
		
		    TextView Title = (TextView)findViewById(R.id.Title); // 책 제목
		    Title.setText(rece.getString("title"));
		   
		    TextView Subject = (TextView)findViewById(R.id.Subject); // 과목명
		    Subject.setText(rece.getString("subject"));
		   
		    TextView Price = (TextView)findViewById(R.id.Price); // 가격
		    Price.setText(rece.getString("price"));
		   
		    TextView Publisher = (TextView)findViewById(R.id.Publisher); // 출판사
		    Publisher.setText(rece.getString("publisher"));
		 
		    TextView Writer = (TextView)findViewById(R.id.Writer); //가격
		    Writer.setText(rece.getString("writer"));
		   
		    TextView Memo = (TextView)findViewById(R.id.Memo); //가격
		    Memo.setText(rece.getString("memo"));
		    
		    RatingBar rating = (RatingBar) findViewById(R.id.Quality_view); 
		    rating.setRating(Float.parseFloat(rece.getString("quality")));
		    
    
			} catch (Exception e) {
				e.printStackTrace();
				client.getConnectionManager().shutdown();	// 연결 지연 종료		
			}
			
	}
	
	/* 종료묻기 */
	public boolean onKeyDown(int keyCode, KeyEvent event){
	     switch(keyCode){
	     case KeyEvent.KEYCODE_BACK:
	      String alertTitle = getResources().getString(R.string.app_name);
	      String buttonMessage = getResources().getString(R.string.alert_msg_exit);
	      String buttonYes = getResources().getString(R.string.button_yes);
	      String buttonNo = getResources().getString(R.string.button_no);
	         
	      new AlertDialog.Builder(Read.this)
	      .setTitle(alertTitle)
	      .setMessage(buttonMessage)
	      .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
	       
	       @Override
	       public void onClick(DialogInterface dialog, int which) {
	        // TODO Auto-generated method stub
	        moveTaskToBack(true);
	        finish();
	       }
	      })
	      .setNegativeButton(buttonNo, null)
	      .show();
	     }
	    return true;
	}
	
}
