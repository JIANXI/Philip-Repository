package philip.godutch.business;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import philip.godutch.R;
import philip.godutch.business.base.BusinessBase;
import philip.godutch.database.dal.SQLiteDALCategory;
import philip.godutch.model.ModelCategory;


public class BusinessCategory extends BusinessBase{
	
	private SQLiteDALCategory mSQLiteDALCategory;
	BusinessPayout mBusinessPayout;
	/**
	 * 0 id 1 name 2 parent id 3 path 4 date 5 state
	 */
	private String[] mColumnNames;
	
	public BusinessCategory(Context pContext) {
		super(pContext);
		mSQLiteDALCategory = new SQLiteDALCategory(pContext);
		mColumnNames = mSQLiteDALCategory.getColumnNames();
		mBusinessPayout = new BusinessPayout(pContext);
	}

	/**
	 * ���������
	 */
	public boolean insertCategory(ModelCategory pModelCategory){
		
		mSQLiteDALCategory.beginTransaction();
		try {
			//����û��������ȷpath���Ե�������
			boolean _Result = mSQLiteDALCategory.insertCategory(pModelCategory);
			if (!_Result) {
				return false;
			}
			//���������ݿ��������ɵ�ID���������ö����path��ParentPath+ID+.��
			ModelCategory _ModelCategory = getModelCategoryByCategoryName(pModelCategory.getCategoryName());
			ModelCategory _ParentModelCategory = getModelCategoryByCategoryID(pModelCategory.getParentID());
			if (_ParentModelCategory != null) {
				_ModelCategory.setPath(_ParentModelCategory.getPath()+_ModelCategory.getCategoryID()+".");
			} else {
				_ModelCategory.setPath(_ModelCategory.getCategoryID()+".");
			}
			
			boolean _Result2 = updateCategoryByCategoryID(_ModelCategory);
			if (_Result2) {
				mSQLiteDALCategory.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALCategory.endTransaction();
		}
		
	}
	
	/**
	 * ͨ��CategoryIDɾ�����,�������µ��������
	 */
	public boolean deleteCategoryByCategoryID(int pCategoryID) {
		boolean _Result;
		boolean _Result2;
		mSQLiteDALCategory.beginTransaction();
		try {
			String _Condition = " And "+mColumnNames[0]+" = "+pCategoryID;
			_Result = mSQLiteDALCategory.deleteCategory(_Condition);
			_Result2 = mBusinessPayout.deletePayoutByPayoutID(pCategoryID);
			if (_Result && _Result2) {
				mSQLiteDALCategory.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALCategory.endTransaction();
		}
	}
	
	/**
	 * ͨ��CategoryID�޸Ķ���type
	 */
	public boolean updateCategoryInsertTypeByCategoryID(ModelCategory pModelCategory) {
		String _Condition = mColumnNames[0]+" = "+pModelCategory.getCategoryID();
		boolean _Result = mSQLiteDALCategory.updateCategory(_Condition, pModelCategory);
		return _Result;
	}
	
	/**
	 * ͨ��CategoryID����������
	 */
	public boolean updateCategoryByCategoryID(ModelCategory pModelCategory) {

		String _Condition = mColumnNames[0]+" = "+pModelCategory.getCategoryID();

		ModelCategory _ParentModelCategory = getModelCategoryByCategoryID(pModelCategory.getParentID());
		if (_ParentModelCategory != null) {
			pModelCategory.setPath(_ParentModelCategory.getPath()+pModelCategory.getCategoryID() + ".");
		} else {
			pModelCategory.setPath(pModelCategory.getCategoryID()+".");
		}

		boolean _Result = mSQLiteDALCategory.updateCategory(_Condition,pModelCategory);
		return _Result;
		
	}
	
	/**
	 * ��һ������е���������ת�Ƶ���һ����Ȼ��ɾ��֮
	 */
	public boolean transferCategory(int pFromCategoryID, int pToCategoryID) {
		mSQLiteDALCategory.beginTransaction();
		try {
			boolean _Result = mBusinessPayout.transferPayoutByCategoryID(pFromCategoryID, pToCategoryID);
			boolean _Result2 = deleteCategoryByCategoryID(pFromCategoryID);
			if (_Result && _Result2) {
				mSQLiteDALCategory.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALCategory.endTransaction();
		}
	}
	
	/**
	 * ͨ���������ȡ���������б�
	 */
	private List<ModelCategory> getCategory(String pCondition) {
		return mSQLiteDALCategory.getCategory(pCondition);
	}
	
	/**
	 * �õ������������б�
	 */
	public List<ModelCategory> getCategory() {
		return mSQLiteDALCategory.getCategory("");
	}
	
	/**
	 * �õ���������������
	 */
	public int getCategoryCount() {
		return mSQLiteDALCategory.getCount("");
	}
	
	/**
	 * �õ����и������б�
	 */
	public List<ModelCategory> getRootCategory() {
		return mSQLiteDALCategory.getCategory(" And "+mColumnNames[2]+" = 0 ");
	}
	
	/**
	 * �õ���ParentID�����������б�
	 */
	public List<ModelCategory> getCategoryByParentID(int pParentID) {
		return mSQLiteDALCategory.getCategory(" And "+mColumnNames[2]+" = "+pParentID);
	}
	
	/**
	 * �õ���ParentID��������������
	 */
	public int getCategoryCountByParentID(int pCategoryID) {
		return mSQLiteDALCategory.getCount(" And "+mColumnNames[2]+" = "+pCategoryID);
	}
	
	/**
	 * ͨ������CategoryIDȡ��������
	 */
	public ModelCategory getModelCategoryByCategoryID(int pCategoryID) {
		List<ModelCategory> _List = getCategory(" And "+mColumnNames[0]+" = "+pCategoryID);
		if (_List.size() == 1) {
			return _List.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * ͨ������CategoryNameȡ��������
	 */
	public ModelCategory getModelCategoryByCategoryName(String pCategoryName) {
		List<ModelCategory> _List = getCategory(" And "+mColumnNames[1]+" = '"+pCategoryName+"'");
		if (_List.size() == 1) {
			return _List.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * ͨ��CategoryName��CategoryID�ж�����Ƿ��Ѿ�����
	 */
	public boolean isExistByCategoryName(String pCategoryName, int pIntCategoryID){
		Integer pCategoryID = Integer.valueOf(pIntCategoryID);
		String _Condition = " And "+mColumnNames[1]+" ='"+pCategoryName+"'";
		if (pCategoryID != null) {
			_Condition += " And "+mColumnNames[0]+" <> "+pCategoryID;
		}
		List<ModelCategory> _List = mSQLiteDALCategory.getCategory(_Condition);
		if (_List.size() > 0) {
			return true;
		}else {
			return false; 
		}
	}
	
	/**
	 * �����и������ӵ�һ��ArrayAdapter�в����б���ǰ���һ����ʾString
	 */
	public ArrayAdapter getRootCategoryArrayAdapter() {
		List _List = getRootCategory();
		_List.add(0, getString(R.string.ArrayListTextCategory));
		ArrayAdapter _ArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, _List);
		_ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return _ArrayAdapter;
	}
	
	/**
	 * �����������ӵ�һ��ArrayAdapter��
	 */
	public ArrayAdapter getAllCategoryArrayAdapter() {
		List<ModelCategory> _List = getCategory();
		
		ArrayAdapter _ArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, _List);

		return _ArrayAdapter;
	}
	
}
