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
 * һ������ֻ�ҡ�εļ�����
 */
public class ShakeListener implements SensorEventListener {
	// �ٶ���ֵ����ҡ���ٶȴﵽ��ֵ���������
	private static final int SPEED_SHRESHOLD = 3600;
	// ���μ���ʱ����
	private static final int UPTATE_INTERVAL_TIME = 70;
	// ������������
	private SensorManager sensorManager;
	// ������
	private Sensor sensor;
	// ������
	private Context context;
	// �ֻ���һ��λ��ʱ������Ӧ����
	private float lastX;
	private float lastY;
	private float lastZ;
	// �ϴμ��ʱ��
	private long lastUpdateTime;
	private Vibrator vibrator;
	private SharedPreferences sp;

	// ������
	public ShakeListener(Context context,SharedPreferences sp) {
		this.context = context;
		this.sp = sp;
		vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
	}

	// ������Ӧ����Ӧ��ñ仯����
	public void onSensorChanged(SensorEvent event) {
		// ���ڼ��ʱ��
		long currentUpdateTime = System.currentTimeMillis();
		// ���μ���ʱ����
		long timeInterval = currentUpdateTime - lastUpdateTime;
		// �ж��Ƿ�ﵽ�˼��ʱ����
		if (timeInterval < UPTATE_INTERVAL_TIME)
			return;
		// ���ڵ�ʱ����lastʱ��
		lastUpdateTime = currentUpdateTime;
		// ���x,y,z����
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		// ���x,y,z�ı仯ֵ
		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;
		// �����ڵ�������last����
		lastX = x;
		lastY = y;
		lastZ = z;
		// sqrt ���������˫���Ƶ�ƽ����
		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ* deltaZ)/ timeInterval * 10000;
		// �ﵽ�ٶȷ�ֵ��������ʾ
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
			System.out.println("�������سɹ�");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
