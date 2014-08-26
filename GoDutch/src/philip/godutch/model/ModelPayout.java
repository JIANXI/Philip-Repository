package philip.godutch.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 0 支出表外键ID	1 帐本ID外键	2 账本名称	3 支出类别ID外键	4 类别名称
 * 5 类别路径	6 付款方式ID外键	7 消费金额	8 消费日期	9 计算方式
 * 10 消费人ID外键	11 备注	12 添加日期	13 状态
 */
public class ModelPayout implements Serializable {
	/**
	 * ModelPayout serialVersionUID = 10001
	 */
	private static final long serialVersionUID = 10001;
	//0 支出表外键ID
	private int mPayoutID = 0;
	//1 帐本ID外键
	private int mAccountBookID;
	//2 账本名称
	private String mAccountBookName;
	//3 支出类别ID外键
	private int mCategoryID;
	//4 类别名称
	private String mCategoryName;
	//5 类别路径
	private String mCategoryPath;
	//6 付款方式ID外键
	private int mPayWayID;
	//7 消费金额
	private BigDecimal mAmount;
	//8 消费日期
	private Date mPayoutDate;
	//9 计算方式
	private String mPayoutType;
	//10 消费人ID外键
	private String mPayoutUserID;
	//11 备注
	private String mComment;
	//12 添加日期
	private Date mCreateDate = new Date();
	//13 状态 0失效 1启用
	private int mState = 1;
	
	public int getPayoutID() {
		return mPayoutID;
	}
	public void setPayoutID(int pPayoutID) {
		mPayoutID = pPayoutID;
	}
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
	public String getCategoryPath() {
		return mCategoryPath;
	}
	public void setCategoryPath(String pCategoryPath) {
		mCategoryPath = pCategoryPath;
	}
	public int getPayWayID() {
		return mPayWayID;
	}
	public void setPayWayID(int pPayWayID) {
		mPayWayID = pPayWayID;
	}
	public BigDecimal getAmount() {
		return mAmount;
	}
	public void setAmount(BigDecimal pAmount) {
		mAmount = pAmount;
	}
	public Date getPayoutDate() {
		return mPayoutDate;
	}
	public void setPayoutDate(Date pPayoutDate) {
		mPayoutDate = pPayoutDate;
	}
	public String getPayoutType() {
		return mPayoutType;
	}
	public void setPayoutType(String pPayoutType) {
		mPayoutType = pPayoutType;
	}
	public String getPayoutUserID() {
		return mPayoutUserID;
	}
	public void setPayoutUserID(String pPayoutUserID) {
		mPayoutUserID = pPayoutUserID;
	}
	public String getComment() {
		return mComment;
	}
	public void setComment(String pComment) {
		mComment = pComment;
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
