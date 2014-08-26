package philip.godutch.database.base;

import java.util.ArrayList;
import java.util.List;

import philip.godutch.database.base.SQLiteHelper.SQLiteDataTable;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLiteDALbase implements SQLiteDataTable {
	
	private Context mContext;
	private SQLiteDatabase mSqLiteDatabase;
	
	public SQLiteDALbase(Context pContext) {
		mContext=pContext;
	}
	
	protected Context getContext() {
		return mContext;
	}
	
	public SQLiteDatabase getSQLiteDatabase() {
		if (mSqLiteDatabase == null) {
			mSqLiteDatabase=SQLiteHelper.getInstance(mContext).getWritableDatabase();
		}
		return mSqLiteDatabase;
	}
	
	public void beginTransaction() {
		mSqLiteDatabase.beginTransaction();
	}	
	public void setTransactionSuccessful() {
		mSqLiteDatabase.setTransactionSuccessful();
	}	
	public void endTransaction() {
		mSqLiteDatabase.endTransaction();
	}
	
	/**
	 * ���ر�����������
	 */
	protected abstract String[] getTableNameAndPK();
	
	/**
	 * ���ص�ǰ��������ֶ���
	 */
	protected abstract String[] getColumnNames();
	
	public int getCount(String pCondition) {
		String _String[] = getTableNameAndPK();
		Cursor _Cursor = execSql("Select "+_String[1]+ " From "+_String[0]+" Where 1=1 "+pCondition );
		int _Count = _Cursor.getCount();
		_Cursor.close();
		return _Count;
	}
	
	protected boolean delete(String pTableName, String pCondition) {
		return getSQLiteDatabase().delete(pTableName, "1=1 "+ pCondition, null)>=0;
	}
	
	protected List getList(String pSqlText) {
		Cursor _Cursor = execSql(pSqlText);
		return cursorToList(_Cursor);
	}
	
	/**
	 * ���α�����λ�õĶ���ȡ��
	 */
	protected abstract Object findModel(Cursor pCursor);
	
	/**
	 * ���α������ж������
	 */
	protected List<Object> cursorToList(Cursor pCursor) {
		List<Object> _List = new ArrayList<Object>();
		while (pCursor.moveToNext()) {
			Object _Object = findModel(pCursor);
			_List.add(_Object);
		}
		pCursor.close();
		return _List;
	}
	
	public Cursor execSql(String pSqlText){		
		return getSQLiteDatabase().rawQuery(pSqlText, null);
	}
}
