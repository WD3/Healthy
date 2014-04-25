package com.pku.healthy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileToZip {
	public static boolean fileToZip(String sourceFilePath, String zipFilePath) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		if (sourceFile.exists() == false) {
			System.out.println(">>>>>> ��ѹ�����ļ�Ŀ¼��" + sourceFilePath
					+ " ������. <<<<<<");
		} else {
			try {
				File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println(">>>>>> ��ѹ�����ļ�Ŀ¼��" + sourceFilePath
							+ " ���治�����ļ�,����ѹ��. <<<<<<");
				} else {
					String fileName = sourceFiles[0].getName().replaceAll("txt", "zip");
					File zipFile = new File(zipFilePath + "/" + fileName);
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// ����ZIPʵ��,�����ӽ�ѹ����
						ZipEntry zipEntry = new ZipEntry(
								sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// ��ȡ��ѹ�����ļ���д��ѹ������
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
						try {
							if (null != bis)
								bis.close();								
						} catch (IOException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						sourceFiles[i].delete();
					}
					flag = true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// �ر���
				try {
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();					
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}
}