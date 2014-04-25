package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class AlarmReceiver extends BroadcastReceiver {
	private int curHourSteps;
	private int curSteps;
	static String stepsString;
	static JSONObject newValue;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("AlarmReceiver")){
			System.out.println("收到广播");
			Date date = new Date();
			int hour = date.getHours();
			if(hour == 0){       //若为零点，保存上一天步数，计步清零
				hour = 24;
				SimpleDateFormat format = new SimpleDateFormat("MM-dd");
				String curDate = format.format(new Date());
				MainActivity.sp.edit().putString("日期", curDate).commit();
				date.setDate(date.getDate() - 1);
				String orgDate = format.format(date);
				MainActivity.sp.edit().putString(orgDate, StepCounter.tvsteps).commit();	
				
				StepCounter.steps = 0;
				StepCounter.tvsteps = "0";
				StepCounter.distance = "0";
				StepCounter.calorie = "0";
				StepCounter.progress = "0%";
				MainActivity.SendMessage(MainActivity.handler, 1);
				for(int i = 1;i<25;i++){
					MainActivity.sp.edit().putInt(i+"hoursteps", 0)
					.putInt(i+"fhoursteps", 0).commit();
				}				
			}
	
			int orgSteps = MainActivity.sp.getInt(hour-1+"fhoursteps", 0);
			curHourSteps = Integer.parseInt(StepCounter.tvsteps) - orgSteps;
			curSteps = Integer.parseInt(StepCounter.tvsteps);
			MainActivity.sp.edit().putInt(hour+"hoursteps", curHourSteps)
			.putInt(hour+"fhoursteps", curSteps).commit();	
			
			StepCounter.stop();
			String sourceFilePath = Environment.getExternalStorageDirectory().getPath()+"/0SENSOR_DATA";
			String zipFilePath = Environment.getExternalStorageDirectory().getPath()+"/0SENSOR_DATA";
			boolean flag = FileToZip.fileToZip(sourceFilePath, zipFilePath);
			if (flag) {
				System.out.println(">>>>>> 文件打包成功. <<<<<<");
			} else {
				System.out.println(">>>>>> 文件打包失败. <<<<<<");
			}
			StepCounter.WriteToFile();
			
			saveSteps();			
			stepsString = MainActivity.sp.getString("steps_lose", null);
			if(stepsString == null) formatSteps();
			else saveLoseSteps();
			System.out.println("StepsString"+stepsString);
			String countId = MainActivity.counterId.getText().toString();
//			String countId = "000000000000";
			StepThread sThread = new StepThread(stepsString, countId);
			sThread.start();							
		}		
	}
	private void saveSteps(){
		newValue = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = format.format(System.currentTimeMillis());
		String formatTime = time+":00";
		try {
			newValue.put("time", formatTime);
			newValue.put("hoursteps", curHourSteps);		
			newValue.put("steps", curSteps);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	private void formatSteps(){
		JSONArray stepsArray = new JSONArray();
		stepsArray.put(newValue);
		stepsString = stepsArray.toString();						
	}
	public static void saveLoseSteps(){
		JSONArray stepsJSONArray;
		stepsString = MainActivity.sp.getString("steps_lose", "[]");
		try {
			stepsJSONArray = new JSONArray(stepsString);
			stepsJSONArray.put(newValue);
			stepsString = stepsJSONArray.toString();
			MainActivity.sp.edit().putString("Steps_lose", stepsString)
					.commit();
			System.out.println("保存上传失败步数成功");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
