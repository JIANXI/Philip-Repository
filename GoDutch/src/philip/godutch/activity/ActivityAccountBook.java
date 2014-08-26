package philip.godutch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.adapter.AdapterAccountBook;
import philip.godutch.business.BusinessAccountBook;
import philip.godutch.controls.SlideMenuItem;
import philip.godutch.controls.SlideMenuView.OnSlideMenuItemListener;
import philip.godutch.model.ModelAccountBook;
import philip.godutch.utility.RegExUtil;

public class ActivityAccountBook extends ActivityFrame implements OnSlideMenuItemListener{

	private ListView listViewAccountBookList;
	private AdapterAccountBook mAdapterAccountBookList;
	private BusinessAccountBook mBusinessAccountBook;
	private ModelAccountBook mSelectedModelAccountBook;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.account_book_list);

		// 初始化
		initVariable();
		initView();
		initListener();
		bindData();
		
		createSlideMenu(R.array.SlideMenuAccountBook);
	}

	private void initVariable() {
		mBusinessAccountBook = new BusinessAccountBook(this);
	}

	private void initView() {
		listViewAccountBookList = (ListView) findViewById(R.id.listViewAccountBookList);
	}

	private void initListener() {
		registerForContextMenu(listViewAccountBookList);
	}

	private void bindData() {
		mAdapterAccountBookList = new AdapterAccountBook(this);
		listViewAccountBookList.setAdapter(mAdapterAccountBookList);
		setTitle();
	}

	private void setTitle() {
		int _AccountBookCount = mBusinessAccountBook.getNotHideAccountBookCount();
		String _Title = getString(R.string.ActivityTitleAccountBook, new Object[]{_AccountBookCount});
		setTitleBar(_Title);
	}
	
	/*
	 * 利用回调函数（接口）设置SlideMenuItem监听事件
	 */
	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		if (pSlideMenuItem.getItemID() == 0) {
			showAccountBookAddOrEditDialog(null);
			bindData();
		}
	}

	/**
	 * 创建添加或者修改帐本的对话框
	 */ 
	private void showAccountBookAddOrEditDialog(ModelAccountBook pModelAccountBook){
		View _View = getLayoutInflater().inflate(R.layout.account_book_add_or_edit, null);
		
		EditText _EditTextAccountBookName = (EditText) _View.findViewById(R.id.editTextAccountBookName);
		CheckBox _CheckBoxIsDefault = (CheckBox) _View.findViewById(R.id.checkBoxIsDefault);
		
		String _Title;
		//对象为空为添加，有对象为编辑
		if (pModelAccountBook == null) {
			_Title = getString(R.string.DialogTitleAccountBook, new Object[]{getString(R.string.TitleAdd)});
		} else {
			_Title = getString(R.string.DialogTitleAccountBook, new Object[]{getString(R.string.TitleEdit)});
			_EditTextAccountBookName.setText(pModelAccountBook.getAccountBookName());
			//如果是默认帐本，不能取消默认状态
			if (pModelAccountBook.getIsdefault() == 1) {
				_CheckBoxIsDefault.setChecked(true);
				_CheckBoxIsDefault.setClickable(false);
			} else {
				_CheckBoxIsDefault.setChecked(false);
			}
		}
		
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(_Title)
				.setIcon(R.drawable.ic_account_book_small)
				.setView(_View)
				.setNeutralButton(getString(R.string.ButtonTextSave), new OnAddOrEditAccountBookListener(pModelAccountBook, _EditTextAccountBookName, _CheckBoxIsDefault, true))
				.setNegativeButton(getString(R.string.ButtontextCancel), new OnAddOrEditAccountBookListener(null, null, null,false))
				.show();
	}
	
	private void showAccountBookDeleteDialog() {
		String _Title = getString(R.string.DialogTitleDelete);
		String _AccountBookName = mSelectedModelAccountBook.getAccountBookName();
		String _Message = getString(R.string.DialogMessageAccountBookDelete, new Object[]{_AccountBookName});
		
		showAlertDialoge(_Title, android.R.drawable.ic_delete, _Message, new OnDeleteAccountBookListener());
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		//得到当前Adapter
		ListAdapter _ListAdapter = listViewAccountBookList.getAdapter();
		//从Adapter中取出用户数据
		AdapterContextMenuInfo _AdapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		mSelectedModelAccountBook = (ModelAccountBook) _ListAdapter.getItem(_AdapterContextMenuInfo.position);
		
		menu.setHeaderIcon(R.drawable.ic_account_book_small)
			.setHeaderTitle(mSelectedModelAccountBook.getAccountBookName());
		createMenu(menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			showAccountBookAddOrEditDialog(mSelectedModelAccountBook);
			break;
		case 2:
			if (mSelectedModelAccountBook.getIsdefault() == 1) {
				showToast(getString(R.string.CheckDataTextAccountBookDeleteDefault));
			} else {
				showAccountBookDeleteDialog();
			}
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * 插入或者修改帐本信息时，监听AlertDialog按钮点击事件
	 */
	private class OnAddOrEditAccountBookListener implements DialogInterface.OnClickListener{
		private ModelAccountBook mModelAccountBook;
		private EditText editTextAccountBookName;
		private CheckBox checkBoxIsDefault;
		private boolean mIsSaveButton;
		
		public OnAddOrEditAccountBookListener(ModelAccountBook pModelAccountBook, EditText pEditText, CheckBox pCheckBoxIsDefault, boolean pIsSaveButton) {
			mModelAccountBook = pModelAccountBook;
			editTextAccountBookName = pEditText;
			checkBoxIsDefault = pCheckBoxIsDefault;
			mIsSaveButton = pIsSaveButton;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//点击的为取消按钮则直接退出对话框
			if (mIsSaveButton == false) {
				setAlertDialogIsClose(dialog, true);
				return;
			}
			//无对象传入则新建一个对象
			if (mModelAccountBook == null) {
				mModelAccountBook = new ModelAccountBook();
			}
			
			//检查帐本名格式是否正确，帐本名是否已经存在
			String _AccountBookName = editTextAccountBookName.getText().toString().trim();
			boolean _CheckResult = RegExUtil.isChineseEnglishNum(_AccountBookName);		
			if (!_CheckResult) {
				showToast(getString(R.string.CheckDataTextChineseEnglishNum,new Object[]{editTextAccountBookName.getHint()}));
				setAlertDialogIsClose(dialog, false);
				return;
			} else {
				setAlertDialogIsClose(dialog, true);
			}
			_CheckResult = mBusinessAccountBook.isExistByAccountBookName(_AccountBookName, mModelAccountBook.getAccountBookID());
			if (_CheckResult) {
				showToast(getString(R.string.CheckDataTextAccountBookExist));
				return;
			} else {
				setAlertDialogIsClose(dialog, true);
			}
			
			//判断checkBox的值并设置进对象
			boolean _IsChecked = checkBoxIsDefault.isChecked();
			mModelAccountBook.setIsdefault(_IsChecked?1:0);
			mModelAccountBook.setAccountBookName(_AccountBookName);
			//如果ID=0插入帐本，不为0更新帐本
			boolean _Result = false;
			if (mModelAccountBook.getAccountBookID() == 0) {			
				_Result = mBusinessAccountBook.insertAccountBook(mModelAccountBook);
			} else {
				_Result = mBusinessAccountBook.updateAccountBookByAccountBookID(mModelAccountBook);
			}
			
			if (_Result) {
				bindData();
				//添加将SlideMenu关闭
				if (mModelAccountBook.getAccountBookID() == 0) {
					slideMenuToggle();
				}
				showToast(getString(R.string.TipsAddSucceed));
			} else {
				showToast(getString(R.string.TipsAddFail));
			}

		}
		
	}
	
	/**
	 * 逻辑删除用户时，监听AlertDialog按钮点击事件
	 */
	private class OnDeleteAccountBookListener implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean _Result = mBusinessAccountBook.deleteAccountBookByAccountBookID(mSelectedModelAccountBook.getAccountBookID());
			if (_Result) {
				bindData();
				showToast(getString(R.string.TipsDeleteSucceed));
			}else {
				showToast(getString(R.string.TipsDeleteFail));
			}
		}	
	}
	
}
