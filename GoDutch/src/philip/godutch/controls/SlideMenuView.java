package philip.godutch.controls;

import java.util.ArrayList;
import java.util.List;

import philip.godutch.R;
import philip.godutch.adapter.AdapterSlideMenu;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SlideMenuView {
	
	private Activity mActivity;
	private List<SlideMenuItem> mMenuList;
	private boolean mIsClosed;
	private RelativeLayout layBottomBox;
	private OnSlideMenuItemListener mSlideMenuItemListener;
	
	/**
	 * ��ʹ�ô˽ӿڵ�ActivityMainʵ�ֵ���¼�
	 */
	public interface OnSlideMenuItemListener {
		public abstract void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem);
	}
	
	public SlideMenuView(Activity pActivity) {
		mActivity=pActivity;

		initView();
		if (pActivity instanceof OnSlideMenuItemListener) {
			mSlideMenuItemListener=(OnSlideMenuItemListener) pActivity;
			initVariable();
			initListener();
		}
		
	}
	
	private void initVariable(){
		mMenuList = new ArrayList<SlideMenuItem>();
		mIsClosed = true;
	}
	
	private void initView(){
		layBottomBox = (RelativeLayout)mActivity.findViewById(R.id.includeBottom);
	}
	
	private void initListener(){
		
		layBottomBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		
		//�����˻�ȡ���� �����¼��Żᴫ��ؼ�
		layBottomBox.setFocusableInTouchMode(true);
		layBottomBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
					toggle();
				}
				return false;
			}
		});
	
	}
	
	private void open() {
		RelativeLayout.LayoutParams _LayoutParams=new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		_LayoutParams.addRule(RelativeLayout.BELOW,R.id.includeTitle);
		_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,0);
		
		layBottomBox.setLayoutParams(_LayoutParams);
		mIsClosed=false;
	}

	private void close() {
		RelativeLayout.LayoutParams _LayoutParams=new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.MATCH_PARENT, dp2px(mActivity, 68));
		_LayoutParams.addRule(RelativeLayout.BELOW,0);
		_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		layBottomBox.setLayoutParams(_LayoutParams);
		mIsClosed=true;
	}

	/**
	 * ����
	 */
	public void toggle() {
		if (mIsClosed) {
			open();
		}else {
			close();
		}
	}
	
	public boolean isClosed() {
		return mIsClosed;
	}

	public void add(SlideMenuItem pSliderMenuItem) {
		mMenuList.add(pSliderMenuItem);
	}

	public void bindList() {
		AdapterSlideMenu _AdapterSlideMenu = new AdapterSlideMenu(mActivity, mMenuList);
	
		ListView _ListView = (ListView) mActivity.findViewById(R.id.listViewSlideList);
		_ListView.setAdapter(_AdapterSlideMenu);
		_ListView.setOnItemClickListener( new OnSlideItemClick() );
	}
	
	public void removeBottomBox() {
		RelativeLayout _MainLayout = (RelativeLayout) mActivity.findViewById(R.id.layMain);
		_MainLayout.removeView(layBottomBox);
		layBottomBox = null;
	}
	
	/**
	 * ���SlideItem�¼�
	 */
	private class OnSlideItemClick implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SlideMenuItem _SlideMenuItem = (SlideMenuItem) parent.getItemAtPosition(position);
			mSlideMenuItemListener.onSlideMenuItemClick(view, _SlideMenuItem);
		}
	}
	
	/**
	 * ��dpֵת�ɵ�ǰ��Ļ��pxֵ
	 */
	public int dp2px(Context context, float dpValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)((dpValue-0.5f) * scale );
	}
}
