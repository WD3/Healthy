package com.pku.healthy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class RenewalUserThread extends Thread{
	private Map<String, String> params;
	
	public RenewalUserThread(Map<String, String> params){
		this.params = params;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String path = "http://162.105.76.252:8081/api/editUser";
		String enc = "UTF-8";
		
		try {
			if(HttpRequest.sendPostRequest(path, params, enc)){
				System.out.println("POST同步成功");
				JSONObject jsonObj = new JSONObject(HttpRequest.reply);
				int userNameFlag = jsonObj.getInt("userName");
				int mailboxFlag = jsonObj.getInt("mailbox");
				int mobileFlag = jsonObj.getInt("mobile");
				int IMSIFlag = jsonObj.getInt("IMSI");
				int passwordFlag = jsonObj.getInt("password");
				int oldmobileFlag = jsonObj.getInt("old_mobile_exist");
				if(userNameFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 12);
				else if(mailboxFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 13);
				else if(mobileFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 3);
				else if(IMSIFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 4);
				else if(passwordFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 10);
				else if(oldmobileFlag == 0)
					RegisterActivity.SendMessage(RegisterActivity.handler, 11);
				else if(userNameFlag == 1&&mailboxFlag==1&&mobileFlag==1&&IMSIFlag==1&&passwordFlag==1&&oldmobileFlag==1)
					RegisterActivity.SendMessage(RegisterActivity.handler, 14);
			}else RegisterActivity.SendMessage(RegisterActivity.handler, 6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


