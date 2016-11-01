/*************************************************************************************************
 * ��Ȩ���� (C)2012,  �����п��Ѽ��Źɷ����޹�˾ 
 * 
 * �ļ����ƣ�FileUtil.java
 * ����ժҪ���ļ�������
 * ��ǰ�汾��
 * ��         �ߣ� hexiaoming
 * ������ڣ�2012-12-26
 * �޸ļ�¼��
 * �޸����ڣ�
 * ��   ��  �ţ�
 * ��   ��  �ˣ�
 * �޸����ݣ�
 ************************************************************************************************/
package com.yuntong.here.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;


public class FileUtil {
	
	public static File updateDir = null;
	public static File updateFile = null;

	public static final String KonkaApplication = "konkaUpdateApplication";
	
	public static boolean isCreateFileSucess;

	public static void createFile(String app_name) {
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			isCreateFileSucess = true;
			
			updateDir = new File(Environment.getExternalStorageDirectory()+ "/" + KonkaApplication +"/");
			updateFile = new File(updateDir + "/" + app_name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					isCreateFileSucess = false;
					e.printStackTrace();
				}
			}

		}else{
			isCreateFileSucess = false;
		}
	}
}