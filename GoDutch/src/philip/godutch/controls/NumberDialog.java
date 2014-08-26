package philip.godutch.controls;

import java.math.BigDecimal;

import philip.godutch.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class NumberDialog extends Dialog implements android.view.View.OnClickListener {

	private Context mContext;
	EditText mEditText;

	public interface OnNumberDialogListener{
		public abstract void setNumberFinish(BigDecimal pNumber);
	}
	
	public NumberDialog(Context context) {
		super(context);
		mContext = context;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.payout_number_dialog);
		
		findViewById(R.id.buttonDot).setOnClickListener(this);
		findViewById(R.id.buttonOne).setOnClickListener(this);
		findViewById(R.id.buttonTwo).setOnClickListener(this);
		findViewById(R.id.buttonThree).setOnClickListener(this);
		findViewById(R.id.buttonFour).setOnClickListener(this);
		findViewById(R.id.buttonFive).setOnClickListener(this);
		findViewById(R.id.buttonSix).setOnClickListener(this);
		findViewById(R.id.buttonSeven).setOnClickListener(this);
		findViewById(R.id.buttonEight).setOnClickListener(this);
		findViewById(R.id.buttonNine).setOnClickListener(this);
		findViewById(R.id.buttonZero).setOnClickListener(this);
		findViewById(R.id.buttonBack).setOnClickListener(this);
		findViewById(R.id.buttonEnter).setOnClickListener(this);
	
		mEditText = (EditText) findViewById(R.id.editTextDisplay);
		mEditText.setText("0");
	}

	@Override
	public void onClick(View v) {
		
		String _Number = mEditText.getText().toString();
		
		int _ID = v.getId();
		switch (_ID) {
		case R.id.buttonDot:
			if (_Number.indexOf(".") == -1) {
				_Number += ".";
			}
			break;
		case R.id.buttonOne:
			if (_Number.equals("0")) {
				_Number = "1";
			} else {
				_Number += "1";
			}
			break;
		case R.id.buttonTwo:
			if (_Number.equals("0")) {
				_Number = "2";
			} else {
				_Number += "2";
			}
			break;
		case R.id.buttonThree:
			if (_Number.equals("0")) {
				_Number = "3";
			} else {
				_Number += "3";
			}
			break;
		case R.id.buttonFour:
			if (_Number.equals("0")) {
				_Number = "4";
			} else {
				_Number += "4";
			}
			break;
		case R.id.buttonFive:
			if (_Number.equals("0")) {
				_Number = "5";
			} else {
				_Number += "5";
			}
			break;
		case R.id.buttonSix:
			if (_Number.equals("0")) {
				_Number = "6";
			} else {
				_Number += "6";
			}
			break;
		case R.id.buttonSeven:
			if (_Number.equals("0")) {
				_Number = "7";
			} else {
				_Number += "7";
			}
			break;
		case R.id.buttonEight:
			if (_Number.equals("0")) {
				_Number = "8";
			} else {
				_Number += "8";
			}
			break;
		case R.id.buttonNine:
			if (_Number.equals("0")) {
				_Number = "9";
			} else {
				_Number += "9";
			}
			break;
		case R.id.buttonZero:
			if (_Number.equals("0")) {
				_Number = "0";
			} else {
				_Number += "0";
			}
			break;
		case R.id.buttonBack:
			if (_Number.length() == 1) {
				_Number = "0";
			} else {
				_Number = _Number.substring(0,_Number.length()-1);
			}
			break;
		case R.id.buttonEnter:
			
			BigDecimal _BigDecimal = new BigDecimal(_Number);	
			
			//四舍五入到两位小数
			_BigDecimal = _BigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN);		
			_Number = _BigDecimal.toString();	
			//小数点后多余的0去掉
			while (_Number.contains(".") && _Number.endsWith("0")) {
				_Number = _Number.substring(0, _Number.length()-1);
			}
			//整数后多余的小数点去掉
			if (_Number.endsWith(".")) {
				_Number = _Number.substring(0, _Number.length()-1);
			}
			
			_BigDecimal = new BigDecimal(_Number);
			((OnNumberDialogListener)mContext).setNumberFinish(_BigDecimal);
			dismiss();		
			break;
		default:
			break;
		}
		
		//长度不能大于10
		if (_Number.length() > 10) {
			_Number = _Number.substring(0, 10);
		}
		
		mEditText.setText(_Number);
	}

}
