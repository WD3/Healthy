package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	static String weightString;
	static JSONObject newValue;


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
					saveWeight();
					SaveWeightThread sWThread = new SaveWeightThread();
					sWThread.start();
					weightString = MainActivity.sp.getString("weight_lose", null);
					if(weightString == null) formatWeight();
					else saveLoseWeight();
					System.out.println("weightString"+weightString);
					WeightThread wThread = new WeightThread(weightString, "000000000000");
					wThread.start();
				}
			}      	
        });
	}
	public void stop(){
		mShakeListener.stop();
	}
	private void saveWeight(){
		newValue = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = format.format(System.currentTimeMillis());
		try {
			newValue.put("time", time);
			newValue.put("weight", String.valueOf(Double.parseDouble(MainActivity.newWeight)));			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	private void formatWeight(){
		JSONArray weightArray = new JSONArray();
		weightArray.put(newValue);
		weightString = weightArray.toString();						
	}
	public static void saveLoseWeight(){
		JSONArray weightJSONArray;
		weightString = MainActivity.sp.getString("weight_lose", "[]");
		try {
			weightJSONArray = new JSONArray(weightString);
			weightJSONArray.put(newValue);
			weightString = weightJSONArray.toString();
			MainActivity.sp.edit().putString("weight_lose", weightString)
					.commit();
			System.out.println("保存上传失败体重成功");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void saveWeightToHistory(){
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
			saveWeightToHistory();
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
