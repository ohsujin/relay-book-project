package relay.book.Relaybook;

import com.google.android.gcm.GCMRegistrar;

import relay.book.Mylist.my_book_list;
import relay.book.NaverOpenAPI.NaverOpenAPI;
import relay.book.Option.Option;
import relay.book.intentdemob2.R;
import relay.book.intentdemob2.ServerUtilities;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Tab extends TabActivity {
	TabHost mTab;
	static boolean First = true;
	static NaverOpenAPI NOA = new NaverOpenAPI();
	 
	public static Activity TabActivity;
	public static String ISBN_num=null; 
	public static String barcode_back=null; 
	
	AsyncTask<Void, Void, Void> mRegisterTask;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		
		/*
		 * GCM 등록
		 */
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		
		if (regId.equals("")) {// 등록이 안된경우
			GCMRegistrar.register(this, "337077831410");
			Log.e("Relaybook", "등록됨 regId = "+regId);
		} else {
			Log.e("Relaybook", "이미 등록 regId = "+regId);
			
			if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                
                mRegisterTask = new AsyncTask<Void, Void, Void>() {           	
                	
                    @Override
                    protected Void doInBackground(Void... params) {
   
                        boolean registered = ServerUtilities.register(context, regId);
                        // At this point all attempts to register with the app
                        // server failed, so we need to unregister the device
                        // from GCM - the app will try to register again when
                        // it is restarted. Note that GCM will send an
                        // unregistered callback upon completion, but
                        // GCMIntentService.onUnregistered() will ignore it.
                        if (!registered) {
                            GCMRegistrar.unregister(context);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        	
		}
		/* */
		
		TabActivity = Tab.this;

		TabHost tabHost = getTabHost();

		Drawable img1 = getResources().getDrawable(R.drawable.tab_buy);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("삽니다", img1)
				.setContent(new Intent(this, Buy2.class)));

		Drawable img2 = getResources().getDrawable(R.drawable.tab_sell);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("팝니다", img2)
				.setContent(new Intent(this, Write.class)));

		Drawable img3 = getResources().getDrawable(
				R.drawable.tab_shopping_basket);
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("장바구니", img3)
				.setContent(new Intent(this, my_book_list.class)));

		Drawable img4 = getResources().getDrawable(R.drawable.tab_option);
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("옵션", img4)
				.setContent(new Intent(this, Option.class)));
		
		/* 바코드로 찍은 ISBN 값을 가져온다 */
		Intent intent = getIntent();   // 값을 받기 위한 Intent 생성
		ISBN_num = intent.getStringExtra("ISBN");
		NOA.Search_book(ISBN_num);
		/* ++++++++++++++++++ */
		
		Intent intent2 = getIntent();   // 값을 받기 위한 Intent 생성
		barcode_back = intent2.getStringExtra("bar_back");
		
		/* 최초 실행시 삽니다 화면을 보여주고 이후에 탭뷰가 다시 실행 될때는 장바구니 항목을 실행한다. */
		if (First) {
			tabHost.setCurrentTab(2);
			First = false;
		}else if((ISBN_num != null) || (barcode_back != null)){
			tabHost.setCurrentTab(1);
		}else {
			tabHost.setCurrentTab(2);
			
			
		}
	}

}
