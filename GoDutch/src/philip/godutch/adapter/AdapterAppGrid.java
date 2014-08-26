package philip.godutch.adapter;

import philip.godutch.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterAppGrid extends BaseAdapter {

	private Context mContext;
	
	private class Holder{
		ImageView imageViewIcon;
		TextView textViewName;
	}
	
	private Integer[] mImageInteger ={
			R.drawable.ic_payout_mid,
			R.drawable.ic_bill_mid,
			R.drawable.ic_report_mid,
			R.drawable.ic_account_book_mid,
			R.drawable.ic_category_mid,
			R.drawable.ic_user_mid
	};
	
	private String[] mImageString=new String[9];
	
	public AdapterAppGrid(Context pContext) {
		mContext=pContext;
		
		mImageString[0] = mContext.getString(R.string.appGridTextPayoutAdd);
		mImageString[1] = mContext.getString(R.string.appGridTextPayoutManage);
		mImageString[2] = mContext.getString(R.string.appGridTextStatisticsManage);
		mImageString[3] = mContext.getString(R.string.appGridTextAccountBookManage);
		mImageString[4] = mContext.getString(R.string.appGridTextCategoryManage);
		mImageString[5] = mContext.getString(R.string.appGridTextUserManage);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageInteger.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mImageString[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder;
		
		if (convertView == null) {
			LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
			convertView = _LayoutInflater.inflate(R.layout.main_gridview_item, null);
			_Holder=new Holder();
			
			_Holder.imageViewIcon =(ImageView) convertView.findViewById(R.id.imageViewIcon);
			_Holder.textViewName =(TextView) convertView.findViewById(R.id.textViewName);
			convertView.setTag(_Holder);
		}else {
			_Holder= (Holder) convertView.getTag();
		}
		
		_Holder.imageViewIcon.setImageResource(mImageInteger[position]);
		_Holder.textViewName.setText(mImageString[position]);
		
		return convertView;
	}

}
