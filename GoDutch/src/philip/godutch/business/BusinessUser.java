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
	 * �������û�
	 */
	public boolean insertUser(ModelUser pModelUser){
		boolean _Result = mSQLiteDALUser.insertUser(pModelUser);
		return _Result;
	}
	
	/**
	 * ͨ��UserIDɾ���û�����
	 */
	public boolean deleteUserByUserID(int pUserID) {
		String _Condition = " And "+mColumnNames[0]+" = "+pUserID;
		boolean _Result = mSQLiteDALUser.deleteUser(_Condition);
		return _Result;
	}
	
	/**
	 * ͨ��UserID�����û� ���߼�ɾ����
	 */
	public boolean hideUserByUserID(int pUserID) {
		String _Condition = mColumnNames[0]+" = "+pUserID;
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(mColumnNames[3], 0);
		boolean _Result = mSQLiteDALUser.updateUser(_Condition, _ContentValues);
		return _Result;
	}
	
	/**
	 * ͨ��UserID�ָ������ص��û�
	 */
	public boolean unhideUserByUserID(int pUserID) {
		String _Condition = mColumnNames[0]+" = "+pUserID;
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put(mColumnNames[3], 1);
		boolean _Result = mSQLiteDALUser.updateUser(_Condition, _ContentValues);
		return _Result;
	}
	
	/**
	 * ��һ���û�������ת�Ƶ���һ���û����£���ɾ�����û�
	 */
	public boolean transferUser(int pFromUserID, int pToUserID) {
		boolean _Result = false;
		BusinessPayout _BusinessPayout = new BusinessPayout(getContext());
		ModelUser _ToUser = getModelUserByUserID(pToUserID);
		_ToUser.setState(1);
		
		mSQLiteDALUser.beginTransaction();
		try {
			//1 �����Ѽ�¼��FromUser��ΪToUser��2ɾ��FromUser��3��ToUser��״̬��Ϊ1
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
	 * ͨ��UserID�����û�����
	 */
	public boolean updateUserByUserID(ModelUser pModelUser) {
		String _Condition = mColumnNames[0]+" = "+pModelUser.getUserID();
		boolean _Result = mSQLiteDALUser.updateUser(_Condition, pModelUser);
		return _Result;
	}
	
	/**
	 * �õ�����״̬Ϊ1���û������б�
	 */
	public List<ModelUser> getNotHideUser() {
		return mSQLiteDALUser.getUser(" And State = 1");
	}
	
	/**
	 * �õ�״̬Ϊ1���û�����
	 */
	public int getNotHideUserCount() {
		return getNotHideUser().size();
	}
	
	/**
	 * ͨ���������ȡ���û������б�
	 */
	private List<ModelUser> getUser(String pCondition) {
		return mSQLiteDALUser.getUser(pCondition);
	}
	
	/**
	 * ͨ������UserIDȡ���û�����
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
	 * ͨ��UserID�õ�UserName
	 */
	public String getUserNameByUserID(int pUserID) {
		ModelUser _ModelUser = getModelUserByUserID(pUserID);
		return _ModelUser.getUserName();
	}
	
	/**
	 * ͨ��UserName��UserID�ж��û��Ƿ��Ѿ�����
	 * ���� -1 �û���û���ظ���0  ��״̬Ϊ1���û��ظ���n ��״̬Ϊ0��IDΪn���û��ظ� 
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
	 * ��һ��"d,d,d,d,"��ʽ��String�ж���UserID���õ�ModelUser����
	 */
	public List<ModelUser> getUserByUserID(String pUserID) {
		List<ModelUser> _List = new ArrayList<ModelUser>();
		
		String _IDString;
		int _ID;
		while (pUserID.indexOf(",") != -1){
			_IDString = pUserID.substring(0, pUserID.indexOf(","));
			_ID = Integer.parseInt(_IDString);
			_List.add(getModelUserByUserID(_ID));
			
			//����һ��","�������һ���ַ�����ȡ��һ��","����Ĳ����ٴζ�ȡ
			if (pUserID.indexOf(",") != pUserID.lastIndexOf(",")) {
				pUserID = pUserID.substring(pUserID.indexOf(",")+1);
			} else {
				pUserID = "";
			}
		}
		
		return _List;
	}
	
	/**
	 * ͨ��һ��"d,d,d,d,"��ʽ��String�õ�"s,s,s,s,"��ʽ��UserName
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
