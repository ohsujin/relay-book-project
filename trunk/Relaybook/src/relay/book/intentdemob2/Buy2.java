package relay.book.intentdemob2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import relay.book.Option.PhoneNum;
import relay.book.intentdemob2.Write.MyThread;


import com.example.staggeredgridviewdemo.ImageItem;
import com.example.staggeredgridviewdemo.StaggeredAdapter;

import com.origamilabs.library.views.StaggeredGridView;

import android.app.*;
import android.content.*;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint.Join;
import android.os.*;
import android.text.Editable;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class Buy2 extends Activity implements OnItemSelectedListener{
	ArrayList<String> arraylist;
	static String URL_book_inform = "http://14.63.212.134:8080/MyRelayServer/Send.jsp";
	static String imageUrl = "http://14.63.212.134:8080/MyRelayServer/Image/";
	
	private String urls[];
	
	/* 서버로 넘겨주는 값을 저장해줌 */
	String Search_option;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy2);
        
        arraylist = new ArrayList<String>();
		arraylist.add("책이름");
		arraylist.add("저자");
		arraylist.add("교과명");
		arraylist.add("학교명");


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, arraylist); 
                               //스피너 속성
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner sp = (Spinner) this.findViewById(R.id.Spinner);
		sp.setPrompt("골라봐"); // 스피너 제목
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(this);
		
		sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	            // TODO Auto-generated method stub
	            //Object item = parent.getAdapter().getItemId(position);
//	            Object item = parent.getAdapter().getItem(position);
//	            Log.i("select Item : ",item.toString());   
	            
	            /* */
	            switch (position) {
				case 0:
					Search_option = "title";
					break;
				case 1:
					Search_option = "writer";
					break;
				case 2:
					Search_option = "subject";
					break;
				case 3:
					Search_option = "school";
					break;	
				default:
					break;
				}
	         }
	         public void onNothingSelected(AdapterView<?> parent) {
	           // TODO Auto-generated method stub
	    
	         }
	   
	      });
		
		
		 
		ImageButton button = (ImageButton)findViewById(R.id.Search_btn);
        button.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				
				String keyword = ((EditText)findViewById(R.id.Search_Text)).getText().toString();
				
				
				send_search(keyword);// 서버로 검색조건과 keyword를 보낸다.						 	
				
//				display_list(); //격자형 배열의 뷰를 생성하여 화면에 표시한다.
				
			}
		});
        adapter.notifyDataSetChanged();
	}
	
	//서버로 부터 항목을 받아오는 부분 
	void send_search(String keyword){ 

		  String Ph_num = PhoneNum.getPhoneNum();
		  DefaultHttpClient client = new DefaultHttpClient();
		
		try {
			/* 체크할 id와 pwd값 서버로 전송 */
			String keyword1 = URLEncoder.encode(keyword, "UTF-8");//한글인코딩 처리를 위해 한번 변환해줌
			
			HttpPost post = new HttpPost(URL_book_inform+"?serch_option="+Search_option+"&keyword="+keyword1);

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
			JSONArray rece = json.getJSONArray("Book_inform");
			
			
			if(rece.length() == 0){ //검색항목이 없을시 다음 wait를 해주어 오류를 잡아준다. ---> 검색항목이 없다는 메시지 알려줘야됨
				wait();
			}
		
			 /*
			  * 서버로 부터 받아온 데이터를 ImageItem(Array list) 에  저장한다.
			  */
			 final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
			 
			 urls = new String[rece.length()];
			 
			 System.out.println("사이즈 : "+rece.length());
			 
			 for (int i = 0; i < rece.length(); i++) {						
					imageItems.add(new ImageItem(rece.getJSONObject(i).getString("title").toString(),rece.getJSONObject(i).getString("writer").toString(),rece.getJSONObject(i).getString("price").toString()+"원"));	 // 이부분이 커스텀 뷰의 텍스트항목에 어떤 값을 보내주는지 알려준다.
					urls[i] = imageUrl + rece.getJSONObject(i).getString("filename").toString()+"_1.jpg";
					System.out.println("이미지 경로 : "+urls[i]);
					
				}
			 
			 
			 /*
			  * 서버로 부터 받아온 데이터를 그리드뷰로 표시해주는 부분 시작
			  */
			 /* Staggered Grid View*/
				
				StaggeredGridView gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
				int margin = getResources().getDimensionPixelSize(R.dimen.margin);
				gridView.setItemMargin(margin); 

				gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
				
				/*
				 * 검색하면 서버로부터 검색 정보를 JSON으로 불러와 urls에 이미지 경로를 넣어주고 bookInform라는 Map함수에는 책정보를 입력해준다.
				 */
				StaggeredAdapter adapter1 = new StaggeredAdapter(Buy2.this, R.id.imageView1, urls,imageItems); // urls 의 크기를 구하여 몇개의 view가 생성되는지 확인
				
				gridView.setAdapter(adapter1);
			   
  
			} catch (Exception e) {
				e.printStackTrace();
				client.getConnectionManager().shutdown();	// 연결 지연 종료		
			}
			
	}
	
	
	private void display_list(){
/* Staggered Grid View*/
		
		StaggeredGridView gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin); 

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
		
		/*
		 * 검색하면 서버로부터 검색 정보를 JSON으로 불러와 urls에 이미지 경로를 넣어주고 bookInform라는 Map함수에는 책정보를 입력해준다.
		 */
		StaggeredAdapter adapter1 = new StaggeredAdapter(Buy2.this, R.id.imageView1, urls,getData()); // urls 의 크기를 구하여 몇개의 view가 생성되는지 확인
		
		gridView.setAdapter(adapter1);
		
	}
	
	private ArrayList<ImageItem> getData() {
		final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		// retrieve String drawable array
		
		
		for (int i = 0; i < 2; i++) {						
			imageItems.add(new ImageItem("토지 " + i+"권","작가","9000원"));	 // 이부분이 커스텀 뷰의 텍스트항목에 어떤 값을 보내주는지 알려준다.
		}
		
		
		
		return imageItems;
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, arraylist.get(arg2), Toast.LENGTH_LONG).show();//해당목차눌렸을때
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub


	}
	
	
	/* 종료묻기 */
	public boolean onKeyDown(int keyCode, KeyEvent event){
	     switch(keyCode){
	     case KeyEvent.KEYCODE_BACK:
	      String alertTitle = getResources().getString(R.string.app_name);
	      String buttonMessage = getResources().getString(R.string.alert_msg_exit);
	      String buttonYes = getResources().getString(R.string.button_yes);
	      String buttonNo = getResources().getString(R.string.button_no);
	         
	      new AlertDialog.Builder(Buy2.this)
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


}

