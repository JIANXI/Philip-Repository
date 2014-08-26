package philip.godutch.adapter;

import java.util.List;

import philip.godutch.R;
import philip.godutch.adapter.base.AdapterBase;
import philip.godutch.controls.SlideMenuItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdapterSlideMenu extends AdapterBase {

	private class Holder{
		TextView textViewMenuName;
	}
	
	public AdapterSlideMenu(Context pContext, List<?> pList) {
		super(pContext, pList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder _Holder;
		
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.slidemenu_list_item, null);
			_Holder=new Holder();
			
			_Holder.textViewMenuName =(TextView) convertView.findViewById(R.id.textViewMenuName);
			convertView.setTag(_Holder);
		}else {
			_Holder= (Holder) convertView.getTag();
		}
		
		SlideMenuItem _Item=(SlideMenuItem) getList().get(position);
		_Holder.textViewMenuName.setText(_Item.getTitle());
		
		return convertView;
	}

}
