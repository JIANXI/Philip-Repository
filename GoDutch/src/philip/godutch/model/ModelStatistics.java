package philip.godutch.model;

import java.math.BigDecimal;

public class ModelStatistics {
	
	private String mPayoutUserName;
	private String mPayoutType;
	private String mCategory;
	private BigDecimal mAmount;
	private String mComment;
	
	public String getPayoutUserName() {
		return mPayoutUserName;
	}
	public void setPayoutUserName(String pPayoutUserName) {
		mPayoutUserName = pPayoutUserName;
	}
	public String getPayoutType() {
		return mPayoutType;
	}
	public void setPayoutType(String pPayoutType) {
		mPayoutType = pPayoutType;
	}
	public BigDecimal getAmount() {
		return mAmount;
	}
	public void setAmount(BigDecimal pAmount) {
		mAmount = pAmount;
	}	
	public String getComment() {
		return mComment;
	}
	public void setComment(String pComment) {
		mComment = pComment;
	}
	public String getCategory() {
		return mCategory;
	}
	public void setCategory(String pCategory) {
		mCategory = pCategory;
	}
	
}