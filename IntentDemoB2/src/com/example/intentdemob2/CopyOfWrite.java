package com.example.intentdemob2;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class CopyOfWrite extends Activity{
	ScrollView scrollview;
	RatingBar rating;     
	TextView tv01; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.write);
	    
	    scrollview = (ScrollView) findViewById(R.id.Scroll);
	    
	    rating = (RatingBar) findViewById(R.id.Quality);         
	    tv01 = (TextView) findViewById(R.id.tv01);           
	    rating.setStepSize((float) 0.5); //별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감         
	    rating.setRating((float) 2.5); // 처음보여줄때(색깔이 한개도없음) default 값이 0  이다 
	    rating.setIsIndicator(false); //true - 별점만 표시 사용자가 변경 불가 , false - 사용자가 변경가능           
	    
	    rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {               
	    	@Override            
	    	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {                 
	    		tv01.setText("평점 : " + rating);               
	    	}         
	    }); 
	}
}
