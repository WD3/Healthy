package com.pku.healthy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Layout;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterUtils {
	private Context context;
	private LinearLayout layout;
	private EditText et_userName;
	private EditText et_password;
	private EditText et_repeatPw;
	private EditText et_email;
	private EditText et_tel;

	public RegisterUtils(Context context, LinearLayout layout) {
		this.context = context;
		this.layout = layout;
		et_userName = (EditText) layout.findViewById(R.id.userName);
		et_password = (EditText) layout.findViewById(R.id.password);
		et_repeatPw = (EditText) layout.findViewById(R.id.repeatPw);
		et_email = (EditText) layout.findViewById(R.id.email);
		et_tel = (EditText)layout.findViewById(R.id.tel);
	}

	public boolean isLegal() {
		String repeatPw = et_repeatPw.getText().toString();
		String password = et_password.getText().toString();
		String userName = et_userName.getText().toString();
		String email = et_email.getText().toString();
		String telNumber = et_tel.getText().toString();
		if (!repeatPw.equals(password)) {
			Toast.makeText(context, "对不起，您两次输入密码不一致，请重设密码", Toast.LENGTH_LONG)
					.show();
			return false;
		} else {
			if (userName.equals("") || email.equals("") || password.equals("")) {
				Toast.makeText(context, "对不起，您输入信息不完整，请重新输入", Toast.LENGTH_LONG)
						.show();
				return false;
			} else if (!isEmail(email)) {
				Toast.makeText(context, "对不起，您输入的邮箱格式有误，请重新输入",
						Toast.LENGTH_LONG).show();
				return false;
			}
			else if(!telNumber.equals("")&&!isTelNumber(telNumber)){
				Toast.makeText(context, "对不起，您输入的电话格式有误，请重新输入",
						Toast.LENGTH_LONG).show();
				return false;
			}
		}
		return true;
	}

	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public boolean isTelNumber(String telNumber) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(telNumber);
		return m.matches();
	}
}
