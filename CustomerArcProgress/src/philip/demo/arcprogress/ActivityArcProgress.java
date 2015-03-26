package philip.demo.arcprogress;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ActivityArcProgress extends Activity implements OnClickListener, OnEditorActionListener{
	
    private PhilipArcProgress arcProgress;
    private EditText etProgress;
    private Button btnRun;
    private Button btnMinus;
    private Button btnPlus;
    private Button btnEmpty;
    private Button btnFull;
    
    private Timer mTimer; 
    private boolean mRunning = false;
	private int mTargetProgress = 0;
	private int gap = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main); 
        
        initView();
        initListener();
        initData();
       
    }

	private void initView() {
    	arcProgress = (PhilipArcProgress) findViewById(R.id.arc_progress);
        etProgress = (EditText) findViewById(R.id.editText);
        btnRun = (Button) findViewById(R.id.btn_progress_run);
        btnMinus = (Button) findViewById(R.id.btn_progress_m20);
        btnPlus = (Button) findViewById(R.id.btn_progress_p20);
        btnEmpty = (Button) findViewById(R.id.btn_progress_to0);
        btnFull = (Button) findViewById(R.id.btn_progress_to100);
        
        arcProgress.setArcAngle(360 * 0.72f);
        arcProgress.setStrokeWidthMain(30); 
        arcProgress.setStrokeWidthBg(80);
        arcProgress.setStrokeColorMain(Color.parseColor("#ffa726"));
        arcProgress.setStrokeColorBg(Color.parseColor("#e7e7e7"));
	}
    
	private void initListener() {
		etProgress.setOnEditorActionListener(this);
		btnRun.setOnClickListener(this);
		btnMinus.setOnClickListener(this);
		btnPlus.setOnClickListener(this);
		btnEmpty.setOnClickListener(this);
		btnFull.setOnClickListener(this);
	}
	
	private void initData(){
        arcProgress.setProgress(mTargetProgress);
        etProgress.setText(String.valueOf(mTargetProgress));
	}

	private class TaskRunProgress extends TimerTask{

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	gap = Math.abs(arcProgress.getProgress() - mTargetProgress)/10 + 1;
						
                	if (arcProgress.getProgress() > mTargetProgress) {
                		arcProgress.setProgress(arcProgress.getProgress() - gap);
					} else if (arcProgress.getProgress() < mTargetProgress) {
                		arcProgress.setProgress(arcProgress.getProgress() + gap);
					} else {
						mRunning = false;
						mTimer.cancel();
					}
                }
            });
			
		}
    	
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_progress_run:
			if (etProgress.getText().toString().trim().equals("")) {
				etProgress.setText("0");
			}
			mTargetProgress = Integer.parseInt(etProgress.getText().toString());
			break;
		case R.id.btn_progress_m20:
			mTargetProgress -= 20;
			break;
		case R.id.btn_progress_p20:
			mTargetProgress += 20;
			break;
		case R.id.btn_progress_to0:
			mTargetProgress = 0;
			break;
		case R.id.btn_progress_to100:
			mTargetProgress = 100;
			break;
		default:
			break;
		}
		startRunProgress();
	}
	
	@Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			
            if (etProgress.getText().toString().trim().equals("")) {
				etProgress.setText("0");
			}
			mTargetProgress = Integer.parseInt(etProgress.getText().toString());
			startRunProgress();
            return true;  
        }
        return false;
    }
	
	private void startRunProgress(){
		if (mTargetProgress < 0) {
			mTargetProgress = 0;
		}
		if (mTargetProgress > 100) {
			mTargetProgress = 100;
		}
		etProgress.setText(String.valueOf(mTargetProgress));
		etProgress.setSelection(etProgress.getText().toString().length());
		if (!mRunning) {
			mTimer = new Timer();
			mTimer.schedule(new TaskRunProgress(), 0, 30);
			mRunning = true;
		}
	}

    @Override
    protected void onDestroy() {
    	if (mRunning) {
    		mTimer.cancel();
		}
        super.onDestroy();
    }
}
