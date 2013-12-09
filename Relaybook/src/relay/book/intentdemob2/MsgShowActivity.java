package relay.book.intentdemob2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MsgShowActivity extends Activity {

	TextView		msgText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_show);
		
		msgText		= (TextView)findViewById(R.id.msgtest);
		
		Intent		intent	 = getIntent();
		
		msgText.setText(intent.getStringExtra("msg"));
		System.out.println("팝업 띄우자");
		Button	button	= (Button)findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}
