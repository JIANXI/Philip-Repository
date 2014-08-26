package philip.godutch.activity.base;

import philip.godutch.R;
import philip.godutch.R.id;
import philip.godutch.controls.SlideMenuItem;
import philip.godutch.controls.SlideMenuView;
import android.R.integer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 框架类，存放和业务有关的复用性强的方法
 */
public class ActivityFrame extends ActivityBase{

	private SlideMenuView mSlideMenuView;
	
	/* 
	 * 构建全局主窗体（无默认标题栏）
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	}
	
	/* 
	 * SlideMenu处于打开状态时，返回键将关闭SlideMenu
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mSlideMenuView != null && !mSlideMenuView.isClosed()) {
				slideMenuToggle();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * 添加Main的主体部分（GridView）
	 */
	protected void appendMainBody(int pResID) {
		View _View=LayoutInflater.from(this).inflate(pResID, null);
		appendMainBody(_View);
	}
	protected void appendMainBody(View pView) {
		LinearLayout _MainBody = (LinearLayout) findViewById(R.id.layMainBody);
		LayoutParams _LayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		_MainBody.addView(pView, _LayoutParams);
	}
	
	/**
	 * 实例化一个SlideMenuView对象，将传入的数组的值添加到其中并绑定到ListView
	 */
	protected void createSlideMenu(int pResID) {
		mSlideMenuView = new SlideMenuView(this);
		String[] _MenuItemArray = getResources().getStringArray(pResID);
		
		for (int i = 0; i < _MenuItemArray.length; i++) {
			SlideMenuItem _Item = new SlideMenuItem(i , _MenuItemArray[i]) ;
			
			mSlideMenuView.add(_Item);
		}
		
		mSlideMenuView.bindList();
	}
	
	/**
	 * 控制SlideMenu开关
	 */
	protected void slideMenuToggle() {
		mSlideMenuView.toggle();
	}
	
	protected void removeBottomBox() {
		mSlideMenuView = new SlideMenuView(this);
		mSlideMenuView.removeBottomBox();
		mSlideMenuView = null;
	}
	
	/**
	 * 创建ContextMenu的菜单选项(只有 1编辑 2删除)
	 */
	protected void createMenu(Menu pMenu) {		
		pMenu.add(0, 1, 0, R.string.MenuTextEdit);
		pMenu.add(0, 2, 0, R.string.MenuTextDelete);
	}
	
	/**
	 * 设置标题栏和返回图片
	 */
	protected void setTitleBar(String pTitle) {
		TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
		ImageView imageViewTitle = (ImageView) findViewById(R.id.imageViewBack);
		
		textViewTitle.setText(pTitle);
		imageViewTitle.setVisibility(View.VISIBLE);;
		imageViewTitle.setOnClickListener(new OnBackImageClickListener());
	}	
	private class OnBackImageClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			finish();
		}
	}
	
}











