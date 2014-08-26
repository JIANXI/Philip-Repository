package philip.godutch.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import philip.godutch.business.base.BusinessBase;
import philip.godutch.database.dal.SQLiteDALUser;
import philip.godutch.model.ModelUser;


public class BusinessUser extends BusinessBase{
	
	private SQLiteDALUser mSQLiteDALUser;
	/**
	 * 0 id 1 name 2 date 3 state 
	 */
	private String[] mColumnNames;

	public BusinessUser(Context pContext) {
		super(pContext);
		mSQLiteDALUser = new SQLiteDALUser(pContext);
		mColumnNames = mSQLiteDALUser.getColumnNames();
	}

	/**
	 * 插入新用户
	 */
	public boolean insertUser(ModelUser pModelUser){
		boolean _Result = mSQLiteDALUser.insertUser(pModelUser);
		return _Result;
	}
	
	/**
	 * 通过UserID删除用户对象
	 */
	public boolean deleteUserByUserID(int pUserID) {
		String _Condition = " And "+mColumnNames[0]+" = "+pUserID;
		boolean _Result = mSQLiteDALUser.deleteUser(_Condition);
		return _Result;
	}
	
	/**
	 * 通过UserID隐藏用户 （逻辑删除）
	 */
	public boolean hideUserByUserID(int pUserID) {
		String _Condition = mColumnNames[0]+" = "+pUserID;
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(mColumnNames[3], 0);
		boolean _Result = mSQLiteDALUser.updateUser(_Condition, _ContentValues);
		return _Result;
	}
	
	/**
	 * 通过UserID恢复被隐藏的用户
	 */
	public boolean unhideUserByUserID(int pUserID) {
		String _Condition = mColumnNames[0]+" = "+pUserID;
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(mColumnNames[3], 1);
		boolean _Result = mSQLiteDALUser.updateUser(_Condition, _ContentValues);
		return _Result;
	}
	
	/**
	 * 将一个用户的消费转移到另一个用户名下，并删除此用户
	 */
	public boolean transferUser(int pFromUserID, int pToUserID) {
		boolean _Result = false;
		BusinessPayout _BusinessPayout = new BusinessPayout(getContext());
		ModelUser _ToUser = getModelUserByUserID(pToUserID);
		_ToUser.setState(1);
		
		mSQLiteDALUser.beginTransaction();
		try {
			//1 将消费记录的FromUser改为ToUser，2删除FromUser，3将ToUser的状态改为1
			if (	_BusinessPayout.transferPayoutByUserID(pFromUserID, pToUserID) 
					&& deleteUserByUserID(pFromUserID) 
					&& updateUserByUserID(_ToUser)) 	{
					_Result = true;
			}
			if (_Result) {
				mSQLiteDALUser.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALUser.endTransaction();
		}
	}
	
	/**
	 * 通过UserID更改用户对象
	 */
	public boolean updateUserByUserID(ModelUser pModelUser) {
		String _Condition = mColumnNames[0]+" = "+pModelUser.getUserID();
		boolean _Result = mSQLiteDALUser.updateUser(_Condition, pModelUser);
		return _Result;
	}
	
	/**
	 * 得到所有状态为1的用户对象列表
	 */
	public List<ModelUser> getNotHideUser() {
		return mSQLiteDALUser.getUser(" And State = 1");
	}
	
	/**
	 * 得到状态为1的用户数量
	 */
	public int getNotHideUserCount() {
		return getNotHideUser().size();
	}
	
	/**
	 * 通过条件语句取得用户对象列表
	 */
	private List<ModelUser> getUser(String pCondition) {
		return mSQLiteDALUser.getUser(pCondition);
	}
	
	/**
	 * 通过单个UserID取得用户对象
	 */
	public ModelUser getModelUserByUserID(int pUserID) {
		List<ModelUser> _List = getUser(" And "+mColumnNames[0]+" = "+pUserID);
		if (_List.size() == 1) {
			return _List.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 通过UserID得到UserName
	 */
	public String getUserNameByUserID(int pUserID) {
		ModelUser _ModelUser = getModelUserByUserID(pUserID);
		return _ModelUser.getUserName();
	}
	
	/**
	 * 通过UserName和UserID判断用户是否已经存在
	 * 返回 -1 用户名没有重复，0  和状态为1的用户重复，n 和状态为0且ID为n的用户重复 
	 */
	public int isExistByUserName(String pUserName, int pIntUserID){
		Integer pUserID = Integer.valueOf(pIntUserID);
		String _Condition = " And "+mColumnNames[1]+"='"+pUserName+"' And "+mColumnNames[0]+"<>"+pUserID;
		List<ModelUser> _List = mSQLiteDALUser.getUser(_Condition);
		if (_List.size() > 0) {
			if (_List.get(0).getState() == 1) {
				return 0;
			} else {
				return _List.get(0).getUserID();
			}
		}else {
			return -1; 
		}
	}

	/**
	 * 从一组"d,d,d,d,"格式的String中读出UserID并得到ModelUser数组
	 */
	public List<ModelUser> getUserByUserID(String pUserID) {
		List<ModelUser> _List = new ArrayList<ModelUser>();
		
		String _IDString;
		int _ID;
		while (pUserID.indexOf(",") != -1){
			_IDString = pUserID.substring(0, pUserID.indexOf(","));
			_ID = Integer.parseInt(_IDString);
			_List.add(getModelUserByUserID(_ID));
			
			//当第一个","不是最后一个字符，截取第一个","后面的部分再次读取
			if (pUserID.indexOf(",") != pUserID.lastIndexOf(",")) {
				pUserID = pUserID.substring(pUserID.indexOf(",")+1);
			} else {
				pUserID = "";
			}
		}
		
		return _List;
	}
	
	/**
	 * 通过一组"d,d,d,d,"格式的String得到"s,s,s,s,"格式的UserName
	 */
	public String getUserNameByUserID(String pUserID) {
		List<ModelUser> _List = getUserByUserID(pUserID);
		String _Name = "";
		for (int i = 0; i < _List.size(); i++) {
			_Name += _List.get(i).getUserName() + ",";
		}
		return _Name;
	}
	
}
