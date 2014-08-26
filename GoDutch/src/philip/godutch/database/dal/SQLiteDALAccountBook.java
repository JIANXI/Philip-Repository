package philip.godutch.database.dal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import philip.godutch.R;
import philip.godutch.database.base.SQLiteDALbase;
import philip.godutch.model.ModelAccountBook;
import philip.godutch.utility.DateUtil;

public class SQLiteDALAccountBook extends SQLiteDALbase {

	private String TABLE_NAME 				= "AccountBook";
	private String COLUMN_AccountBook_ID 	= "AccountBookID";
	private String COLUMN_AccountBook_NAME 	= "AccountBookName";
	private String COLUMN_CREATE_DATE 		= "CreateDate";
	private String COLUMN_STATE 			= "State";
	private String COLUMN_IS_DEFAULT		= "IsDefault";
	
	public SQLiteDALAccountBook(Context pContext) {
		super(pContext);
	}

	public boolean insertAccountBook(ModelAccountBook pModelAccountBook) {
		ContentValues _ContentValues=createContentValues(pModelAccountBook);
		long _NewID = getSQLiteDatabase().insert(TABLE_NAME, null, _ContentValues);
		
		return _NewID>=0;	
	}
	
	public boolean deleteAccountBook(String pCondition) {
		return delete(TABLE_NAME, pCondition);
	}
	
	public boolean updateAccountBook(String pCondition, ModelAccountBook pModelAccountBook) {
		ContentValues _ContentValues = createContentValues(pModelAccountBook);
		return updateAccountBook(pCondition, _ContentValues);
	}
	
	public boolean updateAccountBook(String pCondition, ContentValues pContentValues) {
		return getSQLiteDatabase().update(TABLE_NAME, pContentValues, pCondition, null)>0;
	}
	
	public List<ModelAccountBook> getAccountBook(String pCondition) {
		String _sqlText = "Select * From "+TABLE_NAME+" Where 1=1 "+pCondition;
		return getList(_sqlText);
	}
	
	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE_NAME, COLUMN_AccountBook_ID};
	}
	
	@Override
	public String[] getColumnNames() {
		return new String[]{
								COLUMN_AccountBook_ID,
								COLUMN_AccountBook_NAME,
								COLUMN_CREATE_DATE,
								COLUMN_STATE,
								COLUMN_IS_DEFAULT
		};
	}

	@Override
	protected Object findModel(Cursor pCursor) {
		ModelAccountBook _ModelAccountBook = new ModelAccountBook();
		_ModelAccountBook.setAccountBookID(pCursor.getInt(pCursor.getColumnIndex(COLUMN_AccountBook_ID)));
		_ModelAccountBook.setAccountBookName(pCursor.getString(pCursor.getColumnIndex(COLUMN_AccountBook_NAME)));
		Date _CreateDate=DateUtil.getDate(pCursor.getString(pCursor.getColumnIndex(COLUMN_CREATE_DATE)), "yyyy-MM-dd HH:mm:ss"); 
		_ModelAccountBook.setCreateDate(_CreateDate);
		_ModelAccountBook.setState(pCursor.getInt(pCursor.getColumnIndex(COLUMN_STATE)));
		_ModelAccountBook.setIsdefault(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IS_DEFAULT)));
		
		return _ModelAccountBook;
	}

	private void initDefaultData(SQLiteDatabase pSqLiteDatabase) {
		ModelAccountBook _ModelAccountBook = new ModelAccountBook();
	 	String _AccountBookName = getContext().getResources().getStringArray(R.array.InitDefaultAccountBookName)[0];
		_ModelAccountBook.setAccountBookName(_AccountBookName);
		_ModelAccountBook.setIsdefault(1);
		
		ContentValues _ContentValues = createContentValues(_ModelAccountBook);
		pSqLiteDatabase.insert(TABLE_NAME, null, _ContentValues);
	}
	
	/* 
	 * 通过反射在SQLiteHelper中建表 
	 */
	@Override
	public void onCreate(SQLiteDatabase pSQLiteDatabase) {
		StringBuilder _CreateTableScript = new StringBuilder();
		_CreateTableScript.append("		CREATE TABLE IF NOT EXISTS	"+TABLE_NAME+" (								");
		_CreateTableScript.append("		"+COLUMN_AccountBook_ID+"	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,		");
		_CreateTableScript.append("		"+COLUMN_AccountBook_NAME+"	VARCHAR( 10 )  NOT NULL,						");
		_CreateTableScript.append("		"+COLUMN_CREATE_DATE+"		DATETIME       NOT NULL,						");
		_CreateTableScript.append("		"+COLUMN_STATE+"			INTEGER        NOT NULL,						");
		_CreateTableScript.append("		"+COLUMN_IS_DEFAULT+"		INTEGER        NOT NULL 						");
		_CreateTableScript.append("		);																			");
	
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
	public ContentValues createContentValues(ModelAccountBook pModelAccountBook){
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(COLUMN_AccountBook_NAME, pModelAccountBook.getAccountBookName());
		_ContentValues.put(COLUMN_CREATE_DATE, DateUtil.getFormatDateTime(pModelAccountBook.getCreateDate(), "yyyy-MM-dd HH:mm:ss") );
		_ContentValues.put(COLUMN_STATE, pModelAccountBook.getState());
		_ContentValues.put(COLUMN_IS_DEFAULT, pModelAccountBook.getIsdefault());
		return _ContentValues ;
	}
}
