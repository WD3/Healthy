package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	private AlarmThread alarmThread;
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
			int orgSteps = MainActivity.sp.getInt(hour+"fhoursteps", 0);
			curHourSteps = Integer.parseInt(StepCounter.tvsteps) - orgSteps;
			curSteps = Integer.parseInt(StepCounter.tvsteps);
			MainActivity.sp.edit().putInt(hour+"hoursteps", curHourSteps)
			.putInt(hour+1+"fhoursteps", curSteps).commit();
			if(hour == 23){				
				alarmThread = new AlarmThread(context);
				new Thread(alarmThread).start();	
			}		
			
			saveSteps();			
			stepsString = MainActivity.sp.getString("steps_lose", null);
			if(stepsString == null) formatSteps();
			else saveLoseSteps();
			System.out.println("StepsString"+stepsString);
			StepThread sThread = new StepThread(stepsString, "000000000000");
			sThread.start();							
		}		
	}
	private void saveSteps(){
		newValue = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = format.format(System.currentTimeMillis());
		try {
			newValue.put("time", time);
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
