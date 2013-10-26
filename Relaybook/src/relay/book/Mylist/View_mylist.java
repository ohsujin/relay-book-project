package relay.book.Mylist;

import java.io.*;
import java.net.*;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.json.*;

import relay.book.Option.*;
import relay.book.image.*;
import relay.book.intentdemob2.R;
import relay.book.intentdemob2.Tab;
import relay.book.reservation.Reservation_book;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.SmsManager;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.ImageView.ScaleType;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class View_mylist extends Activity {

	String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";
	Bitmap bmImg;
	URL myFileUrl = null;
	
	
	
	/* 1,0 -> Mybook , Reservation 으로 정의 */
	static int Mybook = 1;
	static int ReservationBook = 0;
	 
	private static final String URL = "http://14.63.212.134:8080/MyRelayServer/Image/";
	
	String filename = null;
	String passwd = null;
	Button Request = null;
	
	//ViewPaper
	private ViewPager mPager;
	private CountDownTimer timer;  
	private int currentPosition;
	private int PAGE_TOTAL_NUMBER = 3;
	//
	
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
        float quality = Float.parseFloat( in.getStringExtra("quality") );
        final int active = Integer.parseInt(in.getStringExtra("active"));
        
        System.out.println("활성화 정도 : "+active);
        
        int section = Integer.parseInt(in.getStringExtra("section"));
        /*
         * section이 1이면 Mybook list를 불러오고 0이면 reservation list를 불러온다.
         */
        System.out.println("section : "+section);
        
        switch (section) { // section 이란 내가 올린 책인지 내가 찜한 책인지를 구분해주는 값이다 이를 통해 각이가들 layout을 구분해주어 하나의 activty에서 두개의 레이아웃을 사용하게 한다.
        
		case 1:  // 1 = Mybook
			passwd = in.getStringExtra("passwd");   
			
			 setContentView(R.layout.view_mylist);
			 Request = (Button)findViewById(R.id.adjust);	
			 
			 Button Sell_complete = (Button)findViewById(R.id.sell_complete);
			 
			 if(active == 0){
				 Sell_complete.setText("판매하기");
			 }
			 
			 Sell_complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Update_book_json update = new Update_book_json();
				
				if(active == 0){//판매 활성화
					update.Enalbe_sell(filename);
					Toast.makeText(getApplicationContext(), "판매시작!!", Toast.LENGTH_SHORT).show();
					
				}else{//판매완료하기
					
					update.Complete_Sell(filename);
					Toast.makeText(getApplicationContext(), "판매완료!!", Toast.LENGTH_SHORT).show();
				}
					finish();
					
					Intent Tab_view = new Intent(View_mylist.this, Tab.class);
					startActivity(Tab_view);
				
					}
			 });
			 
			break;
		case 0:  // 0 = ReservationBook 
			setContentView(R.layout.view_reserv);
			
		
			 final String phone = in.getStringExtra("phone");
		     String school = in.getStringExtra("school");
		     int relaycount = Integer.parseInt(in.getStringExtra("relaycount"));
		      
			 TextView School = (TextView)findViewById(R.id.Seller_school); // 판매자 학교
			 School.setText(school);
			 
			 TextView Phone = (TextView)findViewById(R.id.Seller_phone); // 판매자 전화번호
			 Phone.setText(phone);
			 
			 //판매완료 버튼을 누르면 수행되는 부분
			 Button change = (Button)findViewById(R.id.Seller_Change);	 
			 
			 if(active == 1){
				 change.setEnabled(false);
				 change.setText("구매대기...");
			 }
			 
			 change.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Reservation_book.Chnage_seller(PhoneNum.getPhoneNum(), filename);
						Toast.makeText(getApplicationContext(), "RelayBook 등록!!", Toast.LENGTH_SHORT).show();
						finish();
					
						Intent Tab_view = new Intent(View_mylist.this, Tab.class);
						startActivity(Tab_view);
						
					}
					 });
			 
			break;

		default:
			break;
		}
        
       
        TextView Title = (TextView)findViewById(R.id.title); // 책 제목
	    Title.setText(title);
	    
	    TextView Subject = (TextView)findViewById(R.id.subject); // 과목명
	    Subject.setText(subject);
	   
	    TextView Price = (TextView)findViewById(R.id.price); // 가격
	    Price.setText(price);
	   
	    TextView Publisher = (TextView)findViewById(R.id.publisher); // 출판사
	    Publisher.setText(publisher);
	 
	    TextView Writer = (TextView)findViewById(R.id.writer); //가격
	    Writer.setText(writer);
	   
	    TextView Memo = (TextView)findViewById(R.id.memo); //가격
	    Memo.setText(memo);
	    
	    final RatingBar Rating = (RatingBar) findViewById(R.id.quality); 
	    

	   
	    
	    if(section == Mybook ){
	    	Rating.setStepSize((float) 0.5); // 별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감         
	 	    Rating.setRating((float) 0.0); // 처음보여줄때(색깔이 한개도없음) default 값이 0  이다 
	 	    Rating.setIsIndicator(false); // true - 별점만 표시 사용자가 변경 불가 , false - 사용자가 변경가능   
		    Rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {               
		    	@Override            
		    	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {                 
		        
		    	}         
		    }); 
	    }
	    
	    Rating.setRating(quality);
	    
	    /*
	     * 수정된 값을 EditText로 부터 가져오기
	     */
	    final EditText Title_update = (EditText)findViewById(R.id.title);
	    final EditText Writer_update = (EditText)findViewById(R.id.writer);
	    final EditText Publisher_update = (EditText)findViewById(R.id.publisher);
	    final EditText Price_update = (EditText)findViewById(R.id.price);
	    final EditText Subject_update = (EditText)findViewById(R.id.subject);
	    final EditText Memo_update = (EditText)findViewById(R.id.memo);
	    final EditText passwd_chk = (EditText)findViewById(R.id.passwd);
	
	    
	    
	    if(section == Mybook){  
			    Request.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					/*
					 * R 일떄와 M 일때를 구분해서 코딩해야함
					 */
						System.out.println("비번 : "+passwd);
						if(passwd_chk.getText().toString().equals(passwd)){
					
							JSONObject book_inform = new JSONObject();
							JSONObject listData = new JSONObject();
							
							try {
								book_inform.put("title", Title_update.getText().toString());
								book_inform.put("writer", Writer_update.getText().toString() );
								book_inform.put("publisher",Publisher_update.getText().toString());
								book_inform.put("quality",+ Rating.getRating());
								book_inform.put("price", Price_update.getText().toString());
								book_inform.put("subject", Subject_update.getText().toString());
								book_inform.put("Memo", Memo_update.getText().toString());
								book_inform.put("R_ID", R_ID);
								
								listData.put("BookList", book_inform);
								
								Update_book_json update = new Update_book_json();
								update.HttpPostData(listData.toString());
								Toast.makeText(getApplicationContext(), "수정완료!!", Toast.LENGTH_SHORT).show();
								finish();
								
							
								Intent Tab_view = new Intent(View_mylist.this, Tab.class);
								startActivity(Tab_view);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							 Toast.makeText(getApplicationContext(), "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
						}
						
						
					}
				});
	    }
	    
		//ViewPaper
		timer = new CountDownTimer(2*1000, 1000) {

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
		
		
	}

	

	//ViewPaper
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
				ImageDownloader.download(URL+filename+"_1.jpg", image);
			}else if(position==1){
				ImageDownloader.download(URL+filename+"_2.jpg", image);
			}else if(position==2){
				ImageDownloader.download(URL+filename+"_3.jpg", image);
				
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

