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

public class MainActivity extends TabActivity implements OnClickListener{
	private TabHost tabHost;
	private LinearLayout layout;
	private FrameLayout bmilayout;
	static TextView tvSteps;
	static TextView calorie;
	static TextView progress;
	static TextView distance;
	private TextView tv_tarSteps;
	private TextView tv_tarWeight;
	private TextView tv_BMI;
	private EditText et_tarSteps;
	private EditText et_tarWeight;
	private EditText et_height;
	private ImageButton bt_exit;
	private ImageView iv_mySwitch;
	static String tarSteps;
	static String height;
	static String tarWeight;
	static String newWeight = "0";
	private boolean hourSteps;
	private boolean daySteps;
	private boolean dayWeight;
	
	static SharedPreferences sp;	
	static SharedPreferences hourStepSp;
	private SharedPreferences weightSp;
	private SensorManager sensorManager;
	
	private DayStepsHistory dayStepsHistory;
	private HourStepsHistory hourStepsHistory;
	private DayWeightHistory dayWeightHistory;
	private SetActivity setActivity;
	private ShakeListener shakeListener;

	
    private boolean wheelScrolled = false;    // Wheel scrolled flag
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        实现无标题栏（但有系统自带的任务栏）： 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.activity_main);
        //获取TabHost对象
        tabHost = getTabHost();   
//        tabHost.setup();
		//新建一个newTabSpec,设置标签和图标(setIndicator),设置内容(setContent)
		tabHost.addTab(tabHost.newTabSpec("count").setIndicator("",getResources().getDrawable(R.drawable.icon_count)).setContent(R.id.count));
		tabHost.addTab(tabHost.newTabSpec("weight").setIndicator("",getResources().getDrawable(R.drawable.icon_scale)).setContent(R.id.weight));
		tabHost.addTab(tabHost.newTabSpec("history").setIndicator("",getResources().getDrawable(R.drawable.icon_history)).setContent(R.id.history));
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("",getResources().getDrawable(R.drawable.icon_more)).setContent(R.id.more));

		//设置当前现实哪一个标签
		tabHost.setCurrentTab(0);   //0为标签ID
		tvSteps = (TextView)findViewById(R.id.tv_steps);
		distance = (TextView)findViewById(R.id.tv_distance) ;
		calorie = (TextView)findViewById(R.id.tv_calorie) ;
		progress = (TextView)findViewById(R.id.tv_progress) ;
		layout = (LinearLayout)findViewById(R.id.chart);
		bmilayout = (FrameLayout)findViewById(R.id.bmilayout);
		et_tarSteps = (EditText)findViewById(R.id.et_tarsteps);
		et_tarWeight = (EditText)findViewById(R.id.et_tarweight);
		et_height = (EditText)findViewById(R.id.et_height);
		tv_tarSteps = (TextView)findViewById(R.id.tv_tarsteps);
		tv_tarWeight = (TextView)findViewById(R.id.tv_tarWeight);
		tv_BMI = (TextView)findViewById(R.id.tv_bmi);
		iv_mySwitch = (ImageView)findViewById(R.id.myswitch);
		iv_mySwitch.setOnClickListener(this);
		bt_exit = (ImageButton)findViewById(R.id.exit);
		bt_exit.setOnClickListener(this);
		
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		hourStepSp = PreferenceManager.getDefaultSharedPreferences(this);
		weightSp = PreferenceManager.getDefaultSharedPreferences(this);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		dayStepsHistory = new DayStepsHistory(layout,sp,this);
		hourStepsHistory = new HourStepsHistory(layout,hourStepSp,this);
		dayWeightHistory = new DayWeightHistory(layout,weightSp,this);
		setActivity = new SetActivity(sp,et_tarWeight,et_tarSteps,et_height);
		shakeListener = new ShakeListener(this,weightSp);
		
		//标签切换处理，用setOnTabChangedListener	
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			public void onTabChanged(String tabId){
				if(tabId.equals("weight")){
					setActivity.save();
					initWheel(R.id.passw_1);
				    initWheel(R.id.passw_2);
				    initWheel(R.id.passw_3);
				    initWheel(R.id.passw_4);
				    tv_tarWeight.setText(et_tarWeight.getText().toString());
				    sensorManager.registerListener(shakeListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
				}else if(tabId.equals("history")){
					setActivity.save();
					dayStepsHistory.init();
					hourSteps =  true;
					sensorManager.unregisterListener(shakeListener); 
				}else if(tabId.equals("count")){
					setActivity.save();
					tv_tarSteps.setText("目标："+et_tarSteps.getText()+"步");
					sensorManager.unregisterListener(shakeListener); 
				}else if(tabId.equals("more")){
					setActivity.read();
					sensorManager.unregisterListener(shakeListener); 
				}									
			}
		});
			
		this.startService(new Intent(this,PlayService.class));
		setActivity.read();
		tvSteps.setText(StepCounter.tvsteps);
		distance.setText(StepCounter.distance);
		calorie.setText(StepCounter.calorie);
		progress.setText(StepCounter.progress);
		tv_tarSteps.setText("目标："+et_tarSteps.getText()+"步");	
		tarSteps = ""+et_tarSteps.getText();
		height = ""+et_height.getText();
		tarWeight = ""+et_tarWeight.getText();
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
            newWeight = ""+a+b+c+"."+d;
            double height = Double.parseDouble(et_height.getText().toString());
            double bmi = Double.parseDouble(newWeight)/(height*height/10000);
            tv_BMI.setText(String.format("%.2f", bmi));
            if(bmi<18.5||bmi>32)
            	bmilayout.setBackgroundColor(Color.parseColor("#FF0000"));
            else if(bmi>=18.5&&bmi<=24.99)
            	bmilayout.setBackgroundColor(Color.parseColor("#CCFF33"));
            else if(bmi>=20&&bmi<=25)
            	bmilayout.setBackgroundColor(Color.parseColor("#66FF00"));
            else if(bmi>25&&bmi<=28)
            	bmilayout.setBackgroundColor(Color.parseColor("#FFFF66"));
            else if(bmi>28&&bmi<=32)
            	bmilayout.setBackgroundColor(Color.parseColor("#FF9900"));            
        }
    };
    
    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
            }
        }
    };
    /**
     * Initializes wheel
     * @param id the wheel widget Id
     */
    private void initWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        wheel.setCurrentItem(0);
//        wheel.setCurrentItem((int)(Math.random() * 10));
        
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }
    /**
     * Returns wheel by Id
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
    	return (WheelView) findViewById(id);
    }
    public void onPause(){
    	super.onPause();
    	sensorManager.unregisterListener(shakeListener); 
    }
    public void onDestroy(){
    	super.onDestroy();
    	setActivity.save();
    }
    public static void SendMessage(Handler handler, int i){
		Message msg = handler.obtainMessage();
		msg.what = i;
		handler.sendMessage(msg);
		}
	public static Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
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
		switch(v.getId()){
		case R.id.exit:
			MainActivity.this.stopService(new Intent(MainActivity.this, PlayService.class));
			break;		
		case R.id.myswitch:
			if(hourSteps){
				hourSteps = false;
				daySteps = false;
				hourStepsHistory.init();
				dayWeight = true;
			}else if(dayWeight){
				dayWeight = false;
				hourSteps = false;
				dayWeightHistory.init();
				daySteps = true;
			}else if(daySteps){
				daySteps = false;
				dayWeight = false;
				dayStepsHistory.init();
				hourSteps = true;
			}
			break;
		}
	}
	
}