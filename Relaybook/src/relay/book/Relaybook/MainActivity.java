package relay.book.Relaybook;


import com.google.android.gcm.GCMRegistrar;

import relay.book.Option.PhoneNum;
import relay.book.intentdemob2.R;
import relay.book.intentdemob2.ServerUtilities;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainActivity extends Activity {

	AsyncTask<Void, Void, Void> mRegisterTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
//		/*
//		 * GCM 등록
//		 */
		PhoneNum.setPhoneNum(getMy10DigitPhoneNumber()); 
//		
//		GCMRegistrar.checkDevice(this);
//		GCMRegistrar.checkManifest(this);
//		final String regId = GCMRegistrar.getRegistrationId(this);
//		
//		if (regId.equals("")) {
//			GCMRegistrar.register(this, "337077831410");
//			Log.d("Relaybook", "등록됨 regId = "+regId);
//		} else {
//			Log.d("Relaybook", "이미 등록 regId = "+regId);
//			
//			if (GCMRegistrar.isRegisteredOnServer(this)) {
//                // Skips registration.
//            } else {
//                // Try to register again, but not in the UI thread.
//                // It's also necessary to cancel the thread onDestroy(),
//                // hence the use of AsyncTask instead of a raw thread.
//                final Context context = this;
//                
//                mRegisterTask = new AsyncTask<Void, Void, Void>() {           	
//                	
//                    @Override
//                    protected Void doInBackground(Void... params) {
//   
//                        boolean registered =
//                                ServerUtilities.register(context, regId);
//                        // At this point all attempts to register with the app
//                        // server failed, so we need to unregister the device
//                        // from GCM - the app will try to register again when
//                        // it is restarted. Note that GCM will send an
//                        // unregistered callback upon completion, but
//                        // GCMIntentService.onUnregistered() will ignore it.
//                        if (!registered) {
//                            GCMRegistrar.unregister(context);
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        mRegisterTask = null;
//                    }
//
//                };
//                mRegisterTask.execute(null, null, null);
//            }
//        	
//		}
//		/* */
		
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
		// return s.substring(0);
		return s.replace("-", "").replace("+82", "0");

	}
}
