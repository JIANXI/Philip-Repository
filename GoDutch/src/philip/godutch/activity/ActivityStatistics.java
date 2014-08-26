package philip.godutch.activity;

import java.math.BigDecimal;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.tools.AbstractTool;

import philip.godutch.R;
import philip.godutch.activity.base.ActivityFrame;
import philip.godutch.adapter.AdapterAccountBook;
import philip.godutch.business.BusinessAccountBook;
import philip.godutch.business.BusinessFileOperation;
import philip.godutch.business.BusinessStatistics;
import philip.godutch.controls.SlideMenuItem;
import philip.godutch.controls.SlideMenuView.OnSlideMenuItemListener;
import philip.godutch.model.ModelAccountBook;
import philip.godutch.model.ModelCategoryTotal;
import android.app.AlertDialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityStatistics extends ActivityFrame implements OnSlideMenuItemListener {

	private BusinessStatistics mBusinessStatistics;
	private BusinessAccountBook mBusinessAccountBook;
	
	private ModelAccountBook mModelAccountBook;
	private List<ModelCategoryTotal> mModelCategoryTotals;
	private List<String> mUserName;
	private List<BigDecimal> mAmount;
	
	private TabHost mTabHost;
	private LinearLayout tab1;
	private LinearLayout tab2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.statistic_tabhost);
		createSlideMenu(R.array.SlideMenuPayout);
		
		initVariable();
		initView();
		initListener();
		bindData();
		
	}

	private void initVariable() {
		mBusinessAccountBook = new BusinessAccountBook(this);
		mBusinessStatistics = new BusinessStatistics(this);
		mModelAccountBook = mBusinessAccountBook.getDefaultAccountBook();
	}

	private void initView() {
		//��ʼ��TabHost
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(); 
        getLayoutInflater().inflate(R.layout.statistic_tabs, mTabHost.getTabContentView());
        //��layout���η���tab
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("���ͳ��").setContent(R.id.tab1));   
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("�û�ͳ��").setContent(R.id.tab2)); 
        //�õ���Ҫ����tab��layout
        tab1 = (LinearLayout) findViewById(R.id.tab1);
        tab2 = (LinearLayout) findViewById(R.id.tab2);
	}

	private void initListener() {
		
	}

	@SuppressWarnings("unchecked")
	private void bindData() {
		mModelCategoryTotals = mBusinessStatistics.getRootCategoryTotalByAccountBookID(mModelAccountBook);
		mUserName = (List<String>) mBusinessStatistics.getUserPayoutStatisticByAccountBookID(mModelAccountBook).get(0);
		mAmount = (List<BigDecimal>) mBusinessStatistics.getUserPayoutStatisticByAccountBookID(mModelAccountBook).get(1);
		
		setTitleBar(getString(R.string.ActivityTitleStastics, new Object[]{mModelAccountBook.getAccountBookName()}));
		//����Ҫ��ʾ��view����tab
		View _ViewForTab1 =getCategoryStatisticsView();
		View _ViewForTab2 =getUserStatisticsView();
		tab1.removeAllViewsInLayout();
		tab2.removeAllViewsInLayout();
		tab1.addView(_ViewForTab1);
		tab2.addView(_ViewForTab2);
	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		switch (pSlideMenuItem.getItemID()) {
		// ѡ���ʱ�
		case 0:
			showAccountBookSelectDialog();
			break;
		//����Excel
		case 1:
			BusinessFileOperation _BusinessFileOperation = new BusinessFileOperation(
					this);
			String _ExportFilePath = _BusinessFileOperation.exportPayoutData(mModelAccountBook.getAccountBookID());
			if (_ExportFilePath != null) {
				showToast(R.string.TipsExportSucceed,
						new Object[] { _ExportFilePath });
			} else {
				showToast(R.string.TipsExportFail);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * ѡ���ʱ��Ի���
	 */ 
	private void showAccountBookSelectDialog(){
		
		View _View = getLayoutInflater().inflate(R.layout.account_book_list, null);
		ListView _ListViewAccountBook = (ListView) _View.findViewById(R.id.listViewAccountBookList);
		AdapterAccountBook _AdapterAccountBook = new AdapterAccountBook(this);
		_ListViewAccountBook.setAdapter(_AdapterAccountBook);
		
		String _Title = getString(R.string.DialogTitleAccountBook,new Object[]{getString(R.string.TitleEdit)});
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		AlertDialog _AlertDialog = _Builder.setTitle(_Title)
									.setIcon(R.drawable.ic_account_book_small)
									.setView(_View)
									.setNegativeButton(getString(R.string.ButtontextCancel), null)
									.show();
		
		_ListViewAccountBook.setOnItemClickListener(new OnAccountBookItemClickListener(_AlertDialog));
	}
	
	/**
	 * ѡ���ʱ�ItemListener
	 */
	private class OnAccountBookItemClickListener implements OnItemClickListener{

		private AlertDialog mAlertDialog;
		
		public OnAccountBookItemClickListener(AlertDialog pAlertDialog) {
			mAlertDialog = pAlertDialog;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AdapterAccountBook _AdapterAccountBook = (AdapterAccountBook) parent.getAdapter();
			mModelAccountBook = (ModelAccountBook) _AdapterAccountBook.getItem(position);
			bindData();
			slideMenuToggle();
			mAlertDialog.dismiss();
		}
		
	}

	/*************����Ϊ2��ͼ����ͼ������  1���ͳ��(��) 2�û�����ͳ��(��)     *************/
	
	/**
	 * ��values��ȡ����ɫ����
	 */
	private int[] getColor() {
		TypedArray _ColorArray = getResources().obtainTypedArray(R.array.StatisticColors);
		int[] _Color = new int[_ColorArray.length()];
		for (int i = 0; i < _ColorArray.length(); i++) {
			_Color[i] = _ColorArray.getColor(i, Color.BLUE);
		}
		_ColorArray.recycle();
		return _Color;
	}
	
	/**
	 * �������ͳ��ͼ��
	 */
	private View getCategoryStatisticsView(){
		int[] _Color = getColor();		
		//��ͼ����Ⱦ�� ��������Դ��������ͼ
		DefaultRenderer _DefaultRenderer = buildCategoryRenderer(_Color);
		CategorySeries _Dataset = buildCategoryDataset("�������ͳ��", mModelCategoryTotals);
		GraphicalView _PieView = ChartFactory.getPieChartView(this, _Dataset, _DefaultRenderer);
		
		return _PieView;
	}
	
	/**
	 * �������ͳ�Ʊ�ͼͼ����Ⱦ��
	 */
	private DefaultRenderer buildCategoryRenderer(int[] pColors) {
		DefaultRenderer _Renderer = new DefaultRenderer();
		// ������Ⱦ����ʾ���Ű�ť
		_Renderer.setZoomButtonsVisible(true);
		// ������Ⱦ������Ŵ���С
		_Renderer.setZoomEnabled(true);
		//ͼ���ϵı�ǩ���ִ�С
		_Renderer.setLabelsTextSize(15);
		//ͼ����������ִ�С
		_Renderer.setLegendTextSize(20);
		//ͼ���ϵı�ǩ������ɫ
		_Renderer.setLabelsColor(Color.BLUE);
		//ԭʼ��СΪĬ�ϵ�0.8��
		_Renderer.setScale(0.8f);
		
		int _Color = 0;
		for (int i = 0; i < mModelCategoryTotals.size(); i++) {
			SimpleSeriesRenderer _R = new SimpleSeriesRenderer();
			_R.setColor(pColors[_Color]);
			_Renderer.addSeriesRenderer(_R);
			_Color++;
			if (_Color > pColors.length) {
				_Color = 1;
			}
		}
		return _Renderer;
	}
	
	/**
	 * �������ͳ������Դ
	 */
	private CategorySeries buildCategoryDataset(String pTitle, List<ModelCategoryTotal> pValues) {
		CategorySeries _CategorySeries = new CategorySeries(pTitle);
		for (ModelCategoryTotal value:pValues) {
			_CategorySeries.add(value.getCategoryName()+"\n"+value.getSumAmount()+"Ԫ",Double.parseDouble(value.getSumAmount()));
		}
		return _CategorySeries;
	}
	
	/**
	 * �����û�����ͳ����״ͼ
	 */
	private View getUserStatisticsView(){	
		//��ͼ����Ⱦ�� ��������Դ��������ͼ
		XYMultipleSeriesRenderer _Renderer = buildUserRenderer(getColor());
		XYMultipleSeriesDataset _Dataset = buildUserDataset(mUserName, mAmount);
		View _BarView = ChartFactory.getBarChartView(this, _Dataset, _Renderer, null);
		return _BarView;
	}
	
	/**
	 * �����û�����ͳ����״ͼͼ����Ⱦ��
	 */
	private XYMultipleSeriesRenderer buildUserRenderer(int[] pColor) {
		XYMultipleSeriesRenderer _Renderer = new XYMultipleSeriesRenderer();
		int _ColorCount = 0;
		for (int i = 0; i < mUserName.size(); i++) {
			SimpleSeriesRenderer _SimpleSeriesRenderer =new SimpleSeriesRenderer();
			//ÿ�����ε���ɫ
			_SimpleSeriesRenderer.setColor(pColor[_ColorCount]); 	
			//������ʾÿ�������Ϸ�����ֵ
			_Renderer.addSeriesRenderer(_SimpleSeriesRenderer);
			_Renderer.getSeriesRendererAt(i).setDisplayChartValues(true);
			_Renderer.getSeriesRendererAt(i).setChartValuesTextAlign(Align.RIGHT);
			
			_ColorCount++;
			if (_ColorCount == pColor.length) {
				_ColorCount = 0;
			}
		}
		_Renderer.setChartTitle("�û�����ͳ��");
		_Renderer.setChartTitleTextSize(40);
		_Renderer.setXTitle("�û�");
		_Renderer.setYTitle("���ѽ��(Ԫ)");
		_Renderer.setLabelsTextSize(20);
		_Renderer.setXLabels(0);
		//�������������������ɫ��С
		_Renderer.setLabelsColor(Color.BLUE);
		_Renderer.setAxisTitleTextSize(30);
		_Renderer.setYLabelsAlign(Align.RIGHT);
		//������������ɫ
		_Renderer.setAxesColor(Color.BLACK);
		//ͼ����Χ��С
		_Renderer.setMargins(new int[]{20,70,100,50});
		//ͼ���·�Legend���ִ�С
		_Renderer.setLegendTextSize(20); // ����X���Ĭ����ʾ��С���ֺ��������
		_Renderer.setXAxisMin(-mUserName.size()/2);
		_Renderer.setXAxisMax(mUserName.size()+1);
		// ����Y���Ĭ����ʾ��С���ֺ�������� 
		_Renderer.setYAxisMin(0);
		_Renderer.setYAxisMax(mBusinessStatistics.getMaxBigDecimal(mAmount).intValue()/10*10+50);
		// ������Ⱦ����ʾ���Ű�ť
		_Renderer.setZoomButtonsVisible(true);
		// ������Ⱦ������Ŵ���С
		_Renderer.setZoomEnabled(true);
		//�������ο��
		_Renderer.setBarWidth(100f);
		//����ͼ�����������ɫ
		_Renderer.setMarginsColor(getResources().getColor(R.color.turquoise));
		_Renderer.setAntialiasing(true);
		_Renderer.setBarSpacing(0.5f);
		//�������϶������������϶�
		_Renderer.setPanEnabled(true, false);
	
		
		
	 return _Renderer;
	}
	
	/**
	 * �����û�����ͳ������Դ
	 */
	private XYMultipleSeriesDataset buildUserDataset(List<String> pPayoutName, List<BigDecimal> pAmount) {
		XYMultipleSeriesDataset  _Dataset = new XYMultipleSeriesDataset();

        for (int i = 0; i < pPayoutName.size(); i++) {   
        	CategorySeries _Series = new CategorySeries(pPayoutName.get(i)); 
            _Series.add(pPayoutName.get(i), pAmount.get(i).doubleValue());  
            _Dataset.addSeries(_Series.toXYSeries());         
        }  
		return _Dataset;
	}
}