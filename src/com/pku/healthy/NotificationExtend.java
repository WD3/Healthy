package com.pku.healthy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

/**
 * Notification扩展类
 * @Description: Notification扩展类

 * @File: NotificationExtend.java

 * @Package com.test.background

 * @Author Hanyonglu

 * @Date 2012-4-13 下午02:00:44

 * @Version V1.0
 */
public class NotificationExtend {
	private Context context;
	
	public NotificationExtend(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	// 显示Notification
	public void show() {
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);        
        // 定义Notification的各种属性
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "Healthy";

        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |= Notification.FLAG_NO_CLEAR; 
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.DEFAULT_VIBRATE;
                
        // 设置通知的事件消息
        CharSequence contentTitle = "Healthy"; // 通知栏标题
        CharSequence contentText ="当前步数为："+ StepCounter.tvsteps; // 通知栏内容
        
        Intent notificationIntent = new Intent(context,MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        // 把Notification传递给NotificationManager
        notificationManager.notify(0, notification);
    }
	
	
	// 取消通知
	public void cancel(){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		System.out.println("取消通知");
	}
}
