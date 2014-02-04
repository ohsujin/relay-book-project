package relay.book.Option;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import relay.book.intentdemob2.R;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Option extends Activity {

	static String URL_delete_member = "http://14.63.212.134/MyRelayServer/Member_out.jsp";

	// 어플의 기본 옵션을 설정해주는 Class 입니다.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option);
		
		final SharedPreferences pref = getSharedPreferences("relaybook_Setting", MODE_PRIVATE);
		final SharedPreferences.Editor editor = pref.edit();

		final RadioButton search_option1 = (RadioButton) findViewById(R.id.All_school);
		search_option1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putString("Search_option", "All");
				editor.commit();
			}
		});
		
		final RadioButton search_option2 = (RadioButton) findViewById(R.id.My_school);
		search_option2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putString("Search_option", "My");
				editor.commit();
			}
		});
		
		
		
		Button button = (Button) findViewById(R.id.SignOut);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertTitle = getResources().getString(R.string.app_name);
				String buttonMessage = getResources().getString(
						R.string.alert_msg_out);
				String buttonYes = getResources().getString(R.string.button_yes);
				String buttonNo = getResources().getString(R.string.button_no);

				new AlertDialog.Builder(Option.this)
						.setTitle(alertTitle)
						.setMessage(buttonMessage)
						.setPositiveButton(buttonYes,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Delete_member(PhoneNum.getPhoneNum());

									}
								}).setNegativeButton(buttonNo, null).show();
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
	
	/* 종료묻기 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			String alertTitle = getResources().getString(R.string.app_name);
			String buttonMessage = getResources().getString(
					R.string.alert_msg_exit);
			String buttonYes = getResources().getString(R.string.button_yes);
			String buttonNo = getResources().getString(R.string.button_no);

			new AlertDialog.Builder(Option.this)
					.setTitle(alertTitle)
					.setMessage(buttonMessage)
					.setPositiveButton(buttonYes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									moveTaskToBack(true);
									finish();
								}
							}).setNegativeButton(buttonNo, null).show();
		}
		return true;
	}

}
