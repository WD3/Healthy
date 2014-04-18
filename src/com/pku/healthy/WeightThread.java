package com.pku.healthy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.zip.CRC32;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeightThread extends Thread {
	private String weightStr;
	private String deviceStr;
	
	public WeightThread(String weightnum, String device) {
		weightStr = weightnum;
		deviceStr = device;
	}
	
	public void run() {
		byte[] request = new byte[24];
		request[0] = 0x13;
		request[1] = 0x22;
		request[3] = 0x18;
		request[15] = 0x03;
		int n = deviceStr.length();
		boolean low = true;
		for (int i = 0, j = 0; i < n; i++) {
			char c = deviceStr.charAt(n-1-i);
			if (c>='0'&&c<='9') {
				if (low) {
					request[23-j] = (byte)(c-'0'&0x0f);
					low = false;
				} else {
					request[23-j] |= (byte)(c-'0'<<4&0xf0);
					j++;
				}
			} else if (c>='A'&&c<='F') {
				if (low) {
					request[23-j] = (byte)(c-'A'+10&0x0f);
					low = false;
				} else {
					request[23-j] = (byte)(c-'A'+10<<4&0xf0);
					j++;
				}
			} else {
				return;
			}
		}
		CRC32 crc32 = new CRC32();
		crc32.update(request);
		long crcLong = crc32.getValue();
		short crcShort = (short) (crcLong&0xffff);
		request[8] = (byte)(crcShort>>>8);
		request[9] = (byte)(crcShort&0xff);
		String newIP = "";
		int newPort = 0;
		byte[] authCode = new byte[2];
		Socket socket;
		try {
			socket = new Socket("162.105.76.252", 2013);
			OutputStream os = socket.getOutputStream();
			os.write(request);
			InputStream is = socket.getInputStream();
			byte[] respond = new byte[24];
			int readCount = 0; // 已经成功读取的字节的个数
			while (readCount < respond.length) {
				 readCount += is.read(respond, readCount, respond.length - readCount);
			}
			byte[] port = new byte[4];
			port[0] = 0;
			port[1] = 0;
			port[2] = respond[14];
			port[3] = respond[15];
			newPort = byte4toInt(port);
			byte[] IP_byte1 = new byte[]{0, 0, 0, respond[16]};
			byte[] IP_byte2 = new byte[]{0, 0, 0, respond[17]};
			byte[] IP_byte3 = new byte[]{0, 0, 0, respond[18]};
			byte[] IP_byte4 = new byte[]{0, 0, 0, respond[19]};
			int IP1 = byte4toInt(IP_byte1);
			int IP2 = byte4toInt(IP_byte2);
			int IP3 = byte4toInt(IP_byte3);
			int IP4 = byte4toInt(IP_byte4);
			newIP = IP1 + "." + IP2 + "." + IP3 + "." + IP4;
			authCode[0] = respond[20];
			authCode[1] = respond[21];
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SaveWeight.saveLoseWeight();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SaveWeight.saveLoseWeight();
			return;
		}
		byte[] weightByte = weightStr.getBytes();
		int len = weightByte.length;
		byte[] weightData = new byte[20+len];
		weightData[0] = 0x14;
		weightData[1] = 0x43;
		weightData[3] = 0x18;
		weightData[12] = authCode[0];
		weightData[13] = authCode[1];
		for (int i = 0; i < len; i++) {
			weightData[20+i] = weightByte[i];
		}
		CRC32 crc = new CRC32();
		crc.update(weightData);
		long crclong = crc.getValue();
		short crcshort = (short) (crclong&0xffff);
		weightData[8] = (byte)(crcshort>>>8);
		weightData[9] = (byte)(crcshort&0xff);
		try {
			socket = new Socket(newIP,newPort);
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(weightData);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SaveWeight.saveLoseWeight();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SaveWeight.saveLoseWeight();
			return;
		}
		SaveWeight.weightString = null;
		MainActivity.sp.edit().putString("weight_lose", null).commit();
	}
	
	private int byte4toInt(byte[] buf){

		int firstByte = (0x000000FF & ((int) buf[0]));
		int secondByte = (0x000000FF & ((int) buf[1]));
		int thirdByte = (0x000000FF & ((int) buf[2]));
		int fourthByte = (0x000000FF & ((int) buf[3]));
	
		return ( firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte ) & 0xFFFFFFFF; 
	}		
}
