package com.pku.healthy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterUtils implements OnKeyListener{
	private Context context;
	private LinearLayout layout;
	private SharedPreferences regisgerSp;
	private EditText et_userName;
	private EditText et_password;
	private EditText et_repeatPw;
	private EditText et_email;
	private EditText et_tel;
	private RadioButton rb_male;
	private RadioButton rb_female;
	static String userName;
	static String password;
	static String email;
	static String telNumber;
	static String repeatPw;
	static String birthDate;
	static String imsi;
	static String gender;

	public RegisterUtils(Context context, LinearLayout layout,
			SharedPreferences regisgerSp) {
		this.context = context;
		this.layout = layout;
		this.regisgerSp = regisgerSp;
		et_userName = (EditText) layout.findViewById(R.id.userName);
		et_password = (EditText) layout.findViewById(R.id.password);
		et_repeatPw = (EditText) layout.findViewById(R.id.repeatPw);
		et_email = (EditText) layout.findViewById(R.id.email);
		et_tel = (EditText) layout.findViewById(R.id.tel);
		rb_male = (RadioButton) layout.findViewById(R.id.male);
		rb_female = (RadioButton) layout.findViewById(R.id.female);
		et_password.setOnKeyListener(this);
		et_repeatPw.setOnKeyListener(this);
	}

	private boolean existSpace(String str) {
		if (str.contains(" ")) {
			return true;
		}
		return false;
	}

	public boolean isLegal() {
		repeatPw = et_repeatPw.getText().toString();
		password = et_password.getText().toString();
		userName = et_userName.getText().toString();
		email = et_email.getText().toString();
		telNumber = et_tel.getText().toString();
		birthDate = RegisterActivity.tv_birthDate.getText().toString();
		imsi = MainActivity.imsi;
		if (rb_male.isChecked())
			gender = "m";
		else if (rb_female.isChecked())
			gender = "f";
		String counterId = MainActivity.counterId.getText().toString();
		String scaleId = MainActivity.scaleId.getText().toString();
		if (counterId.equals("") && scaleId.equals("")) {
			Toast.makeText(context, "对不起，您的设备号为空，请购买相关设备", Toast.LENGTH_LONG)
					.show();
			return false;
		} else {
			if (!repeatPw.equals(password)) {
				Toast.makeText(context, "对不起，您两次输入密码不一致，请重设密码",
						Toast.LENGTH_LONG).show();
				return false;
			} else {
				if (userName.equals("") || email.equals("")
						|| password.equals("")) {
					Toast.makeText(context, "对不起，您输入信息不完整，请重新输入",
							Toast.LENGTH_LONG).show();
					return false;
				} else if (!isEmail(email)) {
					Toast.makeText(context, "对不起，您输入的邮箱格式有误，请重新输入",
							Toast.LENGTH_LONG).show();
					return false;
				} else if (!telNumber.equals("") && !isTelNumber(telNumber)) {
					Toast.makeText(context, "对不起，您输入的电话格式有误，请重新输入",
							Toast.LENGTH_LONG).show();
					return false;
				}
			}

		}

		return true;
	}

	private boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isTelNumber(String telNumber) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(telNumber);
		return m.matches();
	}

	public void save() {
		regisgerSp
				.edit()
				.putString("userName", et_userName.getText().toString())
				.putString("password", et_password.getText().toString())
				.putString("repeatPw", et_repeatPw.getText().toString())
				.putString("email", et_email.getText().toString())
				.putString("telNumber", et_tel.getText().toString())
				.putBoolean("male", rb_male.isChecked())
				.putBoolean("female", rb_female.isChecked())
				.putString("birthday",
						RegisterActivity.tv_birthDate.getText().toString())
				.commit();
	}

	public void read() {
		et_userName.setText(regisgerSp.getString("userName", ""));
		et_password.setText(regisgerSp.getString("password", ""));
		et_repeatPw.setText(regisgerSp.getString("repeatPw", ""));
		et_email.setText(regisgerSp.getString("email", ""));
		et_tel.setText(regisgerSp.getString("telNumber", ""));
		rb_male.setChecked(regisgerSp.getBoolean("mail", true));
		rb_female.setChecked(regisgerSp.getBoolean("female", false));
		RegisterActivity.tv_birthDate.setText(regisgerSp.getString("birthday",
				"1990-1-1"));
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.password:
				if(existSpace(et_password.getText().toString())) 
					Toast.makeText(context, "对不起，密码不可以包含空格，请重新输入",Toast.LENGTH_LONG).show();
				break;
			case R.id.repeatPw:
				if(existSpace(et_repeatPw.getText().toString())) 
					Toast.makeText(context, "对不起，密码不可以包含空格，请重新输入",Toast.LENGTH_LONG).show();
				break;
		}				
		return false;
	}
}
