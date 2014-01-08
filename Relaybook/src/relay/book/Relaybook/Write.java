package relay.book.Relaybook;

import java.io.File;
import java.io.FileOutputStream;

import relay.book.Multipart.GeoPictureUploader;
import relay.book.Multipart.GeoPictureUploader.ReturnCode;
import relay.book.NaverOpenAPI.NaverOpenAPI;
import relay.book.Option.PhoneNum;
import relay.book.intentdemob2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Write extends Activity {
	ScrollView scrollview;
	RatingBar rating;
	TextView tv01;

	/* 파일 업로드를 위한 인자값 */
	String filename;
	MyThread mMyThread = null;

	String Subject, Title, Writer, publisher, Price, Quality, Memo; // 판매 정보를

	String image1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Relaybook/" + "/img-1.jpg";
	String image2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Relaybook/" + "/img-2.jpg";
	String image3 = Environment.getExternalStorageDirectory().getAbsolutePath()	+ "/Relaybook/" + "/img-3.jpg";

	/* */

	EditText ISBN;
	
	
	static int i = 1;

	static int REQUEST_PICTURE = 1;
	static int REQUEST_PHOTO_ALBUM = 2;

	/** Called when the activity is first created. */
	Context mContext = this;
	ImageView iv1, iv2, iv3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
	
		findViewById(R.id.isbn_btn).setOnClickListener(scanAnything);//바코드 activity 호출
		
		/*
		 * CaptureActivity에서 찍은 바코드 정보를 Tab activity에 static으로 저장하여 ISBN_num 값이 null일때는 책정보를 자동기입하지 말고 null이 아닐때만 네이버에서 받아온 책정보를 자동으로 기입한다.
		 */
		if(Tab.ISBN_num != null){	
			((EditText) findViewById(R.id.Title)).setText(Tab.NOA.getTitle());
			((EditText) findViewById(R.id.Writer)).setText(Tab.NOA.getAuthor());
			((EditText) findViewById(R.id.Publisher)).setText(Tab.NOA.getPublisher());
		}
		
		Button button = (Button) findViewById(R.id.Send); //
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// MainPage.class이 부분을 이동할 곳으로 수정

				Subject = ((EditText) findViewById(R.id.Subject)).getText().toString();
				
				Title = ((EditText) findViewById(R.id.Title)).getText().toString();
				
				Writer = ((EditText) findViewById(R.id.Writer)).getText().toString();
				
				publisher = ((EditText) findViewById(R.id.Publisher)).getText().toString();
				
				Price = ((EditText) findViewById(R.id.Price)).getText().toString();
				
				Memo = ((EditText) findViewById(R.id.Memo)).getText().toString();
				

				if (Title.equals("") | Writer.equals("") | Price.equals("")) {
					Toast.makeText(getApplicationContext(),
							"책의 제목과 저자 가격은 필수 입력 항목 입니다.", Toast.LENGTH_SHORT)
							.show();
				} else {

					mMyThread = (MyThread) new MyThread().execute((Void) null);

					finish();
					
					Intent intent = new Intent(Write.this, Tab.class);
					startActivity(intent);
				}
			}
		});

		scrollview = (ScrollView) findViewById(R.id.Scroll);

		rating = (RatingBar) findViewById(R.id.Quality);
		rating.setStepSize((float) 0.5); // 별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감
		rating.setRating((float) 0.0); // 처음보여줄때(색깔이 한개도없음) default 값이 0 이다
		rating.setIsIndicator(false); // true - 별점만 표시 사용자가 변경 불가 , false - 사용자가
										// 변경가능

		rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				Toast.makeText(getApplicationContext(), "평점 : " + rating, Toast.LENGTH_SHORT).show();

				Quality = Float.toString(rating);
			}
		});

		iv1 = (ImageView) findViewById(R.id.imgView1);
		iv2 = (ImageView) findViewById(R.id.imgView2);
		iv3 = (ImageView) findViewById(R.id.imgView3);

		iv1.setScaleType(ImageView.ScaleType.FIT_XY);
		iv2.setScaleType(ImageView.ScaleType.FIT_XY);
		iv3.setScaleType(ImageView.ScaleType.FIT_XY);
		

		
	}
	
	/*
	 * ISBN 바코드 activity 호출
	 */
	private final Button.OnClickListener scanAnything = new Button.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	finish();
	      Intent i = new Intent(Write.this, com.google.zxing.client.android.CaptureActivity.class );
	      startActivity(i);
	    }
	  };

	
	
	// button 클릭
	Dialog dialog;

	public void onClickImg(View v) {

		i=0;
		
		switch (v.getId()) {
		case R.id.put_Image:
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			View customLayout = View.inflate(mContext, R.layout.custom_button,null);
			builder.setView(customLayout);
			dialog = builder.create();
			dialog.show();
			Toast toast = Toast.makeText(getApplicationContext(), "앞 표지, 책 내용, 뒤 표지 총 3장을 찍어주세요.",Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 150);
			toast.show();

			break;

		case R.id.camera:
			  dialog.dismiss(); 
			
			  while(i<=2){
				  i++;
				  takePicture();
			  }
			Toast.makeText(getApplicationContext(),"책 표지를 포함한 총 3장, 연속 촬영을 시작합니다.", Toast.LENGTH_LONG).show();
			break;

//		case R.id.photoAlbum:
//			dialog.dismiss();
//			photoAlbum();
//			break;
		}

	}

	// 사진 촬영
	void takePicture() {

		// 카메라 호출 intent 생성
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Relaybook/", "img-" + i + ".jpg");

		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, REQUEST_PICTURE);

	}

	// 사진 불러오기
//	void photoAlbum() {
//		// photo Album 호출 intent 생성
//		Intent intent = new Intent(Intent.ACTION_PICK);
//
//		intent.setType(Images.Media.CONTENT_TYPE);
//		intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
//		startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
//	}

	// 촬영한 사진을 압축하기 위해서
	Bitmap loadPicture() {
	
		filename = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/Relaybook/" + "/img-" + i + ".jpg"; // 파일이름을 저장

		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 8;
		Bitmap src = BitmapFactory.decodeFile(filename, option);
		Bitmap resized = Bitmap.createScaledBitmap(src, 350, 550, true);	

		/*
		 * 리사이즈된 bitmap 파일을 sdcard에 다시 저장해준다.
		 */
		try {
			FileOutputStream out = new FileOutputStream(filename);
			resized.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* ++++ ++++ ++++ ++++ ++++ ++++ ++++ */

		return resized;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		
		if (resultCode != RESULT_OK)
			return;

		if (i == 1) {
			iv3.setImageBitmap(loadPicture());
		} else if (i == 2) {
			iv2.setImageBitmap(loadPicture());
			i--;
		} else {
			iv1.setImageBitmap(loadPicture());
			i--;
		}

		if (requestCode == REQUEST_PHOTO_ALBUM) {
			if (i == 1) {
				iv3.setImageURI(data.getData());
			} else if (i == 2) {
				iv2.setImageURI(data.getData());
			} else {
				iv1.setImageURI(data.getData());
			}
		}

	}

	
	/*
	 * 파일 업로드
	 */
	public class MyThread extends AsyncTask<Void, String, String> {

		ReturnCode Upload_chk;
		  
		@Override
		protected String doInBackground(Void... params) {
			//이미지 파일과 함께 책정보도 보낸다.
			GeoPictureUploader send = new GeoPictureUploader(Subject, Title,Writer, publisher, Price, Quality, PhoneNum.getPhoneNum(),Memo);

			//이미지 파일을 보내고 최종적으로 uploadPicture를 통해 책정보와 이미지 파일이 서버로 전송된다.
			Upload_chk = send.uploadPicture(image1, image2, image3);
			
			System.out.println("응답 : " 	+ Upload_chk);

			return Upload_chk.toString();
		}
		
		
		/*
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 * 역활 : doInBackground(…)가 리턴하는 값을 바탕으로 UI스레드에 background 작업 결과를 표현하도록 구현 함.
		 * 기능 : 서버로 부터 받은 응답을 이용하여 등록성공 여부 확인을 하게 해준다.
		 */
		protected void onPostExecute(String result) {
			
			if(result.equals("http201")){
				Toast T = Toast.makeText(getApplicationContext(),
						"등록 성공!!", Toast.LENGTH_SHORT);
				T.setGravity(Gravity.TOP, 0, 400);
				T.show();
			}else{
				Toast T = Toast.makeText(getApplicationContext(),
						"등록 실패!!", Toast.LENGTH_SHORT);
				T.setGravity(Gravity.TOP, 0, 400);
				T.show();
			}
		}


	}

	/* 종료묻기 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			String alertTitle = getResources().getString(R.string.app_name);
			String buttonMessage = getResources().getString(
					R.string.alert_msg_exit);
			String buttonYes = getResources().getString(R.string.button_yes);
			String buttonNo = getResources().getString(R.string.button_no);

			new AlertDialog.Builder(Write.this)
					.setTitle(alertTitle)
					.setMessage(buttonMessage)
					.setPositiveButton(buttonYes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									moveTaskToBack(true);
									finish();
								}
							}).setNegativeButton(buttonNo, null).show();
		}
		return true;
	}
}
