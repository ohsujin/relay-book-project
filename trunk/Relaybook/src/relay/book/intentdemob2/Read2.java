package relay.book.intentdemob2;

import java.io.*;
import java.net.*;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.json.*;

import relay.book.Option.*;
import relay.book.image.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.Relaybook.Option.*;
import com.relay.image.*;

public class Read2 extends Activity{
	
	String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";
	Bitmap bmImg;
	URL myFileUrl = null;
	
	private static final String URL = "http://14.63.212.134:8080/MyRelayServer/Image/";
	static String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/Send.jsp";
	
	String filename = null;
	
	
	private ViewPager mPager;
	private CountDownTimer timer;  
	private int currentPosition;
	private int PAGE_TOTAL_NUMBER = 3;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.read2);
	    	
	    int i=1;
	    	getInform();
	    	
	    	
	    	System.out.println("파일 이름 : "+filename);
	    	
		    ImageView img1 = (ImageView)findViewById(R.id.Img1); // 이미지
		    ImageView img2 = (ImageView)findViewById(R.id.Img2); // 이미지
		    ImageView img3 = (ImageView)findViewById(R.id.Img3); // 이미지
		    
			ImageDownloader.download(URL+filename+"_1.png", img1);
			ImageDownloader.download(URL+filename+"_2.png", img2);
			ImageDownloader.download(URL+filename+"_3.png", img3);
			
		    
			String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/send.jsp";
//			String URL = "http://14.63.212.134/MyRelayServer/JSONServer.jsp";
//			String URL = "http://121.156.253.22/hello.py";
//			String URL =  "http://api.androidhive.info/contacts/";
			
			
			
			
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
	
	void getInform(){

		  String Ph_num = PhoneNum.getPhoneNum();
		DefaultHttpClient client = new DefaultHttpClient();
		
		try {
			/* 체크할 id와 pwd값 서버로 전송 */
			HttpPost post = new HttpPost(URL_book_inform+"?phone="+Ph_num);

			/* 지연시간 최대 5초 */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}
			
			System.out.println(result);
			
			JSONObject json = new JSONObject(result);	
			JSONObject rece = json.getJSONObject("Book_inform");
			
			filename = rece.getString("filename");
    
		    TextView Title_Title = (TextView)findViewById(R.id.Title_Title);
		    TextView Title = (TextView)findViewById(R.id.Title); // 책 제목
		    Title.setText(rece.getString("title"));
		
		    TextView SubjectTitle = (TextView)findViewById(R.id.Subject_Title);
		    TextView Subject = (TextView)findViewById(R.id.Subject); // 과목명
		    Subject.setText(rece.getString("subject"));
		    
		    TextView PriceTitle = (TextView)findViewById(R.id.Price_Title);
		    TextView Price = (TextView)findViewById(R.id.Price); // 가격
		    Price.setText(rece.getString("price"));
		    
		    TextView PublisherTitle = (TextView)findViewById(R.id.Publisher_Title);
		    TextView Publisher = (TextView)findViewById(R.id.Publisher); // 출판사
		    Publisher.setText(rece.getString("publisher"));
		    
		    TextView WriterTitle = (TextView)findViewById(R.id.Writer_Title);
		    TextView Writer = (TextView)findViewById(R.id.Writer); //가격
		    Writer.setText(rece.getString("writer"));
		    
    
			} catch (Exception e) {
				e.printStackTrace();
				client.getConnectionManager().shutdown();	// 연결 지연 종료		
			}
			
	}
	
	/* 종료묻기 */
	public boolean onKeyDown(int keyCode, KeyEvent event){
	     switch(keyCode){
	     case KeyEvent.KEYCODE_BACK:
	      String alertTitle = getResources().getString(R.string.app_name);
	      String buttonMessage = getResources().getString(R.string.alert_msg_exit);
	      String buttonYes = getResources().getString(R.string.button_yes);
	      String buttonNo = getResources().getString(R.string.button_no);
	         
	      new AlertDialog.Builder(Read2.this)
	      .setTitle(alertTitle)
	      .setMessage(buttonMessage)
	      .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
	       
	       @Override
	       public void onClick(DialogInterface dialog, int which) {
	        // TODO Auto-generated method stub
	        moveTaskToBack(true);
	        finish();
	       }
	      })
	      .setNegativeButton(buttonNo, null)
	      .show();
	     }
	    return true;
	}
	
	
	
	//viewpaper
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
				image.setImageResource(R.drawable.sample_img1);
			}else if(position==1){
				image.setImageResource(R.drawable.sample_img2);
			}else if(position==2){
				image.setImageResource(R.drawable.sample_img3);
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
