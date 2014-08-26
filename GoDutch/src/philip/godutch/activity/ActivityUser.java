package philip.godutch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.adapter.AdapterUser;
import philip.godutch.business.BusinessUser;
import philip.godutch.controls.SlideMenuItem;
import philip.godutch.controls.SlideMenuView.OnSlideMenuItemListener;
import philip.godutch.model.ModelUser;
import philip.godutch.utility.RegExUtil;

public class ActivityUser extends ActivityFrame implements OnSlideMenuItemListener{

	private ListView listViewUserList;
	private AdapterUser mAdapterUserList;
	private BusinessUser mBusinessUser;
	private ModelUser mSelectedModelUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.user_list);

		// 初始化
		initVariable();
		initView();
		initListener();
		bindData();
		
		createSlideMenu(R.array.SlideMenuUser);
	}

	private void initVariable() {
		mBusinessUser = new BusinessUser(this);
	}

	private void initView() {
		listViewUserList = (ListView) findViewById(R.id.listViewUserList);
	}

	private void initListener() {
		registerForContextMenu(listViewUserList);
	}

	private void bindData() {
		mAdapterUserList = new AdapterUser(this);
		listViewUserList.setAdapter(mAdapterUserList);
		setTitle();
	}
	
	private void setTitle(){
		int _UserCount = mBusinessUser.getNotHideUserCount();
		String _Title = getResources().getString(R.string.ActivityTitleUser,new Object[]{_UserCount});
		setTitleBar(_Title);
	}

	/*
	 * 利用回调函数（接口）设置SlideMenuItem监听事件
	 */
	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		if (pSlideMenuItem.getItemID() == 0) {
			showUserAddOrEditDialog(null);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		//得到当前Adapter
		ListAdapter _ListAdapter = listViewUserList.getAdapter();
		//从Adapter中取出用户数据
		AdapterContextMenuInfo _AdapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		mSelectedModelUser = (ModelUser) _ListAdapter.getItem(_AdapterContextMenuInfo.position);
		
		menu.setHeaderIcon(R.drawable.ic_user_small)
			.setHeaderTitle(mSelectedModelUser.getUserName());
		createMenu(menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			showUserAddOrEditDialog(mSelectedModelUser);
			break;
		case 2:
			showUserDeleteDialog();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * 创建添加或者修改用户的对话框
	 */ 
	private void showUserAddOrEditDialog(ModelUser pModelUser){
		View _View = getLayoutInflater().inflate(R.layout.user_add_or_edit, null);
		
		EditText _EditTextUserName = (EditText) _View.findViewById(R.id.editTextUserName);
		
		String _Title;
		if (pModelUser == null) {
			_Title = getString(R.string.DialogTitleUser, new Object[]{getString(R.string.TitleAdd)});
		} else {
			_Title = getString(R.string.DialogTitleUser, new Object[]{getString(R.string.TitleEdit)});
			_EditTextUserName.setText(pModelUser.getUserName());
		}
		
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(_Title)
				.setIcon(R.drawable.ic_user_small)
				.setView(_View)
				.setNeutralButton(getString(R.string.ButtonTextSave), new OnAddOrEditUserListener(pModelUser, _EditTextUserName, true))
				.setNegativeButton(getString(R.string.ButtontextCancel), new OnAddOrEditUserListener(null, null, false))
				.show();
	}
	
	private void showUserDeleteDialog() {
		String _Title = getString(R.string.DialogTitleDelete);
		String _UserName = mSelectedModelUser.getUserName();
		String _Message = getString(R.string.DialogMessageUserDelete, new Object[]{_UserName});
		
		showAlertDialoge(_Title, android.R.drawable.ic_delete, _Message, new OnDeleteUserListener());
	}
	
	/**
	 * 插入或者修改用户信息时，监听AlertDialog按钮点击事件
	 */
	private class OnAddOrEditUserListener implements DialogInterface.OnClickListener{
		private ModelUser mModelUser;
		private EditText editTextUserName;
		private boolean mIsSaveButton;
		
		public OnAddOrEditUserListener(ModelUser pModelUser, EditText pEditText, boolean pIsSaveButton) {
			mModelUser = pModelUser;
			editTextUserName = pEditText;
			mIsSaveButton = pIsSaveButton;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//判断点击的是保存（true）还是取消（false）
			if (mIsSaveButton == false) {
				setAlertDialogIsClose(dialog, true);
				return;
			}
			
			//无对象传入则新建一个对象
			if (mModelUser == null) {
				mModelUser = new ModelUser();
			}
			
			//检查用户名格式是否正确
			String _UserName = editTextUserName.getText().toString().trim();	
			if (!RegExUtil.isChineseEnglishNum(_UserName)) {
				showToast(getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{editTextUserName.getHint()}));
				setAlertDialogIsClose(dialog, false);
				return;
			}
			mModelUser.setUserName(_UserName);
			
			boolean _Result = false;
			//检查用户名是否重复 -1 用户名没有重复，0  和状态为1的用户重复，n 和状态为0且ID为n的用户重复 
			int _CheckResult = mBusinessUser.isExistByUserName(_UserName, mModelUser.getUserID());
			//0 和状态为1的用户有重复
			if (_CheckResult == 0) {
				showToast(getString(R.string.CheckDataTextUserExist));
				setAlertDialogIsClose(dialog, false);
				return;
			} 
			//-1 用户名没有重复
			if (_CheckResult == -1) {
				if (mModelUser.getUserID() == 0) {
					_Result = mBusinessUser.insertUser(mModelUser);
				} else {
					_Result = mBusinessUser.updateUserByUserID(mModelUser);
				}
			}
			//n 和状态为0的用户有重复
			if (_CheckResult > 0) {
				//如果是新建用户，直接启用状态为0的同名用户；如果是改名，启用同名用户并将消费记录转移，最后删除原用户
				if (mModelUser.getUserID() == 0) {
					_Result = mBusinessUser.unhideUserByUserID(_CheckResult);
				} else {
					_Result = mBusinessUser.transferUser(mModelUser.getUserID(),_CheckResult);
				}		
			}
			
			//得到结果
			if (_Result) {
				setAlertDialogIsClose(dialog, true);
				bindData();
				slideMenuToggle();
				showToast(getString(R.string.TipsAddSucceed));
			} else {
				showToast(getString(R.string.TipsAddFail));
			}
			
		}
	}
	
	/**
	 * 逻辑删除用户时，监听AlertDialog按钮点击事件
	 */
	private class OnDeleteUserListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean _Result = mBusinessUser.hideUserByUserID(mSelectedModelUser.getUserID());
			if (_Result) {
				bindData();
				showToast(getString(R.string.TipsDeleteSucceed));
			} else {
				showToast(getString(R.string.TipsDeleteFail));
			}
		}
		
	}
	
}
