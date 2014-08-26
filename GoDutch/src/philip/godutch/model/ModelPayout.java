package philip.godutch.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 0 ֧�������ID	1 �ʱ�ID���	2 �˱�����	3 ֧�����ID���	4 �������
 * 5 ���·��	6 ���ʽID���	7 ���ѽ��	8 ��������	9 ���㷽ʽ
 * 10 ������ID���	11 ��ע	12 �������	13 ״̬
 */
public class ModelPayout implements Serializable {
	/**
	 * ModelPayout serialVersionUID = 10001
	 */
	private static final long serialVersionUID = 10001;
	//0 ֧�������ID
	private int mPayoutID = 0;
	//1 �ʱ�ID���
	private int mAccountBookID;
	//2 �˱�����
	private String mAccountBookName;
	//3 ֧�����ID���
	private int mCategoryID;
	//4 �������
	private String mCategoryName;
	//5 ���·��
	private String mCategoryPath;
	//6 ���ʽID���
	private int mPayWayID;
	//7 ���ѽ��
	private BigDecimal mAmount;
	//8 ��������
	private Date mPayoutDate;
	//9 ���㷽ʽ
	private String mPayoutType;
	//10 ������ID���
	private String mPayoutUserID;
	//11 ��ע
	private String mComment;
	//12 �������
	private Date mCreateDate = new Date();
	//13 ״̬ 0ʧЧ 1����
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
