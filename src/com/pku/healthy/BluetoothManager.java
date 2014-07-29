package com.pku.healthy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;

public class BluetoothManager {
	public BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
	private final int DISCOVERY_TIME = 10000;
	private final int CONNTECT_INTERVAL = 500;
	private BroadcastReceiver mReceiver;
	private BluetoothDevice btdevice;
	private BluetoothSocket socket;
	private Context context;
	private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private OutputStream os;
	private InputStream is;
	private boolean exit = true;
	private boolean isConnected;
	static String find_address;


	public BluetoothManager(Context context) {
		this.context = context;
	}

	public void cancle() {
		context.unregisterReceiver(mReceiver);
	}

	public void broadcast() {
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					System.out.println(device.getName() + device.getAddress());
					if (device.getName() == null || device.getName().equals("HeHu080")) {
						if (device.getAddress().substring(0, 5).equals("00:0E")) {
							addDevice(device);
							Log.e("pService", "响应广播");
						}
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter);
	}

	public void addDevice(BluetoothDevice device) {
		String name = device.getName();
		String address = device.getAddress();

		Log.e("写入内部hash", name + address);
		find_address = address;
		PlayService.sp.edit().putString("find_address", find_address).commit();
		// hashmap.put(address, name);
	}

	public void discovery() {
		if (!adapter.isDiscovering()) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (find_address == "") {
						Log.e("正在搜索", "正在搜索");
//						MainActivity.mSettings.edit()
//								.putBoolean("isDiscovering", true).commit();
						if (!adapter.isEnabled())
							adapter.enable();
						while (true) {
							if (adapter.isEnabled()) {
								adapter.startDiscovery();
								break;
							}
						}
						// MainActivity.SendMessage(MainActivity.handler,2);
						try {
							Thread.sleep(DISCOVERY_TIME);
							if (adapter.isDiscovering())
								adapter.cancelDiscovery();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			thread.start();
		}

	}

	public void connect() throws IOException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				while (exit) {
					if (find_address != "") {
						if (isConnected == false) {
							if (!adapter.isEnabled())
								adapter.enable();
							btdevice = adapter.getRemoteDevice(find_address);
							try {
								Method m = btdevice.getClass().getMethod(
										"createRfcommSocket",
										new Class[] { int.class });
								try {
									socket = (BluetoothSocket) m.invoke(
											btdevice, 1);
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} catch (NoSuchMethodException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							try {
								Log.e("开始连接", "开始连接");
								if (adapter.isDiscovering())
									adapter.cancelDiscovery();
								socket.connect();
								isConnected = true;
								Log.e("连接", "连接完成");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Log.e("异常", e.getMessage());
								try {
									socket.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						} else {
							sendData();
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							Thread.sleep(CONNTECT_INTERVAL);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				Looper.loop();
			}
		});
		thread.start();
	}

	public void sendData() {
		String reply;
		try {
			os = socket.getOutputStream();
			is = socket.getInputStream();
			InputStreamReader isreader = new InputStreamReader(is);
			BufferedReader breader = new BufferedReader(isreader);
			os.write(("ConnectionOK\r\n").getBytes());
			if (breader.readLine().equals("ConnectionOK\r\n"))
				os.write(("SCA,1.0,Command,GetWeight\r\n").getBytes());
			reply = breader.readLine();
			if (reply.contains("SCA,1.0,Data,Weight")) {
				String[] array = reply.split(":");
				String weight = array[1];
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
