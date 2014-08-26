package philip.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import philip.godutch.R;
import philip.godutch.adapter.base.AdapterBase;
import philip.godutch.business.BusinessUser;
import philip.godutch.model.ModelUser;

public class AdapterUser extends AdapterBase {

	private class Holder{
		ImageView imageViewUserSelected;
		TextView textViewUserName;
	}
	
	public AdapterUser(Context pContext) {
		super(pContext, null);
		BusinessUser _BusinessUser = new BusinessUser(pContext);
		List<ModelUser> _List = _BusinessUser.getNotHideUser();
		setList(_List);
	}

	List<ModelUser> mList;	
	/**
	 * 重载构造函数 ， 将传入的User对象内的ImageView显示出来
	 */
	public AdapterUser(Context pContext, List<ModelUser> pList) {	
		super(pContext, null);
		mList = pList;
		BusinessUser _BusinessUser = new BusinessUser(pContext);
		List<ModelUser> _List = _BusinessUser.getNotHideUser();
		setList(_List);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.user_list_item, null);
			_Holder = new Holder();
			_Holder.textViewUserName = (TextView) convertView.findViewById(R.id.textViewUserName);
			_Holder.imageViewUserSelected = (ImageView) convertView.findViewById(R.id.imageViewUserSelected);
			convertView.setTag(_Holder);
		}else {
			_Holder = (Holder) convertView.getTag();
		}
				
		ModelUser _Item = (ModelUser) getList().get(position);
		_Holder.textViewUserName.setText(_Item.getUserName());
		
		if (mList != null &&  mList.size() > 0 && isListCountainModelUser(mList, _Item) != -1) {
			_Holder.imageViewUserSelected.setVisibility(View.VISIBLE);
		} else {
			_Holder.imageViewUserSelected.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	/**
	 * List<ModelUser> 是否包含有某个 ModelUser，如果包含，返回在List中的position，不包含，返回-1
	 */
	public static int isListCountainModelUser(List<ModelUser> pList, ModelUser pModelUser){
		for (int i = 0; i < pList.size(); i++) {
			if (pList.get(i).getUserID() == pModelUser.getUserID()) {
				return i;
			} 
		}
		return -1;
	}

}
