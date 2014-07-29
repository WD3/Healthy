package com.pku.healthy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;

public class CheckVersion extends Thread {
	private Context context;
	private int localVersion = 0;// 本地安装版本
	private int serverVersion = 0;// 服务器版本
	private String verurl = "http://162.105.76.252:8080/apps/version_healthy.txt";		
	private String strResult = "";
	
	public CheckVersion(Context context){
		this.context = context;
	}
	
	public void run(){
		
		Looper.prepare(); 
		
		try {
//			System.out.println("检测更新");
			PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}				

		HttpPost   httpRequest =new HttpPost(verurl);
		List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		params.add(new BasicNameValuePair("str", "I am Post String"));  

		try 
		{ 			
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode() == 200)  
			{ 
				strResult = EntityUtils.toString(httpResponse.getEntity()); 
			} 
			else	  
			{
			}
		}
		catch (ClientProtocolException e) 
		{  
			e.printStackTrace(); 
		} 
		catch (IOException e) 
		{  
			e.printStackTrace(); 
		} 
		catch (Exception e) 
		{  
			e.printStackTrace();  
		}
		
		try { 		
			JSONArray array = new JSONArray(strResult);
			JSONObject jsonObject = array.getJSONObject(0);
			serverVersion = jsonObject.getInt("vercode");  
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if (localVersion < serverVersion) {
			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setTitle("软件升级")
					.setMessage("发现新版本,建议立即更新使用.")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent updateIntent = new Intent(context, UpdateService.class);
									updateIntent.putExtra("app_name", context.getResources().getString(R.string.app_name));	
									context.startService(updateIntent);
								}
							})
					.setNegativeButton("以后再说",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
			alert.create().show();
		}
		Looper.loop();
	}

}
