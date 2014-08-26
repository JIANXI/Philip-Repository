package philip.godutch.database.base;

import java.util.ArrayList;

import philip.godutch.R;
import android.content.Context;
import android.util.Log;

/**
 * 数据库基本设置类（单例），能得到数据库名，版本，所有DAL文件路径
 */
public class SQLiteDatabaseConfig {

	private static final String DATABASE_NAME="GoDutchDatabase";
	private static final int VERSION = 1 ;
	private static SQLiteDatabaseConfig INSTANCE;
	private static Context CONTEXT;
	
	private SQLiteDatabaseConfig(){
		
	}
	
	/**
	 * 单例模式 得到对象
	 */
	public static SQLiteDatabaseConfig getInstant(Context pContext) {
		if (INSTANCE == null) {
			INSTANCE = new SQLiteDatabaseConfig();
			CONTEXT = pContext;
		}
		return INSTANCE;
	}
	
	public String getDatabaseName() {
		return DATABASE_NAME;
	}
	
	public int getVersion() {
		return VERSION;
	}
	
	/**
	 * @return 所有dal类的地址
	 */
	public ArrayList<String> getTables() {
		ArrayList<String> _ArrayList = new ArrayList<String>();
		
		String[] _SQLiteDALClassName = CONTEXT.getResources().getStringArray(R.array.SQLiteDALClassName);
		String _PackagePath = CONTEXT.getPackageName()+".database.dal.";
		for (int i = 0; i < _SQLiteDALClassName.length ; i++) {
			_ArrayList.add(_PackagePath+_SQLiteDALClassName[i]);
		}
		
		return _ArrayList;
	}
	
}
