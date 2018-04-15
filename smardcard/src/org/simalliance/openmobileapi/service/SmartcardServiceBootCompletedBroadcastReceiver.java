
package org.simalliance.openmobileapi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmartcardServiceBootCompletedBroadcastReceiver extends BroadcastReceiver {
	public final static String _TAG = "SmartcardService";
	public final static String _SCAPI_SERVICE = "org.simalliance.openmobileapi.service.SmartcardService";

	@Override
	public void onReceive(Context context, Intent intent) {	
	    final boolean bootCompleted = intent.getAction().equals("android.intent.action.BOOT_COMPLETED");
   
        /* + snb log */
		SmartcardService.log(_TAG + SmartcardService.LOG_LEVEL_BOTTOM, Thread.currentThread().getName() + " Received broadcast");
		/* - snb log */
	    if( bootCompleted ){
	    	/* + snb log */
			SmartcardService.log(_TAG + SmartcardService.LOG_LEVEL_BOTTOM, "Starting smartcard service after boot completed");
			/* - snb log */
	    	Intent serviceIntent = new Intent(context, org.simalliance.openmobileapi.service.SmartcardService.class );
	    	context.startService(serviceIntent);
	    } else {
	    	
	    }
	}
};
