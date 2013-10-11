package com.example.intentdemob2;

import java.io.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.provider.MediaStore.Images;
import android.telephony.*;
import android.view.*;
import android.widget.*;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Write extends Activity{
	ScrollView scrollview;
	RatingBar rating;     
	TextView tv01; 
	
	static int i=1;
	
	static int REQUEST_PICTURE = 1;
	static int REQUEST_PHOTO_ALBUM = 2;

	
	//static String SAMPLEIMG = System.currentTimeMillis() + "-" + i +".png";	

	
    /** Called when the activity is first created. */
	Context mContext = this;
	ImageView iv1,iv2,iv3;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.write);
	    
	    
        Button button = (Button)findViewById(R.id.Send);
        button.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				//MainPage.class이 부분을 이동할 곳으로 수정
				Intent intent = new Intent(Write.this, MainPage.class); 
		    	startActivity(intent);
				
			}
		});
		
        
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

	    iv1 = (ImageView) findViewById(R.id.imgView1);
	    iv2 = (ImageView) findViewById(R.id.imgView2);
	    iv3 = (ImageView) findViewById(R.id.imgView3);
        
        /**
        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	String myNumber = mTelephonyMgr.getLine1Number();
    	
    	TextView phoneText = (TextView) findViewById(R.id.PhoneNum);        
        phoneText.setText(myNumber);
    	**/
	}
	
	//button 클릭
    Dialog dialog;
    public void onClickImg(View v) {
    	switch(v.getId()) {
    	case R.id.put_Image:
    		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    		View customLayout = View.inflate(mContext, R.layout.custom_button, null);
    		builder.setView(customLayout);
    		dialog = builder.create();
    		dialog.show();
    		break;
    	case R.id.camera:
    		dialog.dismiss();
    		takePicture();
    		++i;
    		dialog.dismiss();
    		takePicture();
    		++i;
    		dialog.dismiss();
    		takePicture();
    		break;
    	case R.id.photoAlbum:
    		dialog.dismiss();
    		photoAlbum();
    		break;
    	}
    	
    	
    }
    
    //사진 촬영
  	void takePicture() {
  		
  		String fileName = getMy10DigitPhoneNumber()+ ".png";
  		//카메라 호출 intent 생성
  		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
  		File file = new File(Environment.getExternalStorageDirectory(), "img-" + i +".png");
  		
  		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
  		startActivityForResult(intent, REQUEST_PICTURE);
  		
  		tv01.setText("경로 : " + Environment.getExternalStorageDirectory()+"/DCIM/Camera/"+file.getName());  
  	}
  	
  	//사진 불러오기
  	void photoAlbum() {
  		//photo Album 호출 intent 생성
  		Intent intent = new Intent(Intent.ACTION_PICK);
  		
  		intent.setType(Images.Media.CONTENT_TYPE);
  		intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
  		startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
  	}
  	
  	//촬영한 사진을 수정하기 위해서
  	Bitmap loadPicture() {

  		File file = new File(Environment.getExternalStorageDirectory(), "img-" + i +".png");

  		BitmapFactory.Options option = new BitmapFactory.Options();
  		option.inSampleSize = 4;
  		
  		return BitmapFactory.decodeFile(file.getAbsolutePath(), option);

  	}
  	
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  		// TODO Auto-generated method stub
  		super.onActivityResult(requestCode, resultCode, data);
  		
  		if(resultCode != RESULT_OK)
  			return;
  		
  		if(requestCode == REQUEST_PICTURE) {
  			for(i=1; i<=3; i++){
	  			if(i==1){
	  				iv1.setImageBitmap(loadPicture());
	  			} else if(i==2){
	  				iv2.setImageBitmap(loadPicture());
	  			} else {
	  				iv3.setImageBitmap(loadPicture());
	  			}
  			}
  		}
  		
  		if(requestCode == REQUEST_PHOTO_ALBUM) {
  			if(i==1){
  				iv1.setImageURI(data.getData());
  			} else if(i==2){
  				iv2.setImageURI(data.getData());
  			} else {
  				iv3.setImageURI(data.getData());
  			}
  		}
  		
  	}
  	
  	/* 전화번호 불러오기 */
  	
  	 	public  String getMyPhoneNumber()
	    {
	    	TelephonyManager mTelephonyMgr;
	    	mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	    	
	    	return mTelephonyMgr.getLine1Number();
	    }
	    
	    
	    
	    public  String getMy10DigitPhoneNumber()
	    {
	    	String s = getMyPhoneNumber();
	    	return s.substring(0);
	    	
	    }
	    
	    /* ======================================== */
}
