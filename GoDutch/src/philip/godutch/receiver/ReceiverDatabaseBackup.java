package philip.godutch.receiver;

import philip.godutch.R;
import philip.godutch.activity.ActivityMain;
import philip.godutch.business.BusinessFileOperation;
import philip.godutch.service.ServiceDatabaseBackup;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverDatabaseBackup extends BroadcastReceiver {

	NotificationManager  mNotificationManager;
	Notification mNotification;
	
	Intent mIntent;
	PendingIntent mPendingIntent;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Log.i("GoDutch","广播:"+intent.getLongExtra("Date",0)/1000);
		
		BusinessFileOperation _BusinessFileOperation = new BusinessFileOperation(context);
		if (!_BusinessFileOperation.isDatabaseBackupTiming()) {
			return;
		}
	
		//点击通知打开ActivityMain 
		mIntent = new Intent(context, ActivityMain.class);	
		mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
		
		mNotification = new Notification();
		mNotification.icon = R.drawable.ic_notification;
		mNotification.tickerText = context.getString(R.string.NotificationTickerText);
		mNotification.defaults = Notification.DEFAULT_SOUND;
		mNotification.setLatestEventInfo(context,
				context.getString(R.string.NotificationTickerText),
				context.getString(R.string.NotificationTickerText),
						mPendingIntent);	
		/*//API至少11才能用build
		Notification.Builder _Builder = new Notification.Builder(context);	
		mNotification = _Builder
				.setSmallIcon(R.drawable.ic_notification)
				.setTicker(context.getString(R.string.NotificationTickerText))
				.setDefaults(Notification.DEFAULT_SOUND)
				.setContentTitle(context.getString(R.string.NotificationTickerText))
				.setContentText(context.getString(R.string.NotificationTickerText))
				.setContentIntent(mPendingIntent)
				.build();*/
		
		mNotificationManager.notify(0, mNotification);
		
		Intent _Intent = new Intent(context, ServiceDatabaseBackup.class);
		context.startService(_Intent);
	}

}
