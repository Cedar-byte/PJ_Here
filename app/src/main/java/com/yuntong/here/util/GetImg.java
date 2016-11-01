package com.yuntong.here.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 获取照片
 * 
 * @author Michael.Zhang 2013-07-05 16:45:21
 */
public class GetImg {

	public static final int go_to_camera_code = 1;
	public static final int go_to_cutimg_code = 2;
	public static final int go_to_gallery_code = 3;
	public static final int add_to_gallery_code = 4;
	public static final int add_to_camera_code = 5;

	private Activity mActivity = null;
	public AsyncTask<Intent, String, String> saveImgTask = null;

	/* 拍照所得相片路径 */
	public File file_save = null;
	/* 裁切照片存储路径 */
	public File file_cut = null;

	public File file_path = null;

	public GetImg(Activity activity, File fileSave, File fileCut) {
		mActivity = activity;

		file_save = fileSave;
		file_cut = fileCut;

		if (null == file_save) {
			file_save = setDefaultFile("img.png");
		} else {
			file_save = new File(file_save.getPath() + "/img.png");
		}
		if (null == file_cut) {
			file_cut = setDefaultFile("cut_img.png");
		} else {
			file_cut = new File(fileCut.getPath() + "/cut_img.png");
		}
	}

	public GetImg(Activity activity) {
		this(activity, null, null);
	}

	public File setDefaultFile(String name) {
		if (!TextUtils.isEmpty(name) && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return new File(Environment.getExternalStorageDirectory(), name);
		}
		return null;
	}

	/**
	 * 重置裁切图片存储位置
	 * 
	 * @author windy 2014年6月27日 上午11:43:36
	 * @param file
	 */
	public void resetCutFile(File file) {
		file_cut = file;
	}

	/**
	 * 重置裁切图片存储位置
	 * 
	 * @author windy 2014年6月27日 上午11:44:33
	 * @param name
	 */
	public void resetCutFile(String name) {
		file_cut = setDefaultFile(name);
	}

	/**
	 * 相册
	 * 
	 * @author Michael.Zhang 2013-06-20 17:06:04
	 */
	public void goToGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		mActivity.startActivityForResult(intent, go_to_gallery_code);
	}

	/**
	 * 相机
	 * 
	 * @author Michael.Zhang 2013-06-20 16:54:47
	 */
	public void goToCamera() {
		if (null != file_save) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_save));
			mActivity.startActivityForResult(intent, go_to_camera_code);
		}
	}

	/**
	 * 裁切图片，
	 * 
	 * @param path
	 *            裁切图片途径
	 * 
	 * @author TangWei 2013-12-23下午5:11:46
	 */
	public void gotoCutImage(String path) {
		gotoCutImage(path, 0, null, 0, 0, 0, 0);
	}

	/**
	 * 裁切图片，跳转到CropImageActivity
	 * 
	 * @author windy 2014年6月27日 上午11:01:47
	 * @param path
	 *            裁切图片途径
	 * @param gridView
	 *            图片列表
	 */
	public void gotoCutImage(String path, View gridView) {

		int aspectX = 0, aspectY = 0, outputX = 0, outputY = 0;
		if (null != gridView) {
			aspectX = gridView.getWidth();
			aspectY = gridView.getHeight();
			outputX = gridView.getWidth();
			outputY = gridView.getHeight();
		}

		gotoCutImage(path, 0, null, aspectX, aspectY, outputX, outputY);
	}

	/**
	 * 裁切图片，跳转到CropImageActivity，path/resouceId/bitmap 任选一
	 * 
	 * @author windy 2014年6月27日 上午11:17:15
	 * @param path
	 *            图片对应的本地路径
	 * @param resouceId
	 *            图片资源ID
	 * @param bitmap
	 *            图片Bitmap
	 * @param aspectX
	 *            裁切框比例，宽
	 * @param aspectY
	 *            裁切框比例，高
	 * @param outputX
	 *            裁切图片保存像素，宽
	 * @param outputY
	 *            裁切图片保存像素，高
	 */
	public void gotoCutImage(String path, int resouceId, Bitmap bitmap, int aspectX, int aspectY, int outputX, int outputY) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		Uri uri = null;
		if (!TextUtils.isEmpty(path)) {
			uri = Uri.fromFile(new File(path));
		}
		if (resouceId > 0) {
			Bitmap mBitmap = BitmapFactory.decodeResource(mActivity.getResources(), resouceId);
			uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), mBitmap, null, null));
		}
		if (null != bitmap) {
			uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, null, null));
		}
		if (aspectX > 0) {
			intent.putExtra("aspectX", aspectX);
		}
		if (aspectY > 0) {
			intent.putExtra("aspectY", aspectY);
		}
		if (outputX > 0) {
			intent.putExtra("outputX", outputX);
		}
		if (outputY > 0) {
			intent.putExtra("outputY", outputY);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("return-data", true);
		mActivity.startActivityForResult(intent, go_to_cutimg_code);
	}

	/**
	 * 获取 从相册选择的照片路径
	 * 
	 * @author Michael.Zhang 2013-07-05 23:30:56
	 * @param data
	 */
	@SuppressWarnings("deprecation")
	public String getGalleryPath(Intent data) {
		Uri mImageCaptureUri = data.getData();
		if (mImageCaptureUri != null) {
			String[] proj = { MediaStore.Images.Media.DATA };
			// 好像是android多媒体数据库的封装接口，具体的看Android文档
			Cursor cursor = mActivity.managedQuery(mImageCaptureUri, proj, null, null, null);
			// 按我个人理解 这个是获得用户选择的图片的索引值
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			// 将光标移至开头 ，这个很重要，不小心很容易引起越界
			cursor.moveToFirst();
			// 最后根据索引值获取图片路径www.2cto.com
			return cursor.getString(column_index);
		} else {
			return "";
		}
	}

	/**
	 * 保存裁切后的图片
	 */
	public void saveCutImg(final Intent data) {
		if (null != file_cut) {
			saveImgTask = new AsyncTask<Intent, String, String>() {
				@Override
				protected String doInBackground(Intent... params) {
					// if (params.length > 0) {
					try {
						Bitmap photo = BitmapFactory.decodeFile(file_cut.getAbsolutePath());
						FileOutputStream out = new FileOutputStream(file_cut);
						photo.compress(Bitmap.CompressFormat.JPEG, 35, out);
						return file_cut.getAbsolutePath();
					} catch (Exception e) {
						e.printStackTrace();
					}
					// }
					return null;
				}
			};
			saveImgTask.execute(data);
		}
	}

	public File getFile_path() {
		return file_path;
	}

	public void setFile_path(File file_path) {
		this.file_path = file_path;
	}
}