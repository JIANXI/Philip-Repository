package philip.godutch.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.adapter.AdapterCategory;
import philip.godutch.business.BusinessCategory;
import philip.godutch.controls.SlideMenuItem;
import philip.godutch.controls.SlideMenuView.OnSlideMenuItemListener;
import philip.godutch.model.ModelCategory;

public class ActivityCategory extends ActivityFrame implements OnSlideMenuItemListener{

	private int REQUEST_CODE = 10000;
	
	private ExpandableListView expandableListViewCategory;
	private AdapterCategory mAdapterCategory;
	private BusinessCategory mBusinessCategory;
	private ModelCategory mSelectedModelCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.category);

		// 初始化
		initVariable();
		initView();
		initListener();
		bindData();
		
		createSlideMenu(R.array.SlideMenuCategory);
	}

	private void initVariable() {
		mBusinessCategory = new BusinessCategory(this);
	}

	private void initView() {
		expandableListViewCategory = (ExpandableListView) findViewById(R.id.expandableListViewCategory);
	}

	private void initListener() {
		registerForContextMenu(expandableListViewCategory);
	}

	private void bindData() {
		mAdapterCategory = new AdapterCategory(this);
		expandableListViewCategory.setAdapter(mAdapterCategory);
		setTitle();
	}
	
	private void setTitle() {
		int _Count = mBusinessCategory.getCategoryCount();
		String _Title = getString(R.string.ActivityTitleCategory, _Count);
		setTitleBar(_Title);
	}

	/*
	 * 利用回调函数（接口）设置SlideMenuItem监听事件
	 */
	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		if (pSlideMenuItem.getItemID() == 0) {
			Intent _Intent = new Intent(this, ActivityCategoryAddOrEdit.class);
			this.startActivityForResult(_Intent, REQUEST_CODE);	
			return;
		}
	}
	
	/**
	 * 删除对话框
	 */
	private void showCategoryDeleteDialog() {
		//不能删除有子类的根类别
		if (mSelectedModelCategory.getParentID() == 0 && 
				mBusinessCategory.getCategoryByParentID(mSelectedModelCategory.getCategoryID()).size() > 0) {
			showToast(getString(R.string.CheckDataTextCategoryHasChild));
			return;
		}
		String _Title = getString(R.string.DialogTitleDelete);
		String _CategoryName = mSelectedModelCategory.getCategoryName();
		String _Message = getString(R.string.DialogMessageCategoryDelete, new Object[]{_CategoryName});
		
		showAlertDialoge(_Title, android.R.drawable.ic_delete
				, _Message, new OnDeleteCategoryListener(mSelectedModelCategory));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		//从menuInfo中得到Type和GroupPosition
		ExpandableListContextMenuInfo _ExpandableListContextMenuInfo = (ExpandableListContextMenuInfo) menuInfo;
		long _Position = _ExpandableListContextMenuInfo.packedPosition;
		int _Type = ExpandableListView.getPackedPositionType(_Position);
		int _GroupPosition = ExpandableListView.getPackedPositionGroup(_Position);
		
		//Type判断点击的是Group还是Child，从Adapter得到点击对象
		switch (_Type) {
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			mSelectedModelCategory =  mAdapterCategory.getGroup(_GroupPosition);
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			int _ChildPosition = ExpandableListView.getPackedPositionChild(_Position);
			mSelectedModelCategory = mAdapterCategory.getChild(_GroupPosition, _ChildPosition);
			break;
		default:
			break;
		}
		
		menu.setHeaderIcon(R.drawable.ic_category_small)
			.setHeaderTitle(mSelectedModelCategory.getCategoryName());
		createMenu(menu);
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent _Intent ;
		switch (item.getItemId()) {
		case 1:
			_Intent = new Intent(this, ActivityCategoryAddOrEdit.class);
			_Intent.putExtra("ModelCategory", mSelectedModelCategory);
			startActivityForResult(_Intent, REQUEST_CODE);
			break;
		case 2:
			showCategoryDeleteDialog();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bindData();
		slideMenuToggle();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 逻辑删除时，监听AlertDialog按钮点击事件
	 */
	private class OnDeleteCategoryListener implements DialogInterface.OnClickListener{
		
		private ModelCategory mModelCategory;
		
		public  OnDeleteCategoryListener(ModelCategory pModelCategory) {
			mModelCategory = pModelCategory;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean _Result = mBusinessCategory.deleteCategoryByCategoryID(mModelCategory.getCategoryID());
			if (_Result) {
				bindData();
				showToast(getString(R.string.TipsDeleteSucceed));
			} else {
				showToast(getString(R.string.TipsDeleteFail));
			}
		}
		
	}
	
}
