/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pku.healthy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint.Align;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeightHistoryChartBuilder extends AbstractDemoChart {

//	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	private XYMultipleSeriesRenderer mRenderer;// = new
												// XYMultipleSeriesRenderer();

	private GraphicalView mChartView;

	private LinearLayout linearLayoutChart;

	private Context mContext;
	private SharedPreferences mSettings;
	private int total = 12;

	public WeightHistoryChartBuilder(Context context, SharedPreferences setting,
			LinearLayout layout) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mSettings = setting;
		linearLayoutChart = layout;
	}

	protected void init() {

		JSONArray weightJSONArray = new JSONArray();
	
		String weightString = mSettings.getString("weight_json", "[]");
		try {	
			weightJSONArray = new JSONArray(weightString);
			total = weightJSONArray.length();
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		

		Date[] dateValues = new Date[total];
		double[] doubleValues = new double[total];
		
		try {	
			
			for (int i = 0; i < total; i ++){
				JSONObject obj;
				obj = weightJSONArray.getJSONObject(i);
				Date date = new Date();
				date.setTime(obj.getLong("time"));
				dateValues[i] = date;
				doubleValues[i] = obj.getDouble("weight");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] titles = new String[] { "" };   //曲线名称

		List<Date[]> dates = new ArrayList<Date[]>();
		List<double[]> values = new ArrayList<double[]>();

//		
//		Date[] dateValues = new Date[] { new Date(113, 4, 11),
//		new Date(113, 4, 12), new Date(113, 4, 13), new Date(113, 4, 14),
//		new Date(113, 4, 15), new Date(113, 4, 16), new Date(113, 4, 17),
//		new Date(113, 4, 18), new Date(113, 4, 19), new Date(113, 4, 20),
//		new Date(113, 4, 21), new Date(113, 4, 22) };
//		double[] doubleValues = new double[]{ 54.9, 55.3, 53.2, 54.5, 56.5, 54.7, 55.8, 54.3, 54, 52.3, 50.5, 52.9 };
		dates.add(dateValues);
		values.add(doubleValues);

//		JSONArray weightJSONArray;
//
//		try {
//			
//			weightJSONArray = new JSONArray();
//
//			for (int i = 0; i < doubleValues.length; i ++){
//					JSONObject newValue = new JSONObject();
//					newValue.put("time", dateValues[i].getTime());
//					newValue.put("weight",doubleValues[i]);			
//					weightJSONArray.put(newValue);
//			}
//			SharedPreferences.Editor editor = mSettings.edit();
//			editor.putString("weight_json", weightJSONArray.toString());
//			Log.v("json",weightJSONArray.toString());
//			editor.commit();
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}	

		
		// 定义曲线颜色
		int[] colors = new int[] { Color.GREEN}; //.rgb(164, 222, 20) };

		// 定义曲线点的形状
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT };

		// 鏋勯�缁樺浘鍣╮enderer
		mRenderer = buildRenderer(colors, styles);
		setChartSettings(mRenderer, "", // 标题
				"日期", // x轴
				"", // y轴
				dateValues[0].getTime(), // Xmin
				dateValues[total - 1].getTime()+86400000, // Xmax
				0, // Ymin
				100, // Ymax
				Color.DKGRAY, // 坐标轴颜色
				Color.BLACK // 坐标轴标签颜色
		);

		// 璁剧疆鏂囧瓧澶у皬
		mRenderer.setYLabels(10);
		mRenderer.setAxisTitleTextSize(30);

		// 璁剧疆鍥炬爣鐨勮儗鏅鑹�
		mRenderer.setBackgroundColor(Color.argb(127, 231, 228, 213));
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setApplyBackgroundColor(true);

		int[] margins = new int[] { 20, 40, 20, 40 };
		mRenderer.setMargins(margins);

		// 璁剧疆鍥炬爣鏄剧ず缃戞牸鍙婂叾棰滆壊
		mRenderer.setGridColor(Color.GRAY);
		mRenderer.setShowGrid(true);
		mRenderer.setChartValuesTextSize(25);
		mRenderer.setDisplayChartValues(true);
		mRenderer.setLabelsTextSize(25);
		mRenderer.setXLabels(10);
		mRenderer.setPanLimits(new double[]{dateValues[0].getTime()-86400000, dateValues[total - 1].getTime()+86400000, 0, 120});

		mRenderer.setChartTitleTextSize(0);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setLegendHeight(300);
		

		mChartView = ChartFactory.getTimeChartView(mContext,buildDateDataset(titles, dates, values), mRenderer, "MM-dd");

		LinearLayout layout = (LinearLayout) linearLayoutChart
				.findViewById(R.id.chart);
		
		layout.removeAllViews();                     

		layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,                    
				LayoutParams.FILL_PARENT));

		linearLayoutChart.setVisibility(View.VISIBLE);

	}

	public void draw() {
		init();
	}

}
