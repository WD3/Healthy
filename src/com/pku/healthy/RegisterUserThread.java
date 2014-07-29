package com.pku.healthy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pku.healthy.ShakeListener.OnShakeListener;

import android.content.Context;
import android.widget.Toast;

public class RegisterUserThread extends Thread{
	private Map<String, String> params;
	private OnSucRegisterListener onSucRegisterListener;
	
	public RegisterUserThread(Map<String, String> params){
		this.params = params;
	}
	public interface OnSucRegisterListener {
		public void onSucRegister();
	}
	public void setOnSucRegisterListener(OnSucRegisterListener listener) {
		onSucRegisterListener = listener;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String path = "http://162.105.76.252:8081/api/insertUser";
		String enc = "UTF-8";
		
		try {
			if(HttpRequest.sendPostRequest(path, params, enc)){
				System.out.println("POST用户数据成功");
				JSONObject jsonObj = new JSONObject(HttpRequest.reply);
				int userNameFlag = jsonObj.getInt("userName");
				int mailboxFlag = jsonObj.getInt("mailbox");
				int mobileFlag = jsonObj.getInt("mobile");
				int IMSIFlag = jsonObj.getInt("IMSI");
				if(userNameFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 1);
				else if(mailboxFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 2);
				else if(mobileFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 3);
				else if(IMSIFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 4);
				else if(userNameFlag == 1&&mailboxFlag==1&&mobileFlag==1&&IMSIFlag==1){
					RegisterActivity.SendMessage(RegisterActivity.handler, 5);
					onSucRegisterListener.onSucRegister();
				}
			}else RegisterActivity.SendMessage(RegisterActivity.handler, 6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


