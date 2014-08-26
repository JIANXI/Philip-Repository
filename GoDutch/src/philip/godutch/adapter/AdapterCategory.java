package philip.godutch.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import philip.godutch.R;
import philip.godutch.business.BusinessCategory;
import philip.godutch.model.ModelCategory;

public class AdapterCategory extends BaseExpandableListAdapter {

	private class GroupHolder{
		TextView textViewCategoryName;
		TextView textViewCount;
	}
	
	private class ChildHolder{
		TextView textViewCategoryName;
	}
	
	private Context mContext;
	private BusinessCategory mBusinessCategory;
	private List<ModelCategory> mGroupList;
	public List<Integer> mChildCountOfGroup;
	
	public AdapterCategory(Context pContext) {
		mContext = pContext;
		mBusinessCategory = new BusinessCategory(pContext);
		mGroupList = mBusinessCategory.getRootCategory();
		mChildCountOfGroup = new ArrayList<Integer>();
	}
	
	@Override
	public ModelCategory getChild(int groupPosition, int childPosition) {
		ModelCategory _ModelCategory = getGroup(groupPosition);
		List<ModelCategory> _List = mBusinessCategory.getCategoryByParentID(_ModelCategory.getCategoryID());
		return _List.get(childPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ModelCategory _ParentModelCategory = getGroup(groupPosition);
		List<ModelCategory> _List=mBusinessCategory.getCategoryByParentID(_ParentModelCategory.getCategoryID());
		return _List.size();
	}

	@Override
	public ModelCategory getGroup(int groupPosition) {
		return  mGroupList.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		//分组信息改变时，原来的类别和子类别的ID是否保持不变
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder _GroupHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.category_group_list_item, null);
			_GroupHolder = new GroupHolder();
			_GroupHolder.textViewCategoryName = (TextView) convertView.findViewById(R.id.textViewCategoryName);
			_GroupHolder.textViewCount = (TextView) convertView.findViewById(R.id.textViewCount);
			convertView.setTag(_GroupHolder);
		} else {
			_GroupHolder = (GroupHolder) convertView.getTag();
		}
		
		ModelCategory _ModelCategory = getGroup(groupPosition);
		_GroupHolder.textViewCategoryName.setText(_ModelCategory.getCategoryName());
		int _Count = getChildrenCount(groupPosition);
		_GroupHolder.textViewCount.setText(mContext.getString(R.string.TextViewTextChildrenCategory, _Count));
		
		mChildCountOfGroup.add(Integer.valueOf(_Count));
		
		return convertView;
	}
	

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		ChildHolder _ChildHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.category_child_list_item, null);
			_ChildHolder = new ChildHolder();
			_ChildHolder.textViewCategoryName = (TextView) convertView.findViewById(R.id.textViewCategoryName);
			convertView.setTag(_ChildHolder);
		} else {
			_ChildHolder = (ChildHolder) convertView.getTag();
		}
		
		ModelCategory _ModelCategory = (ModelCategory) getChild(groupPosition, childPosition);
		_ChildHolder.textViewCategoryName.setText(_ModelCategory.getCategoryName());
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		//子类别是否可选
		return true;
	}

}
