package philip.godutch.database.dal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import philip.godutch.R;
import philip.godutch.database.base.SQLiteDALbase;
import philip.godutch.model.ModelUser;
import philip.godutch.utility.DateUtil;

public class SQLiteDALUser extends SQLiteDALbase {

	private String TABLE_NAME 			= "User";
	private String COLUMN_USER_ID 		= "UserID";
	private String COLUMN_USER_NAME 	= "UserName";
	private String COLUMN_CREATE_DATE 	= "CreateDate";
	private String COLUMN_STATE 		= "State";
	
	public SQLiteDALUser(Context pContext) {
		super(pContext);
	}

	public boolean insertUser(ModelUser pModelUser) {
		ContentValues _ContentValues=createContentValues(pModelUser);
		long _NewID = getSQLiteDatabase().insert(TABLE_NAME, null, _ContentValues);
		
		return _NewID>=0;	
	}
	
	public boolean deleteUser(String pCondition) {
		return delete(TABLE_NAME, pCondition);
	}
	
	public boolean updateUser(String pCondition, ModelUser pModelUser) {
		ContentValues _ContentValues = createContentValues(pModelUser);
		return updateUser(pCondition, _ContentValues);
	}
	
	public boolean updateUser(String pCondition, ContentValues pContentValues) {
		return getSQLiteDatabase().update(TABLE_NAME, pContentValues, pCondition, null)>0;
	}
	
	public List<ModelUser> getUser(String pCondition) {
		String _sqlText = "Select * From "+TABLE_NAME+" Where 1=1 "+pCondition;
		return getList(_sqlText);
	}
	
	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE_NAME, COLUMN_USER_ID};
	}
	
	@Override
	public String[] getColumnNames() {
		return new String[]{COLUMN_USER_ID,COLUMN_USER_NAME,COLUMN_CREATE_DATE,COLUMN_STATE};
	}

	@Override
	protected Object findModel(Cursor pCursor) {
		ModelUser _ModelUser = new ModelUser();
		_ModelUser.setUserID(pCursor.getInt(pCursor.getColumnIndex(COLUMN_USER_ID)));
		_ModelUser.setUserName(pCursor.getString(pCursor.getColumnIndex(COLUMN_USER_NAME)));
		Date _CreateDate=DateUtil.getDate(pCursor.getString(pCursor.getColumnIndex(COLUMN_CREATE_DATE)), "yyyy-MM-dd HH:mm:ss"); 
		_ModelUser.setCreateDate(_CreateDate);
		_ModelUser.setState(pCursor.getInt(pCursor.getColumnIndex(COLUMN_STATE)));
		
		return _ModelUser;
	}

	private void initDefaultData(SQLiteDatabase pSqLiteDatabase) {
		ModelUser _ModelUser = new ModelUser();
		String[] _UserName = getContext().getResources().getStringArray(R.array.InitDefaultUserName);
		for (int i = 0; i < _UserName.length; i++) {
			_ModelUser.setUserName(_UserName[i]);
			ContentValues _ContentValues = createContentValues(_ModelUser);
			pSqLiteDatabase.insert(TABLE_NAME, null, _ContentValues);
		}
	}
	
	/* 
	 * 通过反射在SQLiteHelper中建表 
	 */
	@Override
	public void onCreate(SQLiteDatabase pSQLiteDatabase) {
		StringBuilder _CreateTableScript = new StringBuilder();
		_CreateTableScript.append("		CREATE TABLE "+TABLE_NAME+" (										");
		_CreateTableScript.append("		"+COLUMN_USER_ID+"		INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,	");
		_CreateTableScript.append("		"+COLUMN_USER_NAME+"	VARCHAR( 10 )  NOT NULL,					");
		_CreateTableScript.append("		"+COLUMN_CREATE_DATE+"	DATETIME       NOT NULL,					");
		_CreateTableScript.append("		"+COLUMN_STATE+"		INTEGER        NOT NULL 					");
		_CreateTableScript.append("		);											");
	
		pSQLiteDatabase.execSQL(_CreateTableScript.toString());
		
		initDefaultData(pSQLiteDatabase);
	}

	@Override
	public void onUpgrade(SQLiteDatabase pSQLiteDatabase) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 将对象中的变量存入一个ContentValues以便传入数据库
	 */
	public ContentValues createContentValues(ModelUser pModelUser){
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(COLUMN_USER_NAME, pModelUser.getUserName());
		_ContentValues.put(COLUMN_CREATE_DATE, DateUtil.getFormatDateTime(pModelUser.getCreateDate(), "yyyy-MM-dd HH:mm:ss") );
		_ContentValues.put(COLUMN_STATE, pModelUser.getState());
		return _ContentValues;
	}
}
