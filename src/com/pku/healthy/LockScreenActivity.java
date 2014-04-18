package com.pku.healthy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LockScreenActivity extends Activity implements OnClickListener{
	private LockScreen lockScreen;
	private Button unLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lockscreen);
		lockScreen = new LockScreen(this);
		lockScreen.acquireWakeLock();
		unLock = (Button)findViewById(R.id.unlock);
		unLock.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lock_screen, menu);
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) return true;					
		else if(keyCode == KeyEvent.KEYCODE_MENU) return true;					
		else return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.unlock:
			lockScreen.releaseWakeLock();
			LockScreenActivity.this.finish();
			MainActivity.tabHost.setCurrentTab(0);
//			Intent i = new Intent(LockScreenActivity.this,MainActivity.class);
//			LockScreenActivity.this.startActivity(i);
			break;
		}
	}		
}
