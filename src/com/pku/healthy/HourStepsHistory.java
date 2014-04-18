package com.pku.healthy;

import java.text.SimpleDateFormat;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;


public class HourStepsHistory {
	private LinearLayout layout;
	private SharedPreferences sp;
	private Context context;
	private GraphicalView mchartView_day;

	private String[] xhour = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	
	public HourStepsHistory(LinearLayout layout,SharedPreferences sp,Context context){
		this.layout = layout;
		this.sp = sp;
		this.context = context;
	}
	public void init(){			
		mchartView_day = ChartFactory.getBarChartView(context, buildDataset(), buildRenderer(), Type.DEFAULT);
		layout.removeAllViews();
		layout.addView(mchartView_day, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
//		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT){         //设置横屏
//			   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			}
	}
	
	protected XYMultipleSeriesDataset buildDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		
		double[] doubleValues = new double[24];
		for(int i = 0; i<24; i++){
			doubleValues[i] = sp.getInt(i+1+"hoursteps",0);
		}		
		CategorySeries series = new CategorySeries("");
		for (int i = 0; i < doubleValues.length; i ++){		
			series.add(doubleValues[i]);
		}
		dataset.addSeries(series.toXYSeries());		
		return dataset;
	}
	public XYMultipleSeriesRenderer buildRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.parseColor("#505050"));
	    r.setDisplayChartValues(true);
	    renderer.addSeriesRenderer(r);
	    setChartSettings(renderer);
	    return renderer;
	 }
	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
	    renderer.setChartTitle( "" );
	    renderer.setXTitle( "日期" );
	    renderer.setYTitle( "步数" );
	    renderer.setXAxisMin(0.5);
	    renderer.setXAxisMax(24.5);
	    renderer.setYAxisMin(0);
	    renderer.setLabelsTextSize(13);
	    renderer.setChartValuesTextSize(14);
	    renderer.setAxesColor(Color.parseColor("#505050"));
	    renderer.setYAxisMax(Integer.parseInt(MainActivity.tarSteps,10));
	    renderer.setZoomEnabled(false, false);	    
	    renderer.setMarginsColor(Color.parseColor("#70CD18"));
//	    renderer.setShowGrid(true);
	    renderer.setFitLegend(true);
//	    renderer.setInScroll(false);
//	    renderer.setBackgroundColor(Color.parseColor("#ffffff"));
//	    renderer.setApplyBackgroundColor(true);
//	    renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(false);
	    renderer.setPanEnabled(false, false);
//	    renderer.setMargins(new int[] { 40, 30, 15, 0 });
	    renderer.setBarSpacing(0.2f);
	    renderer.setXLabelsColor(Color.parseColor("#505050"));
	    renderer.setYLabelsColor(0, Color.parseColor("#505050"));
	    renderer.setXLabelsAlign(Align.CENTER);	
	    renderer.setXLabels(0);
	    for (int i = 0; i < 24; i++) {  
            renderer.addXTextLabel(i+1,xhour[i]); 
        }  
	 }

}
