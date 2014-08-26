package philip.godutch.database.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import philip.godutch.database.base.SQLiteHelper.SQLiteDataTable;

public class SQLiteDALCreateView implements SQLiteDataTable{

	private Context mContext;

	public SQLiteDALCreateView(Context pContext) {
		mContext = pContext;
	}
	
	/* 
	 * 通过反射在SQLiteHelper中建表 
	 */
	@Override
	public void onCreate(SQLiteDatabase pSQLiteDatabase) {
		StringBuilder _CreateViewScript = new StringBuilder();
		//创建视图，左联两个外键
		_CreateViewScript.append("	CREATE VIEW ViewPayout AS 												");
		_CreateViewScript.append("	SELECT a.*,b.ParentID,b.CategoryName,b.Path,b.State,c.AccountBookName	");
		_CreateViewScript.append("	FROM 		Payout 		a   											");
		_CreateViewScript.append("	LEFT JOIN 	Category 	b ON a.CategoryID = b.CategoryID  				");
		_CreateViewScript.append("	LEFT JOIN 	AccountBook c ON a.AccountBookID = c.AccountBookID  		");
		
		pSQLiteDatabase.execSQL(_CreateViewScript.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase pSQLiteDatabase) {
		// TODO Auto-generated method stub
		
	}

	
	
}
