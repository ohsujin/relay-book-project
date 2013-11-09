package relay.book.Mylist;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import relay.book.Option.PhoneNum;
import relay.book.Relaybook.Tab;
import relay.book.image.ImageDownloader;
import relay.book.intentdemob2.R;
import relay.book.reservation.Reservation_book;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CopyOfView_mylist extends Activity {

	String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";
	Bitmap bmImg;
	URL myFileUrl = null;

	/* 1,0 -> Mybook , Reservation 으로 정의 */
	static int Mybook = 1;
	static int ReservationBook = 0;

	private static final String URL = "http://14.63.212.134:8080/MyRelayServer/Image/";

	String filename = null;
	String passwd = null;
	Button Adjust = null;

	// ViewPaper
	private ViewPager mPager;
	private CountDownTimer timer;
	private int currentPosition;
	private int PAGE_TOTAL_NUMBER = 3;

	//
	ImageView topImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		final String R_ID = in.getStringExtra("filename");
		final String title = in.getStringExtra("title");

		String writer = in.getStringExtra("writer");
		String price = in.getStringExtra("price");
		String subject = in.getStringExtra("subject");
		String memo = in.getStringExtra("memo");
		String publisher = in.getStringExtra("publisher");
		filename = in.getStringExtra("filename");
		float quality = Float.parseFloat(in.getStringExtra("quality"));
		final int active = Integer.parseInt(in.getStringExtra("active"));

		System.out.println("활성화 정도 : " + active);

		int section = Integer.parseInt(in.getStringExtra("section"));
		/*
		 * section이 1이면 Mybook list를 불러오고 0이면 reservation list를 불러온다.
		 */

		switch (section) { // section 이란 내가 올린 책인지 내가 찜한 책인지를 구분해주는 값이다 이를 통해
							// 각이가들 layout을 구분해주어 하나의 activty에서 두개의 레이아웃을 사용하게
							// 한다.

			case 1: // 1 = Mybook
				passwd = in.getStringExtra("passwd");
	
				setContentView(R.layout.view_mylist);
				Adjust = (Button) findViewById(R.id.adjust);
				
				//상단 이미지의 위치를 표시해주기 위한 imageview
				topImg = (ImageView) findViewById(R.id.TopImg);
	
				Button Sell_complete = (Button) findViewById(R.id.sell_complete);
				Sell_complete.setBackgroundResource(R.drawable.btn_sell_complete); // 내글 관리하기 항목
	
				if (active == 0) {
					Sell_complete.setText("판매하기");
				}
	
				Sell_complete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Update_book_json update = new Update_book_json();
	
						if (active == 0) {// 판매 활성화
//							update.Enalbe_sell(filename);
							Toast.makeText(getApplicationContext(), "판매시작!!",Toast.LENGTH_SHORT).show();
	
						} else {// 판매완료하기
	
							update.Complete_Sell(filename);
							Toast.makeText(getApplicationContext(), "판매완료!!",Toast.LENGTH_SHORT).show();
						}
						/*
						 * Stack에 쌓여있는 Tab activity를 종료해주어 tab actitvty가 중복생성 되는걸 막아준다.
						 */
						Tab TabActivity = (Tab)Tab.TabActivity;
						
						TabActivity.finish();
						finish();
	
						Intent Tab_view = new Intent(CopyOfView_mylist.this, Tab.class);
						startActivity(Tab_view);
	
					}
				});
	
				break;
			case 0: // 0 = ReservationBook -> view_reserv.xml이 호출될 경우
				setContentView(R.layout.view_reserv);
				//상단 이미지의 위치를 표시해주기 위한 imageview
				topImg = (ImageView) findViewById(R.id.TopImg);
	
				final String phone = in.getStringExtra("phone");
				String school = in.getStringExtra("school");
				int relaycount = Integer.parseInt(in.getStringExtra("relaycount"));
	
				TextView School = (TextView) findViewById(R.id.Seller_school); // 판매자
																				// 학교
				School.setText(school);
	
				TextView Phone = (TextView) findViewById(R.id.Seller_phone); // 판매자
																				// 전화번호
				Phone.setText(phone);
	
				// 판매완료 버튼을 누르면 수행되는 부분
				Button change = (Button) findViewById(R.id.Seller_Change);
				
				
				if (active == 1) {
					change.setEnabled(false);
					change.setText("구매 대기중");

				}
	
				change.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
	
						Reservation_book.Chnage_seller(PhoneNum.getPhoneNum(),filename);
						
						/*
						 * Stack에 쌓여있는 Tab activity를 종료해주어 tab actitvty가 중복생성 되는걸 막아준다.
						 */
						Tab TabActivity = (Tab)Tab.TabActivity;
						
						TabActivity.finish();
						finish();//현재 보이는 activity를 종료해준다.
						
						/* 새로운 activity 생성*/
						Intent Tab_view = new Intent(CopyOfView_mylist.this, Tab.class);
						startActivity(Tab_view);
						
						Toast.makeText(getApplicationContext(), "RelayBook 등록!!",Toast.LENGTH_SHORT).show();
					}
				});
	
				break;
	
			default:
				break;
		}

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

		final RatingBar Rating = (RatingBar) findViewById(R.id.quality);

		if (section == Mybook) {
			Rating.setStepSize((float) 0.5); // 별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감
			Rating.setRating((float) 0.0); // 처음보여줄때(색깔이 한개도없음) default 값이 0 이다
			Rating.setIsIndicator(false); // true - 별점만 표시 사용자가 변경 불가 , false -
											// 사용자가 변경가능
			Rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {

				}
			});
		}

		Rating.setRating(quality);

		/*
		 * 수정된 값을 EditText로 부터 가져오기
		 */
		final EditText Title_update = (EditText) findViewById(R.id.title);
		final EditText Writer_update = (EditText) findViewById(R.id.writer);
		final EditText Publisher_update = (EditText) findViewById(R.id.publisher);
		final EditText Price_update = (EditText) findViewById(R.id.price);
		final EditText Subject_update = (EditText) findViewById(R.id.subject);
		final EditText Memo_update = (EditText) findViewById(R.id.memo);
		final EditText passwd_chk = (EditText) findViewById(R.id.passwd);

		if (section == Mybook) {
			Adjust.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
					 * R 일떄와 M 일때를 구분해서 코딩해야함
					 */
					System.out.println("비번 : " + passwd);
					if (passwd_chk.getText().toString().equals(passwd)) {

						JSONObject book_inform = new JSONObject();
						JSONObject listData = new JSONObject();

						try {
							book_inform.put("title", Title_update.getText().toString());
							book_inform.put("writer", Writer_update.getText().toString());
							book_inform.put("publisher", Publisher_update.getText().toString());
							book_inform.put("quality", +Rating.getRating());
							book_inform.put("price", Price_update.getText().toString());
							book_inform.put("subject", Subject_update.getText().toString());
							book_inform.put("Memo", Memo_update.getText().toString());
							book_inform.put("R_ID", R_ID);
							
							listData.put("BookList", book_inform);

							Update_book_json update = new Update_book_json();
							update.HttpPostData(listData.toString());
							
							Tab TabActivity = (Tab)Tab.TabActivity;
							
							TabActivity.finish();
							finish();
							
							Intent Tab_view = new Intent(CopyOfView_mylist.this,Tab.class);
							startActivity(Tab_view);
							
							Toast.makeText(getApplicationContext(), "수정완료!!",
									Toast.LENGTH_SHORT).show();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(getApplicationContext(), "비밀번호가 틀립니다.",
								Toast.LENGTH_SHORT).show();
					}

				}
			});
		}

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
