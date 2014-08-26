package philip.godutch.service;

import java.util.Date;

import philip.godutch.business.BusinessFileOperation;
import philip.godutch.receiver.ReceiverDatabaseBackup;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceDatabaseBackup extends Service {

	private static final long SPACINGIN_TERVAL = 1000*60;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		BusinessFileOperation _BusinessFileOperation = new BusinessFileOperation(this);
		long _LastBackupDate = _BusinessFileOperation.getDatabaseBackupDate();
		Date _BackupDate = new Date();
		if (_LastBackupDate == 0) {
			_BusinessFileOperation.databaseBackup();
			_LastBackupDate = _BusinessFileOperation.getDatabaseBackupDate();
		} else {
			if (_BackupDate.getTime() - _LastBackupDate >= SPACINGIN_TERVAL) {
				_BusinessFileOperation.databaseBackup();
				_LastBackupDate = _BusinessFileOperation.getDatabaseBackupDate();
			}
		}
		
		Log.i("GoDutch", "·þÎñ:"+_LastBackupDate/1000);
		Intent _Intent = new Intent(this,ReceiverDatabaseBackup.class);
		_Intent.putExtra("Date", _LastBackupDate);
		PendingIntent _PendingIntent = PendingIntent.getBroadcast(this, 0, _Intent, PendingIntent.FLAG_ONE_SHOT);
		
		AlarmManager _AlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		_AlarmManager.set(AlarmManager.RTC_WAKEUP, _LastBackupDate+SPACINGIN_TERVAL, _PendingIntent);
		
		return super.onStartCommand(intent, flags, startId);
	}

}
