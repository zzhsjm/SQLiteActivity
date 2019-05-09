package com.iot.sqlite_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteDB extends SQLiteOpenHelper{

	public MySQLiteDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "Create table student (_id integer primary key autoincrement," +
				"name varchar(10)," +
				"age varchar(10)," +
				"number varchar(10)," +
				"sex varchar(2)," +
				"class varchar(20) )";
		// TODO Auto-generated method stub
		db.execSQL(sql);
		
		db.execSQL("insert into student (name,age,number,sex,class ) values ('刘雯雯','22','20180254','女','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('张    莉','21','20180255','女','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('李晓晨','22','20180256','女','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('张曦','24','20180257','女','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('张友和','23','20180258','男','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('陈泰铭','22','20180259','男','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('薛志宇','23','20180260','男','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('王太利','22','20180261','男','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('刘    瑜','20','20180262','女','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('陈国庆','21','20180263','男','计算机学院计算机技术与科学1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('邹庆飞','22','20180301','女','信息与工程学院物联网工程1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('杨韩舒','25','20180302','女','信息与工程学院物联网工程1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('王文鑫','22','20180303','男','信息与工程学院物联网工程1班')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('刘庆梅','21','20180304','女','信息与工程学院物联网工程1班')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
