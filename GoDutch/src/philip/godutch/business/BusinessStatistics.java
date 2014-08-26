package philip.godutch.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import philip.godutch.business.base.BusinessBase;
import philip.godutch.model.ModelAccountBook;
import philip.godutch.model.ModelCategory;
import philip.godutch.model.ModelCategoryTotal;
import philip.godutch.model.ModelPayout;

public class BusinessStatistics  extends BusinessBase{

	private BusinessPayout mBusinessPayout;
	private BusinessUser mBusinessUser;
	private BusinessCategory mbusienssCategory;
	
	public BusinessStatistics(Context pContext) {
		super(pContext);
		mBusinessPayout = new BusinessPayout(pContext);
		mBusinessUser = new BusinessUser(pContext);
		new BusinessAccountBook(pContext);
		mbusienssCategory = new BusinessCategory(pContext);
	}
	
	/**
	 * ͨ��һ������𣬵õ������������ѵ� �������ܽ�����ModelCategoryTotal
	 */
	public ModelCategoryTotal getCategoryTotalByPath(ModelCategory pModelCategory,int pAccountBookID) {
		String _Condition = "And Path LIKE '"+pModelCategory.getPath()+"%' And AccountBookID = "+pAccountBookID;
		String[] _Total = mBusinessPayout.getPayoutTotal(_Condition);
		
		ModelCategoryTotal _ModelCategoryTotal = new ModelCategoryTotal();
		_ModelCategoryTotal.setCategoryName(pModelCategory.getCategoryName());
		_ModelCategoryTotal.setCount(_Total[0]);
		_ModelCategoryTotal.setSumAmount(_Total[1]);
		return _ModelCategoryTotal;
		
	}
	
	/**
	 * ���˱������и�����ͳ�����η���һ���б�
	 */
	public List<ModelCategoryTotal> getRootCategoryTotalByAccountBookID(ModelAccountBook pModelAccountBook) {
		List<ModelCategoryTotal> _ListCategoryTotal = new ArrayList<>();
		
		List<ModelCategory> _ListRootCategory =mbusienssCategory.getRootCategory();
		for (ModelCategory _RootCategory:_ListRootCategory) {
			_ListCategoryTotal.add(getCategoryTotalByPath(_RootCategory, pModelAccountBook.getAccountBookID())) ;
		}
		return _ListCategoryTotal;
	}
	
	/**
	 * �õ��û�������Ϣͳ�ƣ�0 �û����б�1 ��Ӧ�Ľ���б� 
	 */
	public List<List<?>> getUserPayoutStatisticByAccountBookID(ModelAccountBook pModelAccountBook) {
		//Ҫ����� �û����б�ͽ���б�
		List<String> _ListUserName = new ArrayList<String>();
		List<BigDecimal> _ListAmount = new ArrayList<BigDecimal>();
		//��Ҫͳ�Ƶ������б�
		List<ModelPayout> _ListPayout = mBusinessPayout.getPayoutByAccountBookID(pModelAccountBook.getAccountBookID());
		
		String _PayoutUser; //ԭʼ�û�������
		String[] _SplitName; //ÿһ�����ѵ������û����б�
		BigDecimal _Amount; //ÿһ�� ����ÿ�˾�̯�Ľ��
		//��ʼ����ÿһ�����ѵ�����
		for (ModelPayout _ModelPayout : _ListPayout) {
			_PayoutUser = _ModelPayout.getPayoutUserID(); //�õ��û�ID
			_PayoutUser = mBusinessUser.getUserNameByUserID(_PayoutUser);//�õ��û���
			_SplitName = _PayoutUser.split(","); //�õ��ֿ����û�������
			_Amount = _ModelPayout.getAmount().divide(BigDecimal.valueOf(_SplitName.length)); //ÿ�˸ø��Ľ��
			//����ñ����ѣ�����ÿ���������ܵ����ѽ��
			for (int i = 0; i < _SplitName.length; i++) {
				//���û��Ƿ��Ѿ������б���
				if (!_ListUserName.contains(_SplitName[i])) {
					_ListUserName.add(_SplitName[i]);
					_ListAmount.add(_Amount);
				} else {
					int index = _ListUserName.indexOf(_SplitName[i]); //�Ѿ����ڣ��õ������б��λ��
					_ListAmount.set(index, _ListAmount.get(index).add(_Amount));
				}
			}
		}
		
		List<List<?>> _List = new ArrayList<List<?>>();
		_List.add(_ListUserName);
		_List.add(_ListAmount);
		return _List;
		
	}
	
	/**
	 * �ӽ���б��еõ�����ֵ
	 */
	public BigDecimal getMaxBigDecimal(List<BigDecimal> pAmountList) {
		BigDecimal _MaxAmount = BigDecimal.valueOf(0);
		for (BigDecimal _Amount : pAmountList) {
			if (_Amount.compareTo(_MaxAmount) == 1) {
				_MaxAmount = _Amount;
			}
		}
		return _MaxAmount;
	}

	
}
