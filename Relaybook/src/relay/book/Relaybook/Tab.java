package relay.book.Relaybook;

import com.google.android.gcm.GCMRegistrar;

import relay.book.Mylist.*;
import relay.book.NaverOpenAPI.*;
import relay.book.Option.*;
import relay.book.intentdemob2.*;
import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;


public class Tab extends TabActivity {
	TabHost mTab;
	static boolean First = true;
	static NaverOpenAPI NOA = new NaverOpenAPI();
	 
	public static Activity TabActivity;
	public static String ISBN_num=null; 
	public static String barcode_back=null; 
	
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	// ViewPaper
	private ViewPager mPager;
	private CountDownTimer timer;
	private int currentPosition;
	private int PAGE_TOTAL_NUMBER = 5;
		
	//
		
	/*튜토리얼 다시 보지 않기*/
	CheckBox chAgree;
	SharedPreferences mSharedPref;
	SharedPreferences.Editor mSharedPrefEditor;
	/* */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		/* 튜토리얼 */
		Window win = getWindow();
		win.setContentView(R.layout.tab);

		LayoutInflater inflater = (LayoutInflater)getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.tutorial, null);
		LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		win.addContentView(linear, paramlinear);
		
		Button btnclose = (Button)win.findViewById(R.id.btnclose);
		btnclose.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				linear.setVisibility(View.GONE);
			}
		});
		
		/*튜토리얼 다시 보지 않기*/
		mSharedPref = getSharedPreferences("Test", 0);
		mSharedPrefEditor = mSharedPref.edit();

		chAgree = (CheckBox) findViewById(R.id.checkBox1);
		chAgree.setChecked(mSharedPref.getBoolean("Agreed", false));

		if(chAgree.isChecked() == true)
		{
			linear.setVisibility(View.GONE);
		}
		
		/**/
		
		//ViewPaper 시작
		timer = new CountDownTimer(2*1300, 1300) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				if(currentPosition==PAGE_TOTAL_NUMBER-1)
					mPager.setCurrentItem(0);
				else
					mPager.setCurrentItem(currentPosition+1);
			}
		};

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				currentPosition = position;
				timer.cancel();
				timer.start();
			}

			@Override public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override public void onPageScrollStateChanged(int arg0) {}
		});
		//ViewPaper 끝
		/* */
		
		TabActivity = Tab.this;

		TabHost tabHost = getTabHost();
		
		//0번
		Drawable img1 = getResources().getDrawable(R.drawable.tab_buy); //tab바의 이미지 표
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("삽니다", img1)
				.setContent(new Intent(this, Buy2.class))); // activity 호출
		//1번 
		Drawable img2 = getResources().getDrawable(R.drawable.tab_sell);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("팝니다", img2)
				.setContent(new Intent(this, Write.class)));
		//2번
		Drawable img3 = getResources().getDrawable(
				R.drawable.tab_shopping_basket);
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("장바구니", img3)
				.setContent(new Intent(this, my_book_list.class)));
		//3번
		Drawable img4 = getResources().getDrawable(R.drawable.tab_option);
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("옵션", img4)
				.setContent(new Intent(this, Option.class)));
		
		/* 바코드로 찍은 ISBN 값을 가져온다 */
		Intent intent = getIntent();   // 값을 받기 위한 Intent 생성
		ISBN_num = intent.getStringExtra("ISBN");
		NOA.Search_book(ISBN_num);
		/* ++++++++++++++++++ */
		
		Intent intent2 = getIntent();   // 값을 받기 위한 Intent 생성
		barcode_back = intent2.getStringExtra("bar_back"); // 바코드값이 null이 아니면 Write class를 호출해주기 위한 변수
		
		/* 최초 실행시 삽니다 화면을 보여주고 이후에 탭뷰가 다시 실행 될때는 장바구니 항목을 실행한다. */
		if (First) {
			tabHost.setCurrentTab(0);
			
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
			
			First = false;
		}else if((ISBN_num != null) || (barcode_back != null)){
			tabHost.setCurrentTab(1);
		}else {
			tabHost.setCurrentTab(2);

		}
	}

	/*튜토리얼 다시 보지 않기*/
	@Override
	protected void onPause() {
		super.onPause();
		
		if (chAgree.isChecked())
			mSharedPrefEditor.putBoolean("Agreed", true);
		else
			mSharedPrefEditor.putBoolean("Agreed", false);
		
		mSharedPrefEditor.commit();
	}

	/**/
	
	private class PagerAdapterClass extends PagerAdapter{

		private Context context;
		private LayoutInflater mInflater;

		public PagerAdapterClass(Context c){
			context = c;
			mInflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PAGE_TOTAL_NUMBER;
		}

		@Override
		public Object instantiateItem(View pager, int position) {

			ImageView image = new ImageView(context);
			if(position==0){
				image.setImageResource(R.drawable.tutorial_img1);
			}else if(position==1){
				image.setImageResource(R.drawable.tutorial_img2);
			}else if(position==2){
				image.setImageResource(R.drawable.tutorial_img3);
			}else if(position==3){
				image.setImageResource(R.drawable.tutorial_img4);
			}else if(position==4){
				image.setImageResource(R.drawable.tutorial_img5);
				image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
			}

			((ViewPager)pager).addView(image, 0);
			return image;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0==arg1;
		}

		public void destroyItem(View pager, int position, Object view) {    
			((ViewPager)pager).removeView((View)view);
		}

	}
}
