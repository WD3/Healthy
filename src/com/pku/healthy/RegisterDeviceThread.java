package com.pku.healthy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class RegisterDeviceThread extends Thread{
	private Map<String, String> params;
	
	public RegisterDeviceThread(Map<String, String> params){
		this.params = params;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String path = "http://162.105.76.252:8081/api/insertDevice";
		String enc = "UTF-8";
		
		try {
			if(HttpRequest.sendPostRequest(path, params, enc)){
				System.out.println("POST设备成功");
				JSONObject jsonObj = new JSONObject(HttpRequest.reply);
				int deviceSNFlag = jsonObj.getInt("deviceSN");
				int user_existFlag = jsonObj.getInt("user_exist");
				if(deviceSNFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 7);
				else if(user_existFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 8);				
				else if(deviceSNFlag == 1&&user_existFlag==1)
					RegisterActivity.SendMessage(RegisterActivity.handler, 9);
			}else RegisterActivity.SendMessage(RegisterActivity.handler, 6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


