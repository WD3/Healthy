package com.pku.healthy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	private LinearLayout registerLayout;
	private Button bt_regisger;
//	private EditText et_userName;
//	private EditText et_password;
//	private EditText et_repeatPw;
//	private EditText et_email;
//	private EditText et_tel;
	private EditText et_age;
	
	private RegisterUtils registerUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		registerLayout = (LinearLayout)findViewById(R.id.registerLayout);
		bt_regisger = (Button)findViewById(R.id.register);
		bt_regisger.setOnClickListener(this);
//		et_userName = (EditText)findViewById(R.id.userName);
//		et_password = (EditText)findViewById(R.id.password);
//		et_repeatPw = (EditText)findViewById(R.id.repeatPw);
//		et_email = (EditText)findViewById(R.id.email);
//		et_tel = (EditText)findViewById(R.id.tel);
		et_age = (EditText)findViewById(R.id.age);
		registerUtils = new RegisterUtils(this,registerLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register:
			if(registerUtils.isLegal())
				System.out.println("输入成功");
			else System.out.println("输入失败");
			break;
		}
	}

}
