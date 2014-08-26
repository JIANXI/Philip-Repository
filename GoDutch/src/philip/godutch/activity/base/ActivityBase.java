package philip.godutch.activity.base;

import java.lang.reflect.Field;

import philip.godutch.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

public class ActivityBase extends Activity {

	ProgressDialog mProgressDialog;
	
	/**
	 * ��Toast����
	 */
	protected void showToast(String pText) {
		Toast.makeText(this, pText, Toast.LENGTH_LONG).show();
	}
	protected void showToast(int pResID) {
		String _Msg = getString(pResID);
		showToast(_Msg);
	}
	protected void showToast(int pResID,Object[] pObjects) {
		String _Msg = getString(pResID, pObjects);
		showToast(_Msg);
	}
	
	/**
	 * ����ͨ��startActivity����
	 */
	protected void startActivity(Class<?> pClass) {
		Intent _Intent=new Intent();
		_Intent.setClass(this, pClass);
		startActivity(_Intent);
	}
	
	/**
	 * ���õ��Dialog�İ�ť���Ƿ�ر�Dialog
	 */
	protected void setAlertDialogIsClose(DialogInterface pDialogInterface, boolean pIsClose) {
		try {
			Field _Field = pDialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
			_Field.setAccessible(true);
			_Field.set(pDialogInterface, pIsClose);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������ʾһ����AlertDialog(2����ť)
	 */
	protected AlertDialog showAlertDialoge(String pTitle,int pIconRes, String pMessage, DialogInterface.OnClickListener pOnClickListener) {
		return new AlertDialog.Builder(this)
		.setTitle(pTitle)
		.setIcon(pIconRes)
		.setMessage(pMessage)
		.setPositiveButton(R.string.ButtonTextYes, pOnClickListener)
		.setNegativeButton(R.string.ButtonTextNo, null)
		.show();
	}
	
	/**
	 * ��ʾ���ȿ�
	 */
	protected void showProgressDialog(int pTitleResID, int pMessageResID) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(pTitleResID);
		mProgressDialog.setMessage(getString(pMessageResID));
		mProgressDialog.show();
	}
	
	/**
	 * �رս��ȿ�
	 */
	protected void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
	/**
	 * ��dpֵת����pxֵ�Ĳ���
	 */
	public int dp2px(float dpValue){
		final float scale = getResources().getDisplayMetrics().density;
		return (int)((dpValue-0.5f) * scale );
	}
	
}
