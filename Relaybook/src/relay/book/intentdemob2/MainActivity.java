package relay.book.intentdemob2;

import com.example.intentdemob2.R;

import android.app.*;
import android.content.*;
import android.os.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startActivity(new Intent(this, SplashActivity.class));
		
		setContentView(R.layout.activity_main);
		
		finish(); //이전 액티비티 종료	

    	//Intent myIntent = new Intent(MainActivity.this, IntentCaller.class);
    	//MainActivity.this.startActivity(myIntent); //새로운 액티비티 이동
    	
    	Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            //Do Something
            public void run() { 
                Intent intent = new Intent(MainActivity.this, IntentCaller.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    	
	}
}
