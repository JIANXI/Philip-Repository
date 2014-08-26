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
		//初始化TabHost
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(); 
        getLayoutInflater().inflate(R.layout.statistic_tabs, mTabHost.getTabContentView());
        //将layout依次放入tab
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("类别统计").setContent(R.id.tab1));   
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("用户统计").setContent(R.id.tab2)); 
        //得到需要放入tab的layout
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
		//将需要显示的view放入tab
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
		// 选择帐本
		case 0:
			showAccountBookSelectDialog();
			break;
		//导出Excel
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
	 * 选择帐本对话框
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
	 * 选择帐本ItemListener
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

	/*************下面为2个图表视图的生成  1类别统计(饼) 2用户消费统计(柱)     *************/
	
	/**
	 * 从values中取出颜色数组
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
	 * 生成类别统计图表
	 */
	private View getCategoryStatisticsView(){
		int[] _Color = getColor();		
		//绑定图像渲染器 ，绑定数据源，建立视图
		DefaultRenderer _DefaultRenderer = buildCategoryRenderer(_Color);
		CategorySeries _Dataset = buildCategoryDataset("消费类别统计", mModelCategoryTotals);
		GraphicalView _PieView = ChartFactory.getPieChartView(this, _Dataset, _DefaultRenderer);
		
		return _PieView;
	}
	
	/**
	 * 建立类别统计饼图图像渲染器
	 */
	private DefaultRenderer buildCategoryRenderer(int[] pColors) {
		DefaultRenderer _Renderer = new DefaultRenderer();
		// 设置渲染器显示缩放按钮
		_Renderer.setZoomButtonsVisible(true);
		// 设置渲染器允许放大缩小
		_Renderer.setZoomEnabled(true);
		//图表上的标签文字大小
		_Renderer.setLabelsTextSize(15);
		//图表下面的文字大小
		_Renderer.setLegendTextSize(20);
		//图表上的标签文字颜色
		_Renderer.setLabelsColor(Color.BLUE);
		//原始大小为默认的0.8倍
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
	 * 建立类别统计数据源
	 */
	private CategorySeries buildCategoryDataset(String pTitle, List<ModelCategoryTotal> pValues) {
		CategorySeries _CategorySeries = new CategorySeries(pTitle);
		for (ModelCategoryTotal value:pValues) {
			_CategorySeries.add(value.getCategoryName()+"\n"+value.getSumAmount()+"元",Double.parseDouble(value.getSumAmount()));
		}
		return _CategorySeries;
	}
	
	/**
	 * 生成用户消费统计柱状图
	 */
	private View getUserStatisticsView(){	
		//绑定图像渲染器 ，绑定数据源，建立视图
		XYMultipleSeriesRenderer _Renderer = buildUserRenderer(getColor());
		XYMultipleSeriesDataset _Dataset = buildUserDataset(mUserName, mAmount);
		View _BarView = ChartFactory.getBarChartView(this, _Dataset, _Renderer, null);
		return _BarView;
	}
	
	/**
	 * 建立用户消费统计柱状图图像渲染器
	 */
	private XYMultipleSeriesRenderer buildUserRenderer(int[] pColor) {
		XYMultipleSeriesRenderer _Renderer = new XYMultipleSeriesRenderer();
		int _ColorCount = 0;
		for (int i = 0; i < mUserName.size(); i++) {
			SimpleSeriesRenderer _SimpleSeriesRenderer =new SimpleSeriesRenderer();
			//每条柱形的颜色
			_SimpleSeriesRenderer.setColor(pColor[_ColorCount]); 	
			//设置显示每条柱形上方的数值
			_Renderer.addSeriesRenderer(_SimpleSeriesRenderer);
			_Renderer.getSeriesRendererAt(i).setDisplayChartValues(true);
			_Renderer.getSeriesRendererAt(i).setChartValuesTextAlign(Align.RIGHT);
			
			_ColorCount++;
			if (_ColorCount == pColor.length) {
				_ColorCount = 0;
			}
		}
		_Renderer.setChartTitle("用户消费统计");
		_Renderer.setChartTitleTextSize(40);
		_Renderer.setXTitle("用户");
		_Renderer.setYTitle("消费金额(元)");
		_Renderer.setLabelsTextSize(20);
		_Renderer.setXLabels(0);
		//设置坐标轴标题字体颜色大小
		_Renderer.setLabelsColor(Color.BLUE);
		_Renderer.setAxisTitleTextSize(30);
		_Renderer.setYLabelsAlign(Align.RIGHT);
		//设置坐标轴颜色
		_Renderer.setAxesColor(Color.BLACK);
		//图表外围大小
		_Renderer.setMargins(new int[]{20,70,100,50});
		//图表下方Legend文字大小
		_Renderer.setLegendTextSize(20); // 设置X轴的默认显示最小数字和最大数字
		_Renderer.setXAxisMin(-mUserName.size()/2);
		_Renderer.setXAxisMax(mUserName.size()+1);
		// 设置Y轴的默认显示最小数字和最大数字 
		_Renderer.setYAxisMin(0);
		_Renderer.setYAxisMax(mBusinessStatistics.getMaxBigDecimal(mAmount).intValue()/10*10+50);
		// 设置渲染器显示缩放按钮
		_Renderer.setZoomButtonsVisible(true);
		// 设置渲染器允许放大缩小
		_Renderer.setZoomEnabled(true);
		//设置柱形宽度
		_Renderer.setBarWidth(100f);
		//设置图表外区域的颜色
		_Renderer.setMarginsColor(getResources().getColor(R.color.turquoise));
		_Renderer.setAntialiasing(true);
		_Renderer.setBarSpacing(0.5f);
		//可左右拖动，不可上下拖动
		_Renderer.setPanEnabled(true, false);
	
		
		
	 return _Renderer;
	}
	
	/**
	 * 建立用户消费统计数据源
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