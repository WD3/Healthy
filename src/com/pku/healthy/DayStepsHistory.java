package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DayStepsHistory{

	private LinearLayout layout;
	private SharedPreferences sp;
	private Context context;
	private int total;
	private long oneday = 86400000l;
	private String firstdate;
	private String twicedate;
	private String thirddate;
	private String fourthdate;
	private String fifthdate;
	private String sixthdate;
	private String seventhdate;
	private String eighthdate;
	private GraphicalView mchartView_week;
	private String[] xdate = new String[7];
	
	public DayStepsHistory(LinearLayout layout,SharedPreferences sp,Context context){
		this.layout = layout;
		this.sp = sp;
		this.context = context;
	}
	public void init(){			
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		seventhdate = format.format(System.currentTimeMillis());
		firstdate = format.format((System.currentTimeMillis()-oneday*6));
		twicedate = format.format((System.currentTimeMillis()-oneday*5));
		thirddate = format.format((System.currentTimeMillis()-oneday*4));
		fourthdate = format.format((System.currentTimeMillis()-oneday*3));
		fifthdate = format.format((System.currentTimeMillis()-oneday*2));
		sixthdate = format.format((System.currentTimeMillis()-oneday));
		String[] date = {firstdate,twicedate,thirddate,fourthdate,fifthdate,sixthdate,seventhdate};
		for(int i = 0;i<date.length;i++){
			xdate[i] = date[i];
		}
		mchartView_week = ChartFactory.getBarChartView(context, buildDataset(), buildRenderer(), Type.DEFAULT);
		layout.removeAllViews();
		layout.addView(mchartView_week, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
//		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT){         //设置横屏
//			   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			}
	}
	
	protected XYMultipleSeriesDataset buildDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		double[] yValues = new double[7];
//		int step6 = Integer.parseInt(sp.getString(seventhdate, "0"));
//		yValues[6] = (double)step6;
		yValues[6] = Double.parseDouble(StepCounter.tvsteps);
		int step5 = Integer.parseInt(sp.getString(sixthdate, "0"));
		yValues[5] = (double)step5;
		int step4 = Integer.parseInt(sp.getString(fifthdate, "0"));
		yValues[4] = (double)step4;
		int step3 = Integer.parseInt(sp.getString(fourthdate, "0"));
		yValues[3] = (double)step3;
		int step2 = Integer.parseInt(sp.getString(thirddate, "0"));
		yValues[2] = (double)step2;
		int step1 = Integer.parseInt(sp.getString(twicedate, "0"));
		yValues[1] = (double)step1;
		int step0 = Integer.parseInt(sp.getString(firstdate, "0"));
		
		CategorySeries series = new CategorySeries("");
		for (int i = 0; i < 7; i ++){		
			series.add(yValues[i]);
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
	    renderer.setXAxisMax(7.5);
	    renderer.setYAxisMin(0);
	    renderer.setChartValuesTextSize(24);
	    renderer.setLabelsTextSize(20);
	    renderer.setYAxisMax(Integer.parseInt(MainActivity.tarSteps,10));
	    renderer.setMarginsColor(Color.parseColor("#70CD18"));
	    renderer.setZoomEnabled(false, false);
	    renderer.setShowGrid(true);
	    renderer.setFitLegend(true);
	    renderer.setAxesColor(Color.parseColor("#505050"));
//	    renderer.setMarginsColor(Color.parseColor("#ffe908"));
//	    renderer.setInScroll(false);
//	    renderer.setBackgroundColor(Color.parseColor("#ffaa60"));
//	    renderer.setApplyBackgroundColor(true);
//	    renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(false);
	    renderer.setPanEnabled(false, false);
//	    renderer.setMargins(new int[] { 40, 30, 15, 0 });
	    renderer.setBarSpacing(0.2f);
	    renderer.setXLabelsAlign(Align.CENTER);	    
	    renderer.setXLabelsColor(Color.parseColor("#505050"));
	    renderer.setYLabelsColor(0, Color.parseColor("#505050"));
	    for (int i = 0; i < 7; i++) {  
            renderer.addXTextLabel(i+1,xdate[i]); 
        }  
	    renderer.setXLabels(0);
	 }
}
