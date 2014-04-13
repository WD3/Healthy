package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountSet {
	
	public static void countSet(){
		String orgDate = MainActivity.sp.getString("����", "");
		String orgSteps = MainActivity.sp.getString("����", "0");
		String orgDistance = MainActivity.sp.getString("����", "0");
		String orgCalorie = MainActivity.sp.getString("��·��", "0");
		String orgProgress = MainActivity.sp.getString("����", "0%");
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String curDate = format.format(new Date());
		if(curDate.equals(orgDate)){
			StepCounter.tvsteps = orgSteps;
			StepCounter.distance = orgDistance;
			StepCounter.calorie = orgCalorie;
			StepCounter.progress = orgProgress;
			MainActivity.SendMessage(MainActivity.handler, 1);
		}
		else {
			StepCounter.steps = 0;
			StepCounter.tvsteps = "0";
			StepCounter.distance = "0";
			StepCounter.calorie = "0";
			StepCounter.progress = "0%";
			MainActivity.SendMessage(MainActivity.handler, 1);
		}
			
	}

}
