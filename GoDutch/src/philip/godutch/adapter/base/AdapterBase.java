package philip.godutch.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class AdapterBase extends BaseAdapter {

	private List<?> mList;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	public AdapterBase(Context pContext,List<?> pList) {
		mContext=pContext;
		mList=pList;
		mLayoutInflater=LayoutInflater.from(mContext);
	}
	
	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public List getList() {
		return mList;
	}
	
	public void setList(List<?> pList) {
		mList = pList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
