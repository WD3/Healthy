package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountSet {
	
	public static void countSet(){
		String orgDate = MainActivity.sp.getString("日期", "");
		String orgSteps = MainActivity.sp.getString("步数", "0");
		String orgDistance = MainActivity.sp.getString("距离", "0");
		String orgCalorie = MainActivity.sp.getString("卡路里", "0");
		String orgProgress = MainActivity.sp.getString("进度", "0%");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String curDate = format.format(date);
		if(curDate.equals(orgDate)){
			StepCounter.tvsteps = orgSteps;
			StepCounter.distance = orgDistance;
			StepCounter.calorie = orgCalorie;
			StepCounter.progress = orgProgress;
			MainActivity.SendMessage(MainActivity.handler, 1);
			int a = MainActivity.sp.getInt(date.getHours()+"hoursteps", 0);
			if(a == 0)
				MainActivity.sp.edit().putInt(date.getHours()+"fhoursteps", Integer.parseInt(StepCounter.tvsteps)).commit();
		}
		else {
			StepCounter.steps = 0;
			StepCounter.tvsteps = "0";
			StepCounter.distance = "0";
			StepCounter.calorie = "0";
			StepCounter.progress = "0%";
			MainActivity.SendMessage(MainActivity.handler, 1);
			MainActivity.sp.edit().clear().commit();
		}
			
	}

}
