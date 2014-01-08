package relay.book.Relaybook;

import java.util.Random;

import relay.book.Option.PhoneNum;
import relay.book.intentdemob2.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PhoneNum.setPhoneNum(getMy10DigitPhoneNumber()); 
		 
		System.out.println("전화번호 : "+getMy10DigitPhoneNumber());
		
		startActivity(new Intent(this, SplashActivity.class));
		setContentView(R.layout.activity_main);
		
		finish(); 
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			// Do Something
			@Override
			public void run() {
				Intent intent = new Intent(MainActivity.this,
						RelayBookLogin.class);
				startActivity(intent);
				finish();
			}
		}, 2500);

		
		
	}
	
	/* 전화번호 불러오기 */
	public String getMyPhoneNumber() {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		return mTelephonyMgr.getLine1Number();
	}

	public String getMy10DigitPhoneNumber() {
		
		String s = getMyPhoneNumber();

			try
			{
				return s.replace("-", "").replace("+82", "0");
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				SharedPreferences pref = getSharedPreferences("Test", MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				
				if(pref.getString("MyPhoneNum", null) == null){
					/* 10자리 임의의 문자열 생성 */
					String pwChar = "1234567890";
					char[] Random_ID = new char[10];
					Random random = new Random();
					for ( int i=0; i<10; i++ )
						Random_ID[i] = pwChar.charAt(random.nextInt(10));
					
					String R_ID = new String(Random_ID); //글과 파일의 고유값 생성		
					/* */
			
					editor.putString("MyPhoneNum", R_ID);
					editor.commit();
				}
					
				
				return pref.getString("MyPhoneNum", null);
			}
		
	}
}
