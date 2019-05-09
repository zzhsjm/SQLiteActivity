package com.iot.sqlite_test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*
 *   Ĭ����ʾһ��ListView���г������ݿ��еõ��ĸ����ֶΡ� 
 *   ����һ��item������Toast
 *   ����һ��item�������Ի��򣬿��Զ����ݽ������±༭��ѡ�񱣴��ȡ�����Ի��������ˢ��ListView
 *   ���Menu��������ѡ��˵������Զ����ݿ������һ����¼
 */
public class MainActivity extends Activity {

	ListView LS_SQL;
	SQLiteDatabase SQL_DB;
	String uid;
	TextView TX_MAIN;
	ProgressDialog progressdialog;
	private int copy_times = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		find_id();
		key_things();
		init_SQL();
		init_List();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init_List();
		
	}
	
	private void init_List() {
		// TODO Auto-generated method stub
		// 1.���� item �� ListView
		// 2.��֯���� 
		ArrayList<HashMap<String, Object>> ulist = new ArrayList<HashMap<String,Object>>();
		String sql= "select * from student";
		Cursor cur = SQL_DB.rawQuery(sql, null);
		while (cur.moveToNext()) {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			String hm_id    = cur.getString(0);
			String hm_name 	= cur.getString(1);
			String hm_age 	= cur.getString(2);
			String hm_number = cur.getString(3);
			String hm_sex 	= cur.getString(4);
			String hm_class = cur.getString(5);
			
			hm.put("id", hm_id);
			hm.put("name", hm_name);
			hm.put("age", hm_age);
			hm.put("number", hm_number);
			hm.put("sex", hm_sex);
			hm.put("class", hm_class);
			
			ulist.add(hm);
		}
		// 3.ʵ��Adapter
		SimpleAdapter simple_ADP = new SimpleAdapter(
				MainActivity.this, 
				ulist, 
				R.layout.list_item, 
				new String[]{"id","name","age","number","sex","class"}, 
				new int[]{R.id.item_id,R.id.item_name,R.id.item_age,R.id.item_number,R.id.item_sex,R.id.item_class}
		);
		// 4.adapter��Listview��
		LS_SQL.setAdapter(simple_ADP);
		// 5 ListView��item�ļ���
		registerForContextMenu(LS_SQL);
	}

	private void init_SQL() {
		// TODO Auto-generated method stub
		SQLiteOpenHelper SQL_Helper = new MySQLiteDB(MainActivity.this, "sgxy_iot.db3", null, 1);
		SQL_DB = SQL_Helper.getWritableDatabase();
	}

	private void key_things() {
		// TODO Auto-generated method stub
		LS_SQL.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> views, View arg,
					int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String, Object> hm =  (HashMap<String, Object>) views.getItemAtPosition(position);
				uid = hm.get("number").toString();
				return false;
			}
		});
	}

	private void find_id() {
		// TODO Auto-generated method stub
		LS_SQL = (ListView) findViewById(R.id.sql_test_listview);
		TX_MAIN = (TextView) findViewById(R.id.sqlite_test_text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		String File_Name = "t.txt";
		
		switch (item.getItemId()) {
		case R.id.menu_add:
			//����һ��Activity
			Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_import:
			//��ָ����·�����ļ��е�����Ϣ���洢�����ݿ��У�ˢ����ʾ(Android �ļ��洢)
			//1.��ʼ���ļ����
			//2.�ҵ��ļ�
			//3.��������
			//4.����д�����ݿ�
			//5.ˢ��Listview
			try {
				byte[] buffer = new byte[1024];
				FileInputStream input = MainActivity.this.openFileInput(File_Name);
				int in_size =input.read(buffer);
				Toast.makeText(MainActivity.this, new String(buffer), Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				System.out.println("-------------------FileNotFoundException failed ");
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("-------------------IOException   failed ");
				e.printStackTrace();
			}

			break;
		case R.id.menu_derived:
			//�����ݿ��е�ȫ������д��ָ��·�����ļ��У�ʵ���ļ��洢
			//1.��ʼ���ļ����
			//2.��ʼ�����ݣ���֯���ݣ�
			//3.������д���ļ���
			//4.ʹ�ý�����
			//5.ʹ���߳�				
			String File_Context = "";
			String sql= "select * from student";
			Cursor cur = SQL_DB.rawQuery(sql, null);
			while (cur.moveToNext()) {
				String hm_id    = cur.getString(0);
				String hm_name 	= cur.getString(1);
				String hm_age 	= cur.getString(2);
				String hm_number = cur.getString(3);
				String hm_sex 	= cur.getString(4);
				String hm_class = cur.getString(5);
				
				File_Context = File_Context + hm_id +" ������"+ hm_name +"   �Ա�"+hm_sex+"   ���䣺"+ hm_age+"   ѧ�ţ�"+hm_number+"   �༶��"+hm_class+'\n';
			}
			
			byte[] Fbuffer = File_Context.getBytes();
		
			try {
				FileOutputStream output = MainActivity.this.openFileOutput(File_Name, Context.MODE_PRIVATE + MODE_WORLD_READABLE + MODE_WORLD_WRITEABLE);
				output.write(Fbuffer);
				output.close();
				Toast.makeText(MainActivity.this, "���ݵ��������", Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("-------------------FileNotFoundException failed ");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("-------------------IOException   failed ");
				e.printStackTrace();
			}

			break;			
		case R.id.menu_copy10000:
			
			//�����������Ի��� ProgressbarDialog 
			progressdialog("����������", "���Ե�......");
			Copy_Thread copy_thread = new Copy_Thread();
			copy_thread.start();
			
			break;	
		case R.id.menu_AsyncTask:
			//�����������Ի��� ProgressbarDialog 
			progressdialog("����������", "���Ե�......");
			/**
		     * ����1������AsyncTask����
		     * ע��
		     *   a. �̳�AsyncTask��
		     *   b. Ϊ3�����Ͳ���ָ�����ͣ�����ʹ�ã�����java.lang.Void���ʹ���
		     *      �˴�ָ��Ϊ��������� = String���͡�ִ�н��� = Integer���͡�ִ�н�� = String����
		     *   c. ����������AsyncTask������ʵ�ֺ��ķ���
		     */
			MyAsyncTask mytask = new MyAsyncTask();
			mytask.execute();
			
			break;
		case R.id.menu_cancel:
			
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	class MyAsyncTask extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressdialog.setMessage("������..");
			System.out.println("---------------onPreExecute");
		}
		
		@Override
		protected String doInBackground(String... arg) {
			// TODO Auto-generated method stub
			System.out.println("---------------doInBackground start");
			
			String sql_copy = "insert into student (name,age,sex,number,class) values('������','22','��','20183206','�����ѧԺ��Ϣ����2��')";
			//д��10000������
			for (int i = 0; i <= copy_times; i++) {
				SQL_DB.execSQL(sql_copy );	
				//���½�����
				publishProgress(i);
			}
			
			System.out.println("---------------doInBackground finish");

			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("---------------onPostExecute");
			
			progressdialog.cancel();
			Toast.makeText(MainActivity.this, "�������", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			System.out.println("---------------onProgressUpdate");
			progressdialog.setProgress(values[0]);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			System.out.println("---------------onCancelled");
			progressdialog.cancel();
			Toast.makeText(MainActivity.this, "��ȡ��", Toast.LENGTH_SHORT).show();
		}

		
	}
	private void progressdialog(String progress_title, String  progress_message) {
		// TODO Auto-generated method stub
		progressdialog = new ProgressDialog(MainActivity.this);
		progressdialog.setTitle(progress_title);
		progressdialog.setMessage(progress_message);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressdialog.setMax(copy_times);
		progressdialog.setProgress(0);
		progressdialog.setCancelable(false);
		progressdialog.show();
	}

	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what >= copy_times) {
				progressdialog.cancel();
			}
			progressdialog.setProgress(msg.what);	

			super.handleMessage(msg);
		}
	};
	
	class Copy_Thread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub			
			System.out.println("Copy_Thread runing ...");
			String sql_copy = "insert into student (name,age,sex,number,class) values('������','22','��','20183206','�����ѧԺ��Ϣ����2��')";
			//д��10000������
			for (int i = 0; i <= copy_times; i++) {
				SQL_DB.execSQL(sql_copy );	

				
				handler.sendEmptyMessage(i);
				
	/*        	Message msg = new Message();  
	        	msg.what =  0;
	        	msg.obj = "";
				handler.sendMessage(msg);
	*/
			}
			System.out.println("Copy_Thread finish!!!");
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		menu.setHeaderIcon(R.drawable.ic_launcher);
		menu.setHeaderTitle("������");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_del:
			//1. �����Ի���
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			//2. ��ʼ���Ի�����ʾ
			builder.setIcon(R.drawable.waring);
			builder.setMessage("ȷ��ɾ����"+ uid + "��ص�������");
			builder.setTitle("ɾ��");
			//3. ��ʼ���Ի���İ�ť
			builder.setNegativeButton("ɾ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface view, int id) {
					// TODO Auto-generated method stub
					String del_str = "delete from student where number = ?";
					SQL_DB.execSQL(del_str, new String[]{uid});
					init_List();
				}
			});
			builder.setPositiveButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				
				}
			});
			//4. ��ʾ�Ի���
			builder.create();
			builder.show();
			break;
		case R.id.menu_updata:
			AlertDialog.Builder builder_updata = new Builder(MainActivity.this);
			LayoutInflater inf = getLayoutInflater();
			builder_updata.setIcon(R.drawable.lock);
			builder_updata.setTitle("�޸�");
			View Updata_View = inf.inflate(R.layout.updata_layout, (ViewGroup) findViewById(R.id.updata_layout));
			builder_updata.setView(Updata_View);
			final EditText UET_NAME = (EditText) Updata_View.findViewById(R.id.edit_name);
			final EditText UET_AGE = (EditText) Updata_View.findViewById(R.id.edit_age);
			final EditText UET_NUMBER = (EditText) Updata_View.findViewById(R.id.edit_number);
			final EditText UET_CLASS = (EditText) Updata_View.findViewById(R.id.edit_class);

			RadioButton URB_MAN = (RadioButton) Updata_View.findViewById(R.id.sex_man);
			RadioButton URB_WOMAN = (RadioButton) Updata_View.findViewById(R.id.sex_woman);
			
			RadioGroup URG_SEX = (RadioGroup) Updata_View.findViewById(R.id.rg_sex);

			String sql_select = "select * from student where number = ?";
			Cursor cur = SQL_DB.rawQuery(sql_select , new String[]{uid});
			cur.moveToNext();
			UET_NAME.setText(cur.getString(1));
			UET_AGE.setText(cur.getString(2));
			UET_NUMBER.setText(cur.getString(3));
			UET_CLASS.setText(cur.getString(5));
			
			if (cur.getString(4).equals("Ů")) {
				URB_WOMAN.setChecked(true);
			} else {
				URB_MAN.setChecked(true);
			}
			builder_updata.setPositiveButton("�޸�", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String u_name = UET_NAME.getText().toString();
					String u_age = UET_AGE.getText().toString();
					String u_number = UET_NUMBER.getText().toString();
					String u_class = UET_CLASS.getText().toString();
					String sql_update = "update student set name = ?,age = ?,class= ? where number = ?";
					SQL_DB.execSQL(sql_update , new String[]{u_name,u_age,u_class,u_number});
					
					init_List();
				}
			});
			builder_updata.setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			builder_updata.create();
			builder_updata.show(); 
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	public boolean isExternalStorageWritable(){
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
}
