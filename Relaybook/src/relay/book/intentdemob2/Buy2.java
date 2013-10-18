package relay.book.intentdemob2;

import java.util.*;


import com.example.staggeredgridviewdemo.ImageItem;
import com.example.staggeredgridviewdemo.StaggeredAdapter;

import com.origamilabs.library.views.StaggeredGridView;

import android.app.*;
import android.content.*;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class Buy2 extends Activity implements OnItemSelectedListener{
	ArrayList<String> arraylist;
	
	private String urls[] = { 
			"http://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
			"http://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
		};

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
		
		/* Staggered Grid View*/
		
		StaggeredGridView gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin); 

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
		
		/*
		 * 검색하면 서버로부터 검색 정보를 JSON으로 불러와 urls에 이미지 경로를 넣어주고 bookInform라는 Map함수에는 책정보를 입력해준다.
		 */
		ArrayList<Map<String, String>> book_list = new ArrayList<Map<String,String>>();
		
		Map<String,String> bookInform = new HashMap<String,String>(); 
	
		for(int i=0;i<2;i++){
			
			bookInform.put("Title", "테스트");
			bookInform.put("Writer", "수진");
			bookInform.put("Price", "600"+i);
			
			book_list.add(bookInform);
			
			System.out.println("헷갈려 : "+book_list.get(i).get("Price"));
		}
		
		StaggeredAdapter adapter1 = new StaggeredAdapter(Buy2.this, R.id.imageView1, urls,getData());
		
		gridView.setAdapter(adapter1);
		adapter.notifyDataSetChanged();

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

