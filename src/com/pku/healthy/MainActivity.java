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
	static TextView tvSteps;
	static TextView calorie;
	static TextView progress;
	static TextView distance;
	private TextView tv_tarSteps;
	private EditText et_tarSteps;
	private EditText et_tarWeight;
	private EditText et_stepLength;
	private ImageButton bt_exit;
	private ImageView iv_mySwitch;
	static String tarSteps;
	static String stepLength;
	static String tarWeight;
	
	static SharedPreferences sp;	
	static SharedPreferences hourStepSp;
	private DayStepsHistory dayStepsHistory;
	private HourStepsHistory hourStepsHistory;
	private SetActivity setActivity;
	private boolean hourSteps;
	private boolean daySteps;
	
	
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
		et_tarSteps = (EditText)findViewById(R.id.et_tarsteps);
		et_tarWeight = (EditText)findViewById(R.id.et_tarweight);
		et_stepLength = (EditText)findViewById(R.id.et_steplength);
		tv_tarSteps = (TextView)findViewById(R.id.tv_tarsteps);
		iv_mySwitch = (ImageView)findViewById(R.id.myswitch);
		iv_mySwitch.setOnClickListener(this);
		bt_exit = (ImageButton)findViewById(R.id.exit);
		bt_exit.setOnClickListener(this);
		
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		hourStepSp = PreferenceManager.getDefaultSharedPreferences(this);
		dayStepsHistory = new DayStepsHistory(layout,sp,this);
		hourStepsHistory = new HourStepsHistory(layout,hourStepSp,this);
		setActivity = new SetActivity(sp,et_tarWeight,et_tarSteps,et_stepLength);
		
		
		//标签切换处理，用setOnTabChangedListener	
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			public void onTabChanged(String tabId){
				if(tabId.equals("weight")){
					setActivity.save();
					initWheel(R.id.passw_1);
				    initWheel(R.id.passw_2);
				    initWheel(R.id.passw_3);
				}else if(tabId.equals("history")){
					setActivity.save();
					dayStepsHistory.init();
					daySteps =  true;
				}else if(tabId.equals("count")){
					setActivity.save();
					tv_tarSteps.setText("目标："+et_tarSteps.getText()+"步");
				}else if(tabId.equals("more")){
					setActivity.read();
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
		stepLength = ""+et_stepLength.getText();
		tarWeight = ""+et_tarWeight.getText();
    }
 // Wheel scrolled flag
    private boolean wheelScrolled = false;
    
    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
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
				dayStepsHistory.init();
				daySteps = true;
			}else if(daySteps){
				daySteps = false;
				hourStepsHistory.init();
				hourSteps = true;
			}
			break;
		}
	}
	
}