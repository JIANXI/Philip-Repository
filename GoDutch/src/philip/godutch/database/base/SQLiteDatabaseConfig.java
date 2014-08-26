package philip.godutch.database.base;

import java.util.ArrayList;

import philip.godutch.R;
import android.content.Context;
import android.util.Log;

/**
 * ���ݿ���������ࣨ���������ܵõ����ݿ������汾������DAL�ļ�·��
 */
public class SQLiteDatabaseConfig {

	private static final String DATABASE_NAME="GoDutchDatabase";
	private static final int VERSION = 1 ;
	private static SQLiteDatabaseConfig INSTANCE;
	private static Context CONTEXT;
	
	private SQLiteDatabaseConfig(){
		
	}
	
	/**
	 * ����ģʽ �õ�����
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
	 * @return ����dal��ĵ�ַ
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
