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
		
		db.execSQL("insert into student (name,age,number,sex,class ) values ('������','22','20180254','Ů','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('��    ��','21','20180255','Ů','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('������','22','20180256','Ů','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('����','24','20180257','Ů','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('���Ѻ�','23','20180258','��','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('��̩��','22','20180259','��','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('Ѧ־��','23','20180260','��','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('��̫��','22','20180261','��','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('��    �','20','20180262','Ů','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('�¹���','21','20180263','��','�����ѧԺ������������ѧ1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('�����','22','20180301','Ů','��Ϣ�빤��ѧԺ����������1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('���','25','20180302','Ů','��Ϣ�빤��ѧԺ����������1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('������','22','20180303','��','��Ϣ�빤��ѧԺ����������1��')");
		db.execSQL("insert into student (name,age,number,sex,class ) values ('����÷','21','20180304','Ů','��Ϣ�빤��ѧԺ����������1��')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
