package philip.godutch.database.dal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import philip.godutch.database.base.SQLiteDALbase;
import philip.godutch.model.ModelPayout;
import philip.godutch.utility.DateUtil;

public class SQLiteDALPayout extends SQLiteDALbase {
	
	private String VIEW_NAME 				= "ViewPayout";
	private String TABLE_NAME 				= "Payout";
	private String COLUMN_PAYOUT_ID 		= "PayoutID";
	private String COLUMN_ACCOUNT_BOOK_ID 	= "AccountBookID";
	private String COLUMN_ACCOUNT_BOOK_NAME = "AccountBookName";
	private String COLUMN_CATEGORY_ID 		= "CategoryID";
	private String COLUMN_CATEGORY_NAME 	= "CategoryName";
	private String COLUMN_CATEGORY_PATH 	= "Path";
	private String COLUMN_PAY_WAY_ID 		= "PayWayID";
	private String COLUMN_AMOUNT 			= "Amount";
	private String COLUMN_PAYOUT_DATE		= "PayoutDate";
	private String COLUMN_PAYOUT_TYPE 		= "PayoutType";
	private String COLUMN_PAYOUT_USER_ID	= "PayoutUserID";
	private String COLUMN_COMMENT			= "Comment";
	private String COLUMN_CREATE_DATE		= "CreateDate";
	private String COLUMN_STATE 			= "State";
	
	public SQLiteDALPayout(Context pContext) {
		super(pContext);
	}

	/* 
	 * 通过反射在SQLiteHelper中建表 
	 */
	@Override
	public void onCreate(SQLiteDatabase pSQLiteDatabase) {
		
		StringBuilder _CreateTableScript = new StringBuilder();
		_CreateTableScript.append("	CREATE TABLE 			"	+TABLE_NAME+"	(					");
		_CreateTableScript.append(	COLUMN_PAYOUT_ID+		"	INTEGER PRIMARY KEY AUTOINCREMENT,	");
		_CreateTableScript.append(	COLUMN_ACCOUNT_BOOK_ID+	"	INTEGER  		NOT NULL,			");
		_CreateTableScript.append(	COLUMN_CATEGORY_ID+		"	INTEGER  		NOT NULL,			");
		_CreateTableScript.append(	COLUMN_PAY_WAY_ID+		"	INTEGER	,							");
		_CreateTableScript.append(	COLUMN_AMOUNT+			"	DECIMAL  		NOT NULL,			");
		_CreateTableScript.append(	COLUMN_PAYOUT_DATE+		"	DATETIME  		NOT NULL,			");
		_CreateTableScript.append(	COLUMN_PAYOUT_TYPE+		"	VARCHAR( 20 )	NOT NULL,			");
		_CreateTableScript.append(	COLUMN_PAYOUT_USER_ID+	"	TEXT			NOT NULL,			");
		_CreateTableScript.append(	COLUMN_COMMENT+			"	TEXT,								");
		_CreateTableScript.append(	COLUMN_CREATE_DATE+		"	DATETIME		NOT NULL,			");
		_CreateTableScript.append(	COLUMN_STATE+			"	INTEGER			NOT NULL 			");
		_CreateTableScript.append("	);																");
	
		pSQLiteDatabase.execSQL(_CreateTableScript.toString());

	}

	@Override
	public void onUpgrade(SQLiteDatabase pSQLiteDatabase) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean insertPayout(ModelPayout pModelPayout) {
		ContentValues _ContentValues=createContentValues(pModelPayout);
		long _NewID = getSQLiteDatabase().insert(TABLE_NAME, null, _ContentValues);
		
		return _NewID>=0;	
	}
	
	public boolean deletePayout(String pCondition) {
		return delete(TABLE_NAME, pCondition);
	}
	
	public boolean updatePayout(String pCondition, ModelPayout pModelPayout) {
		ContentValues _ContentValues = createContentValues(pModelPayout);
		return updatePayout(pCondition, _ContentValues);
	}
	
	public boolean updatePayout(String pCondition, ContentValues pContentValues) {
		return getSQLiteDatabase().update(TABLE_NAME, pContentValues, pCondition, null)>0;
	}
	
	public List<ModelPayout> getPayout(String pCondition) {
		String _sqlText = "Select * From "+VIEW_NAME+" Where 1=1 "+pCondition;
		return getList(_sqlText);
	}
	
	public int getCount(String pCondition) {
		List<ModelPayout> _List = getPayout(pCondition);
		return _List.size();
	}
	
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{TABLE_NAME, COLUMN_PAYOUT_ID};
	}
	
	@Override
	public String[] getColumnNames() {
		return new String[]{
				COLUMN_PAYOUT_ID,	COLUMN_ACCOUNT_BOOK_ID,	COLUMN_ACCOUNT_BOOK_NAME,
				COLUMN_CATEGORY_ID,	COLUMN_CATEGORY_NAME,	COLUMN_CATEGORY_PATH,
				COLUMN_PAY_WAY_ID,	COLUMN_AMOUNT,			COLUMN_PAYOUT_DATE,
				COLUMN_PAYOUT_TYPE,	COLUMN_PAYOUT_USER_ID,	COLUMN_COMMENT,
				COLUMN_CREATE_DATE,	COLUMN_STATE 
				};
	}

	@Override
	protected ModelPayout findModel(Cursor pCursor) {
		
		ModelPayout _ModelPayout = new ModelPayout();
		BigDecimal _Amount = new BigDecimal(pCursor.getString(pCursor.getColumnIndex(COLUMN_AMOUNT)));
		Date _PayoutDate = DateUtil.getDate(pCursor.getString(pCursor.getColumnIndex(COLUMN_PAYOUT_DATE)), "yyyy-MM-dd");
		Date _CreateDate = DateUtil.getDate(pCursor.getString(pCursor.getColumnIndex(COLUMN_CREATE_DATE)), "yyyy-MM-dd HH:mm:ss");
		
		_ModelPayout.setPayoutID(			pCursor.getInt(		pCursor.getColumnIndex(COLUMN_PAYOUT_ID)));
		_ModelPayout.setAccountBookID(		pCursor.getInt(		pCursor.getColumnIndex(COLUMN_ACCOUNT_BOOK_ID)));
		_ModelPayout.setAccountBookName(	pCursor.getString(	pCursor.getColumnIndex(COLUMN_ACCOUNT_BOOK_NAME)));
		_ModelPayout.setCategoryID(			pCursor.getInt(		pCursor.getColumnIndex(COLUMN_CATEGORY_ID)));
		_ModelPayout.setCategoryName(		pCursor.getString(	pCursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
		_ModelPayout.setCategoryPath(		pCursor.getString(	pCursor.getColumnIndex(COLUMN_CATEGORY_PATH)));
		_ModelPayout.setPayWayID(			pCursor.getInt(		pCursor.getColumnIndex(COLUMN_PAY_WAY_ID)));
		_ModelPayout.setAmount(				_Amount );
		_ModelPayout.setPayoutDate(			_PayoutDate);
		_ModelPayout.setPayoutType(			pCursor.getString(	pCursor.getColumnIndex(COLUMN_PAYOUT_TYPE)));
		_ModelPayout.setPayoutUserID(		pCursor.getString(	pCursor.getColumnIndex(COLUMN_PAYOUT_USER_ID)));
		_ModelPayout.setComment(			pCursor.getString(	pCursor.getColumnIndex(COLUMN_COMMENT)));
		_ModelPayout.setCreateDate(			_CreateDate);
		_ModelPayout.setState(				pCursor.getInt(pCursor.getColumnIndex(COLUMN_STATE)));
		
		return _ModelPayout;
	}

	/**
	 * 将对象中的变量存入一个ContentValues以便传入数据库
	 */
	public ContentValues createContentValues(ModelPayout pModelPayout){
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(COLUMN_ACCOUNT_BOOK_ID,	pModelPayout.getAccountBookID());
		_ContentValues.put(COLUMN_CATEGORY_ID,		pModelPayout.getCategoryID());
		_ContentValues.put(COLUMN_PAY_WAY_ID,		pModelPayout.getPayWayID());
		_ContentValues.put(COLUMN_AMOUNT, 			pModelPayout.getAmount().toString());
		_ContentValues.put(COLUMN_PAYOUT_DATE, 		DateUtil.getFormatDateTime(pModelPayout.getPayoutDate(), "yyyy-MM-dd"));
		_ContentValues.put(COLUMN_PAYOUT_TYPE, 		pModelPayout.getPayoutType());
		_ContentValues.put(COLUMN_PAYOUT_USER_ID, 	pModelPayout.getPayoutUserID());
		_ContentValues.put(COLUMN_COMMENT, 			pModelPayout.getComment());
		_ContentValues.put(COLUMN_CREATE_DATE, 		DateUtil.getFormatDateTime(pModelPayout.getCreateDate(), "yyyy-MM-dd HH:mm:ss") );
		_ContentValues.put(COLUMN_STATE, 			pModelPayout.getState());
		return _ContentValues;
	}
}
