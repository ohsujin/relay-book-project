package relay.book.Relaybook;


import android.app.*;
import android.content.*;
import android.os.*;

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
			public void run() {
				Intent intent = new Intent(MainActivity.this,
						IntentCaller.class);
				startActivity(intent);
				finish();
			}
		}, 2500);

	}
}
