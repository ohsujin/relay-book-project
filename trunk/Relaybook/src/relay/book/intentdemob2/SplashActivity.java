package relay.book.intentdemob2;

import com.example.intentdemob2.R;

import android.app.*;
import android.os.*;
import android.view.*;

public class SplashActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
	    setContentView(R.layout.splash);
	    Handler handler = new Handler(){
	    	public void handleMessage(Message msg){
	    		finish();
	    	}
	    };

	    handler.sendEmptyMessageDelayed(0, 2500); 
	}
}
