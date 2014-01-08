/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package relay.book.intentdemob2;

import static relay.book.intentdemob2.CommonUtilities.SENDER_ID;

import java.util.List;

import relay.book.Relaybook.MainActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        
        //구글 GCM 서버로 부터 전송 받은 기기의 gcm regId 값을 개발자의 서버로 넘겨주기 위한 부분.
        ServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
     
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }
   
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
	
		
	}
	//서버로 부터 메시지가 오면 실행 되는 부분
	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Received message");
		
		//진동 활성화
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.vibrate = new long[] { 500, 1000, 0, 0 };
										//	( 순서대로 대기시간, 진동시간, 대기시간, 진동시간 이런 식으로 반복이다.)
		notificationManager.notify(0, notification);  
		
		
		 //---------MsgShowActivity 호출 (Pop up창 띄우기)--------------------
        Intent		msgshowintnet	= new Intent(context, MsgShowActivity.class);
        msgshowintnet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        msgshowintnet.putExtra("msg", intent.getStringExtra("Reservation"));
        context.startActivity(msgshowintnet);
        //--------------------------------------------------
				
		String c2dm_msg = intent.getStringExtra("Reservation");;

		
			  if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")){ 
				  	c2dm_msg = intent.getExtras().getString("Reservation"); // Server에서 "Reservation"이란 파라메터로 메시지를 보낸것을 받아온다.
			  }
			  ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			  List<RunningTaskInfo> runList = am.getRunningTasks(10);
			  ComponentName name = runList.get(0).topActivity;
			  String className = name.getClassName();
			  boolean isAppRunning = false;

			  if(className.contains("relay.book.intentdemob2")) {
				  isAppRunning = true;
			  }
			  
			  if(isAppRunning == true) {//앱이 실행 중일 경우 
				  Intent run_intent = new Intent();
				  run_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				  run_intent.setComponent(new ComponentName("relay.book.intentdemob2", "relay.book.Relaybook.MainActivity"));
				  generateNotification(context, c2dm_msg);
//			      startActivity(run_intent);
			  } else {//앱이 실행 중이 아닐 경우
				   Intent start_intent = new Intent();
				   start_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				   start_intent.setComponent(new ComponentName("relay.book.intentdemob2", "relay.book.Relaybook.MainActivity"));
				   generateNotification(context,c2dm_msg);
//				   startActivity(start_intent);
			  }
			 

	}

}
