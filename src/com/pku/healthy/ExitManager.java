package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.SharedPreferences;

public class ExitManager {

	public static void save() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String sDate = format.format(date);
		int hour = date.getHours();
//		int orgSteps = MainActivity.sp.getInt(hour+"fhoursteps", 0);
//		int curSteps = Integer.parseInt(StepCounter.tvsteps) - orgSteps;
//		MainActivity.sp.edit().putInt(hour+"hoursteps", curSteps).commit();
		PlayService.sp.edit().putString("����", sDate)
				.putString(sDate, StepCounter.tvsteps)
				.putString("����", StepCounter.tvsteps)
				.putString("����", StepCounter.distance)
				.putString("��·��", StepCounter.calorie)
				.putString("����", StepCounter.progress).commit();
	}
}
