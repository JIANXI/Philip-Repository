package philip.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import philip.godutch.R;
import philip.godutch.adapter.base.AdapterBase;
import philip.godutch.business.BusinessPayout;
import philip.godutch.business.BusinessUser;
import philip.godutch.model.ModelPayout;
import philip.godutch.utility.DateUtil;

public class AdapterPayout extends AdapterBase{

	private class Holder {
		TextView textViewDate;
		TextView textViewTotal;
		TextView textViewCategoryName;
		TextView textViewAmount;
		TextView textViewPayoutUserAndPayoutType;
		View relativeLayoutDate;
	}
	
	private BusinessPayout mBusinessPayout;
	private BusinessUser mBusinessUser;
	private int mAccoutnBookID;
	
	public AdapterPayout(Context pContext, int pAccountBookID) {
		super(pContext, null);
		mBusinessPayout = new BusinessPayout(pContext);
		mBusinessUser = new BusinessUser(pContext);
		mAccoutnBookID = pAccountBookID;
		List<ModelPayout> _List = mBusinessPayout.getPayoutByAccountBookID(mAccoutnBookID);
		setList(_List);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder;
		
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.payout_list_item, null);
			_Holder = new Holder();
			_Holder.relativeLayoutDate = (View) convertView.findViewById(R.id.relativeLayoutDate);
			_Holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewPayoutDate);
			_Holder.textViewTotal = (TextView) convertView.findViewById(R.id.textViewTotal);
			_Holder.textViewCategoryName = (TextView) convertView.findViewById(R.id.textViewCategoryName);
			_Holder.textViewAmount = (TextView) convertView.findViewById(R.id.textViewAmount);
			_Holder.textViewPayoutUserAndPayoutType = (TextView) convertView.findViewById(R.id.textViewPayoutUserAndPayoutType);		
			convertView.setTag(_Holder);
		} else {
			_Holder = (Holder) convertView.getTag();
		}
		
		ModelPayout _ModelPayout = (ModelPayout) getItem(position);
		String _PayoutDate = DateUtil.getFormatDate(_ModelPayout.getPayoutDate());
		boolean _IsShow = false;	
		if (position > 0) {
			ModelPayout _ModelPayoutLast = (ModelPayout) getItem(position - 1);
			String _PayoutDateLast  = DateUtil.getFormatDate(_ModelPayoutLast.getPayoutDate());
			_IsShow = !_PayoutDate.equals(_PayoutDateLast);
		}
		if (_IsShow || position == 0 ) {
			_Holder.relativeLayoutDate.setVisibility(View.VISIBLE);
			String _Message = mBusinessPayout.getPayoutTotalMessage(_PayoutDate, mAccoutnBookID);
			_Holder.textViewDate.setText(DateUtil.getFormatDate(_ModelPayout.getPayoutDate()));
			_Holder.textViewTotal.setText(_Message);
		}
		
		_Holder.textViewCategoryName.setText(_ModelPayout.getCategoryName());
		_Holder.textViewPayoutUserAndPayoutType.setText(mBusinessUser.getUserNameByUserID(_ModelPayout.getPayoutUserID())
														+" ( "+_ModelPayout.getPayoutType()+" )");
		_Holder.textViewAmount.setText(_ModelPayout.getAmount().toString()+"Ԫ");
		
		return convertView;
	}

}
