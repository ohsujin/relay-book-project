package relay.book.intentdemob2;

import relay.book.Mylist.my_book_list;
import relay.book.Option.Option;
import android.app.*;
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


			Drawable img1 = getResources().getDrawable(R.drawable.tab_buy);

			
			tabHost.addTab(tabHost.newTabSpec("tab1")
			          .setIndicator("삽니다", img1)
			          .setContent(new Intent(this, Buy2.class)));

			Drawable img2 = getResources().getDrawable(R.drawable.tab_sell);


			tabHost.addTab(tabHost.newTabSpec("tab2")
			          .setIndicator("팝니다", img2)
			          .setContent(new Intent(this, Write.class)));

			Drawable img3 = getResources().getDrawable(R.drawable.tab_shopping_basket);
		
			
			tabHost.addTab(tabHost.newTabSpec("tab3")
			          .setIndicator("장바구니", img3)
			          .setContent(new Intent(this, my_book_list.class)));

			Drawable img4 = getResources().getDrawable(R.drawable.tab_option);
			
			tabHost.addTab(tabHost.newTabSpec("tab4")
			          .setIndicator("옵션", img4)
			          .setContent(new Intent(this, Option.class)));
			
			tabHost.setCurrentTab(0);

			}
	

	


	
	
}


