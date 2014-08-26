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
	 * 简化Toast操作
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
	 * 简化普通的startActivity操作
	 */
	protected void startActivity(Class<?> pClass) {
		Intent _Intent=new Intent();
		_Intent.setClass(this, pClass);
		startActivity(_Intent);
	}
	
	/**
	 * 设置点击Dialog的按钮后，是否关闭Dialog
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
	 * 创建并显示一个简单AlertDialog(2个按钮)
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
	 * 显示进度框
	 */
	protected void showProgressDialog(int pTitleResID, int pMessageResID) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(pTitleResID);
		mProgressDialog.setMessage(getString(pMessageResID));
		mProgressDialog.show();
	}
	
	/**
	 * 关闭进度框
	 */
	protected void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
	/**
	 * 将dp值转化成px值的操作
	 */
	public int dp2px(float dpValue){
		final float scale = getResources().getDisplayMetrics().density;
		return (int)((dpValue-0.5f) * scale );
	}
	
}
