package relay.book.Relaybook;


import relay.book.intentdemob2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startActivity(new Intent(this, SplashActivity.class));
		
		setContentView(R.layout.activity_main);
		
		finish(); // 이전 액티비티 종료

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
}
