package philip.godutch.model;

import java.util.Date;

public class ModelUser {

	//�û�������
	private int mUserID=0;
	//�û�����
	private String mUserName;
	//�������
	private Date mCreateDate = new Date();
	//״̬��0ʧЧ��1����
	private int mState = 1;
	
	public int getUserID() {
		return mUserID;
	}
	public void setUserID(int pUserID) {
		mUserID = pUserID;
	}
	public String getUserName() {
		return mUserName;
	}
	public void setUserName(String pUserName) {
		mUserName = pUserName;
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
	
	
}
