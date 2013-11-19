package relay.book.Relaybook;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import relay.book.Option.PhoneNum;
import relay.book.image.ImageDownloader;
import relay.book.intentdemob2.KakaoLink;
import relay.book.intentdemob2.R;
import relay.book.reservation.Reservation_book;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Read2 extends Activity {

	String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";
	Bitmap bmImg;
	URL myFileUrl = null;

	private String encoding = "UTF-8";
	String title = null;
	
	private static final String URL = "http://14.63.212.134:8080/MyRelayServer/Image/";
	static String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/Send.jsp";

	String filename = null;

	// ViewPaper
	private ViewPager mPager;
	private CountDownTimer timer;
	private int currentPosition;
	private int PAGE_TOTAL_NUMBER = 3;
	
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read2);

		//상단 이미지의 위치를 표시해주기 위한 imageview
		final ImageView topImg = (ImageView) findViewById(R.id.TopImg);
		
		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		final String R_ID = in.getStringExtra("R_ID");
		title = in.getStringExtra("title");
		final String phone = in.getStringExtra("phone");
		String school = in.getStringExtra("school");
		String writer = in.getStringExtra("writer");
		String price = in.getStringExtra("price");
		String subject = in.getStringExtra("subject");
		String memo = in.getStringExtra("memo");
		int relaycount = Integer.parseInt(in.getStringExtra("relaycount"));
		String publisher = in.getStringExtra("publisher");
		filename = in.getStringExtra("filename");
		float quality = Float.parseFloat(in.getStringExtra("quality"));

		TextView Title = (TextView) findViewById(R.id.title); // 책 제목
		Title.setText(title);

		TextView Subject = (TextView) findViewById(R.id.subject); // 과목명
		Subject.setText(subject);

		TextView Price = (TextView) findViewById(R.id.price); // 가격
		Price.setText(price);

		TextView Publisher = (TextView) findViewById(R.id.publisher); // 출판사
		Publisher.setText(publisher);

		TextView Writer = (TextView) findViewById(R.id.writer); // 가격
		Writer.setText(writer);

		TextView Memo = (TextView) findViewById(R.id.memo); // 가격
		Memo.setText(memo);

		RatingBar Rating = (RatingBar) findViewById(R.id.quality);
		Rating.setRating(quality);

		EditText School = (EditText) findViewById(R.id.Seller_school);
		School.setText(school);

		EditText Phone = (EditText) findViewById(R.id.Seller_phone);
		Phone.setText("구매요청 후 공개");

		Button Request = (Button) findViewById(R.id.Request);

		Request.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SmsManager smsManager = SmsManager.getDefault();
				String sendTo = phone;
				String myMessage = "-RelayBook-\n등록하신 " + title + " 책을 구매 하고 싶습니다.";
				smsManager.sendTextMessage(sendTo, null, myMessage, null, null);
				Toast.makeText(getApplicationContext(), "구매 요청이 전송되었습니다.",  Toast.LENGTH_SHORT).show();

				// 구매 예약 목록에 추가해주기
				Reservation_book.send_reserV(PhoneNum.getPhoneNum(), R_ID);

				finish();
			}
		});
		
		//현재 이미지뷰의 위치를 표시해주기 위한 변수
		
		
		// ViewPaper
		timer = new CountDownTimer(2 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				if (currentPosition == PAGE_TOTAL_NUMBER - 1){
					mPager.setCurrentItem(0);
				}
				else{
					mPager.setCurrentItem(currentPosition + 1);
				}
			}
		};
		

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				currentPosition = position;
				
				if(currentPosition == 0)
					topImg.setImageDrawable( getResources().getDrawable(R.drawable.top1) );
				else if(currentPosition == 1)
					topImg.setImageDrawable( getResources().getDrawable(R.drawable.top2) );
				else
					topImg.setImageDrawable( getResources().getDrawable(R.drawable.top3) );
				
				timer.cancel();
				timer.start();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
		
		/*
		 * 카톡 공유를 위한 리스너 등록
		 */
		public void Share_KaKao(View v) throws NameNotFoundException {
			ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();

			// If application is support Android platform.
			Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
			metaInfoAndroid.put("os", "android");
			metaInfoAndroid.put("devicetype", "phone");
			metaInfoAndroid.put("installurl", "market://details?id=relay.book.intentdemob2");
			metaInfoAndroid.put("executeurl", "RB://RelayBook");
			
			
			// add to array
			metaInfoArray.add(metaInfoAndroid);
		
			
			// Recommended: Use application context for parameter. 
			KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());
			
			// check, intent is available.
			if(!kakaoLink.isAvailableIntent()) {		
				Toast.makeText(getApplicationContext(), "카톡을 설치해주세요.",Toast.LENGTH_LONG);
				return;
			}
			
			/**
			 * @param activity
			 * @param url
			 * @param message
			 * @param appId
			 * @param appVer
			 * @param appName
			 * @param encoding
			 * @param metaInfoArray
			 */
			kakaoLink.openKakaoAppLink(
					this, 
					"http://link.kakao.com/?test-android-app", 
					title+" 책이 RelayBook에 등록되어 있습니다~!! 어플을 설치하고 확인해 보세요.",  
					"relay.book.intentdemob2", 
					getPackageManager().getPackageInfo("relay.book.intentdemob2", 0).versionName,
					"Relaybook",
					encoding, 
					metaInfoArray);
		}




	// ViewPaper
	private class PagerAdapterClass extends PagerAdapter {

		private Context context;
		public PagerAdapterClass(Context c) {
			context = c;
			LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PAGE_TOTAL_NUMBER;
		}

		@Override
		public Object instantiateItem(View pager, int position) {

			ImageView image = new ImageView(context);
			image.setScaleType(ImageView.ScaleType.FIT_XY);
			
			
			if (position == 0) {
				ImageDownloader.download(URL + filename + "_1.jpg", image);
			} else if (position == 1) {
				ImageDownloader.download(URL + filename + "_2.jpg", image);
			} else if (position == 2) {
				ImageDownloader.download(URL + filename + "_3.jpg", image);

				image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
			}

			((ViewPager) pager).addView(image, 0);
			return image;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

	}

}