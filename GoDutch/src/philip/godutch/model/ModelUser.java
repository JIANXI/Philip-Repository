package philip.godutch.model;

import java.util.Date;

public class ModelUser {

	//用户表主键
	private int mUserID=0;
	//用户名称
	private String mUserName;
	//添加日期
	private Date mCreateDate = new Date();
	//状态，0失效，1启用
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
