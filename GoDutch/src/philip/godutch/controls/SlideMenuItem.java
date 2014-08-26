package philip.godutch.controls;

public class SlideMenuItem {

	private int mItemID;
	private String mTitle;
	
	public SlideMenuItem(int pItemID,String pTitle) {
		// TODO Auto-generated constructor stub
		mItemID=pItemID;
		mTitle=pTitle;
	}
	
	public int getItemID() {
		return mItemID;
	}
	public void setItemID(int pItemID) {
		this.mItemID =pItemID;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String pTitle) {
		this.mTitle = pTitle;
	}
		
}
