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
 * ����࣬��ź�ҵ���йصĸ�����ǿ�ķ���
 */
public class ActivityFrame extends ActivityBase{

	private SlideMenuView mSlideMenuView;
	
	/* 
	 * ����ȫ�������壨��Ĭ�ϱ�������
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	}
	
	/* 
	 * SlideMenu���ڴ�״̬ʱ�����ؼ����ر�SlideMenu
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
	 * ���Main�����岿�֣�GridView��
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
	 * ʵ����һ��SlideMenuView���󣬽�����������ֵ��ӵ����в��󶨵�ListView
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
	 * ����SlideMenu����
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
	 * ����ContextMenu�Ĳ˵�ѡ��(ֻ�� 1�༭ 2ɾ��)
	 */
	protected void createMenu(Menu pMenu) {		
		pMenu.add(0, 1, 0, R.string.MenuTextEdit);
		pMenu.add(0, 2, 0, R.string.MenuTextDelete);
	}
	
	/**
	 * ���ñ������ͷ���ͼƬ
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











