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
import org.achartengine.model.TimeSeries;
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

public class DayWeightHistory{

	private LinearLayout layout;
	private SharedPreferences sp;
	private Context context;
	private int total;
	
	private GraphicalView mchartView_week;
	
	public DayWeightHistory(LinearLayout layout,SharedPreferences sp,Context context){
		this.layout = layout;
		this.sp = sp;
		this.context = context;
	}
	public void init(){			

		try {
			mchartView_week = ChartFactory.getTimeChartView(context, buildDataset(), buildRenderer(),"MM-dd HH:mm");
			layout.removeAllViews();
			layout.addView(mchartView_week, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
//		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT){         //璁剧疆妯睆
//			   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			}
	}
	
	protected XYMultipleSeriesDataset buildDataset() throws JSONException {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		
		String weightString = sp.getString("weight_json", "[]");
		JSONArray weightJSONArray = new JSONArray(weightString);
		total = weightJSONArray.length();
					
		Date[] dateValues = new Date[total];
		double[] doubleValues = new double[total];
		for (int i = 0; i < total; i ++){
			JSONObject obj;
			obj = weightJSONArray.getJSONObject(i);
			Date date = new Date();
			date.setTime(obj.getLong("time"));
			dateValues[i] = date;
			doubleValues[i] = obj.getDouble("weight");
		}
		TimeSeries series = new TimeSeries("");
		for (int i = 0; i < total; i ++){		
			series.add(dateValues[i], doubleValues[i]);
		}
		dataset.addSeries(series);		
		return dataset;
	}
	public XYMultipleSeriesRenderer buildRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    r.setColor(Color.parseColor("#505050"));
	    r.setPointStyle(PointStyle.TRIANGLE);
	    r.setDisplayChartValues(true);
	    renderer.addSeriesRenderer(r);
	    setChartSettings(renderer);
	    return renderer;
	 }
	private void setChartSettings(XYMultipleSeriesRenderer renderer) {		
	    renderer.setChartTitle( "" );
	    renderer.setXTitle( "时间(月-日  时:分)" );
	    renderer.setGridColor(Color.GRAY);
	    renderer.setYTitle( "体重" );
//	    renderer.setXAxisMin(0.5);
//	    renderer.setXAxisMax(7.5);
	    renderer.setYAxisMin(0);
	    renderer.setYAxisMax(100);
	    renderer.setChartValuesTextSize(24);
	    renderer.setLabelsTextSize(20);
	    renderer.setMarginsColor(Color.parseColor("#70CD18"));
	    renderer.setShowGrid(true);
	    renderer.setFitLegend(true);
	    renderer.setAxesColor(Color.parseColor("#505050"));
//	    renderer.setMarginsColor(Color.parseColor("#ffe908"));
//	    renderer.setInScroll(false);
//	    renderer.setBackgroundColor(Color.parseColor("#ffaa60"));
//	    renderer.setApplyBackgroundColor(true);
//	    renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(true);
	    renderer.setPanEnabled(true, true);
//	    renderer.setMargins(new int[] { 40, 30, 15, 0 });
	    renderer.setXLabelsAlign(Align.CENTER);	    
	    renderer.setXLabelsColor(Color.parseColor("#505050"));
	    renderer.setYLabelsColor(0, Color.parseColor("#505050"));
//	    for (int i = 0; i < 7; i++) {  
//            renderer.addXTextLabel(i+1,xdate[i]); 
//        }  
//	    renderer.setXLabels(0);
	 }
}
