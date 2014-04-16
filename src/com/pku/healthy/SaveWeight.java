package com.pku.healthy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pku.healthy.ShakeListener.OnShakeListener;


import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

public class SaveWeight {
	private Context context;
	private Vibrator vibrator;
	private SharedPreferences sp;
	private ShakeListener mShakeListener;
	private boolean savingFlag;

	public SaveWeight(Context context, SharedPreferences sp) {
		this.context = context;
		this.sp = sp;
		vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		mShakeListener = new ShakeListener(context);
		vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
	}
	public void start(){
		mShakeListener.start();
        mShakeListener.setOnShakeListener(new OnShakeListener(){
			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				if(!savingFlag){
					SaveWeightThread sWThread = new SaveWeightThread();
					sWThread.start();
				}
			}      	
        });
	}
	public void stop(){
		mShakeListener.stop();
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
			System.out.println("保存体重成功");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	class SaveWeightThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			savingFlag = true;
			saveWeight();
			vibrator.vibrate(500);
			mShakeListener.stop();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mShakeListener.start();
			savingFlag = false;
		}
		
	}
}
