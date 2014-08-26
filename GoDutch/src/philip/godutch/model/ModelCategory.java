package philip.godutch.model;

import java.io.Serializable;
import java.util.Date;

public class ModelCategory implements Serializable{

	/**
	 *  ModelCategory serialVersionUID = 10000
	 */
	private static final long serialVersionUID = 10000;
	//类别表主键ID
	private int mCategoryID;
	//类别名称
	private String mCategoryName;
	//父类型ID
	private int mParentID = 0;
	//路径
	private String mPath = "0";
	//添加日期
	private Date mCreateDate = new Date();
	//状态 0失效 1启用
	private int mState = 1;
	
	public int getCategoryID() {
		return mCategoryID;
	}
	public void setCategoryID(int pCategoryID) {
		mCategoryID = pCategoryID;
	}
	
	public String getCategoryName() {
		return mCategoryName;
	}
	public void setCategoryName(String pCategoryName) {
		mCategoryName = pCategoryName;
	}
	
	public int getParentID() {
		return mParentID;
	}
	public void setParentID(int pParentID) {
		mParentID = pParentID;
	}
	
	public String getPath() {
		return mPath;
	}
	public void setPath(String pPath) {
		mPath = pPath;
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
	
	@Override
	public String toString() {
		return mCategoryName;
	}
	
}
