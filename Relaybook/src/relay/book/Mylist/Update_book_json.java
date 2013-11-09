package relay.book.Mylist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

	// ------------------------------
	// 책정보 수정
	// ------------------------------
	public void HttpPostData(String msg) {
		try {
			HttpClient client = new DefaultHttpClient();

			String postURL = "http://14.63.212.134/MyRelayServer/UpdateBookInform.jsp";

			HttpPost post = new HttpPost(postURL);
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); // 파라미터를
																					// List에
																					// 담아서
																					// 보냅니다.

			params.add(new BasicNameValuePair("update_book", msg)); // 파라미터 이름,
																	// 보낼 데이터
																	// 순입니다.

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();

			if (resEntity != null) {
				Log.w("RESPONSE", EntityUtils.toString(resEntity));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void Complete_Sell(String msg) {
		try {
			HttpClient client = new DefaultHttpClient();

			String postURL = "http://14.63.212.134/MyRelayServer/Complete_Sell.jsp";

			HttpPost post = new HttpPost(postURL);
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); // 파라미터를
																					// List에
																					// 담아서
																					// 보냅니다.

			params.add(new BasicNameValuePair("R_ID", msg)); // 파라미터 이름, 보낼 데이터
																// 순입니다.

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();

			if (resEntity != null) {
				Log.w("RESPONSE", EntityUtils.toString(resEntity));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void Enalbe_sell(String R_ID,String Reser_date) {

		DefaultHttpClient client = new DefaultHttpClient();

		try {

			String postURL = "http://14.63.212.134/MyRelayServer/Enable_sell.jsp";

			/* 체크할 id와 pwd값 서버로 전송 */
			HttpPost post = new HttpPost(postURL + "?R_ID=" + R_ID + "&Reser_date=" + Reser_date);

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

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}