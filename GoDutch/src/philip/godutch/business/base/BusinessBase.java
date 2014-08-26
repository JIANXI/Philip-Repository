package philip.godutch.business.base;

import android.content.Context;

public class BusinessBase {

	private Context mContext;
	
	protected  BusinessBase(Context pContext ) {
		mContext = pContext;
	}
	
	protected String getString(int pResID) {
		return mContext.getString(pResID);
	}
	
	protected String getString(int pResID, Object[] pObject) {
		return mContext.getString(pResID, pObject);
	}
	
	protected String[] getStringArray(int pResID) {
		return mContext.getResources().getStringArray(pResID);
	}
	
	protected Context getContext() {
		return mContext;
	}
	
}
