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
	 * 通过一个根类别，得到其下所有消费的 数量和总金额，传入ModelCategoryTotal
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
	 * 将账本下所有根类别的统计依次放入一个列表
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
	 * 得到用户消费信息统计，0 用户名列表，1 相应的金额列表 
	 */
	public List<List<?>> getUserPayoutStatisticByAccountBookID(ModelAccountBook pModelAccountBook) {
		//要输出的 用户名列表和金额列表
		List<String> _ListUserName = new ArrayList<String>();
		List<BigDecimal> _ListAmount = new ArrayList<BigDecimal>();
		//需要统计的消费列表
		List<ModelPayout> _ListPayout = mBusinessPayout.getPayoutByAccountBookID(pModelAccountBook.getAccountBookID());
		
		String _PayoutUser; //原始用户名数据
		String[] _SplitName; //每一次消费的所有用户名列表
		BigDecimal _Amount; //每一次 消费每人均摊的金额
		//开始计算每一笔消费的数据
		for (ModelPayout _ModelPayout : _ListPayout) {
			_PayoutUser = _ModelPayout.getPayoutUserID(); //得到用户ID
			_PayoutUser = mBusinessUser.getUserNameByUserID(_PayoutUser);//得到用户名
			_SplitName = _PayoutUser.split(","); //得到分开的用户名数组
			_Amount = _ModelPayout.getAmount().divide(BigDecimal.valueOf(_SplitName.length)); //每人该付的金额
			//计算该笔消费，更新每个消费者总的消费金额
			for (int i = 0; i < _SplitName.length; i++) {
				//该用户是否已经存在列表中
				if (!_ListUserName.contains(_SplitName[i])) {
					_ListUserName.add(_SplitName[i]);
					_ListAmount.add(_Amount);
				} else {
					int index = _ListUserName.indexOf(_SplitName[i]); //已经存在，得到所在列表的位置
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
	 * 从金额列表中得到最大的值
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
