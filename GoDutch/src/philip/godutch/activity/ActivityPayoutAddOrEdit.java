package philip.godutch.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.adapter.AdapterAccountBook;
import philip.godutch.adapter.AdapterCategory;
import philip.godutch.adapter.AdapterUser;
import philip.godutch.business.BusinessAccountBook;
import philip.godutch.business.BusinessCategory;
import philip.godutch.business.BusinessPayout;
import philip.godutch.business.BusinessUser;
import philip.godutch.controls.NumberDialog;
import philip.godutch.controls.NumberDialog.OnNumberDialogListener;
import philip.godutch.model.ModelAccountBook;
import philip.godutch.model.ModelCategory;
import philip.godutch.model.ModelPayout;
import philip.godutch.model.ModelUser;
import philip.godutch.utility.DateUtil;
import philip.godutch.utility.RegExUtil;

public class ActivityPayoutAddOrEdit extends ActivityFrame
			implements OnClickListener, OnNumberDialogListener{
	
	private ModelPayout mModelPayout;
	private ModelAccountBook mModelAccountBook;
	private ModelCategory mModelCategory;
	private BigDecimal mAmount;
	private Date mPayoutDate;
	private String mPayoutType;
	private String mPayoutUserID;
	
	private EditText editTextAccountBookName;
	private EditText editTextEnterAmount;
	private AutoCompleteTextView actvCategoryName;
	private EditText editTextPayoutDate;
	private EditText editTextPayoutType;
	private EditText editTextPayoutUser;
	private EditText editTextComment;
	private Button buttonSelectAccountBook;
	private Button buttonEnterAmount;
	private Button buttonSelectCategory;
	private Button buttonSelectPayoutDate;
	private Button buttonSelectPayoutType;
	private Button buttonSelectUser;
	private Button buttonSave;
	private Button buttonCancel;

	private BusinessPayout mBussinessPayout;
	private BusinessAccountBook mBusinessAccountBook;
	private BusinessCategory mBusinessCategory;
	private BusinessUser mBusinessUser;
	
	private List<ModelUser> mSelectedUser;
	/**
	 * 0 均分 1 个人
	 */
	String[] mPayoutTypeArr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.payout_add_or_edit);
		removeBottomBox();
		// 初始化
		initVariable();
		initView();
		initListener();
		bindData();
		setTitle();
	}
	
	private void initVariable(){
		mBussinessPayout = new BusinessPayout(this);
		mBusinessUser = new BusinessUser(this);
		mBusinessAccountBook = new BusinessAccountBook(this);
		mBusinessCategory = new BusinessCategory(this);	
	}

	private void initView(){
		editTextAccountBookName = (EditText) findViewById(R.id.editTextAccountBookName);
		editTextEnterAmount = (EditText) findViewById(R.id.editTextEnterAmount);
		actvCategoryName = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewCategoryName);
		editTextPayoutDate = (EditText) findViewById(R.id.editTextPayoutDate);
		editTextPayoutType = (EditText) findViewById(R.id.editTextPayoutType);
		editTextPayoutUser = (EditText) findViewById(R.id.editTextPayoutUser);
		editTextComment = (EditText) findViewById(R.id.editTextComment);
		
		buttonSelectAccountBook = (Button) findViewById(R.id.buttonSelectAccountBook);
		buttonEnterAmount = (Button) findViewById(R.id.buttonEnterAmount);
		buttonSelectCategory = (Button) findViewById(R.id.buttonSelectCategory);
		buttonSelectPayoutDate = (Button) findViewById(R.id.buttonSelectPayoutDate);
		buttonSelectPayoutType = (Button) findViewById(R.id.buttonSelectPayoutType);
		buttonSelectUser = (Button) findViewById(R.id.buttonSelectUser);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
	}

	private void initListener(){
		buttonSelectAccountBook.setOnClickListener(this);
		buttonEnterAmount.setOnClickListener(this);
		buttonSelectCategory.setOnClickListener(this);
		buttonSelectPayoutDate.setOnClickListener(this);
		buttonSelectPayoutType.setOnClickListener(this);
		buttonSelectUser.setOnClickListener(this);
		buttonSave.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
		actvCategoryName.setOnItemClickListener(new OnAutoCompleteTextViewItemClickListener());
	}
	
	private void bindData() {
		//如果传入了ModelPayout（编辑）,将数据填入界面,如果是新建，new一个ModelPayout
		mModelPayout = (ModelPayout) getIntent().getSerializableExtra("ModelPayout");	
		if (mModelPayout != null) {
			initData(mModelPayout);
		} else {
			mModelPayout = new ModelPayout();
			initData();
		}
		
		actvCategoryName.setAdapter(mBusinessCategory.getAllCategoryArrayAdapter());
		mPayoutTypeArr = getResources().getStringArray(R.array.PayoutType);
		
	}
	
	/**
	 * 编辑时,初始化数据
	 */
	private void initData(ModelPayout pModelPayout){		
		mModelAccountBook = mBusinessAccountBook.getModelAccountBookByAccountBookID(pModelPayout.getAccountBookID());
		mAmount = pModelPayout.getAmount();
		mModelCategory = mBusinessCategory.getModelCategoryByCategoryID(pModelPayout.getCategoryID());
		mPayoutType = pModelPayout.getPayoutType();
		mPayoutUserID = pModelPayout.getPayoutUserID();
		mPayoutDate = pModelPayout.getPayoutDate();
		mSelectedUser = mBusinessUser.getUserByUserID(pModelPayout.getPayoutUserID());
		editTextAccountBookName.setText(pModelPayout.getAccountBookName());
		editTextEnterAmount.setText(pModelPayout.getAmount().toString());
		actvCategoryName.setText(pModelPayout.getCategoryName());		
		editTextPayoutDate.setText(DateUtil.getFormatDateTime(pModelPayout.getPayoutDate(), "yyyy-MM-dd"));
		editTextPayoutType.setText(pModelPayout.getPayoutType());		
		String _UserName = mBusinessUser.getUserNameByUserID(mPayoutUserID);
		editTextPayoutUser.setText(_UserName);		
		editTextComment.setText(pModelPayout.getComment());
	}
	
	/**
	 * 添加时,初始化数据
	 */
	private void initData(){
		//初始帐本为默认帐本
		mModelAccountBook = mBusinessAccountBook.getDefaultAccountBook();
		editTextAccountBookName.setText(mModelAccountBook.getAccountBookName());
		//初始日期为当前日期
		mPayoutDate = new Date();
		editTextPayoutDate.setText(DateUtil.getFormatDate(mPayoutDate));
		//初始消费方式为个人付款
		mPayoutType = getResources().getStringArray(R.array.PayoutType)[1];
		editTextPayoutType.setText(mPayoutType);
		
		mAmount = BigDecimal.valueOf(0);
		mSelectedUser = new ArrayList<ModelUser>();
	}
	
	private void setTitle(){
		String _Title;
		if (mModelPayout.getPayoutID() == 0) {
			_Title = getString(R.string.ActivityTitlePayoutAddOrEdit,new Object[]{getString(R.string.TitleAdd)});
		} else {
			_Title = getString(R.string.ActivityTitlePayoutAddOrEdit,new Object[]{getString(R.string.TitleEdit)});
		}
		setTitleBar(_Title);
	}
	
	@Override
	public void onClick(View v) {
		int _ID = v.getId();
		
		switch (_ID) {
		case R.id.buttonSelectAccountBook:
			showAccountBookSelectDialog();
			break;
		case R.id.buttonEnterAmount:
			(new NumberDialog(this)).show();
			break;
		case R.id.buttonSelectCategory:
			showCategorySelectDialog();
			break;
		case R.id.buttonSelectPayoutDate:
			Calendar _Calendar = Calendar.getInstance();
			_Calendar.setTime(mPayoutDate);
			showDateSelectDialog(_Calendar.get(Calendar.YEAR),_Calendar.get(Calendar.MONTH),_Calendar.get(Calendar.DAY_OF_MONTH));
			break;
		case R.id.buttonSelectPayoutType:
			showPayoutTypeSelectDialog();
			break;
		case R.id.buttonSelectUser:
			showUserSelectDialog();
			break;
		case R.id.buttonSave:
			addOrEditPayout();
			break;
		case R.id.buttonCancel:
			finish();
			break;
		default:
			break;
		}
	}

	/* 
	 * 实现输入数字对话框的接口，可以接受数字对话框的结果
	 */
	@Override
	public void setNumberFinish(BigDecimal pAmount) {
		editTextEnterAmount.setText(pAmount.toString());
		mAmount = pAmount;
	}
	
	/**
	 * 选择帐本对话框
	 */
	private void showAccountBookSelectDialog(){	
		//建立ListView
		View _View = getLayoutInflater().inflate(R.layout.account_book_list, null);
		LinearLayout _LinearLayout = (LinearLayout) _View.findViewById(R.id.layAccountBookList);
		_LinearLayout.setBackgroundResource(R.drawable.bg_title);
		ListView _ListView = (ListView) _View.findViewById(R.id.listViewAccountBookList);
		AdapterAccountBook _AdapterUser = new AdapterAccountBook(this);
		_ListView.setAdapter(_AdapterUser);
		
		//建立AlertDialog
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(R.string.ButtonTextSelectAccountBook)
				.setNegativeButton(getString(R.string.ButtonTextBack), new OnSelectUserBackListener())
				.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		
		_ListView.setOnItemClickListener(new OnAccountBookItemClickListener(_AlertDialog));
	}
	
	/**
	 * 选择帐本对话框，ListItem的Listener
	 */
	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {

		private AlertDialog mAlertDialog;
		
		public OnAccountBookItemClickListener(AlertDialog pAlertDialog) {
			mAlertDialog = pAlertDialog;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mModelAccountBook = (ModelAccountBook) parent.getAdapter().getItem(position);
			editTextAccountBookName.setText(mModelAccountBook.getAccountBookName());
			mAlertDialog.dismiss();
		}	
		
	}
	
	/**
	 * 选择消费类别对话框
	 */
	private void showCategorySelectDialog(){
		//建立ListView
		View _View = getLayoutInflater().inflate(R.layout.category, null);
		ExpandableListView _ExpandableListView = (ExpandableListView) _View.findViewById(R.id.expandableListViewCategory);
		AdapterCategory _AdapterCategory = new AdapterCategory(this);
		_ExpandableListView.setAdapter(_AdapterCategory);
		
		//建立AlertDialog
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(R.string.ButtonTextSelectCategory)
				.setNegativeButton(getString(R.string.ButtonTextBack), new OnSelectUserBackListener())
				.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		
		_ExpandableListView.setOnChildClickListener(new OnCategoryChildItemClickListener(_AlertDialog, _AdapterCategory));
	}
	
	/**
	 * 选择消费类别 Child Listener
	 */
	private class OnCategoryChildItemClickListener implements OnChildClickListener {

		private AlertDialog mAlertDialog;
		private AdapterCategory mAdapterCategory;
		
		public OnCategoryChildItemClickListener(AlertDialog pAlertDialog, AdapterCategory pAdapterCategory) {
			mAlertDialog = pAlertDialog;
			mAdapterCategory = pAdapterCategory;
		}
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			mModelCategory = (ModelCategory) mAdapterCategory.getChild(groupPosition, childPosition);
			actvCategoryName.setText(mModelCategory.getCategoryName());
			mAlertDialog.dismiss();
			return false;
		}
	
	}
	
	/**
	 * 日期选择对话框
	 */
	private void showDateSelectDialog(int pYear, int pMonth, int pDay) {
		(new DatePickerDialog(this, new OnDateSelectedListener(), pYear, pMonth, pDay)).show();
	}
	
	/**
	 * DatePicker中确定按钮Listener
	 */
	private class OnDateSelectedListener implements OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar _Calendar = Calendar.getInstance();
			_Calendar.set(year, monthOfYear, dayOfMonth);
			mPayoutDate = _Calendar.getTime();
			editTextPayoutDate.setText(DateUtil.getFormatDate(mPayoutDate));
		}		
	}
	
	/**
	 * 消费类型对话框
	 */
	private void showPayoutTypeSelectDialog(){
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		View _View = getLayoutInflater().inflate(R.layout.payout_type_select_list, null);
		ListView _ListView = (ListView) _View.findViewById(R.id.listViewPayoutType);
		
		_Builder.setTitle(R.string.ButtonTextSelectPayoutType)
				.setNegativeButton(R.string.ButtonTextBack, null)
				.setView(_View);
		
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
				
		_ListView.setOnItemClickListener(new OnPayoutTypeItemClickListener(_AlertDialog));
	}
	
	/**
	 * 选择消费类型对话框，ListItem的Listener
	 */
	private class OnPayoutTypeItemClickListener implements AdapterView.OnItemClickListener {
		private AlertDialog mAlertDialog;
		
		public OnPayoutTypeItemClickListener(AlertDialog pAlertDialog) {
			mAlertDialog = pAlertDialog;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mPayoutType = (String) parent.getAdapter().getItem(position);
			editTextPayoutType.setText(mPayoutType);
			editTextPayoutUser.setText("");
			mPayoutUserID = "";
			mSelectedUser.clear();
			mAlertDialog.dismiss();
		}	
	}
	
	/**
	 * 选择用户对话框
	 */
	private void showUserSelectDialog(){
		
		//建立ListView
		View _View = getLayoutInflater().inflate(R.layout.user_list, null);
		LinearLayout _LinearLayout = (LinearLayout) _View.findViewById(R.id.layUserList);
		_LinearLayout.setBackgroundResource(R.drawable.bg_title);
		ListView _ListView = (ListView) _View.findViewById(R.id.listViewUserList);
		//重构构造函数的Adapter，将已经选中的User标记
		AdapterUser _AdapterUser = new AdapterUser(this, mSelectedUser);
		_ListView.setAdapter(_AdapterUser);
		
		//建立AlertDialog
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(R.string.ButtonTextSelectUser)
				.setNegativeButton(getString(R.string.ButtonTextBack), new OnSelectUserBackListener())
				.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		
		_ListView.setOnItemClickListener(new OnUserItemClickListener(_AlertDialog));
	}
	
	/**
	 * 选择用户对话框，ListItem的Listener
	 */
	private class OnUserItemClickListener implements AdapterView.OnItemClickListener {

		private AlertDialog mAlertDialog;
		
		public OnUserItemClickListener(AlertDialog pAlertDialog) {
			mAlertDialog = pAlertDialog;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
	
			//Click的User对象
			ModelUser _ModelUser = (ModelUser) parent.getAdapter().getItem(position);
			ImageView _CheckImage = (ImageView) view.findViewById(R.id.imageViewUserSelected);
			
			//均分类：选中或取消
			if (mPayoutType.equals(mPayoutTypeArr[0])) {
				if (AdapterUser.isListCountainModelUser(mSelectedUser, _ModelUser) != -1) {
					_CheckImage.setVisibility(View.GONE);;
					mSelectedUser.remove(AdapterUser.isListCountainModelUser(mSelectedUser, _ModelUser));
				} else {
					_CheckImage.setVisibility(View.VISIBLE);
					mSelectedUser.add(_ModelUser);
				}
			}
			//个人类：选中即退出
			if (mPayoutType.equals(mPayoutTypeArr[1])) {
				mPayoutUserID = _ModelUser.getUserID() + ",";
				editTextPayoutUser.setText(_ModelUser.getUserName()+",");
				mSelectedUser.clear();
				mSelectedUser.add(_ModelUser);
				mAlertDialog.dismiss();
			}
			
		}	
	}

	/**
	 * 选择用户对话框 返回按钮 Listener
	 */
	private class OnSelectUserBackListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			if (mPayoutType.equals(mPayoutTypeArr[1])){
				return;
			}
			
			mPayoutUserID = "";
			for (int i = 0; i < mSelectedUser.size(); i++) {
				mPayoutUserID += mSelectedUser.get(i).getUserID()+",";
			}
			editTextPayoutUser.setText(mBusinessUser.getUserNameByUserID(mPayoutUserID));
	
		}
		
	}
	
	/**
	 * 自动完成表单的Click Listener
	 */
	private class OnAutoCompleteTextViewItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mModelCategory = (ModelCategory) parent.getAdapter().getItem(position);
			actvCategoryName.setText(mModelCategory.getCategoryName());
		}
		
	}
		
	/**
	 * 	保存Button 
	 */
	private void addOrEditPayout() {
		
		//金额格式是否正确
		if (editTextEnterAmount.getText().toString().equals("") 
				|| editTextEnterAmount.getText().toString().equals("0")) {
			showToast(getString(R.string.CheckDataTextMoney));
			buttonEnterAmount.requestFocus();
			return;
		}
		
		//是否未选类别
		if (mModelCategory == null || actvCategoryName.getText().toString().trim().equals("")) {
			showToast(getString(R.string.CheckDataTextCategoryIsNull));
			buttonSelectCategory.requestFocus();
			return;
		}
		//输入的类别名是否有错
		if (mModelCategory.getCategoryName() != actvCategoryName.getText().toString()) {
			mModelCategory = mBusinessCategory.getModelCategoryByCategoryName(actvCategoryName.getText().toString());
			if (mModelCategory == null) {
				showToast(getString(R.string.CheckDataTextCategoryIsWrong));
				buttonSelectCategory.requestFocus();
				return;
			}
		}
		
		//是否选了消费人
		if (mPayoutUserID == null || mPayoutUserID.isEmpty()) {
			showToast(getString(R.string.CheckDataTextPayoutUser2));
			buttonSelectUser.requestFocus();
			return;
		}
		
		//多人均分是否选中多人
		if (mPayoutType.equals(mPayoutTypeArr[0]) && mPayoutUserID.length() == mPayoutUserID.indexOf(',')+1) {
			showToast(getString(R.string.CheckDataTextPayoutUser1));
			buttonSelectUser.requestFocus();
			return;
		}
		
		mModelPayout.setAccountBookID(mModelAccountBook.getAccountBookID());
		mModelPayout.setCategoryID(mModelCategory.getCategoryID());
		mModelPayout.setAmount(mAmount);
		mModelPayout.setPayoutDate(mPayoutDate);
		mModelPayout.setPayoutType(mPayoutType);
		mModelPayout.setPayoutUserID(mPayoutUserID);
		mModelPayout.setComment(editTextComment.getText().toString());
		
		//添加或编辑
		if (mModelPayout.getPayoutID() == 0) {
			boolean _Result =  mBussinessPayout.insertPayout(mModelPayout);
			if (_Result) {
				showToast(getString(R.string.TipsAddSucceed));
				finish();
			} else {
				showToast(getString(R.string.TipsAddFail));
			}
		} else {
			boolean _Result = mBussinessPayout.updatePayoutByPayoutID(mModelPayout);
			if (_Result) {
				showToast(getString(R.string.TipsAddSucceed));
				setResult(RESULT_OK);
				finish();
			} else {
				showToast(getString(R.string.TipsAddFail));
			}
		}

	}
	
	
}
