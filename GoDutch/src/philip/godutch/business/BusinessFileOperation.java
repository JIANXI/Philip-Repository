package philip.godutch.business;

import java.io.File;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import philip.godutch.activity.ActivityMain;
import philip.godutch.business.base.BusinessBase;
import philip.godutch.model.ModelPayout;
import philip.godutch.utility.DateUtil;
import philip.godutch.utility.FileUtil;

public class BusinessFileOperation extends BusinessBase{

	private Context mContext;
	public String SOURCE_PATH = getContext().getDatabasePath("GoDutchDatabase").getAbsolutePath();
	public static String DESTINATION_PATH =  Environment.getExternalStorageDirectory().getPath()+"/GoDutch/DatabaseBak";
	public static String SHARED_PREFERENCES_NAME = "GoDutchSharedPreference";
	public static String EXPORT_PATH = Environment.getExternalStorageDirectory().getPath()+"/GoDutch/Export";
	
	public BusinessFileOperation(Context pContext) {
		super(pContext); 
		mContext = pContext;
	}
	
	/**
	 * �Ƿ��������Զ�����
	 */
	public boolean isDatabaseBackupTiming() {
		SharedPreferences _SharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		if (_SharedPreferences != null) {
			return _SharedPreferences.getBoolean(ActivityMain.PREFERENCE_BACKUP_TIMING, false);
		} else {
			return false;
		}
	}
	
	/**
	 * �������ݲ���
	 */
	public boolean databaseBackup() {
		
		try {
			File _SourceFile = new File(SOURCE_PATH);
			
			if (_SourceFile.exists()) {
				File _FileDir = new File(DESTINATION_PATH);
				if (!_FileDir.exists()) {
					_FileDir.mkdirs();
				}
				
				FileUtil.copyAndReplaceFile(SOURCE_PATH, DESTINATION_PATH+"/GoDutchDatabase.bak");
			}		
			saveDatabaseBackupDate(new Date().getTime());
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean deleteDatabase() {
		
		try {
			
			FileUtil.deleteFile(SOURCE_PATH);
			SQLiteHelper.getInstance(mContext).resetHelper();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * ��ԭ���ݲ���
	 */
	public boolean databaseRestore() {
		try {
			long _DatabaseBackupDate = getDatabaseBackupDate();
			
			if (_DatabaseBackupDate != 0) {
				FileUtil.copyAndReplaceFile(DESTINATION_PATH+"/GoDutchDatabase.bak", SOURCE_PATH);
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * SharedPreference �����ϴα���ʱ��
	 */
	public void saveDatabaseBackupDate(long _Millise) {
		SharedPreferences _SharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor _Editor = _SharedPreferences.edit();
		_Editor.putLong(SHARED_PREFERENCES_NAME, _Millise);
		_Editor.commit();
	}
	
	/**
	 * �õ��ϴα��ݵ�ʱ��
	 */
	public long getDatabaseBackupDate() {
		long _DatabaseBackupDate = 0;
		SharedPreferences _SharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		if (_SharedPreferences != null) {
			_DatabaseBackupDate = _SharedPreferences.getLong(SHARED_PREFERENCES_NAME, 0);
		}
		return _DatabaseBackupDate;
	}
	
	/**
	 * �����������ݵ�Excel�ļ�����������ļ�·��
	 */
	public String exportPayoutData(int pAccountBookID) {
		BusinessPayout _BusinessPayout = new BusinessPayout(mContext);
		BusinessUser _BusinessUser = new BusinessUser(mContext);
		List<ModelPayout> _PayoutList = _BusinessPayout.getPayoutByAccountBookID(pAccountBookID);
		Date _Date = new Date();
		//�����������ļ��� ��ʱ��id����
		String _FileName = "Payout_"+_PayoutList.get(0).getAccountBookID()+"_"+DateUtil.getFormatDateTime(_Date, "yyMMdd")+".xls";
		try {
			File _FileDir = new File(EXPORT_PATH);
			if (!_FileDir.exists()) {
				_FileDir.mkdirs();
			}
	        // ���ļ�
	        WritableWorkbook book = Workbook.createWorkbook(new File(EXPORT_PATH+"/"+_FileName));
	        // ���ɹ���������0��ʾ���ǵ�һҳ
            WritableSheet sheet = book.createSheet(_PayoutList.get(0).getAccountBookName(), 0);
            //�½�������
            Label _Label;
    		String[] _Titles = {"���","������","��������","��������","���ѽ�Ԫ��"};
            for (int i = 0; i < _Titles.length; i++) {
				_Label = new Label(i, 0, _Titles[i]);
				sheet.addCell(_Label);
			}
			//�����
            ModelPayout _ModelPayout;
            for (int i = 0; i < _PayoutList.size(); i++) {
				_ModelPayout = _PayoutList.get(i);
				//���
				jxl.write.Number _CellID = new Number(0, i+1, i+1);
				sheet.addCell(_CellID);
				//������
            	_Label = new Label(1, i+1, _BusinessUser.getUserNameByUserID(_ModelPayout.getPayoutUserID()));
            	sheet.addCell(_Label);
            	//��������
            	_Label = new Label(2, i+1, _ModelPayout.getCategoryName());
            	sheet.addCell(_Label);
            	//��������
            	_Label = new Label(3, i+1, DateUtil.getFormatDate(_ModelPayout.getPayoutDate()));
            	sheet.addCell(_Label);
            	//���ѽ��
            	_Label = new Label(4, i+1, _ModelPayout.getAmount().toString());
            	sheet.addCell(_Label);
			}
            //�ܼ���
        	_Label = new Label(0,  _PayoutList.size()+1, "�ܼƣ�");
        	sheet.addCell(_Label);
        	_Label = new Label(4,  _PayoutList.size()+1, _BusinessPayout.getPayoutTotalByAccountBookID(pAccountBookID)[1]);
        	sheet.addCell(_Label);
        	
        	book.write();
        	book.close();
        	return EXPORT_PATH+"/"+_FileName;
            
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
