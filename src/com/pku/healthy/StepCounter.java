package com.pku.healthy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class StepCounter implements SensorEventListener {
	
	private float acc[] = null;
	private File 				txtFile;
	private File				txtResult;
	private File				txtDir;
	private FileOutputStream 	fos;
	private FileOutputStream 	fos1;
	private String line = "";
	private long timeLast;
	private long timeZero;
	private long timePress;
	private int freq = 20;
	private int dt = 1000 / freq;
	private int total = 10000;
	static int count = -1;
	private boolean recording = false;
	private boolean dateChanged;
	static String tvsteps;
	static String distance;
	static String calorie;
	static String progress;
	private String sOrgSteps;
	private String orgDate;
	
	long time;
	
	static int steps;
	long stepLength = 1000;
	
	double max = 15;
	long last = 0;
	long index = 0;
	double next = 0;
	double tmp;
	long timeTmp;
	long startTime = 0;
	long endTime =  stepLength;
	long ignoreSpan = 0;
	double weight = 1.0 / 4;
	long ignoreStart = 0;
	
	long index2 = 0;
	double next2 = 0;
	
	double[] data = {0,0,0,0,0,0,0};
	long[] dataTime = {0,0,0,0,0,0,0};

	int nextData = 0;
	double sum = 0;		
	
    
	public void start() {
//		再次启动程序时判断是否当天，若当天则加上之前步数
		orgDate = MainActivity.sp.getString("日期", "");
		sOrgSteps = MainActivity.sp.getString("步数", "0");
		int iOrgSteps = Integer.parseInt(sOrgSteps);
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String curDate = format.format(new Date());
		if(curDate.equals(orgDate))
			steps = iOrgSteps/2;
		else steps = 0;
			
		
    	count = 0;
    	recording = false;
    			
		timeLast = System.currentTimeMillis();
		timePress = timeLast;
		
		SimpleDateFormat  formatter   =   new   SimpleDateFormat("yyyy-MM-dd HH-mm-ss");    
		String fileName = formatter.format(new Date(timePress));
		
		try {
			
			txtDir = new File(Environment.getExternalStorageDirectory().getPath()+"/0SENSOR_DATA");
			if (!txtDir.exists())
				txtDir.mkdirs();
			
			txtFile = new File(Environment.getExternalStorageDirectory().getPath()+"/0SENSOR_DATA/"+ fileName + " originaldata.txt");
			if (!txtFile.exists())
				txtFile.createNewFile();
			fos = new FileOutputStream(txtFile);
			line = "version:1.0"+"\r\n"+ "手机型号:"+android.os.Build.MODEL+"\r\n"+"姓名:"+" "+"\r\n"+"身高:"+"  "+"\r\n"+"体重:"+"  "+"\r\n"+"年龄:"+"  "+"\r\n"+
					"       "+"time"+"        "+"        "+"a[x]"+"  "+"        "+"a[y]"+"  "+"        "+"a[z]"+"\r\n";
			fos.write(line.getBytes());
			
			/*txtResult = new File(Environment.getExternalStorageDirectory().getPath()+"/0SENSOR_DATA/"+ fileName + "-result.txt");
			if (!txtResult.exists())
				txtResult.createNewFile();
			fos1 = new FileOutputStream(txtResult);*/

		} catch (IOException e) {
			e.printStackTrace();
		}
//		tvsteps = "0";
//		MainActivity.SendMessage(MainActivity.handler, 1);
//		wakeLock.acquire();
//    	mSensorManager.registerListener(this, sLinearAcceleration, SensorManager.SENSOR_DELAY_FASTEST);
		
		stepLength = 1100;
//		steps = 0;
		last = 0;
		index = 0;
		next = 0;
		startTime = 0;
		endTime = stepLength;
		ignoreSpan = 0;
		ignoreStart = 0;
		
		index2 = 0;
		next2 = 0;
		
		for (int i = 0; i < 7 ; i++){
			data[i] = 0;
			dataTime[i] = 0;
		}
		nextData = 0;
		sum = 0;

	}
	
	public void stop() {
		
    	try {
    		
			fos.close();
	//		fos1.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
       
	public void onSensorChanged(SensorEvent event) {

		if ( event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
			return;
		
		acc = event.values;
		
		time = System.currentTimeMillis();
		
		if (time - timePress < 4000) 
			return;     //  ignore data before 4s
		
		if (!recording){  //  vibrate before start
//			Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
//			vib.vibrate(10);
			recording = true;
			timeLast = time;
			return;
		}
		
		if (time - timeLast < dt)  //ignore data between sampling points 
			return;
		
		if (++count > total){ 
			stop();
			recording = false;
//			Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
//			vib.vibrate(5000);   //  vibrate when stop
			return;
		}
		

		
		if (count == 1){
			timeZero = time;
		}
		
		timeLast = time;
//		countTV.setText(String.valueOf(count));
		
		countSteps();
		
	}
	
	private void countSteps() {

		try {
			
			tmp = Math.sqrt(acc[0] * acc[0] + acc[1] * acc[1] + acc[2] * acc[2]);
			timeTmp = time - timeZero; 
			
			SimpleDateFormat sDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date curdate = new Date(time);
			String date = sDateFormat.format(curdate);
			
			line = date + "    " + acc[0] +"    "+ acc[1] + "    " +acc[2] + "    " + "\r\n";		
			fos.write(line.getBytes());
			
			sum += tmp - data[nextData];
			data[nextData] = tmp;
			dataTime[nextData] = timeTmp;
			nextData = (nextData + 1) % 7;
			
			tmp = sum;
			timeTmp = dataTime[(nextData + 3) %7];
			
			//line = String.valueOf(time-timeZero) + " "
			//		+ acc[0] + " " + acc[1] + " " + acc[2] + "\n";
			

				
			if (timeTmp < startTime)   //未到 检测窗口，忽略
				return;
			
			if (tmp > next ){   //检测窗口内的加速度 大于 阈值 且 大于待检测窗口的当前极大值时，更行当前极大值
				
				line = "new next : " + "timeTmp: " + timeTmp + "  " + 
						"index: " + index + "  tmp: " + tmp + "  next: " + next + "\n";
	
				//fos1.write(line.getBytes());
				
				index = timeTmp;
				next = tmp;
				
				index2 = 0;
				next2 = 0;
			}
			
			if (tmp > next2 && index > 0 && (timeTmp > index + (long)(stepLength  * 0.7)) ){
					index2 = timeTmp;
					next2 = tmp;
			}
			
			if (timeTmp > endTime){    // 当前 检查窗口 处理完毕
				
				if ( next < max){ // 待检查区间内，加速度均未达到阈值，判定为未走动，将待检查窗口移到 【endTime，endTime+ stepLength】 
					last += endTime - startTime;
					ignoreSpan += endTime - startTime;
					
					line = "ignore : " + "window: " + startTime + "--" + endTime + "\n" + 
							"next: " + next + "   max: "  + max + "\n";
		
					//fos1.write(line.getBytes());
					
					startTime = endTime;
					endTime = startTime + stepLength;
					
					index = 0;
					next = 0;
					
					index2 = 0;
					next2 = 0;
					
					return;
				}
				
				if (steps > 0){  // 不是第一步，则需要更新  步长（时间）
					//stepLength = (index - ignoreSpan) / steps;
					if (ignoreSpan < 100)
						stepLength = (long)(stepLength * (1 - weight) + (index - last) * weight);

				}else{  //否则 将 第一个极大值之前的时间作为无效数据，忽略，
					ignoreStart = index;
				}
				
				//移动 待检测窗口
				startTime = index + (long)(stepLength  * 0.7);
				endTime = index + (long)(stepLength * 1.25);
				
				
				//System.out.println("next: " + startTime + "--"  + endTime);
				
				// 动态刷新 max
				max = max * (1 - weight ) + (next * 0.6) * (weight);
				max = (15 > max)? 15 : max;
				max = (25 < max)? 25 : max;
				
				line = "" + steps + "  " + String.valueOf(index) + " " + next + "\n" + 
						"window: " + startTime + "--" + endTime + "\n" + 
						"stepLength: " + stepLength + "   max: "  + max + "\n";
	
				//fos1.write(line.getBytes());
				
				// 记录上一步的时间
				last = index;
				ignoreSpan = 0;
				index = 0;
				next = 0;
				
				if (startTime < index2 && index2 < endTime ){
					index = index2;
					next = next2;
				}
				
				index2 = 0;
				next2 = 0;
				
				//极大值数目加1
				steps ++;
												
				//实际步数应为极大值数*2  
				
				tvsteps = ""+steps * 2;
				distance = String.format("%.1f", steps * 2 * Double.parseDouble(MainActivity.stepLength) / 100);
				calorie = String.format("%.2f", steps * 1.23);
				progress = String.format("%.3f", steps * 200 / Double.parseDouble(MainActivity.tarSteps)) + "%";
				MainActivity.SendMessage(MainActivity.handler, 1);				
				return;
				
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
