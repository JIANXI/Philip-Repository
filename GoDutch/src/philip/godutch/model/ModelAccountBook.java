package philip.godutch.model;

import java.util.Date;

public class ModelAccountBook {

	//�ʱ�����
	private int mAccountBookID = 0;
	
	//�˱�����
	private String mAccountBookName;
	
	//�������
	private Date mCreateDate = new Date();
	
	//״̬ 0��Ч 1����
	private int mState = 1;
	
	//�Ƿ�Ĭ���ʱ� 0�� 1��
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
