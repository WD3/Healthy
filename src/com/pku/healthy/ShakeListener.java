package com.pku.healthy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

/**
 * 一个检测手机摇晃的监听器
 */
public class ShakeListener implements SensorEventListener {
	// 速度阈值，当摇晃速度达到这值后产生作用
	private static final int SPEED_SHRESHOLD = 3600;
	// 两次检测的时间间隔
	private static final int UPTATE_INTERVAL_TIME = 70;
	// 传感器管理器
	private SensorManager sensorManager;
	// 传感器
	private Sensor sensor;
	// 上下文
	private Context context;
	// 手机上一个位置时重力感应坐标
	private float lastX;
	private float lastY;
	private float lastZ;
	// 上次检测时间
	private long lastUpdateTime;
	private Vibrator vibrator;
	private SharedPreferences sp;

	// 构造器
	public ShakeListener(Context context,SharedPreferences sp) {
		this.context = context;
		this.sp = sp;
		vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
	}

	// 重力感应器感应获得变化数据
	public void onSensorChanged(SensorEvent event) {
		// 现在检测时间
		long currentUpdateTime = System.currentTimeMillis();
		// 两次检测的时间间隔
		long timeInterval = currentUpdateTime - lastUpdateTime;
		// 判断是否达到了检测时间间隔
		if (timeInterval < UPTATE_INTERVAL_TIME)
			return;
		// 现在的时间变成last时间
		lastUpdateTime = currentUpdateTime;
		// 获得x,y,z坐标
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		// 获得x,y,z的变化值
		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;
		// 将现在的坐标变成last坐标
		lastX = x;
		lastY = y;
		lastZ = z;
		// sqrt 返回最近的双近似的平方根
		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ* deltaZ)/ timeInterval * 10000;
		// 达到速度阀值，发出提示
		if (speed >= SPEED_SHRESHOLD) {
			saveWeight();
			vibrator.vibrate(500);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private void saveWeight(){
		JSONArray weightJSONArray;
		JSONObject newValue = new JSONObject();

		String weightString = sp.getString("weight_json", "[]");
		try {

			newValue.put("time", System.currentTimeMillis());
			newValue.put("weight", Double.parseDouble(MainActivity.newWeight));

			weightJSONArray = new JSONArray(weightString);
			weightJSONArray.put(newValue);

			sp.edit().putString("weight_json", weightJSONArray.toString())
					.commit();
			String device = "000000000000";
			WeightThread weightThread = new WeightThread(MainActivity.newWeight,device);
			weightThread.start();
			System.out.println("保存体重成功");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
