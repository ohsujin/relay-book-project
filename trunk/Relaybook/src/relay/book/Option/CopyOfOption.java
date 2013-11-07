package relay.book.Option;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import relay.book.intentdemob2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CopyOfOption extends Activity {

	static String URL_delete_member = "http://14.63.212.134/MyRelayServer/Member_out.jsp";

	// 어플의 기본 옵션을 설정해주는 Class 입니다.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option);

		Button button = (Button) findViewById(R.id.SignOut);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Delete_member(PhoneNum.getPhoneNum());

			}
		});

	}

	void Delete_member(String phone) {

		DefaultHttpClient client = new DefaultHttpClient();

		try {
			/* 삭제할 phone 번호를 서버로 전송 */

			HttpPost post = new HttpPost(URL_delete_member + "?Phone_Num="
					+ phone);

			/* 지연시간 최대 5초 */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"utf-8"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}

			if (result.equals("delete")) {
				Toast T = Toast.makeText(getApplicationContext(),
						"RelayBook 에서 탈퇴 하였습니다..", Toast.LENGTH_LONG);
				T.setGravity(Gravity.TOP, 0, 400);
				T.show();

				finish();
				finish();
			}

		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown(); // 연결 지연 종료
		}

	}

}
