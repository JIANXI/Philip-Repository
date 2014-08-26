package philip.godutch.database.dal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import philip.godutch.R;
import philip.godutch.database.base.SQLiteDALbase;
import philip.godutch.model.ModelCategory;
import philip.godutch.utility.DateUtil;

public class SQLiteDALCategory extends SQLiteDALbase {

	private String TABLE_NAME 			= "Category";
	private String COLUMN_Category_ID 	= "CategoryID";
	private String COLUMN_Category_NAME = "CategoryName";
	private String COLUMN_PARENT_ID 	= "ParentID";
	private String COLUMN_PATH 			= "Path";
	private String COLUMN_CREATE_DATE 	= "CreateDate";
	private String COLUMN_STATE 		= "State";
	
	public SQLiteDALCategory(Context pContext) {
		super(pContext);
	}

	public boolean insertCategory(ModelCategory pModelCategory) {
		ContentValues _ContentValues=createContentValues(pModelCategory);
		long _NewID = getSQLiteDatabase().insert(TABLE_NAME, null, _ContentValues);	
		return _NewID>=0;	
	}
	
	public boolean deleteCategory(String pCondition) {
		return delete(TABLE_NAME, pCondition);
	}
	
	public boolean updateCategory(String pCondition, ModelCategory pModelCategory) {
		ContentValues _ContentValues = createContentValues(pModelCategory);
		return updateCategory(pCondition, _ContentValues);
	}
	
	public boolean updateCategory(String pCondition, ContentValues pContentValues) {
		return getSQLiteDatabase().update(TABLE_NAME, pContentValues, pCondition, null)>0;
	}
	
	public List<ModelCategory> getCategory(String pCondition) {
		String _sqlText = "Select * From "+TABLE_NAME+" Where 1=1 "+pCondition;
		return getList(_sqlText);
	}
	
	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE_NAME, COLUMN_Category_ID};
	}
	
	@Override
	public String[] getColumnNames() {
		return new String[]{	COLUMN_Category_ID,
								COLUMN_Category_NAME,
								COLUMN_PARENT_ID,
								COLUMN_PATH,
								COLUMN_CREATE_DATE,
								COLUMN_STATE	};
	}

	@Override
	protected Object findModel(Cursor pCursor) {
		ModelCategory _ModelCategory = new ModelCategory();
		_ModelCategory.setCategoryID(pCursor.getInt(pCursor.getColumnIndex(COLUMN_Category_ID)));
		_ModelCategory.setCategoryName(pCursor.getString(pCursor.getColumnIndex(COLUMN_Category_NAME)));
		_ModelCategory.setParentID(pCursor.getInt(pCursor.getColumnIndex(COLUMN_PARENT_ID)));
		_ModelCategory.setPath(pCursor.getString(pCursor.getColumnIndex(COLUMN_PATH)));
		Date _CreateDate=DateUtil.getDate(pCursor.getString(pCursor.getColumnIndex(COLUMN_CREATE_DATE)), "yyyy-MM-dd HH:mm:ss"); 
		_ModelCategory.setCreateDate(_CreateDate);
		_ModelCategory.setState(pCursor.getInt(pCursor.getColumnIndex(COLUMN_STATE)));
		
		return _ModelCategory;
	}

	private void initDefaultData(SQLiteDatabase pSqLiteDatabase) {
		
		ModelCategory _ModelCategory = new ModelCategory();
		
		String[] _CategoryName = getContext().getResources().getStringArray(R.array.InitDefaultCategotyName);
		String[] _Path = getContext().getResources().getStringArray(R.array.InitDefaultCategotyPath);
		String[] _ParentID = getContext().getResources().getStringArray(R.array.InitDefaultCategotyParentID);
		for (int i = 0; i < _CategoryName.length; i++) {
			_ModelCategory.setCategoryName(_CategoryName[i]);
			_ModelCategory.setPath(_Path[i]);
			_ModelCategory.setParentID(Integer.parseInt(_ParentID[i]));
			ContentValues _ContentValues = createContentValues(_ModelCategory);
			pSqLiteDatabase.insert(TABLE_NAME, null, _ContentValues);
		}
		
	}
	
	/* 
	 * 通过反射在SQLiteHelper中建表 
	 */
	@Override
	public void onCreate(SQLiteDatabase pSQLiteDatabase) {
		
		StringBuilder _CreateTableScript = new StringBuilder();
		_CreateTableScript.append("		CREATE TABLE IF NOT EXISTS  "+TABLE_NAME+" (							");
		_CreateTableScript.append("		"+COLUMN_Category_ID+"		INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,	");
		_CreateTableScript.append("		"+COLUMN_Category_NAME+"	VARCHAR( 10 )	NOT NULL,					");
		_CreateTableScript.append("		"+COLUMN_PARENT_ID+"		INTEGER			NOT NULL,					");
		_CreateTableScript.append("		"+COLUMN_PATH+"				VARCHAR( 10 )	NOT NULL,					");
		_CreateTableScript.append("		"+COLUMN_CREATE_DATE+"		DATETIME		NOT NULL,					");
		_CreateTableScript.append("		"+COLUMN_STATE+"			INTEGER			NOT NULL 					");
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
	public ContentValues createContentValues(ModelCategory pModelCategory){
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(COLUMN_Category_NAME, pModelCategory.getCategoryName());
		_ContentValues.put(COLUMN_PARENT_ID, pModelCategory.getParentID());
		_ContentValues.put(COLUMN_PATH, pModelCategory.getPath());
		_ContentValues.put(COLUMN_CREATE_DATE, DateUtil.getFormatDateTime(pModelCategory.getCreateDate(), "yyyy-MM-dd HH:mm:ss") );
		_ContentValues.put(COLUMN_STATE, pModelCategory.getState());
		return _ContentValues;
	}
}
