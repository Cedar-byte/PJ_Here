
package com.yuntong.here.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.yuntong.here.R;
import com.yuntong.here.util.FileUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateService extends Service {
	
	public static final String Install_Apk = "Install_Apk";
	private static final int down_step_custom = 2;
	private static final int TIMEOUT = 10 * 1000;
	private static String down_url;
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;
	private String app_name;
	private NotificationManager notificationManager;
	private Intent updateIntent;
	private PendingIntent pendingIntent;
	private RemoteViews contentView;
	private Context context;
	private Notification notification;

		
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		app_name = intent.getStringExtra("App_Name");
		down_url = intent.getStringExtra("url");
		
		FileUtil.createFile(app_name);
		
		if(FileUtil.isCreateFileSucess == true){			
			createNotification();
			createThread();
		}else{
			Toast.makeText(this, R.string.insert_card, Toast.LENGTH_SHORT).show();
			stopSelf();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}


	

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_OK:
				contentView.setTextViewText(R.id.notificationTitle, app_name + getString(R.string.down_sucess));
				contentView.setTextViewText(R.id.notificationPercent, "100%");
				notificationManager.notify(R.layout.notification_item, notification);
				notificationManager.cancelAll();
				installApk();
				android.os.Process.killProcess(android.os.Process.myPid());
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				stopSelf();
				break;
				
			case DOWN_ERROR:
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				contentView.setTextViewText(R.id.notificationTitle, app_name + getString(R.string.down_fail));
				stopSelf();
				break;
				
			default:
				break;
			}
		}
	};
	
	private void installApk() {
		// TODO Auto-generated method stub

		Uri uri = Uri.fromFile(FileUtil.updateFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri,"application/vnd.android.package-archive");
        UpdateService.this.startActivity(intent);
	}
	

	public void createThread() {
		new DownLoadThread().start();
	}

	
	private class DownLoadThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message();
			try {								
				long downloadSize = downloadUpdateFile(down_url,FileUtil.updateFile.toString());
				if (downloadSize > 0) {					
					// down success										
					message.what = DOWN_OK;
					handler.sendMessage(message);																		
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.what = DOWN_ERROR;
				handler.sendMessage(message);
			}						
		}		
	}
	



	public void createNotification() {
		
		notification = new Notification(
				R.mipmap.icon_logo,
				app_name + getString(R.string.is_downing),
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		contentView = new RemoteViews(getPackageName(),R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, app_name + getString(R.string.is_downing));
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
		notification.contentView = contentView;
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(R.layout.notification_item, notification);
	}


	public long downloadUpdateFile(String down_url, String file)throws Exception {
		
		int down_step = down_step_custom;
		int totalSize;
		int downloadCount = 0;
		int updateCount = 0;
		InputStream inputStream;
		OutputStream outputStream;
		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);

		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");

		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);
		
		byte buffer[] = new byte[1024];
		int readsize = 0;
		
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;

			if (updateCount == 0 || (downloadCount * 100 / totalSize-down_step) >= updateCount) {
				updateCount += down_step;
				contentView.setTextViewText(R.id.notificationPercent,updateCount + "%");
				contentView.setProgressBar(R.id.notificationProgress, 100,updateCount, false);			
				notification.contentView = contentView;
				notificationManager.notify(R.layout.notification_item, notification);			
			}
		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();
		
		return downloadCount;
	}

}