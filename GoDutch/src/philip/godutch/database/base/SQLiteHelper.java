package philip.godutch.database.base;

import java.util.ArrayList;

import philip.godutch.utility.ReflectionUtil;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper {

	private static SQLiteDatabaseConfig SQLITE_DATABASE_CONFIG;
	private static SQLiteHelper INSTANCE;
	private Context mContext;
	private ReflectionUtil mReflectionUtil;
	
	public interface SQLiteDataTable{
		public void onCreate(SQLiteDatabase pSQLiteDatabase);
		public void onUpgrade(SQLiteDatabase pSQLiteDatabase);
	}
	
	private SQLiteHelper(Context pContext){
		super(pContext, SQLITE_DATABASE_CONFIG.getDatabaseName(), null, SQLITE_DATABASE_CONFIG.getVersion());
		mContext=pContext;
	}

	//单例模式得到对象
	public static SQLiteHelper getInstance(Context pContext) {
		if (INSTANCE == null) {
			SQLITE_DATABASE_CONFIG = SQLiteDatabaseConfig.getInstant(pContext);
			INSTANCE = new SQLiteHelper(pContext);
		}
		return INSTANCE;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		ArrayList<String> _ArrayList = SQLITE_DATABASE_CONFIG.getTables();
		mReflectionUtil = new ReflectionUtil();
		for (int i = 0; i < _ArrayList.size(); i++) {
			try {
				SQLiteDataTable _SQLiteDataTable = (SQLiteDataTable) mReflectionUtil
								.newInstance(_ArrayList.get(i), new Object[]{mContext}, new Class[]{Context.class});
				_SQLiteDataTable.onCreate(db);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
