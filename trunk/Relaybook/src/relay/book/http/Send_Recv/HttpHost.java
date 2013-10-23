package relay.book.http.Send_Recv;

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

public class HttpHost {
	
	
	public String ChkRegister(String msg) {
		if(msg == null)
			msg = "";


		String URL = "http://14.63.212.134/MyRelayServer/JSONServer.jsp";
		
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
			BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));

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
				
				   String postURL = "http://14.63.212.134/MyRelayServer/JSONServer.jsp";
				 
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
