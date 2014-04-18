package com.pku.healthy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

/**
 * Notification��չ��
 * @Description: Notification��չ��

 * @File: NotificationExtend.java

 * @Package com.test.background

 * @Author Hanyonglu

 * @Date 2012-4-13 ����02:00:44

 * @Version V1.0
 */
public class NotificationExtend {
	private Context context;
	
	public NotificationExtend(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	// ��ʾNotification
	public void show() {
        // ����һ��NotificationManager������
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);        
        // ����Notification�ĸ�������
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "Healthy";

        // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
        notification.flags |= Notification.FLAG_NO_CLEAR; 
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.DEFAULT_VIBRATE;
                
        // ����֪ͨ���¼���Ϣ
        CharSequence contentTitle = "Healthy"; // ֪ͨ������
        CharSequence contentText ="��ǰ����Ϊ��"+ StepCounter.tvsteps; // ֪ͨ������
        
        Intent notificationIntent = new Intent(context,MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        // ��Notification���ݸ�NotificationManager
        notificationManager.notify(0, notification);
    }
	
	
	// ȡ��֪ͨ
	public void cancel(){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		System.out.println("ȡ��֪ͨ");
	}
}
