package philip.godutch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.adapter.AdapterAccountBook;
import philip.godutch.adapter.AdapterPayout;
import philip.godutch.business.BusinessAccountBook;
import philip.godutch.business.BusinessFileOperation;
import philip.godutch.business.BusinessPayout;
import philip.godutch.controls.SlideMenuItem;
import philip.godutch.controls.SlideMenuView.OnSlideMenuItemListener;
import philip.godutch.model.ModelAccountBook;
import philip.godutch.model.ModelPayout;

public class ActivityPayout extends ActivityFrame implements OnSlideMenuItemListener{

	private ListView listViewPayoutList;
	private AdapterPayout mAdapterPayout;
	private ModelPayout mSelectedModelPayout;
	private ModelAccountBook mSelectedModelAccountBook;
	private BusinessPayout mBusinessPayout;
	private BusinessAccountBook mBusinessAccountBook;
	
	private final int REQUEST_CODE=10000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.payout_list);

		// 初始化
		initVariable();
		initView();
		initListener();
		bindData();
		
		createSlideMenu(R.array.SlideMenuPayout);
	}

	private void initVariable() {
		mBusinessPayout = new BusinessPayout(this);
		mBusinessAccountBook = new BusinessAccountBook(this);
		mSelectedModelAccountBook = mBusinessAccountBook.getDefaultAccountBook();
	}

	private void initView() {
		listViewPayoutList = (ListView) findViewById(R.id.listViewPayoutList);
	}

	private void initListener() {
		registerForContextMenu(listViewPayoutList);
	}

	private void bindData() {
		mAdapterPayout = new AdapterPayout(this, mSelectedModelAccountBook.getAccountBookID());
		listViewPayoutList.setAdapter(mAdapterPayout);
		setTitle();
	}
	
	private void setTitle(){
		String _AccountBookName = mSelectedModelAccountBook.getAccountBookName();
		int _PayoutCount = mBusinessPayout.getPayoutCountByAccountBookID(mSelectedModelAccountBook.getAccountBookID());
		String _Amount = mBusinessPayout.getPayoutTotalByAccountBookID(mSelectedModelAccountBook.getAccountBookID())[1];
		String _Title = getResources().getString(R.string.ActivityTitlepaytou,new Object[]{_AccountBookName, _PayoutCount, _Amount});
		setTitleBar(_Title);
	}

	/*
	 * 利用回调函数（接口）设置SlideMenuItem监听事件
	 */
	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		if (pSlideMenuItem.getItemID() == 0) {
			showAccountBookSelectDialog();
		} else if (pSlideMenuItem.getItemID() == 1) {
			BusinessFileOperation _BusinessFileOperation = new BusinessFileOperation(this);
			String _ExportFilePath = _BusinessFileOperation.exportPayoutData(mSelectedModelAccountBook.getAccountBookID());
			if ( _ExportFilePath != null) {
				showToast(R.string.TipsExportSucceed,new Object[]{_ExportFilePath});
			} else {
				showToast(R.string.TipsExportFail);
			}
		}
	}

	/**
	 * 选择帐本对话框
	 */ 
	private void showAccountBookSelectDialog(){
		
		View _View = getLayoutInflater().inflate(R.layout.account_book_list, null);
		ListView _ListViewAccountBook = (ListView) _View.findViewById(R.id.listViewAccountBookList);
		AdapterAccountBook _AdapterAccountBook = new AdapterAccountBook(this);
		_ListViewAccountBook.setAdapter(_AdapterAccountBook);
		
		String _Title = getString(R.string.DialogTitleAccountBook,new Object[]{getString(R.string.TitleEdit)});
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		AlertDialog _AlertDialog = _Builder.setTitle(_Title)
									.setIcon(R.drawable.ic_account_book_small)
									.setView(_View)
									.setNegativeButton(getString(R.string.ButtontextCancel), null)
									.show();
		
		_ListViewAccountBook.setOnItemClickListener(new OnAccountBookItemClickListener(_AlertDialog));
	}
	
	/**
	 * 选择帐本ItemListener
	 */
	private class OnAccountBookItemClickListener implements OnItemClickListener{

		private AlertDialog mAlertDialog;
		
		public OnAccountBookItemClickListener(AlertDialog pAlertDialog) {
			mAlertDialog = pAlertDialog;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AdapterAccountBook _AdapterAccountBook = (AdapterAccountBook) parent.getAdapter();
			mSelectedModelAccountBook = (ModelAccountBook) _AdapterAccountBook.getItem(position);
			bindData();
			slideMenuToggle();
			mAlertDialog.dismiss();
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		//得到当前Adapter
		ListAdapter _ListAdapter = listViewPayoutList.getAdapter();
		//从Adapter中取出点击的Item
		AdapterContextMenuInfo _AdapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		mSelectedModelPayout = (ModelPayout) _ListAdapter.getItem(_AdapterContextMenuInfo.position);
		
		menu.setHeaderIcon(R.drawable.ic_bill_small)
			.setHeaderTitle(mSelectedModelPayout.getCategoryName());
		createMenu(menu);
		menu.add(0, 3, 0, R.string.MenuTextComment);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent _Intent = new Intent(this, ActivityPayoutAddOrEdit.class);
			_Intent.putExtra("ModelPayout", mSelectedModelPayout);
			startActivityForResult(_Intent,REQUEST_CODE);
			break;
		case 2:
			showPayoutDeleteDialog();
			break;
		case 3:
			showCommentDialog();
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	private void showPayoutDeleteDialog() {
		String _Title = getString(R.string.DialogTitleDelete);
		String _CategoryName = mSelectedModelPayout.getCategoryName();
		String _Message = getString(R.string.DialogMessagePayoutDelete, new Object[]{_CategoryName});
		
		showAlertDialoge(_Title, R.drawable.ic_delete_small, _Message, new OnDeletePayoutListener());
	}
	
	private void showCommentDialog(){
		String _Title = getString(R.string.DialogTitleComment);
		String _Message = mSelectedModelPayout.getComment();
		if (_Message.trim().equals("")) {
			_Message = getString(R.string.DialogMessageCommentNull);
		}
		new AlertDialog.Builder(this)
			.setTitle(_Title)
			.setIcon(R.drawable.ic_bill_small)
			.setMessage(_Message)
			.setNegativeButton(R.string.ButtonTextBack, null)
			.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			bindData();
		}
	}
	
	/**
	 * 删除消费时，监听AlertDialog按钮点击事件
	 */
	private class OnDeletePayoutListener implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean _Result = mBusinessPayout.deletePayoutByPayoutID(mSelectedModelPayout.getPayoutID());
			if (_Result) {
				bindData();
				showToast(getString(R.string.TipsDeleteSucceed));
			} else {
				showToast(getString(R.string.TipsDeleteFail));
			}
		}		
	}
	
	
	
}
