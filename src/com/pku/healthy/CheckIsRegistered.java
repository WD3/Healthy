package com.pku.healthy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

public class CheckIsRegistered extends Thread {
	private Context context;
	private Map<String, String> params;

	public CheckIsRegistered(Context context, Map<String, String> params) {
		this.context = context;
		this.params = params;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		String path = "http://162.105.76.252:8081/api/getUserInfo";
		String enc = "UTF-8";

		try {
			JSONObject jsonObj = null;
			if (HttpRequest.sendPostRequest(path, params, enc)) {
				System.out.println("检测是否注册");
				if(HttpRequest.reply.contains("user_exist")){
					jsonObj = new JSONObject(HttpRequest.reply);
					int user_existFlag = jsonObj.getInt("user_exist");
					if(user_existFlag == 0)
						onCreateDialog();
				}else {
					jsonObj = new JSONObject(HttpRequest.reply);
					RegisterUtils.userName = jsonObj.getString("userName");
					RegisterUtils.password = jsonObj.getString("password");
					RegisterUtils.email = jsonObj.getString("mailbox");
					RegisterUtils.telNumber = jsonObj.getString("mobile");
					RegisterUtils.imsi = jsonObj.getString("IMSI");
					RegisterUtils.birthDate = jsonObj.getString("birthDate");
					RegisterUtils.gender = jsonObj.getString("gender");
					PlayService.sp.edit()
							.putString("userName", RegisterUtils.userName)
							.putString("password", RegisterUtils.password)
							.putString("repeatPw", RegisterUtils.password)
							.putString("email", RegisterUtils.email)
							.putString("telNumber", RegisterUtils.telNumber)
							.putString("birthday", RegisterUtils.birthDate)
							.commit();
					if (RegisterUtils.gender.equals("m"))
						PlayService.sp.edit()
								.putBoolean("male", true)
								.putBoolean("female", false).commit();
					else
						PlayService.sp.edit()
								.putBoolean("male", false)
								.putBoolean("female", true).commit();
				}
			} else
				RegisterActivity.SendMessage(RegisterActivity.handler, 6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Looper.loop();
	}

	protected void onCreateDialog() {
		new AlertDialog.Builder(context)
				.setTitle("提示")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("检测到您尚未注册，是否马上注册")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,RegisterActivity.class);
						context.startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).setCancelable(false).show();
	}

}
