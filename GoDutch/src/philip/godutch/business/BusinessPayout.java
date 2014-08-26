package philip.godutch.business;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import philip.godutch.R;
import philip.godutch.business.base.BusinessBase;
import philip.godutch.database.dal.SQLiteDALPayout;
import philip.godutch.model.ModelPayout;


public class BusinessPayout extends BusinessBase{
	
	private SQLiteDALPayout mSQLiteDALPayout;
	
	/**
	 * Payout��������ֶ���
	 * 0  PayoutID		1 AccountBookID	2 AccountBookName	3 CategoryID
	 * 4  CategoryName	5 Path			6 PayWayID			7 Amount
	 * 8  PayoutDate	9 PayoutType 	10 PayoutUserID		11 Comment
	 * 12 CreateDate	13 State
	 */
	private String[] mColumnNames;
	private String mTableName;

	public BusinessPayout(Context pContext) {
		super(pContext);
		mSQLiteDALPayout = new SQLiteDALPayout(pContext);
		mColumnNames = mSQLiteDALPayout.getColumnNames();
		mTableName = mSQLiteDALPayout.getTableName();
	}

	/**
	 * ����������
	 */
	public boolean insertPayout(ModelPayout pModelPayout){
		boolean _Result = mSQLiteDALPayout.insertPayout(pModelPayout);
		return _Result;
	}
	
	/**
	 * ͨ��PayoutIDɾ������
	 */
	public boolean deletePayoutByPayoutID(int pPayoutID) {
		String _Condition = " And "+mColumnNames[0]+" = "+pPayoutID;
		return mSQLiteDALPayout.deletePayout(_Condition);
	}
	
	/**
	 * ͨ��AccountBookIDɾ������
	 */
	public boolean deletePayoutByAccountBookID(int pAccountBookID) {
		String _Condition = " And "+mColumnNames[1]+" = "+pAccountBookID;
		return mSQLiteDALPayout.deletePayout(_Condition);
	}
	
	/**
	 * ͨ��CategoryIDɾ������
	 */
	public boolean delectPayoutByCategoryID(int pCategoryID) {
		String _Condition = " And "+mColumnNames[3]+" = "+pCategoryID;
		return mSQLiteDALPayout.deletePayout(_Condition);
	}
	
	/**
	 * ͨ��PayoutID��������
	 */
	public boolean updatePayoutByPayoutID(ModelPayout pModelPayout) {
		String _Condition = mColumnNames[0]+" = "+pModelPayout.getPayoutID();
		boolean _Result = mSQLiteDALPayout.updatePayout(_Condition, pModelPayout);
		return _Result;
	}
	
	/**
	 * ��һ�����������������ת�Ƶ���һ�������
	 */
	public boolean transferPayoutByCategoryID(int pFromCategoryID, int pToCategoryID) {
		String _Condition = mColumnNames[3]+" = "+pFromCategoryID;
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(mColumnNames[3], pToCategoryID);
		return mSQLiteDALPayout.updatePayout(_Condition, _ContentValues);		
	}
	
	/**
	 * ������FromUser�������޸�ΪToUser������
	 */
	public boolean transferPayoutByUserID(int pFromUserID, int pToUserID) {
		List<ModelPayout> _PayoutList = getPayout("");
		
		mSQLiteDALPayout.beginTransaction();
		try {			
			for (ModelPayout _ModelPayout : _PayoutList) {
				String _PayoutUserID = _ModelPayout.getPayoutUserID();
				boolean _Result = true;
				//������б��滻���û� �����ǵ�һ��
				if (_PayoutUserID.startsWith(pFromUserID+",")) {
					_PayoutUserID.replaceFirst(pFromUserID+",", pToUserID+",");
					//���ظ���ɾ��
					_PayoutUserID.replace(","+pToUserID+",", ",");
				} 
				//������б��滻���û� ���Ҳ��ǵ�һ��
				if (_PayoutUserID.contains(","+pFromUserID+",")) {
					if (_PayoutUserID.startsWith(pToUserID+",")) {
						_PayoutUserID.replace(","+pFromUserID+",", ",");
					} else {
						_PayoutUserID.replace(","+pToUserID+",", ",");
						_PayoutUserID.replaceFirst(pFromUserID+",", pToUserID+",");
					}
				}
				//�޸�
				if (!_ModelPayout.getPayoutUserID().equals(_PayoutUserID)) {
					_ModelPayout.setPayoutUserID(_PayoutUserID);
					_Result = updatePayoutByPayoutID(_ModelPayout);
				}
				//�޸�ʧ�ܣ�����false
				if (!_Result) {
					return false;
				}
			}
			//ȫ���޸���ϣ�����true
			mSQLiteDALPayout.setTransactionSuccessful();
			return true;			
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALPayout.endTransaction();
		}

	}
	
	/**
	 * ͨ���������ȡ�������б�
	 */
	public List<ModelPayout> getPayout(String pCondition) {
		return mSQLiteDALPayout.getPayout(pCondition);
	}
	
	/**
	 *  �õ��������ѵ�����
	 */
	public int getPayoutCount() {
		return mSQLiteDALPayout.getCount("");
	}
	
	/**
	 * ͨ��AccountBookID�õ����ʱ��µ���������
	 */
	public int getPayoutCountByAccountBookID(int pAccountBookID) {
		List<ModelPayout> _List = getPayoutByAccountBookID(pAccountBookID);
		return _List.size();
	}
	
	/**
	 * ͨ������PayoutIDȡ���û�����
	 */
	public ModelPayout getModelPayoutByPayoutID(int pPayoutID) {
		List<ModelPayout> _List = getPayout(" And "+mColumnNames[0]+" = "+pPayoutID);
		if (_List.size() == 1) {
			return _List.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * ͨ��AccountBookID�õ��˱�������Payout
	 */
	public List<ModelPayout> getPayoutByAccountBookID(int pAccountBookID) {
		String _Condition = " And "+mColumnNames[1]+" = "+pAccountBookID
						+" Order By "+mColumnNames[8]+" DESC,"+mColumnNames[0]+" DESC";
		return getPayout(_Condition);
	}
	
	/**
	 * ͨ��CategoryID�õ������������Payout
	 */
	public List<ModelPayout> getPayoutByCategoryID(int pCategoryID) {
		String _Condition = " And "+mColumnNames[3]+" = "+pCategoryID;
		return getPayout(_Condition);
	}
	
	/**
	 * ͨ���������õ����� ���ѱ��� �� �����ܶ� ��String����  0 ���� 1���
	 */
	public String[] getPayoutTotal(String pCondition) {
		String _SqlText = "Select Ifnull(Sum("+mColumnNames[7]+"),0) As SumAmount,"
				+ " Count("+mColumnNames[7]+") As Count From ViewPayout Where 1=1 "+pCondition; 
		String[] _Total = new String[2];
		Cursor _Cursor = mSQLiteDALPayout.execSql(_SqlText);
		if (_Cursor.getCount() == 1) {
			while (_Cursor.moveToNext()) {
				_Total[0] = _Cursor.getString(_Cursor.getColumnIndex("Count"));
				_Total[1] = _Cursor.getString(_Cursor.getColumnIndex("SumAmount"));
			}
		}
		return _Total;
	}
	
	/**
	 * �õ��ʱ�ͳ�Ʊ��⣨��  %1$s �ʣ��ϼ����� %2$s Ԫ��
	 */
	public String getPayoutTotalMessage(String pPayoutDate, int pAccountBookID) {
		String[] _Total = getPayoutTotalByPayoutDate(pPayoutDate, pAccountBookID);
		return getContext().getString(R.string.TextViewTextPayoutTotal, new Object[]{_Total[0],_Total[1]});
	}
	
	/**
	 * ͨ��PayoutDate���õ����� ���ѱ��� �� �����ܶ� ��String���� 0 ���� 1���
	 */
	public String[] getPayoutTotalByPayoutDate(String pPayoutDate, int pAccountBookID) {
		String _Condition = " And "+mColumnNames[8]+" LIKE '"+pPayoutDate
				+"' And "+mColumnNames[1]+" = "+pAccountBookID;
		return getPayoutTotal(_Condition);
	}
	
	/**
	 * ͨAccountBookID���õ����� ���ѱ��� �� �����ܶ� ��String���� 0 ���� 1���
	 */
	public String[] getPayoutTotalByAccountBookID(int pAccountBookID) {
		String _Condition = " And "+mColumnNames[1]+" = "+pAccountBookID;
		return getPayoutTotal(_Condition);
	}
	
	/**
	 * ͨ�������õ���PayoutUserID����������б�
	 */
	public List<ModelPayout> getPayoutOrderByPayoutUserID(String pCondition) {
		pCondition += " Order By "+mColumnNames[10];
		List<ModelPayout> _List = getPayout(pCondition);
		if (_List.size() > 0) {
			return _List;
		} else {
			return null;
		}
	}
	
}
