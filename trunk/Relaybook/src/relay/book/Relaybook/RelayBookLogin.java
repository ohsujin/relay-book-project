package relay.book.Relaybook;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import relay.book.Option.PhoneNum;
import relay.book.http.Send_Recv.HttpHost;
import relay.book.intentdemob2.R;
import relay.book.saveInform.SchoolList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;

public class RelayBookLogin extends Activity implements View.OnClickListener {

	ScrollView scrollview;
	
	HttpHost http = new HttpHost();
	private EditText passwordEdit;
	private EditText RepasswordEdit;

	SchoolList sch_list = new SchoolList(); // 학교명단이 많아 따로 클래스로 분류

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intentcaller);

		scrollview = (ScrollView) findViewById(R.id.Scroll);
		
		/* 이미지 저장 폴더 생성 */
		File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Relaybook/");
		if (!path.isDirectory()) {
			path.mkdirs();
		}

		EditText editText = (EditText) findViewById(R.id.Phone);
		editText.setText(PhoneNum.getPhoneNum());

		int i = 1;
		
		while (i < 4) {

			/* 서버를 통해 가입유무 확인 */
			String regi_Chk = http.ChkRegister(PhoneNum.getPhoneNum()).trim();

			if (regi_Chk.equalsIgnoreCase("join")) { // 서버에서 join을 반환하면 가입된것이므로
														// 로그인 화면을 건너 뛰게 해준다.
				System.out.println("가입됨: " + regi_Chk);

				Intent myIntent = new Intent(RelayBookLogin.this, Tab.class);
				RelayBookLogin.this.startActivity(myIntent); // 새로운 액티비티 이동

				finish(); // 이전 액티비티 종료
				break;
			} else if (regi_Chk.equalsIgnoreCase("unjoin")) {
				System.out.println("가입안됨 : " + regi_Chk);
				break;
			} else {

				Toast.makeText(getApplicationContext(),
						"서버와의 통신을 실패했습니다. 재접속 시도 " + i + "번",Toast.LENGTH_SHORT).show();
				System.out.println("접속실패 : " + regi_Chk);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}

			if (i == 4) {
				Toast.makeText(getApplicationContext(),
						"네트워크 연결 상태가 좋지 않습니다.\n3G 또는 WIFI 연결 상태를 확인해 주세요.",	Toast.LENGTH_LONG).show();
				finish();
			}
			/**/

		}

		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.University);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, SchoolList.Schools);
		textView.setAdapter(adapter);

		Button button = (Button) findViewById(R.id.Send);
		button.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		EditText phoneEdit = (EditText) findViewById(R.id.Phone);
		passwordEdit = (EditText) findViewById(R.id.Password);
		RepasswordEdit = (EditText) findViewById(R.id.RePassword);
		AutoCompleteTextView universityEdit = (AutoCompleteTextView) findViewById(R.id.University);

		String passwd = passwordEdit.getText().toString();
		String repasswd = RepasswordEdit.getText().toString();

		Intent intent = new Intent(this, Tab.class);
		intent.putExtra("phone", phoneEdit.getText());
		intent.putExtra("password", passwordEdit.getText());
		intent.putExtra("university", universityEdit.getText());

		if (universityEdit.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "학교를 선택해 주세요.",
					Toast.LENGTH_LONG).show();
		} else if (passwd.equals("") || passwd.length() < 4) {
			Toast.makeText(getApplicationContext(), "4자리 비밀번호를 입력해 주세요.",
					Toast.LENGTH_LONG).show();
		} else if (passwd.equals(repasswd) != true) {
			Toast.makeText(getApplicationContext(),
					"비밀번호가 일치 하지 않습니다.\n 다시 입력 해주세요.", Toast.LENGTH_LONG)
					.show();
		} else {
			try {

				/* 서버로 넘기기 */
				JSONObject regi = new JSONObject();
				JSONObject listData = new JSONObject();

				regi.put("phone", phoneEdit.getText().toString());
				regi.put("school", universityEdit.getText().toString());
				regi.put("passwd", passwordEdit.getText().toString());
				
				listData.put("Register", regi);

				http.Register(listData.toString());

				startActivity(intent);
				finish();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
