package philip.godutch.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.business.BusinessCategory;
import philip.godutch.business.BusinessPayout;
import philip.godutch.model.ModelCategory;
import philip.godutch.model.ModelPayout;
import philip.godutch.utility.RegExUtil;

public class ActivityCategoryAddOrEdit extends ActivityFrame implements OnClickListener{

	private Button buttonSava;
	private Button buttonCancel;
	private Spinner spinnerParentID;
	private EditText editTextCategoryName;
	
	private BusinessCategory mBusinessCategory;
	private ModelCategory mModelCategory;
		
	@Override
	public void onClick(View v) {
		int _ID = v.getId();
		
		switch (_ID) {
		case R.id.buttonSave:
			addOrEditCategory();
			break;
		case R.id.buttonCancel:
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.category_add_or_edit);
		removeBottomBox();
		// 初始化
		initVariable();
		initView();
		initListener();
		bindData();
	}
	
	private void initVariable() {
		mBusinessCategory = new BusinessCategory(this);
		mModelCategory = (ModelCategory) getIntent().getSerializableExtra("ModelCategory");
	}

	private void initView() {
		buttonSava = (Button) findViewById(R.id.buttonSave);
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		editTextCategoryName = (EditText) findViewById(R.id.editTextCategoryName);
		spinnerParentID = (Spinner) findViewById(R.id.SpinnerParentID);
	}

	private void initListener() {
		buttonSava.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
	}

	private void bindData() {
		ArrayAdapter<ModelCategory> _ArrayAdapter = mBusinessCategory.getRootCategoryArrayAdapter();
		spinnerParentID.setAdapter(_ArrayAdapter);
		setTitle();
	}
	
	/**
	 * 判断是Add还是Edit，并设置Title，如果是Edit，初始化为相应Spinner和EditText
	 */
	private void setTitle() {
		String _Title;
		if (mModelCategory == null) {
			_Title = getString(R.string.ActivityTitleCategoryAddOrEdit, getString( R.string.TitleAdd));
		} else {
			_Title = getString(R.string.ActivityTitleCategoryAddOrEdit, getString( R.string.TitleEdit));
			initData(mModelCategory);
		}
		
		setTitleBar(_Title);
	}
	
	/**
	 * Edit时，初始化数据到View上
	 */
	private void initData(ModelCategory pModelCategory) {
		editTextCategoryName.setText(pModelCategory.getCategoryName());
		ArrayAdapter _ArrayAdapter = (ArrayAdapter) spinnerParentID.getAdapter();
		
		//如果不是根类别，可以选择其他父类别，如果是根类别，spinner将不可用
		if (pModelCategory.getParentID() != 0) {
			int _Position = 0;
			for (int i = 1; i < _ArrayAdapter.getCount(); i++) {
				ModelCategory _ModelCategory = (ModelCategory) _ArrayAdapter.getItem(i);
				if (_ModelCategory.getCategoryID() == pModelCategory.getParentID()) {
					_Position = _ArrayAdapter.getPosition(_ModelCategory);
				}
			}
			spinnerParentID.setSelection(_Position);
		} else {
			int _Count = mBusinessCategory
					.getCategoryCountByParentID(pModelCategory.getParentID());
			if (_Count != 0) {
				spinnerParentID.setEnabled(false);
			}
		}
	}
	
	/**
	 * 执行添加或者编辑
	 */
	private void addOrEditCategory() {
		
		boolean _Result = false;
		//判断名称格式是否正确
		String _CategoryName = editTextCategoryName.getText().toString().trim();
		boolean _CheckResult = RegExUtil.isChineseEnglishNum(_CategoryName);
		if (!_CheckResult) {
			showToast(getString(R.string.CheckDataTextChineseEnglishNum
					, new Object[]{getString(R.string.TextViewTextCategoryName)}));
			return;
		} 
		
		//判断是添加还是编辑
		if (mModelCategory == null) {
			mModelCategory = new ModelCategory();
		}
		mModelCategory.setCategoryName(_CategoryName);
		
		//判断是否重名 
		boolean _CheckIsExist = mBusinessCategory
				.isExistByCategoryName(_CategoryName, mModelCategory.getCategoryID());
		if (_CheckIsExist ) {
			showToast(getString(R.string.CheckDataTextCategoryExist));
			return;
		}
		
		//判断是否成为根类别，如是，判断其下有无消费，如没有则继续
		if (spinnerParentID.getSelectedItem().toString().equals(getString(R.string.SpinnerTextSelect))) {
			BusinessPayout _BusinessPayout = new BusinessPayout(this);
			List<ModelPayout> _ListPayout = _BusinessPayout.getPayoutByCategoryID(mModelCategory.getCategoryID());
			if (_ListPayout != null && _ListPayout.size()>0) {
				showToast(getString(R.string.CheckDataTextCategoryHasPayout));
				return;
			}
			mModelCategory.setParentID(0);
		} else {
			ModelCategory _ModelCategory = (ModelCategory) spinnerParentID.getSelectedItem();
			if (_ModelCategory != null) {
				mModelCategory.setParentID(_ModelCategory.getCategoryID());
			}
		}
		
		//判断是添加还是编辑，并执行
		if (mModelCategory.getCategoryID() == 0) {
			_Result = mBusinessCategory.insertCategory(mModelCategory);
		} else {
			_Result = mBusinessCategory.updateCategoryByCategoryID(mModelCategory);
		}	
		if (_Result) {
			showToast(getString(R.string.TipsAddSucceed));
			finish();
		} else {
			showToast(getString(R.string.TipsAddFail));
		}
		
	}
	
}
