package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountSet {
	
	public static void countSet(){
		String orgDate = PlayService.sp.getString("日期", "");
		String orgSteps = PlayService.sp.getString("步数", "0");
		String orgDistance = PlayService.sp.getString("距离", "0");
		String orgCalorie = PlayService.sp.getString("卡路里", "0");
		String orgProgress = PlayService.sp.getString("进度", "0%");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String curDate = format.format(date);
		if(curDate.equals(orgDate)){
			StepCounter.tvsteps = orgSteps;
			StepCounter.distance = orgDistance;
			StepCounter.calorie = orgCalorie;
			StepCounter.progress = orgProgress;
			MainActivity.SendMessage(MainActivity.handler, 1);
			int a = PlayService.sp.getInt(date.getHours()+1+"hoursteps", 0);
			if(a == 0)
				PlayService.sp.edit().putInt(date.getHours()+"fhoursteps", Integer.parseInt(StepCounter.tvsteps)).commit();
		}
		else {
			StepCounter.steps = 0;
			StepCounter.tvsteps = "0";
			StepCounter.distance = "0";
			StepCounter.calorie = "0";
			StepCounter.progress = "0%";
			MainActivity.SendMessage(MainActivity.handler, 1);
//			PlayService.sp.edit().clear().commit();
			PlayService.sp.edit().putString("步数", "0").putString("距离", "0").putString("卡路里", "0").putString("进度", "0%").putString("日期", curDate).commit();
			for(int i = 1;i<25;i++){
				PlayService.sp.edit().putInt(i+"hoursteps", 0)
				.putInt(i+"fhoursteps", 0).commit();
			}	
		}
			
	}

}
