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
	 * Payout表的所有字段名
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
	 * 插入新消费
	 */
	public boolean insertPayout(ModelPayout pModelPayout){
		boolean _Result = mSQLiteDALPayout.insertPayout(pModelPayout);
		return _Result;
	}
	
	/**
	 * 通过PayoutID删除消费
	 */
	public boolean deletePayoutByPayoutID(int pPayoutID) {
		String _Condition = " And "+mColumnNames[0]+" = "+pPayoutID;
		return mSQLiteDALPayout.deletePayout(_Condition);
	}
	
	/**
	 * 通过AccountBookID删除消费
	 */
	public boolean deletePayoutByAccountBookID(int pAccountBookID) {
		String _Condition = " And "+mColumnNames[1]+" = "+pAccountBookID;
		return mSQLiteDALPayout.deletePayout(_Condition);
	}
	
	/**
	 * 通过CategoryID删除消费
	 */
	public boolean delectPayoutByCategoryID(int pCategoryID) {
		String _Condition = " And "+mColumnNames[3]+" = "+pCategoryID;
		return mSQLiteDALPayout.deletePayout(_Condition);
	}
	
	/**
	 * 通过PayoutID更改消费
	 */
	public boolean updatePayoutByPayoutID(ModelPayout pModelPayout) {
		String _Condition = mColumnNames[0]+" = "+pModelPayout.getPayoutID();
		boolean _Result = mSQLiteDALPayout.updatePayout(_Condition, pModelPayout);
		return _Result;
	}
	
	/**
	 * 将一个类别的里的所有消费转移到另一个类别中
	 */
	public boolean transferPayoutByCategoryID(int pFromCategoryID, int pToCategoryID) {
		String _Condition = mColumnNames[3]+" = "+pFromCategoryID;
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(mColumnNames[3], pToCategoryID);
		return mSQLiteDALPayout.updatePayout(_Condition, _ContentValues);		
	}
	
	/**
	 * 将所有FromUser的消费修改为ToUser的消费
	 */
	public boolean transferPayoutByUserID(int pFromUserID, int pToUserID) {
		List<ModelPayout> _PayoutList = getPayout("");
		
		mSQLiteDALPayout.beginTransaction();
		try {			
			for (ModelPayout _ModelPayout : _PayoutList) {
				String _PayoutUserID = _ModelPayout.getPayoutUserID();
				boolean _Result = true;
				//如果含有被替换的用户 并且是第一个
				if (_PayoutUserID.startsWith(pFromUserID+",")) {
					_PayoutUserID.replaceFirst(pFromUserID+",", pToUserID+",");
					//将重复的删除
					_PayoutUserID.replace(","+pToUserID+",", ",");
				} 
				//如果含有被替换的用户 并且不是第一个
				if (_PayoutUserID.contains(","+pFromUserID+",")) {
					if (_PayoutUserID.startsWith(pToUserID+",")) {
						_PayoutUserID.replace(","+pFromUserID+",", ",");
					} else {
						_PayoutUserID.replace(","+pToUserID+",", ",");
						_PayoutUserID.replaceFirst(pFromUserID+",", pToUserID+",");
					}
				}
				//修改
				if (!_ModelPayout.getPayoutUserID().equals(_PayoutUserID)) {
					_ModelPayout.setPayoutUserID(_PayoutUserID);
					_Result = updatePayoutByPayoutID(_ModelPayout);
				}
				//修改失败，返回false
				if (!_Result) {
					return false;
				}
			}
			//全部修改完毕，返回true
			mSQLiteDALPayout.setTransactionSuccessful();
			return true;			
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALPayout.endTransaction();
		}

	}
	
	/**
	 * 通过条件语句取得消费列表
	 */
	public List<ModelPayout> getPayout(String pCondition) {
		return mSQLiteDALPayout.getPayout(pCondition);
	}
	
	/**
	 *  得到所有消费的数量
	 */
	public int getPayoutCount() {
		return mSQLiteDALPayout.getCount("");
	}
	
	/**
	 * 通过AccountBookID得到该帐本下的消费数量
	 */
	public int getPayoutCountByAccountBookID(int pAccountBookID) {
		List<ModelPayout> _List = getPayoutByAccountBookID(pAccountBookID);
		return _List.size();
	}
	
	/**
	 * 通过单个PayoutID取得用户对象
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
	 * 通过AccountBookID得到账本下所有Payout
	 */
	public List<ModelPayout> getPayoutByAccountBookID(int pAccountBookID) {
		String _Condition = " And "+mColumnNames[1]+" = "+pAccountBookID
						+" Order By "+mColumnNames[8]+" DESC,"+mColumnNames[0]+" DESC";
		return getPayout(_Condition);
	}
	
	/**
	 * 通过CategoryID得到该类别下所有Payout
	 */
	public List<ModelPayout> getPayoutByCategoryID(int pCategoryID) {
		String _Condition = " And "+mColumnNames[3]+" = "+pCategoryID;
		return getPayout(_Condition);
	}
	
	/**
	 * 通过条件，得到含有 消费笔数 和 消费总额 的String数组  0 数量 1金额
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
	 * 得到帐本统计标题（共  %1$s 笔，合计消费 %2$s 元）
	 */
	public String getPayoutTotalMessage(String pPayoutDate, int pAccountBookID) {
		String[] _Total = getPayoutTotalByPayoutDate(pPayoutDate, pAccountBookID);
		return getContext().getString(R.string.TextViewTextPayoutTotal, new Object[]{_Total[0],_Total[1]});
	}
	
	/**
	 * 通过PayoutDate，得到含有 消费笔数 和 消费总额 的String数组 0 数量 1金额
	 */
	public String[] getPayoutTotalByPayoutDate(String pPayoutDate, int pAccountBookID) {
		String _Condition = " And "+mColumnNames[8]+" LIKE '"+pPayoutDate
				+"' And "+mColumnNames[1]+" = "+pAccountBookID;
		return getPayoutTotal(_Condition);
	}
	
	/**
	 * 通AccountBookID，得到含有 消费笔数 和 消费总额 的String数组 0 数量 1金额
	 */
	public String[] getPayoutTotalByAccountBookID(int pAccountBookID) {
		String _Condition = " And "+mColumnNames[1]+" = "+pAccountBookID;
		return getPayoutTotal(_Condition);
	}
	
	/**
	 * 通过条件得到用PayoutUserID排序的消费列表
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
