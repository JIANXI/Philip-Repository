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
		// ��ʼ��
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
	 * �ж���Add����Edit��������Title�������Edit����ʼ��Ϊ��ӦSpinner��EditText
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
	 * Editʱ����ʼ�����ݵ�View��
	 */
	private void initData(ModelCategory pModelCategory) {
		editTextCategoryName.setText(pModelCategory.getCategoryName());
		ArrayAdapter _ArrayAdapter = (ArrayAdapter) spinnerParentID.getAdapter();
		
		//������Ǹ���𣬿���ѡ���������������Ǹ����spinner��������
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
	 * ִ����ӻ��߱༭
	 */
	private void addOrEditCategory() {
		
		boolean _Result = false;
		//�ж����Ƹ�ʽ�Ƿ���ȷ
		String _CategoryName = editTextCategoryName.getText().toString().trim();
		boolean _CheckResult = RegExUtil.isChineseEnglishNum(_CategoryName);
		if (!_CheckResult) {
			showToast(getString(R.string.CheckDataTextChineseEnglishNum
					, new Object[]{getString(R.string.TextViewTextCategoryName)}));
			return;
		} 
		
		//�ж�����ӻ��Ǳ༭
		if (mModelCategory == null) {
			mModelCategory = new ModelCategory();
		}
		mModelCategory.setCategoryName(_CategoryName);
		
		//�ж��Ƿ����� 
		boolean _CheckIsExist = mBusinessCategory
				.isExistByCategoryName(_CategoryName, mModelCategory.getCategoryID());
		if (_CheckIsExist ) {
			showToast(getString(R.string.CheckDataTextCategoryExist));
			return;
		}
		
		//�ж��Ƿ��Ϊ��������ǣ��ж������������ѣ���û�������
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
		
		//�ж�����ӻ��Ǳ༭����ִ��
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
