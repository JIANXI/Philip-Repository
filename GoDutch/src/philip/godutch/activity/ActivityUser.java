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

		// ��ʼ��
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
	 * ���ûص��������ӿڣ�����SlideMenuItem�����¼�
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
		//�õ���ǰAdapter
		ListAdapter _ListAdapter = listViewUserList.getAdapter();
		//��Adapter��ȡ���û�����
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
	 * ������ӻ����޸��û��ĶԻ���
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
	 * ��������޸��û���Ϣʱ������AlertDialog��ť����¼�
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
			//�жϵ�����Ǳ��棨true������ȡ����false��
			if (mIsSaveButton == false) {
				setAlertDialogIsClose(dialog, true);
				return;
			}
			
			//�޶��������½�һ������
			if (mModelUser == null) {
				mModelUser = new ModelUser();
			}
			
			//����û�����ʽ�Ƿ���ȷ
			String _UserName = editTextUserName.getText().toString().trim();	
			if (!RegExUtil.isChineseEnglishNum(_UserName)) {
				showToast(getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{editTextUserName.getHint()}));
				setAlertDialogIsClose(dialog, false);
				return;
			}
			mModelUser.setUserName(_UserName);
			
			boolean _Result = false;
			//����û����Ƿ��ظ� -1 �û���û���ظ���0  ��״̬Ϊ1���û��ظ���n ��״̬Ϊ0��IDΪn���û��ظ� 
			int _CheckResult = mBusinessUser.isExistByUserName(_UserName, mModelUser.getUserID());
			//0 ��״̬Ϊ1���û����ظ�
			if (_CheckResult == 0) {
				showToast(getString(R.string.CheckDataTextUserExist));
				setAlertDialogIsClose(dialog, false);
				return;
			} 
			//-1 �û���û���ظ�
			if (_CheckResult == -1) {
				if (mModelUser.getUserID() == 0) {
					_Result = mBusinessUser.insertUser(mModelUser);
				} else {
					_Result = mBusinessUser.updateUserByUserID(mModelUser);
				}
			}
			//n ��״̬Ϊ0���û����ظ�
			if (_CheckResult > 0) {
				//������½��û���ֱ������״̬Ϊ0��ͬ���û�������Ǹ���������ͬ���û��������Ѽ�¼ת�ƣ����ɾ��ԭ�û�
				if (mModelUser.getUserID() == 0) {
					_Result = mBusinessUser.unhideUserByUserID(_CheckResult);
				} else {
					_Result = mBusinessUser.transferUser(mModelUser.getUserID(),_CheckResult);
				}		
			}
			
			//�õ����
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
	 * �߼�ɾ���û�ʱ������AlertDialog��ť����¼�
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
