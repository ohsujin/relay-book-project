package relay.book.saveInform;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class GetPhone_Num extends Activity {
	  public String getMyPhoneNumber()
	    {
	    	TelephonyManager mTelephonyMgr;
	    	mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	    	
	    	return mTelephonyMgr.getLine1Number();
	    }
	    
	    
	    
	    public String getMy10DigitPhoneNumber()
	    {
	    	String s = getMyPhoneNumber();
	    	return s.substring(0);
	    	
	    }
}
