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
 *   默认显示一个ListView，列出从数据库中得到的各种字段。 
 *   单点一个item，弹出Toast
 *   长按一个item，弹出对话框，可以对内容进行重新编辑并选择保存或取消。对话框结束，刷新ListView
 *   点击Menu键，弹出选择菜单，可以对数据库表新增一条记录
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
		// 1.布局 item 和 ListView
		// 2.组织数据 
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
		// 3.实现Adapter
		SimpleAdapter simple_ADP = new SimpleAdapter(
				MainActivity.this, 
				ulist, 
				R.layout.list_item, 
				new String[]{"id","name","age","number","sex","class"}, 
				new int[]{R.id.item_id,R.id.item_name,R.id.item_age,R.id.item_number,R.id.item_sex,R.id.item_class}
		);
		// 4.adapter和Listview绑定
		LS_SQL.setAdapter(simple_ADP);
		// 5 ListView中item的监听
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
			//启动一个Activity
			Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_import:
			//从指定的路径和文件中导入信息并存储在数据库中，刷新显示(Android 文件存储)
			//1.初始化文件组件
			//2.找到文件
			//3.解析数据
			//4.数据写入数据库
			//5.刷新Listview
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
			//把数据库中的全部内容写入指定路径的文件中，实现文件存储
			//1.初始化文件组件
			//2.初始化数据（组织数据）
			//3.把数据写入文件中
			//4.使用进度条
			//5.使用线程				
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
				
				File_Context = File_Context + hm_id +" 姓名："+ hm_name +"   性别："+hm_sex+"   年龄："+ hm_age+"   学号："+hm_number+"   班级："+hm_class+'\n';
			}
			
			byte[] Fbuffer = File_Context.getBytes();
		
			try {
				FileOutputStream output = MainActivity.this.openFileOutput(File_Name, Context.MODE_PRIVATE + MODE_WORLD_READABLE + MODE_WORLD_WRITEABLE);
				output.write(Fbuffer);
				output.close();
				Toast.makeText(MainActivity.this, "数据导出已完成", Toast.LENGTH_SHORT).show();
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
			
			//创建进度条对话框 ProgressbarDialog 
			progressdialog("下载数据中", "请稍等......");
			Copy_Thread copy_thread = new Copy_Thread();
			copy_thread.start();
			
			break;	
		case R.id.menu_AsyncTask:
			//创建进度条对话框 ProgressbarDialog 
			progressdialog("下载数据中", "请稍等......");
			/**
		     * 步骤1：创建AsyncTask子类
		     * 注：
		     *   a. 继承AsyncTask类
		     *   b. 为3个泛型参数指定类型；若不使用，可用java.lang.Void类型代替
		     *      此处指定为：输入参数 = String类型、执行进度 = Integer类型、执行结果 = String类型
		     *   c. 根据需求，在AsyncTask子类内实现核心方法
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
			progressdialog.setMessage("加载中..");
			System.out.println("---------------onPreExecute");
		}
		
		@Override
		protected String doInBackground(String... arg) {
			// TODO Auto-generated method stub
			System.out.println("---------------doInBackground start");
			
			String sql_copy = "insert into student (name,age,sex,number,class) values('王世奎','22','男','20183206','计算机学院信息管理2班')";
			//写入10000条数据
			for (int i = 0; i <= copy_times; i++) {
				SQL_DB.execSQL(sql_copy );	
				//更新进度条
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
			Toast.makeText(MainActivity.this, "复制完成", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(MainActivity.this, "已取消", Toast.LENGTH_SHORT).show();
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
			String sql_copy = "insert into student (name,age,sex,number,class) values('王世奎','22','男','20183206','计算机学院信息管理2班')";
			//写入10000条数据
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
		menu.setHeaderTitle("操作栏");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_del:
			//1. 创建对话框
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			//2. 初始化对话框显示
			builder.setIcon(R.drawable.waring);
			builder.setMessage("确定删除和"+ uid + "相关的数据吗？");
			builder.setTitle("删除");
			//3. 初始化对话框的按钮
			builder.setNegativeButton("删除", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface view, int id) {
					// TODO Auto-generated method stub
					String del_str = "delete from student where number = ?";
					SQL_DB.execSQL(del_str, new String[]{uid});
					init_List();
				}
			});
			builder.setPositiveButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				
				}
			});
			//4. 显示对话框
			builder.create();
			builder.show();
			break;
		case R.id.menu_updata:
			AlertDialog.Builder builder_updata = new Builder(MainActivity.this);
			LayoutInflater inf = getLayoutInflater();
			builder_updata.setIcon(R.drawable.lock);
			builder_updata.setTitle("修改");
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
			
			if (cur.getString(4).equals("女")) {
				URB_WOMAN.setChecked(true);
			} else {
				URB_MAN.setChecked(true);
			}
			builder_updata.setPositiveButton("修改", new OnClickListener() {
				
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
			builder_updata.setNegativeButton("取消", new OnClickListener() {
				
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
