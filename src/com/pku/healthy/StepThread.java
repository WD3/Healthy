package com.pku.healthy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.zip.CRC32;

public class StepThread extends Thread {
	private int steps;
	private String deviceStr;
	
	public StepThread(int stepnum, String device) {
		steps = stepnum;
		deviceStr = device;
	}
	
	public void run() {
		byte[] request = new byte[24];
		request[0] = 0x13;
		request[1] = 0x22;
		request[3] = 0x18;
		request[15] = 0x04;
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
			int readCount = 0; // �Ѿ��ɹ���ȡ���ֽڵĸ���
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] stepData = new byte[24];
		stepData[0] = 0x14;
		stepData[1] = 0x44;
		stepData[3] = 0x18;
		stepData[12] = authCode[0];
		stepData[13] = authCode[1];
		stepData[20] = (byte)(steps>>>24);
		stepData[21] = (byte)((steps>>>16)&0xff);
		stepData[22] = (byte)((steps>>>8)&0xff);
		stepData[23] = (byte)(steps&0xff);
		CRC32 crc = new CRC32();
		crc.update(stepData);
		long crclong = crc.getValue();
		short crcshort = (short) (crclong&0xffff);
		stepData[8] = (byte)(crcshort>>>8);
		stepData[9] = (byte)(crcshort&0xff);
		try {
			socket = new Socket(newIP,newPort);
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(stepData);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int byte4toInt(byte[] buf){

		int firstByte = (0x000000FF & ((int) buf[0]));
		int secondByte = (0x000000FF & ((int) buf[1]));
		int thirdByte = (0x000000FF & ((int) buf[2]));
		int fourthByte = (0x000000FF & ((int) buf[3]));
	
		return ( firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte ) & 0xFFFFFFFF; 
	}
	
}
