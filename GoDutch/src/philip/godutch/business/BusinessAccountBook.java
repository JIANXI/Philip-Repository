package philip.godutch.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import philip.godutch.business.base.BusinessBase;
import philip.godutch.database.dal.SQLiteDALAccountBook;
import philip.godutch.model.ModelAccountBook;


public class BusinessAccountBook extends BusinessBase{
	
	private SQLiteDALAccountBook mSQLiteDALAccountBook;
	/**
	 * 0 id 1 name 2 date 3 state 4 is default
	 */
	private String[] mColumnNames; 

	public BusinessAccountBook(Context pContext) {
		super(pContext);
		mSQLiteDALAccountBook = new SQLiteDALAccountBook(pContext);
		mColumnNames = mSQLiteDALAccountBook.getColumnNames();
	}

	/**
	 * �������ʱ�  �����Ĭ���˱�������ǰ�Ƚ������ʱ���Ĭ��ֵ��Ϊ0
	 */
	public boolean insertAccountBook(ModelAccountBook pModelAccountBook){
		mSQLiteDALAccountBook.beginTransaction();
		try {		
			boolean _Result = true;
			if (pModelAccountBook.getIsdefault() == 1) {
				_Result = setNoDefaultAccountBook();
			}
			boolean _Result2 = mSQLiteDALAccountBook.insertAccountBook(pModelAccountBook);
		
			if (_Result && _Result2) {
				mSQLiteDALAccountBook.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALAccountBook.endTransaction();
		}
	}
	
	/**
	 * ͨ��AccountBookIDɾ���ʱ������ʱ��й��������Ѽ�¼
	 */
	public boolean deleteAccountBookByAccountBookID(int pAccountBookID) {
		mSQLiteDALAccountBook.beginTransaction();
		try {
			String _Condition = " And "+mColumnNames[0]+" = "+pAccountBookID;
			boolean _Result = mSQLiteDALAccountBook.deleteAccountBook(_Condition);
			if (!_Result) {
				return false;
			}
			//ɾ���ʱ����������Ѽ�¼
			BusinessPayout _BusinessPayout = new BusinessPayout(getContext());
			_Result = _BusinessPayout.deletePayoutByAccountBookID(pAccountBookID);
			if (_Result) {
				mSQLiteDALAccountBook.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALAccountBook.endTransaction();
		}
	}
	
	/**
	 * ͨ��AccountBookID�����˱�����Ĭ���ʱ�״̬
	 */
	public boolean updateAccountBookByAccountBookID(ModelAccountBook pModelAccountBook) {
		mSQLiteDALAccountBook.beginTransaction();
		try {
			boolean _Result = true;
			if (pModelAccountBook.getIsdefault() == 1) {
				_Result = setNoDefaultAccountBook();
			}
			
			boolean _Result2 = false;
			if (_Result) {
				String _Condition = mColumnNames[0]+" = "+pModelAccountBook.getAccountBookID();
				_Result2 = mSQLiteDALAccountBook.updateAccountBook(_Condition, pModelAccountBook);
			}
			
			if (_Result && _Result2) {
				mSQLiteDALAccountBook.setTransactionSuccessful();
			}
			return _Result && _Result2;
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALAccountBook.endTransaction();
		}
	}
	
	/**
	 * �õ�����״̬Ϊ1���ʱ������б�
	 */
	public List<ModelAccountBook> getNotHideAccountBook() {
		return mSQLiteDALAccountBook.getAccountBook(" And State = 1");
	}
	
	/**
	 * �õ�״̬Ϊ1���ʱ���������
	 */
	public int getNotHideAccountBookCount() {
		return getNotHideAccountBook().size();
	}
	
	/**
	 * ͨ���������ȡ���ʱ������б�
	 */
	private List<ModelAccountBook> getAccountBook(String pCondition) {
		return mSQLiteDALAccountBook.getAccountBook(pCondition);
	}
	
	/**
	 * ͨ������AccountBookIDȡ���˱�����
	 */
	public ModelAccountBook getModelAccountBookByAccountBookID(int pAccountBookID) {
		List<ModelAccountBook> _List = getAccountBook(" And "+mColumnNames[0]+" = "+pAccountBookID);
		if (_List.size() == 1) {
			return _List.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * ͨ��AccountBookID����ȡ���˱������б�
	 */
	public List<ModelAccountBook> getAccountBookByAccountBookID(int[] pAccountBookIDs) {
		List<ModelAccountBook> _List = new ArrayList<ModelAccountBook>();
		for (int i = 0; i < pAccountBookIDs.length; i++) {
			_List.add(getModelAccountBookByAccountBookID(pAccountBookIDs[i]));
		}
		
		return _List;
	}
	
	/**
	 * �õ�Ĭ���ʱ�
	 */
	public ModelAccountBook getDefaultAccountBook() {
		List<ModelAccountBook> _List = mSQLiteDALAccountBook.getAccountBook(" And "+mColumnNames[4]+" = 1");
		if (_List.size() == 1) {
			return _List.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * ͨ��AccountBookName��AccountBookID�ж��ʱ��Ƿ��Ѿ�����
	 */
	public boolean isExistByAccountBookName(String pAccountBookName, int pIntAccountBookID){
		Integer pAccountBookID = Integer.valueOf(pIntAccountBookID);
		String _Condition = " And "+mColumnNames[1]+"='"+pAccountBookName+"'";
		if (pAccountBookID != null) {
			_Condition += " And "+mColumnNames[0]+"<>"+pAccountBookID;
		}
		List<ModelAccountBook> _List = mSQLiteDALAccountBook.getAccountBook(_Condition);
		if (_List.size() > 0) {
			return true;
		}else {
			return false; 
		}
	}
	
	/**
	 * �������ʱ���Ĭ���˱�״̬��Ϊ0
	 */
	public boolean setNoDefaultAccountBook() {
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(mColumnNames[4], 0);
		
		boolean _Result = mSQLiteDALAccountBook.updateAccountBook(null, _ContentValues);
		return _Result;
	}
	
}
