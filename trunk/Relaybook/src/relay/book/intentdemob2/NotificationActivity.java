package relay.book.intentdemob2;

import relay.book.intentdemob2.util.SystemUiHider;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class NotificationActivity extends Activity {
	 private static final int NOTIFY_1 = 0x1001;
	    private NotificationManager notifier = null;
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_notification);

	        notifier =  (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	        final Notification notify = new Notification(R.drawable.ic_stat_gcm,"Hello!", System.currentTimeMillis());
	       
	     
	    }
	}
