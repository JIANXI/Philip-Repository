package philip.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import philip.godutch.R;
import philip.godutch.adapter.base.AdapterBase;
import philip.godutch.business.BusinessAccountBook;
import philip.godutch.business.BusinessPayout;
import philip.godutch.model.ModelAccountBook;

public class AdapterAccountBook extends AdapterBase {

	private BusinessPayout mBusinessPayout;
	
	private class Holder{
		ImageView imageViewAccountBookIcon;
		TextView textViewAccountBookName;
		TextView textViewTotal;
	}
	
	public AdapterAccountBook(Context pContext) {
		super(pContext, null);
		mBusinessPayout = new BusinessPayout(pContext);
		BusinessAccountBook _BusinessAccountBook = new BusinessAccountBook(pContext);
		List<ModelAccountBook> _List = _BusinessAccountBook.getNotHideAccountBook();
		setList(_List);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.account_book_list_item, null);
			_Holder = new Holder();
			_Holder.imageViewAccountBookIcon = (ImageView) convertView.findViewById(R.id.imageViewAccountBookIcon);
			_Holder.textViewAccountBookName = (TextView) convertView.findViewById(R.id.textViewAccountBookName);
			_Holder.textViewTotal = (TextView) convertView.findViewById(R.id.textViewTotal);
			convertView.setTag(_Holder);
		}else {
			_Holder = (Holder) convertView.getTag();
		}
		
		ModelAccountBook _Item = (ModelAccountBook) getList().get(position);
		if (_Item.getIsdefault() == 1) {
			_Holder.imageViewAccountBookIcon.setImageResource(R.drawable.ic_account_book_default);
		} else {
			_Holder.imageViewAccountBookIcon.setImageResource(R.drawable.ic_account_book);
		}
		_Holder.textViewAccountBookName.setText(_Item.getAccountBookName());
		//ÕÊ±¾Í³¼Æ
		String _Total = mBusinessPayout.getPayoutTotalMessage("%", _Item.getAccountBookID());
		_Holder.textViewTotal.setText(_Total);
		
		return convertView;
	}

}
