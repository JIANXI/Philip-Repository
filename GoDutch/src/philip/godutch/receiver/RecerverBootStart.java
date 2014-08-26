package philip.godutch.receiver;

import philip.godutch.activity.ActivityMain;
import philip.godutch.business.BusinessFileOperation;
import philip.godutch.service.ServiceDatabaseBackup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;

public class RecerverBootStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent _Intent = new Intent(context, ServiceDatabaseBackup.class);
		SharedPreferences _SharedPreferences = context.getSharedPreferences(BusinessFileOperation.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		if (_SharedPreferences != null) {
			boolean _IsStartBackupServise =  _SharedPreferences.getBoolean(ActivityMain.PREFERENCE_BACKUP_TIMING, false);
			if (_IsStartBackupServise) {
				context.startService(_Intent);
			}
		}
	}

}
