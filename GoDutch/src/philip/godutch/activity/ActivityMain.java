package philip.godutch.activity;

import java.util.Date;

import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.adapter.AdapterAppGrid;
import philip.godutch.business.BusinessFileOperation;
import philip.godutch.controls.SlideMenuItem;
import philip.godutch.controls.SlideMenuView.OnSlideMenuItemListener;
import philip.godutch.service.ServiceDatabaseBackup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;

public class ActivityMain extends ActivityFrame implements OnSlideMenuItemListener,OnClickListener,OnCheckedChangeListener {

	public static String PREFERENCE_BACKUP_TIMING = "BackupTiming";
	
	private BusinessFileOperation mBusinessFileOperation;
	private AdapterAppGrid mAdapterAppGrid;
	private GridView gridViewAppGrid;
	private AlertDialog mAlertDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加除了主框体外的组件（GridView和SlideMenu）
		appendMainBody(R.layout.main_gridview);
		createSlideMenu(R.array.SlideMenuActivityMain);
		
		//初始化
		initVariable();
		initView();
		initListener();
		bindData();
	}
	
	private void initVariable() {
		mAdapterAppGrid=new AdapterAppGrid(this);
		mBusinessFileOperation = new BusinessFileOperation(this);
	}
	
	private void initView() {
		gridViewAppGrid=(GridView) findViewById(R.id.gridViewAppGrid);
	}
	
	private void initListener() {
		gridViewAppGrid.setOnItemClickListener(new onAppGridItemClick());
	}
	
	private void bindData() {
		gridViewAppGrid.setAdapter(mAdapterAppGrid);
	}
	
	private void startService(){
		Intent _Intent = new Intent(this, ServiceDatabaseBackup.class);
		startService(_Intent);
	}
	private void stopService(){
		Intent _Intent = new Intent(this, ServiceDatabaseBackup.class);
		stopService(_Intent);
	}
	
	/**
	 * 自定义GridView的Listener
	 */
	private class onAppGridItemClick implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String _MenuName = (String) parent.getAdapter().getItem(position);
			if (_MenuName.equals(getString(R.string.appGridTextUserManage))) {
				startActivity(ActivityUser.class);
			} else if (_MenuName.equals(getString(R.string.appGridTextAccountBookManage))) {
				startActivity(ActivityAccountBook.class);
			} else if (_MenuName.equals(getString(R.string.appGridTextCategoryManage))) {
				startActivity(ActivityCategory.class);
			} else if (_MenuName.equals(getString(R.string.appGridTextPayoutAdd))) {
				startActivity(ActivityPayoutAddOrEdit.class);
			} else if (_MenuName.equals(getString(R.string.appGridTextPayoutManage))) {
				startActivity(ActivityPayout.class);
			} else if (_MenuName.equals(getString(R.string.appGridTextStatisticsManage))) {
				startActivity(ActivityStatistics.class);
			}
		}	
	}
	
	/* 
	 * 利用回调函数（接口）设置SlideMenuItem监听事件
	*/
	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		slideMenuToggle();
		switch (pSlideMenuItem.getItemID()) {
		case 0:
			showDatabaseBackupDialog();
			break;

		default:
			showToast("还没有该功能");
			break;
		}
	}

	/**
	 * 数据备份对话框
	 */
	private void showDatabaseBackupDialog() {
		View _View = getLayoutInflater().inflate(R.layout.database_backup, null);
		Button buttonDatabaseBackup = (Button) _View.findViewById(R.id.buttonDatabaseBackup);
		Button buttonDatabaseRestore = (Button) _View.findViewById(R.id.buttonDatabaseRestore);
		CheckBox checkBoxDatabaseBackup = (CheckBox) _View.findViewById(R.id.checkBoxDatabaseBackup);
		
		//自动备份是否启动
		checkBoxDatabaseBackup.setChecked(mBusinessFileOperation.isDatabaseBackupTiming());
		
		buttonDatabaseBackup.setOnClickListener(this);
		buttonDatabaseRestore.setOnClickListener(this);
		checkBoxDatabaseBackup.setOnCheckedChangeListener(this);
		
		String _Title = getString(R.string.DialogTitleDatabaseBackup);
		
		mAlertDialog = new Builder(this)
							.setTitle(_Title)
							.setView(_View)
							.setIcon(R.drawable.ic_database_backup_small)
							.setNegativeButton(getString(R.string.ButtonTextBack), null)
							.create();
		mAlertDialog.show();	
	}
	
	/**
	 * 数据备份操作
	 */
	private void databaseBackup(){
		if (mBusinessFileOperation.databaseBackup()) {
			showToast(R.string.TipsBackupSucceed);
		} else {
			showToast(R.string.TipsBackupFail);
		}
		mAlertDialog.dismiss();
	}
	
	/**
	 * 备份还原操作
	 */
	private void databaseRestore(){
		if (mBusinessFileOperation.databaseRestore()) {
			showToast(R.string.TipsRestoreSucceed);
		} else {
			showToast(R.string.TipsRestoreFail);
		}
		mAlertDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonDatabaseBackup:
			databaseBackup();
			break;
		case R.id.buttonDatabaseRestore:
			databaseRestore();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.checkBoxDatabaseBackup:
			SharedPreferences _SharedPreferences = getSharedPreferences(BusinessFileOperation.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
			Editor _Editor = _SharedPreferences.edit();
			_Editor.putBoolean(PREFERENCE_BACKUP_TIMING, isChecked);
			_Editor.commit();
			if (isChecked) {
				startService();
			} else {
				stopService();
			}
			break;

		default:
			break;
		}
		
	}
	
}
