package com.example.account_book;

import java.util.Calendar;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;


public class Monlist extends Activity {

    private ListView list;
    private Button set;
    private TextView thismon,sum,title;
    private Calendar calendar;
	private int mYear, mMonth,mDay;
	private int money=0;
	private DatePickerDialog datePickerDialog;
	private Context mContext;
	private ImageView titleimg;
	SQLiteDatabase db;
	
    String[] datas = new String[DBhelper.queryNumEntries()];
    String[] prices = new String[DBhelper.queryNumEntries()];
    String[] names = new String[DBhelper.queryNumEntries()];
    String[] KEY_ROWID = new String[DBhelper.queryNumEntries()];
    String[] sortname=new String[DBhelper.queryNumEntries()];
    MyAdapter adapter = null;

    @SuppressLint("SdCardPath")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monlist);
        list = (ListView) findViewById(R.id.list);
        adapter = new MyAdapter(this);
        set=(Button)findViewById(R.id.set);
        title=(TextView)findViewById(R.id.title);
        thismon=(TextView)findViewById(R.id.thismon);
        sum=(TextView)findViewById(R.id.sum);
        titleimg=(ImageView)findViewById(R.id.titleimg);
        mContext=this;
        Intent intent = this.getIntent();
        String day = intent.getStringExtra("day");
        Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density  = getResources().getDisplayMetrics().density;
	    float dpHeight = dm.heightPixels / density;
	    
	    titleimg.getLayoutParams().width=(int)(dm.widthPixels*(0.9));
		titleimg.getLayoutParams().height=(int)(dm.heightPixels*(0.2));
		set.getLayoutParams().height=(int)(dm.heightPixels*(0.05));
		thismon.setTextSize((int)(dpHeight*(0.035)));
		title.setTextSize((int)(dpHeight*(0.035)));
		sum.setTextSize((int)(dpHeight*(0.035)));
	    
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                final int position, long id) {
            	
            	AlertDialog.Builder builder = new AlertDialog.Builder(Monlist.this);
        	    builder.setMessage("確定要刪除嗎")
        	           .setCancelable(false)
        	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	               public void onClick(DialogInterface dialog, int id) {
        	            	   Toast.makeText(getApplicationContext(), "删除成功",  
       		            	    	 Toast.LENGTH_LONG).show(); 
        	            	   Intent intent = new Intent();
     		            	  intent.setClass(Monlist.this, Monlist.class);
     		            	  intent.putExtra("day",thismon.getText());
     		            	  startActivity(intent); 
     		            	  Monlist.this.finish();
          	            	   db.delete("mish", "KEY_ROWID =?", new  String[]{ String.valueOf(KEY_ROWID[position])});
        	               }
        	           })
        	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
        	               public void onClick(DialogInterface dialog, int id) {
        	                    dialog.cancel();
        	               }
        	           });
        	    AlertDialog alert = builder.create();
        	    alert.show();
            }

          });
        
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        if(day!=null){
        	thismon.setText(day);
        }else{
        	if (mMonth<9)
            	thismon.setText(mYear+"/0"+(mMonth+1));
            else
            	thismon.setText(mYear+"/"+(mMonth+1)); 
        }
        
       int j=0;
        DBhelper helper = new DBhelper(this);
		db = helper.getReadableDatabase();
		Cursor c = db.query("mish", null, null, null, null, null, null, null);
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {
			if ((c.getString(0).substring(0,7)).equals(thismon.getText())){
			datas[j] = c.getString(0);
			names[j] = c.getString(2);
			if(names[j].equals("food"))
				sortname[j]="食品飲料";
			else if(names[j].equals("home"))
				sortname[j]="家庭開銷";
			else if(names[j].equals("entertainment"))
				sortname[j]="休閒娛樂";
			else if(names[j].equals("medicine"))
				sortname[j]="醫療藥品";
			else if(names[j].equals("otherspend"))
				sortname[j]="其他消費";
			else if(names[j].equals("work"))
				sortname[j]="工作收入";
			else if(names[j].equals("other"))
				sortname[j]="其他收入";
			prices[j] = c.getString(1);
			KEY_ROWID[j] = c.getString(3);
			char sym=prices[j].charAt(0);
			if(sym == '+')
				money=money+Integer.parseInt(prices[j].substring(1));
			else
				money=money-Integer.parseInt(prices[j].substring(1));
			j++;
			}
			c.moveToNext();
		}
		c.close();
		if (money<0)
			sum.setTextColor(Color.RED);
		else
			sum.setTextColor(Color.BLUE);
        sum.setText("$"+money);
        set.setOnClickListener(new OnClickListener(){
	    	@Override
	        public void onClick(View v) {
	    		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	             builder.setTitle("請選擇欲顯示的日期");

	             builder.setNeutralButton("修改",null);
	             builder.setPositiveButton("確定",new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface dialog, int whichButton) {
		            	  Intent intent = new Intent();
		            	  intent.setClass(Monlist.this, Monlist.class);
		            	  if (mMonth<9)
			                	thismon.setText(mYear+"/0"+(mMonth+1));
			                else
			                	thismon.setText(mYear+"/"+(mMonth+1));
		            	  intent.putExtra("day",thismon.getText());
		            	  startActivity(intent); 
		            	  Monlist.this.finish();
           }
     }); 
	             builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int whichButton) {
	            	  			mYear=calendar.get(Calendar.YEAR);
	            	  			mMonth=calendar.get(Calendar.MONTH);
      	  			
	                             dialog.cancel();
	              }
	        }); 
	             final AlertDialog alert = builder.create();
			     alert.show();
			     alert.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener
		            (new View.OnClickListener()
		                {   
		            		@Override
		                    public void onClick(View v)
		                    {	
		                    	showDialog(0);	            
		                    	datePickerDialog.updateDate(mYear, mMonth, mDay);
		    	                
		                    }
		                });
	    		
	    	}
	 });
        
    }
    

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.myxml2, null);
            Display display = getWindowManager().getDefaultDisplay();
    		DisplayMetrics dm = new DisplayMetrics();
    		display.getMetrics(dm);
    		getWindowManager().getDefaultDisplay().getMetrics(dm);
    		float density  = getResources().getDisplayMetrics().density;
    	    float dpHeight = dm.heightPixels / density;
           
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView datalist = (TextView) convertView
                    .findViewById(R.id.txtdata);
            TextView pricelist = (TextView) convertView
                    .findViewById(R.id.txtprice);

            name.setTextSize((int)(dpHeight*(0.035)));
            datalist.setTextSize((int)(dpHeight*(0.025)));
            pricelist.setTextSize((int)(dpHeight*(0.025)));
            
            name.setText(sortname[position]);
            datalist.setText(datas[position]);
            pricelist.setText(prices[position]);

            return convertView;
        }

    }
    protected Dialog onCreateDialog(int id) {
	     datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
	            @Override
	            public void onDateSet(DatePicker view, int year, int month,
	                    int day) {
	                mYear = year;
	                mMonth = month;
	                Toast.makeText(Monlist.this,("您選擇的日期為:"+mYear+"/"+(mMonth+1)),Toast.LENGTH_SHORT).show();
	            }
	            
	        }, mYear,mMonth, mDay);
	        
	        return datePickerDialog;        
	 }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void onBackPressed(){
		 Intent intent = new Intent();
		intent.setClass(Monlist.this, Main.class);
		startActivity(intent); 
		Monlist.this.finish();
	}
    public boolean delete(SQLiteDatabase mDb, String table, int id) {  
   	 String whereClause = "id=?";  
   	 String[] whereArgs = new String[] { String.valueOf(id) };  
   	 try {  
   	 mDb.delete("mish", whereClause, whereArgs);
   	
   	 } catch (SQLException e) {  
   	 Toast.makeText(getApplicationContext(), "删除数据库失败",  
   	 Toast.LENGTH_LONG).show();  
   	 return false;  
   	 }  
   	 return true;  
   	   
   	 }

}
