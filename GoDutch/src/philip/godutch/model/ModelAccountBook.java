package philip.godutch.model;

import java.util.Date;

public class ModelAccountBook {

	//帐本主键
	private int mAccountBookID = 0;
	
	//账本名称
	private String mAccountBookName;
	
	//添加日期
	private Date mCreateDate = new Date();
	
	//状态 0无效 1启用
	private int mState = 1;
	
	//是否默认帐本 0否 1是
	private int mIsdefault = 0;

	public int getAccountBookID() {
		return mAccountBookID;
	}

	public void setAccountBookID(int pAccountBookID) {
		mAccountBookID = pAccountBookID;
	}

	public String getAccountBookName() {
		return mAccountBookName;
	}

	public void setAccountBookName(String pAccountBookName) {
		mAccountBookName = pAccountBookName;
	}

	public Date getCreateDate() {
		return mCreateDate;
	}

	public void setCreateDate(Date pCreateDate) {
		mCreateDate = pCreateDate;
	}

	public int getState() {
		return mState;
	}

	public void setState(int pState) {
		mState = pState;
	}

	public int getIsdefault() {
		return mIsdefault;
	}

	public void setIsdefault(int pIsdefault) {
		mIsdefault = pIsdefault;
	}

}
