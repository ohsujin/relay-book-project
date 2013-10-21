package relay.book.intentdemob2;

import relay.book.intentdemob2.R;

import android.app.*;
import android.app.AlertDialog.Builder;
import android.content.*;
import android.graphics.drawable.*;
import android.os.*;
import android.widget.*;

public class Tab extends TabActivity {
	TabHost mTab;
	
	public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.tab);

			TabHost tabHost = getTabHost();

			Drawable img = getResources().getDrawable(R.drawable.icon);
			
			tabHost.addTab(tabHost.newTabSpec("tab1")
			          .setIndicator("삽니다", img)
			          .setContent(new Intent(this, Buy.class)));

			tabHost.addTab(tabHost.newTabSpec("tab2")
			          .setIndicator("팝니다", img)
			          .setContent(new Intent(this, Write.class)));
			
			tabHost.addTab(tabHost.newTabSpec("tab3")
			          .setIndicator("장바구니", img)
			          .setContent(new Intent(this, Read.class)));
			
			tabHost.addTab(tabHost.newTabSpec("tab4")
			          .setIndicator("옵션", img)
			          .setContent(new Intent(this, Buy2.class)));
			
			tabHost.setCurrentTab(0);

			}
	

	


	
	
}


