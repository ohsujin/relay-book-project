package relay.book.Relaybook;

import relay.book.Mylist.my_book_list;
import relay.book.Option.Option;
import relay.book.intentdemob2.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;

public class Tab extends TabActivity {
	TabHost mTab;
	static boolean First = true;
	
	public static Activity TabActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		
		TabActivity = Tab.this;

		TabHost tabHost = getTabHost();

		Drawable img1 = getResources().getDrawable(R.drawable.tab_buy);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("삽니다", img1)
				.setContent(new Intent(this, Buy2.class)));

		Drawable img2 = getResources().getDrawable(R.drawable.tab_sell);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("팝니다", img2)
				.setContent(new Intent(this, Write.class)));

		Drawable img3 = getResources().getDrawable(
				R.drawable.tab_shopping_basket);
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("장바구니", img3)
				.setContent(new Intent(this, my_book_list.class)));

		Drawable img4 = getResources().getDrawable(R.drawable.tab_option);
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("옵션", img4)
				.setContent(new Intent(this, Option.class)));

		/* 최초 실행시 삽니다 화면을 보여주고 이후에 탭뷰가 다시 실행 될때는 장바구니 항목을 실행한다. */
		if (First) {
			tabHost.setCurrentTab(0);
			First = false;
		} else {
			tabHost.setCurrentTab(2);
			
			
		}
	}

}
