package relay.book.intentdemob2;

import java.util.*;

import relay.book.intentdemob2.R;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class ListViewTest extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listviewtest);

		//* 데이터 원본 준비
		ArrayList<String> arGeneral = new ArrayList<String>();
		arGeneral.add("네트워크 프로그래밍");
		arGeneral.add("프로그래밍 언어론");
		arGeneral.add("소프트웨어 프로젝트");
		arGeneral.add("모바일 프로그래밍");
		//*/
		
		/* 배열로 준비
		String[] arGeneral = {"김유신", "이순신", "강감찬", "을지문덕"};
		//*/

		// 어댑터 준비
		ArrayAdapter<String> Adapter;
		Adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, arGeneral);

		// 어댑터 연결
		ListView list = (ListView)findViewById(R.id.list);
		list.setAdapter(Adapter);
	}
	
	public void mOnClick(View v)
	{
		finish();		
	}
}
