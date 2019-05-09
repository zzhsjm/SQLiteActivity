package com.iot.sqlite_test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends Activity{

	EditText ET_NAME,ET_AGE,ET_NUMBER,ET_CLASS;
	RadioButton RB_MAN,RB_WOMAN;
	RadioGroup  RG_SEX;
	Button BTN_ADD,BTN_CANCEL;
	String Str_sex = null;
	SQLiteDatabase sql_db;
	SharedPreferences share_pfs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		find_id();
		key_things();
		init_PFS();
	}


	private void init_PFS() {
		// TODO Auto-generated method stub
		share_pfs = RegisterActivity.this.getSharedPreferences("sqlite_pfs", MODE_WORLD_WRITEABLE);
	}


	private void init_SQL() {
		// TODO Auto-generated method stub
		SQLiteOpenHelper sql_helper = new MySQLiteDB(RegisterActivity.this, "sgxy_iot.db3", null, 1);
		sql_db = sql_helper.getWritableDatabase();

	}

	private void key_things() {
		// TODO Auto-generated method stub
		BTN_ADD.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//初始化数据库
				init_SQL();
				//初始化数据
				String Reg_name 	= ET_NAME.getText().toString();
				String Reg_age 		= ET_AGE.getText().toString();
				String Reg_number 	= ET_NUMBER.getText().toString();
				String Reg_class 	= ET_CLASS.getText().toString();
				String sql = "insert into student (name,age,number,sex,class) values (?,?,?,?,?)";
				//添加进数据库
				sql_db.execSQL(sql , new String[]{Reg_name,Reg_age,Reg_number,Str_sex,Reg_class});
				System.out.println(" >>>>>>>>>>>>>>   insert OK !" );
				
				//清除EditText
				ET_NAME.setText( null );
				ET_AGE.setText( null );
				ET_NUMBER.setText(null );
				ET_CLASS.setText( null );				
				//返回前一个Activity
				finish();
			}
		});
		
		BTN_CANCEL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		RG_SEX.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				// TODO Auto-generated method stub
				if (checkId == R.id.sex_man && RB_MAN.isChecked()) {
					Str_sex = RB_MAN.getText().toString();
				}else if (checkId == R.id.sex_woman && RB_WOMAN.isChecked()) {
					Str_sex = RB_WOMAN.getText().toString();					
				}
			}
		});
	}

	private void find_id() {
		// TODO Auto-generated method stub
		ET_NAME = (EditText) findViewById(R.id.edit_name);
		ET_AGE = (EditText) findViewById(R.id.edit_age);
		ET_NUMBER = (EditText) findViewById(R.id.edit_number);
		ET_CLASS = (EditText) findViewById(R.id.edit_class);

		RB_MAN = (RadioButton) findViewById(R.id.sex_man);
		RB_WOMAN = (RadioButton) findViewById(R.id.sex_woman);
		
		RG_SEX = (RadioGroup) findViewById(R.id.rg_sex);
		
		BTN_ADD = (Button) findViewById(R.id.register_add);
		BTN_CANCEL = (Button) findViewById(R.id.register_cancel);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		//在这里利用SharePreference做数据恢复		
		ET_NAME.setText( share_pfs.getString("spf_name", null) );
		ET_AGE.setText( share_pfs.getString("spf_age", null) );
		ET_NUMBER.setText( share_pfs.getString("spf_number", null) );
		ET_CLASS.setText( share_pfs.getString("spf_class", null) );
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//在这里利用SharePreference开始数据保护		
		Editor editor = share_pfs.edit();
		
		editor.putString("spf_name", ET_NAME.getText().toString() );
		editor.putString("spf_age", ET_AGE.getText().toString() );
		editor.putString("spf_number", ET_NUMBER.getText().toString() );
		editor.putString("spf_class", ET_CLASS.getText().toString() );
		
		editor.commit();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
