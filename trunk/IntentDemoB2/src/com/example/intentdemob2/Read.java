package com.example.intentdemob2;

import android.app.*;
import android.os.*;
import android.widget.*;

public class Read extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.read);
  
	    ImageView img = (ImageView)findViewById(R.id.Img); // 이미지
	    
	    TextView Title_Title = (TextView)findViewById(R.id.Title_Title);
	    TextView Title = (TextView)findViewById(R.id.Title); // 책 제목

	    TextView SubjectTitle = (TextView)findViewById(R.id.Subject_Title);
	    TextView Subject = (TextView)findViewById(R.id.Subject); // 과목명

	    TextView PriceTitle = (TextView)findViewById(R.id.Price_Title);
	    TextView Price = (TextView)findViewById(R.id.Price); // 가격

	    TextView PublisherTitle = (TextView)findViewById(R.id.Publisher_Title);
	    TextView Publisher = (TextView)findViewById(R.id.Publisher); // 출판사

	    TextView WriterTitle = (TextView)findViewById(R.id.Writer_Title);
	    TextView Writer = (TextView)findViewById(R.id.Writer); //가격

	
	}
}
