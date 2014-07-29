package com.pku.healthy;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.pku.healthy.RegisterUserThread.OnSucRegisterListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	private LinearLayout registerLayout;
	private Button bt_regisger;
	private Button bt_renewal;
	static TextView tv_birthDate;
	private EditText et_oldTel;
	private String birthDate;
	private Calendar c = null;
	private final static int DATE_DIALOG = 0;
	private final static int TEL_DIALOG = 1;
	private static Context context;
	private RegisterUtils registerUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		registerLayout = (LinearLayout) findViewById(R.id.registerLayout);
		bt_regisger = (Button) findViewById(R.id.register);
		bt_regisger.setOnClickListener(this);
		bt_renewal = (Button) findViewById(R.id.renewal);
		bt_renewal.setOnClickListener(this);
		tv_birthDate = (TextView) findViewById(R.id.birthDate);
		tv_birthDate.setOnClickListener(this);
		registerUtils = new RegisterUtils(this, registerLayout, PlayService.sp);
		context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void onStart() {
		super.onStart();
		registerUtils.read();
	}

	public void onStop() {
		super.onStop();
		registerUtils.save();
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
				Toast.makeText(context, "���û�����ע�ᣬ����������", Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(context, "��������ע�ᣬ����������", Toast.LENGTH_LONG).show();
				break;
			case 3:
				Toast.makeText(context, "���ֻ�����ע�ᣬ�����ظ�ע��", Toast.LENGTH_LONG).show();
				break;
			case 4:
				Toast.makeText(context, "��IMSI����ע�ᣬ�����ظ�ע��", Toast.LENGTH_LONG).show();
				break;
			case 5:
				Toast.makeText(context, "ע��ɹ�", Toast.LENGTH_LONG).show();
				break;
			case 6:
				Toast.makeText(context, "�������ӳ�����������", Toast.LENGTH_LONG).show();
				break;
			case 7:
				Toast.makeText(context, "���豸����ע�ᣬ�����ظ�ע��", Toast.LENGTH_LONG).show();
				break;
			case 8:
				Toast.makeText(context, "�Բ��𣬸���Ϣ�Ѵ���", Toast.LENGTH_LONG).show();
				break;
			case 9:
				Toast.makeText(context, "�豸�󶨳ɹ�", Toast.LENGTH_LONG).show();
				break;
			case 10:
				Toast.makeText(context, "�Բ�����������������������������", Toast.LENGTH_LONG).show();
				break;
			case 11:
				Toast.makeText(context, "�Բ������ľ��ֻ���û���û���Ϣ", Toast.LENGTH_LONG).show();
				break;
			case 12:
				Toast.makeText(context, "�Բ��������û���������������������",Toast.LENGTH_LONG).show();
				break;
			case 13:
				Toast.makeText(context, "�Բ�����������������������������", Toast.LENGTH_LONG).show();
				break;
			case 14:
				Toast.makeText(context, "ͬ���ɹ�", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register:
			if (registerUtils.isLegal()) {
				Map<String, String> userParams = new HashMap<String, String>();
				userParams.put("userName", RegisterUtils.userName);
				userParams.put("password", RegisterUtils.password);
				userParams.put("mailbox", RegisterUtils.email);
				userParams.put("birthDate", RegisterUtils.birthDate);
				userParams.put("gender", RegisterUtils.gender);
				userParams.put("mobile", RegisterUtils.telNumber);
				userParams.put("IMSI", RegisterUtils.imsi);
				RegisterUserThread registerUserThread = new RegisterUserThread(userParams);
				registerUserThread.start();
				// �����¼�������ע���û��ɹ��󣬰��豸
				registerUserThread.setOnSucRegisterListener(new OnSucRegisterListener() {
					@Override
					public void onSucRegister() {
						// TODO Auto-generated method stub
						Map<String, String> device1Params = new HashMap<String, String>();
						device1Params.put("IMSI", RegisterUtils.imsi);
						device1Params.put("deviceSN",MainActivity.scaleId.getText().toString());
						device1Params.put("deviceType", "3");
						RegisterDeviceThread registerDevice1Thread = new RegisterDeviceThread(device1Params);
						registerDevice1Thread.start();

						Map<String, String> device2Params = new HashMap<String, String>();
						device2Params.put("IMSI", RegisterUtils.imsi);
						device2Params.put("deviceSN",MainActivity.counterId.getText().toString());
						device2Params.put("deviceType", "4");
						RegisterDeviceThread registerDevice2Thread = new RegisterDeviceThread(device2Params);
						registerDevice2Thread.start();
					}
				});
			}
			break;
		case R.id.birthDate:
			showDialog(DATE_DIALOG);
			break;
		case R.id.renewal:
			showDialog(TEL_DIALOG);
			break;

		}
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DATE_DIALOG:
			birthDate = tv_birthDate.getText().toString();
			String[] birthArray = birthDate.split("-");
			int year = Integer.parseInt(birthArray[0]);
			int month = Integer.parseInt(birthArray[1]);
			int day = Integer.parseInt(birthArray[2]);
			c = Calendar.getInstance();
			c.set(year, month - 1, day);
			dialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
					tv_birthDate.setText(year + "-" + (month + 1) + "-"+ dayOfMonth);
				}
			}, c.get(Calendar.YEAR), // �������
			c.get(Calendar.MONTH), // �����·�
			c.get(Calendar.DAY_OF_MONTH) // ��������						
			);
			break;
		case TEL_DIALOG:
			et_oldTel = new EditText(this);
			et_oldTel.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
			et_oldTel.setInputType(InputType.TYPE_CLASS_NUMBER);
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("����������ֻ���")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(et_oldTel)
			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int arg1) {
					// TODO Auto-generated method stub
					if (RegisterUtils.isTelNumber(et_oldTel.getText().toString())) {
						Map<String, String> renewalParams = new HashMap<String, String>();
						renewalParams.put("userName",RegisterUtils.userName);
						renewalParams.put("password",RegisterUtils.password);
						renewalParams.put("mailbox",
								RegisterUtils.email);
						// renewalParams.put("birthDate",RegisterUtils.birthDate);
						// renewalParams.put("gender", RegisterUtils.gender);
						renewalParams.put("mobile",RegisterUtils.telNumber);
						renewalParams.put("IMSI",RegisterUtils.imsi);
						renewalParams.put("oldMobile",et_oldTel.getText().toString());
						RenewalUserThread renewalUserThread = new RenewalUserThread(renewalParams);
						renewalUserThread.start();
					} else {
						dialog.dismiss();
						Toast.makeText(context,"�Բ��������ֻ��Ÿ�ʽ����������ȷ���ٴ�����",Toast.LENGTH_LONG).show();
					}
				}
			}).setCancelable(false).show();
			break;
		}
		return dialog;
	}
}
