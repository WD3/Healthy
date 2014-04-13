package com.pku.healthy;

import android.content.SharedPreferences;
import android.widget.EditText;

public class SetActivity {
	private SharedPreferences sp;
	private EditText et1;
	private EditText et2;
	private EditText et3;

	public SetActivity(SharedPreferences sp, EditText et1, EditText et2,
			EditText et3) {
		this.sp = sp;
		this.et1 = et1;
		this.et2 = et2;
		this.et3 = et3;
	}

	public void save() {
		sp.edit().putString("目标体重", et1.getText().toString())
				.putString("目标步数", et2.getText().toString())
				.putString("步距", et3.getText().toString()).commit();
	}

	public void read() {
		et1.setText(sp.getString("目标体重", "65"));
		et2.setText(sp.getString("目标步数", "5000"));
		et3.setText(sp.getString("步距", "60"));
	}
}
