package relay.book.reservation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class Reservation_book {

	static String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/Reservation_List.jsp";

	// 문자를 보낸 항목을 Reservation 테이블에 입력한다.
	public static void send_reserV(String phone, String R_ID) {

		DefaultHttpClient client = new DefaultHttpClient();

		try {
			/* 체크할 id와 pwd값 서버로 전송 */
			String phone1 = URLEncoder.encode(phone, "UTF-8");// 한글인코딩 처리를 위해 한번
															 // 변환해줌
			String R_ID1 = URLEncoder.encode(R_ID, "UTF-8");// 한글인코딩 처리를 위해 한번
															// 변환해줌

			HttpPost post = new HttpPost(URL_book_inform + "?phone=" + phone1 + "&R_ID=" + R_ID1);

			/* 지연시간 최대 5초 */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);

		} catch (Exception e) {
			// TODO: handle exception

		}
	}

	// 구매완료한 책의 소유주를 바꾼다.
	public static void Chnage_seller(String phone, String R_ID) {

		String URL_Complete_Sell = "http://14.63.212.134:8080/MyRelayServer/Change_Seller.jsp";
		DefaultHttpClient client = new DefaultHttpClient();

		try {

			/* 체크할 id와 pwd값 서버로 전송 */
			String phone1 = URLEncoder.encode(phone, "UTF-8");// 한글인코딩 처리를 위해 한번
																// 변환해줌
			String R_ID1 = URLEncoder.encode(R_ID, "UTF-8");// 한글인코딩 처리를 위해 한번
															// 변환해줌

			HttpPost post = new HttpPost(URL_Complete_Sell + "?phone=" + phone1
					+ "&R_ID=" + R_ID1);

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

			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception

		}
	}

}
