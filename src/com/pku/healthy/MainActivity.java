package com.pku.healthy;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TabActivity implements OnClickListener {
	static TabHost tabHost;
	private LinearLayout layout;
	private FrameLayout bmilayout;
	static TextView tvSteps;
	static TextView calorie;
	static TextView progress;
	static TextView distance;
	private TextView tv_tarSteps;
	private TextView tv_tarWeight;
	private TextView tv_BMI;
	static TextView counterId;
	static TextView scaleId;
	private EditText et_tarSteps;
	private EditText et_tarWeight;
	private EditText et_height;
//	static EditText counterId;
//	static EditText scaleId;
	private ImageButton bt_exit;
	private ImageButton bt_lock;
	private ImageView iv_mySwitch;
	private ImageView iv_register;
	static String tarSteps;
	static String height;
	static String tarWeight;
	static String newWeight = "0";
	private String BMI;
	private String imsi;
	private boolean hourSteps;
	private boolean daySteps;
	private boolean dayWeight;

	static SharedPreferences sp;
	private SensorManager sensorManager;

	private DayStepsHistory dayStepsHistory;
	private HourStepsHistory hourStepsHistory;
	private DayWeightHistory dayWeightHistory;
	private SetActivity setActivity;
	private SaveWeight saveWeight;
	private NotificationExtend notification;
	private LockScreen lockScreen;
	// private ShakeListenerUtils shakeListenerUtils;

	private boolean wheelScrolled = false; // Wheel scrolled flag

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 实现无标题栏（但有系统自带的任务栏）：
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 获取TabHost对象
		tabHost = getTabHost();
		// tabHost.setup();
		// 新建一个newTabSpec,设置标签和图标(setIndicator),设置内容(setContent)
		tabHost.addTab(tabHost.newTabSpec("count").setIndicator("",getResources().getDrawable(R.drawable.icon_count)).setContent(R.id.count));
		tabHost.addTab(tabHost.newTabSpec("weight").setIndicator("",getResources().getDrawable(R.drawable.icon_scale)).setContent(R.id.weight));
		tabHost.addTab(tabHost.newTabSpec("history").setIndicator("",getResources().getDrawable(R.drawable.icon_history)).setContent(R.id.history));
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("",getResources().getDrawable(R.drawable.icon_more)).setContent(R.id.more));

		// 设置当前现实哪一个标签
		tabHost.setCurrentTab(0); // 0为标签ID
		
		tvSteps = (TextView) findViewById(R.id.tv_steps);
		distance = (TextView) findViewById(R.id.tv_distance);
		calorie = (TextView) findViewById(R.id.tv_calorie);
		progress = (TextView) findViewById(R.id.tv_progress);
		counterId = (TextView) findViewById(R.id.counterId);
		scaleId = (TextView) findViewById(R.id.scaleId);
		layout = (LinearLayout) findViewById(R.id.chart);
		bmilayout = (FrameLayout) findViewById(R.id.bmilayout);
		et_tarSteps = (EditText) findViewById(R.id.et_tarsteps);
		et_tarWeight = (EditText) findViewById(R.id.et_tarweight);
		et_height = (EditText) findViewById(R.id.et_height);
//		counterId = (EditText) findViewById(R.id.counterId);
//		scaleId = (EditText) findViewById(R.id.scaleId);
		tv_tarSteps = (TextView) findViewById(R.id.tv_tarsteps);
		tv_tarWeight = (TextView) findViewById(R.id.tv_tarWeight);
		tv_BMI = (TextView) findViewById(R.id.tv_bmi);
		iv_mySwitch = (ImageView) findViewById(R.id.myswitch);
		iv_mySwitch.setOnClickListener(this);		
		iv_register = (ImageView) findViewById(R.id.register);
		iv_register.setOnClickListener(this);
		bt_exit = (ImageButton) findViewById(R.id.exit);
		bt_exit.setOnClickListener(this);
		bt_lock = (ImageButton) findViewById(R.id.lock);
		bt_lock.setOnClickListener(this);

		sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		imsi = mTelephonyMgr.getSubscriberId();
//		imsi = "460010604602195";

		dayStepsHistory = new DayStepsHistory(layout, sp, this);
		hourStepsHistory = new HourStepsHistory(layout, sp, this);
		dayWeightHistory = new DayWeightHistory(layout, sp, this);
		setActivity = new SetActivity(sp, et_tarWeight, et_tarSteps, et_height);
		saveWeight = new SaveWeight(this, sp);
		notification = new NotificationExtend(this);
		lockScreen = new LockScreen(this);
		// shakeListenerUtils = new ShakeListenerUtils(this,weightSp);

		// 标签切换处理，用setOnTabChangedListener
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				if (tabId.equals("weight")) {
					setActivity.save();
					newWeight = sp.getString("体重", "000.0");
					int a = Integer.parseInt("" + newWeight.charAt(0));
					int b = Integer.parseInt("" + newWeight.charAt(1));
					int c = Integer.parseInt("" + newWeight.charAt(2));
					int d = Integer.parseInt("" + newWeight.charAt(4));
					initWheel(R.id.passw_1, a);
					initWheel(R.id.passw_2, b);
					initWheel(R.id.passw_3, c);
					initWheel(R.id.passw_4, d);
					tv_tarWeight.setText(et_tarWeight.getText().toString());
					BMI = sp.getString("BMI", "0");
					tv_BMI.setText(BMI);
					double bmi = Double.parseDouble(BMI);
					displayBMI(bmi);
					saveWeight.start();
					// sensorManager.registerListener(shakeListenerUtils,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
				} else if (tabId.equals("history")) {
					setActivity.save();
					dayStepsHistory.init();
					hourSteps = true;
					saveWeight.stop();
					// sensorManager.unregisterListener(shakeListenerUtils);
				} else if (tabId.equals("count")) {
					setActivity.save();
					tv_tarSteps.setText("目标：" + et_tarSteps.getText() + "步");
					saveWeight.stop();
					// sensorManager.unregisterListener(shakeListenerUtils);
				} else if (tabId.equals("more")) {
					setActivity.read();
					saveWeight.stop();
					counterId.setText(imsi.replaceFirst("460", "0000"));
					scaleId.setText(imsi.replaceFirst("460", "0100"));
//					counterId.setText(text);
					// sensorManager.unregisterListener(shakeListenerUtils);
				}
			}
		});

		this.startService(new Intent(this, PlayService.class));
		setActivity.read();
		tvSteps.setText(StepCounter.tvsteps);
		distance.setText(StepCounter.distance);
		calorie.setText(StepCounter.calorie);
		progress.setText(StepCounter.progress);
		tv_tarSteps.setText("目标：" + et_tarSteps.getText() + "步");
		tarSteps = "" + et_tarSteps.getText();
		height = "" + et_height.getText();
		tarWeight = "" + et_tarWeight.getText();
	}

	
	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			int a = getWheel(R.id.passw_1).getCurrentItem();
			int b = getWheel(R.id.passw_2).getCurrentItem();
			int c = getWheel(R.id.passw_3).getCurrentItem();
			int d = getWheel(R.id.passw_4).getCurrentItem();
			newWeight = "" + a + b + c + "." + d;
			sp.edit().putString("体重", newWeight).commit();
			double height = Double.parseDouble(et_height.getText().toString());
			double bmi = Double.parseDouble(newWeight)
					/ (height * height / 10000);
			BMI = String.format("%.2f", bmi);
			sp.edit().putString("BMI", BMI).commit();
			tv_BMI.setText(BMI);
			displayBMI(bmi);			
		}
	};
	private void displayBMI(double bmi){		
		if (bmi < 18.5 || bmi > 32)
			bmilayout.setBackgroundColor(Color.parseColor("#FF0000"));
		else if (bmi >= 18.5 && bmi <= 24.99)
			bmilayout.setBackgroundColor(Color.parseColor("#CCFF33"));
		else if (bmi > 25 && bmi <= 28)
			bmilayout.setBackgroundColor(Color.parseColor("#FFFF66"));
		else if (bmi > 28 && bmi <= 32)
			bmilayout.setBackgroundColor(Color.parseColor("#FF9900"));
		if (bmi >= 20 && bmi <= 25)
			bmilayout.setBackgroundColor(Color.parseColor("#00CC00"));
	}

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
			}
		}
	};

	/**
	 * Initializes wheel
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	private void initWheel(int id, int num) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
		wheel.setCurrentItem(num);
		// wheel.setCurrentItem((int)(Math.random() * 10));

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	public void onResume() {
		super.onResume();
		lockScreen.acquireWakeLock();
		if(tabHost.getCurrentTab() == 1)
			saveWeight.start();
	}

	public void onPause() {
		super.onPause();
		notification.show();
		saveWeight.stop();
		lockScreen.releaseWakeLock();
	}

	public void onDestroy() {
		super.onDestroy();
		setActivity.save();
	}

	public static void SendMessage(Handler handler, int i) {
		Message msg = handler.obtainMessage();
		msg.what = i;
		handler.sendMessage(msg);
	}

	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				tvSteps.setText(StepCounter.tvsteps);
				distance.setText(StepCounter.distance);
				calorie.setText(StepCounter.calorie);
				progress.setText(StepCounter.progress);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.exit:
			MainActivity.this.stopService(new Intent(MainActivity.this,
					PlayService.class));
			break;
		case R.id.lock:
			Intent i = new Intent(MainActivity.this, LockScreenActivity.class);
			MainActivity.this.startActivity(i);
			break;
		case R.id.register:
			Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
			MainActivity.this.startActivity(intent);
		case R.id.myswitch:
			if (hourSteps) {
				hourSteps = false;
				daySteps = false;
				hourStepsHistory.init();
				dayWeight = true;
			} else if (dayWeight) {
				dayWeight = false;
				hourSteps = false;
				dayWeightHistory.init();
				daySteps = true;
			} else if (daySteps) {
				daySteps = false;
				dayWeight = false;
				dayStepsHistory.init();
				hourSteps = true;
			}
			break;
		}
	}

}