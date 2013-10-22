package relay.book.Mylist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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

import android.util.Log;

public class Update_book_json {
	
	
	
	
	//------------------------------ 
	  //   Http Post로 주기
	  //------------------------------ 
		public void HttpPostData(String msg){
			try{
				   HttpClient client = new DefaultHttpClient();
				
				   String postURL = "http://14.63.212.134/MyRelayServer/UpdateBookInform.jsp";
				 
				   HttpPost post = new HttpPost(postURL);
				   List params = new ArrayList(); // 파라미터를 List에 담아서 보냅니다.
				  
				   params.add(new BasicNameValuePair("update_book", msg)); //파라미터 이름, 보낼 데이터 순입니다.
				   
  	
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
