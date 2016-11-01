package com.yuntong.here.activity;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.adapter.ImgAdapter;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.view.GalleryFlow;
import com.yuntong.here.view.ICallback;

import java.util.ArrayList;


public class ImageShower extends BaseActivity {
	private ImgAdapter imgAdapter;
	private ArrayList<String> imgData = new ArrayList<String>(); // 滚动数据
	private GalleryFlow galleryFlow; // 横向滚动图片
	private boolean isTouch = false; // 是否触摸滚动图片
	private long perWaitTime = 150000; // 每次等待时间
	private boolean isCanwork = true;
	private int i ;
	private ImageView simple_image1;
	private TextView text_imgshower;
	Handler autoGalleryHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			super.handleMessage(message);
			if (message.what == 0) {
				galleryFlow.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
			} else {
			}
		}
	};


	@Override
	protected int getLayoutId() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		return R.layout.imageshower;
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		imgData=getIntent().getStringArrayListExtra("imgData");
		imgAdapter=new ImgAdapter(this,imgData);
		galleryFlow = (GalleryFlow)findViewById(R.id.galleryFlow1);
		text_imgshower=(TextView)findViewById(R.id.text_imgshower);
		text_imgshower.setText(getIntent().getStringExtra("i")+"/"+imgData.size());
		i=Integer.valueOf(getIntent().getStringExtra("i"));
		simple_image1=(ImageView)findViewById(R.id.simple_image1);
		if (imgData.size()==1){
			galleryFlow.setVisibility(View.GONE);
			simple_image1.setVisibility(View.VISIBLE);
			ImageLoaderUtil.disPlayBig(imgData.get(0),simple_image1);
		}else {
			galleryFlow.setVisibility(View.VISIBLE);
			simple_image1.setVisibility(View.GONE);
			getHeadView();
		}
		simple_image1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


	}

	@Override
	protected void bindView() {

	}

	@Override
	protected void widgetClick(View v) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	protected void getHeadView() {
		imgAdapter = new ImgAdapter(ImageShower.this,imgData);
		new Thread() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage(0, new ICallback() {
					@Override
					public void run() {
						if (imgData == null || imgData.size() == 0) {
							return;
						}
						galleryFlow.setAdapter(imgAdapter);
						galleryFlow.setSelection(imgData.size() * 100);
						galleryFlow.setOnItemClickListener(onItemClickListener);
						galleryFlow.setOnItemSelectedListener(imgOnItemSelectedListener);
						galleryFlow.setSelection(i-1);
						new Thread() {
							@Override
							public void run() {
								while (isCanwork) {
									try {
										Thread.sleep(perWaitTime);
										if (!isTouch) {
											autoGalleryHandler.sendEmptyMessage(0);
										}
									} catch (Exception e) {
									}
								}
							}
						}.start();
					}
				});
				handler.sendMessage(msg);
			}
		}.start();

		galleryFlow.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
					isTouch = true;
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
					isTouch = false;
				}
				return false;
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ICallback callback = (ICallback) msg.obj;
			if (callback != null) {
				callback.run();
			}
		}
	};

	protected AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (imgData.size() > 0) {
              finish();
			}
		}
	};

	protected AdapterView.OnItemSelectedListener imgOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			i=position%imgData.size()+1;
			text_imgshower.setText(i+"/"+imgData.size());

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

}
