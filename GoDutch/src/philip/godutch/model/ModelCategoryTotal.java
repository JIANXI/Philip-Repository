package philip.godutch.model;

import java.io.Serializable;

public class ModelCategoryTotal implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mCount;
	private String mSumAmount;
	private String mCategoryName;
	
	public String getCount() {
		return mCount;
	}
	public void setCount(String pCount) {
		mCount = pCount;
	}
	public String getSumAmount() {
		return mSumAmount;
	}
	public void setSumAmount(String pSumAmount) {
		mSumAmount = pSumAmount;
	}
	public String getCategoryName() {
		return mCategoryName;
	}
	public void setCategoryName(String pCategoryName) {
		mCategoryName = pCategoryName;
	}
		
	
	
}
