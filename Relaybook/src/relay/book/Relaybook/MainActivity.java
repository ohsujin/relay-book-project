package relay.book.Relaybook;

import relay.book.Option.PhoneNum;
import relay.book.intentdemob2.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
			s.replace("-", "").replace("+82", "0");
			return s;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "01012345678";
		}
	}
}
