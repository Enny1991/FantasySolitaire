package com.eneaceolini.fantasysolitaire;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


	


public class MainActivity extends Activity {

	
	private DatabaseHelper dbHelper;
	private Spinner sItems;
	private SQLiteDatabase dbWrite;
	private Typeface typeFace;
	private List<String> spinnerArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		typeFace=Typeface.createFromAsset(getAssets(),"fonts/SkipLegDay.ttf");
		TextView s = (TextView)findViewById(R.id.s);
		s.setTypeface(typeFace);
		
		
		
		dbHelper = new DatabaseHelper(this);
		dbWrite =  dbHelper.getWritableDatabase();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		spinnerArray =  new ArrayList<>();
		Cursor cursor = null;
			int k=0;
		try{
			cursor = db.query(TableNotes.TABLE_NAME,
					new String[] { TableNotes.COLUMN_NAME }, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				spinnerArray.add(cursor.getString(0));
			}
		}catch(Exception e){
			Log.d("exception","qualche tipo di ecc");
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, spinnerArray) ;
		
		
		
		
	    
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    sItems = (Spinner) findViewById(R.id.timespinner);
	    sItems.setAdapter(adapter);
		db.close();
		
		
	    
		Button toBegin = (Button)findViewById(R.id.sss);
		toBegin.setTypeface(typeFace);
		toBegin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sItems.getChildCount()==0){
					
					
					
					final Dialog dialog = new Dialog(MainActivity.this,R.style.PauseDialog);
					  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					  dialog.setContentView(R.layout.for_dialog_text);
					  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
					  final EditText input = (EditText)dialog.findViewById(R.id.editText1);
					  input.setTypeface(typeFace);
					  dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					  a.setText(R.string.enter_your_name);
					  a.setTypeface(typeFace);
					  Button one =(Button)dialog.findViewById(R.id.achbutton);
					  one.setText(R.string.go);
					  one.setTypeface(typeFace);	  
					    
					  
					  one.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String toPut = input.getText().toString().trim();
							if("".compareTo(toPut)!=0){
							try{
							ContentValues values = new ContentValues();
							
							values.put(TableNotes.COLUMN_LVL, "1");
							values.put(TableNotes.COLUMN_EXP, "0");
							values.put(TableNotes.COLUMN_NAME, toPut);
							values.put("inarow",0);
							SQLiteDatabase db = dbHelper.getWritableDatabase();

							long id = db.insertOrThrow(TableNotes.TABLE_NAME, null, values);

							
							
							//put in normal mode
							values = new ContentValues();
							values.put(TableNotes.COLUMN_NAME, toPut);
							values.put(TableNotes.COLUMN_MATCH, "0");
							values.put(TableNotes.COLUMN_MIN_MOVES, "99999999");
							values.put(TableNotes.COLUMN_MIN_TIME, "99999999");
							values.put(TableNotes.COLUMN_MOVES, "0");
							values.put(TableNotes.COLUMN_TIME, "0");
							values.put(TableNotes.COLUMN_WIN, "0");
							
							id = db.insertOrThrow("normal", null, values);
							
							//thre cards mode
							values = new ContentValues();
							values.put(TableNotes.COLUMN_NAME, toPut);
							values.put(TableNotes.COLUMN_MATCH, "0");
							values.put(TableNotes.COLUMN_MIN_MOVES, "99999999");
							values.put(TableNotes.COLUMN_MIN_TIME, "99999999");
							values.put(TableNotes.COLUMN_MOVES, "0");
							values.put(TableNotes.COLUMN_TIME, "0");
							values.put(TableNotes.COLUMN_WIN, "0");
							
							id = db.insertOrThrow("three", null, values);
							
							
							//vegas mode
							values = new ContentValues();
							values.put(TableNotes.COLUMN_NAME, toPut);
							values.put(TableNotes.COLUMN_MATCH, "0");
							values.put(TableNotes.COLUMN_MIN_MOVES, "99999999");
							values.put(TableNotes.COLUMN_MIN_TIME, "99999999");
							values.put(TableNotes.COLUMN_MOVES, "0");
							values.put(TableNotes.COLUMN_TIME, "0");
							values.put(TableNotes.COLUMN_WIN, "0");
						
							id = db.insertOrThrow("vegas", null, values);
							
							
							values = new ContentValues();
							values.put(TableNotes.COLUMN_NAME, toPut);
							values.put(TableNotes.COLUMN_ICE, "3");
							values.put(TableNotes.COLUMN_FIRE, "3");
							values.put(TableNotes.COLUMN_EARTH, "3");
							values.put(TableNotes.COLUMN_THUNDER, "3");
							values.put(TableNotes.COLUMN_TI, "3");
							values.put(TableNotes.COLUMN_MANA, "10000");

							
							id = db.insertOrThrow("power", null, values);
							
							
							
							values = new ContentValues();
							values.put(TableNotes.COLUMN_NAME, toPut);
							values.put("wnorm1", 0);
							values.put("wnorm2", 0);
							values.put("wnorm3", 0);
							values.put("tnorm100", 0);
							values.put("tnorm200", 0);
							values.put("tnorm300", 0);
							values.put("mnorm25", 0);
							values.put("mnorm50", 0);
							values.put("mnorm100", 0);
							values.put("wthree1", 0);
							values.put("wthree2", 0);
							values.put("wthree3", 0);
							values.put("tthree100", 0);
							values.put("tthree200", 0);
							values.put("tthree300", 0);
							values.put("mthree25", 0);
							values.put("mthree50", 0);
							values.put("mthree100", 0);
							values.put("wvegas1", 0);
							values.put("wvegas2", 0);
							values.put("wvegas3", 0);
							values.put("tvegas100", 0);
							values.put("tvegas200", 0);
							values.put("tvegas300", 0);
							values.put("mvegas25", 0);
							values.put("mvegas50", 0);
							values.put("mvegas100", 0);
							values.put("ice1", 0);
							values.put("ice2", 0);
							values.put("ice3", 0);
							values.put("thunder1", 0);
							values.put("thunder2", 0);
							values.put("thunder3", 0);
							values.put("earth1", 0);
							values.put("earth2", 0);
							values.put("earth3", 0);
							values.put("time1", 0);
							values.put("time2", 0);
							values.put("time3", 0);
							values.put("fire1", 0);
							values.put("fire2", 0);
							values.put("fire3", 0);
							values.put("winboth", 0);
							values.put("windraw", 0);
							values.put("moves200", 0);
							values.put("time600", 0);
							values.put("win10", 0);
							values.put("winnocomp", 0);
							values.put("wastetime", 0);

							
							id = db.insertOrThrow("achive", null, values);
							
							
							
							db.close();
							Intent i = new Intent( getApplicationContext(), GameField.class);
							
							i.putExtra("playerName", toPut);
							startActivity(i);
							dialog.dismiss();
							overridePendingTransition(R.drawable.transition1,R.drawable.transition2);
							finish();
							}catch(SQLException e){
								final Toast toast = new Toast(getApplicationContext());
								toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
								(Toast.makeText(getApplicationContext(), getString(R.string.the_name) + toPut + getString(R.string.belongs), Toast.LENGTH_LONG)).show();
							}
							}else{
								(Toast.makeText(getApplicationContext(),R.string.not_null, Toast.LENGTH_LONG)).show();
							}
						}
					});
				     
				      
				     
					    
				     
				      
				      dialog.show();
					
					
					
					
					
					
					
					
					

				}
				else{
					String nameOfPlayer = sItems.getSelectedItem().toString();

					Intent i = new Intent( getApplicationContext(), GameField.class);
					nameOfPlayer = sItems.getSelectedItem().toString();
					i.putExtra("playerName", nameOfPlayer);
					startActivity(i);
					overridePendingTransition(R.drawable.transition1,R.drawable.transition2);
					finish();
				}
				
			}
		});
		
		
		Button delete = (Button)findViewById(R.id.ddd);
		delete.setTypeface(typeFace);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(sItems.getChildCount()!=0){
				//alert dialog are u sure
				final Dialog dialog = new Dialog(MainActivity.this,R.style.PauseDialog);
				  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				  dialog.setContentView(R.layout.for_dialog);
				  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
				  a.setText(getString(R.string.sure_delete) +" '"+sItems.getSelectedItem().toString() +"' ?");
				  a.setTypeface(typeFace);
				  Button one =(Button)dialog.findViewById(R.id.dialogbutton1);
				  one.setText(R.string.delete);
				  one.setTypeface(typeFace);	  
				    
				  
				  one.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						try{
							SQLiteDatabase db = dbHelper.getWritableDatabase();
							String nameToUse = sItems.getSelectedItem().toString() ;
							String[] wh = {nameToUse};
							db.delete("notes", "name = ?", wh);
							db.delete("power", "name = ?", wh);
							db.delete("normal", "name = ?", wh);
							db.delete("vegas", "name = ?", wh);
							db.delete("three", "name = ?", wh);
							db.delete("achive", "name = ?", wh);
							Toast.makeText(getApplicationContext(), R.string.succs, Toast.LENGTH_LONG).show();
							spinnerArray =  new ArrayList<>();
							Cursor cursor = null;
								int k=0;
							try{
								cursor = db.query(TableNotes.TABLE_NAME,
										new String[] { TableNotes.COLUMN_NAME }, null, null,
										null, null, null);
								while (cursor.moveToNext()) {
									spinnerArray.add(cursor.getString(0));
								}
							}catch(Exception e){
								Log.d("exception","qualche tipo di ecc");
							}
							

							MyArrayAdapter adapter = new MyArrayAdapter(MainActivity.this);
						    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						    sItems = (Spinner) findViewById(R.id.timespinner);
						    sItems.setAdapter(adapter);
							db.close();
							dialog.dismiss();
							}catch(Exception e){
								Log.d("exc",e.toString());
							}
					}
					
				});
				  
				  Button two =(Button)dialog.findViewById(R.id.dialogbutton2);
				  two.setTypeface(typeFace);
				  two.setText(R.string.no);
				  two.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
					}
				});
			      
			      dialog.show();

				//
				
				}
			}
		});
		
		Button toNewStart = (Button)findViewById(R.id.newplayer);
		toNewStart.setTypeface(typeFace);
		toNewStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final Dialog dialog = new Dialog(MainActivity.this,R.style.PauseDialog);
				  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				  dialog.setContentView(R.layout.for_dialog_text);
				  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
				  final EditText input = (EditText)dialog.findViewById(R.id.editText1);
				  input.setTypeface(typeFace);
				  dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				  a.setText(R.string.enter_your_name);
				  a.setTypeface(typeFace);
				  Button one =(Button)dialog.findViewById(R.id.achbutton);
				  one.setText(R.string.go);
				  one.setTypeface(typeFace);	  
				    
				  
				  one.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String toPut = input.getText().toString().trim();
						try{
						ContentValues values = new ContentValues();
						
						values.put(TableNotes.COLUMN_LVL, "1");
						values.put(TableNotes.COLUMN_EXP, "0");
						values.put(TableNotes.COLUMN_NAME, toPut);
						values.put("inarow",0);
						SQLiteDatabase db = dbHelper.getWritableDatabase();

						long id = db.insertOrThrow(TableNotes.TABLE_NAME, null, values);

						
						
						//put in normal mode
						values = new ContentValues();
						values.put(TableNotes.COLUMN_NAME, toPut);
						values.put(TableNotes.COLUMN_MATCH, "0");
						values.put(TableNotes.COLUMN_MIN_MOVES, "99999999");
						values.put(TableNotes.COLUMN_MIN_TIME, "99999999");
						values.put(TableNotes.COLUMN_MOVES, "0");
						values.put(TableNotes.COLUMN_TIME, "0");
						values.put(TableNotes.COLUMN_WIN, "0");
						
						id = db.insertOrThrow("normal", null, values);
						
						//thre cards mode
						values = new ContentValues();
						values.put(TableNotes.COLUMN_NAME, toPut);
						values.put(TableNotes.COLUMN_MATCH, "0");
						values.put(TableNotes.COLUMN_MIN_MOVES, "99999999");
						values.put(TableNotes.COLUMN_MIN_TIME, "99999999");
						values.put(TableNotes.COLUMN_MOVES, "0");
						values.put(TableNotes.COLUMN_TIME, "0");
						values.put(TableNotes.COLUMN_WIN, "0");
						
						id = db.insertOrThrow("three", null, values);
						
						
						//vegas mode
						values = new ContentValues();
						values.put(TableNotes.COLUMN_NAME, toPut);
						values.put(TableNotes.COLUMN_MATCH, "0");
						values.put(TableNotes.COLUMN_MIN_MOVES, "99999999");
						values.put(TableNotes.COLUMN_MIN_TIME, "99999999");
						values.put(TableNotes.COLUMN_MOVES, "0");
						values.put(TableNotes.COLUMN_TIME, "0");
						values.put(TableNotes.COLUMN_WIN, "0");
					
						id = db.insertOrThrow("vegas", null, values);
						
						
						values = new ContentValues();
						values.put(TableNotes.COLUMN_NAME, toPut);
						values.put(TableNotes.COLUMN_ICE, "0");
						values.put(TableNotes.COLUMN_FIRE, "0");
						values.put(TableNotes.COLUMN_EARTH, "0");
						values.put(TableNotes.COLUMN_THUNDER, "0");
						values.put(TableNotes.COLUMN_TI, "3");
						values.put(TableNotes.COLUMN_MANA, "10000");

						
						id = db.insertOrThrow("power", null, values);
						
						

						values = new ContentValues();
						values.put(TableNotes.COLUMN_NAME, toPut);
						values.put("wnorm1", 0);
						values.put("wnorm2", 0);
						values.put("wnorm3", 0);
						values.put("tnorm100", 0);
						values.put("tnorm200", 0);
						values.put("tnorm300", 0);
						values.put("mnorm25", 0);
						values.put("mnorm50", 0);
						values.put("mnorm100", 0);
						values.put("wthree1", 0);
						values.put("wthree2", 0);
						values.put("wthree3", 0);
						values.put("tthree100", 0);
						values.put("tthree200", 0);
						values.put("tthree300", 0);
						values.put("mthree25", 0);
						values.put("mthree50", 0);
						values.put("mthree100", 0);
						values.put("wvegas1", 0);
						values.put("wvegas2", 0);
						values.put("wvegas3", 0);
						values.put("tvegas100", 0);
						values.put("tvegas200", 0);
						values.put("tvegas300", 0);
						values.put("mvegas25", 0);
						values.put("mvegas50", 0);
						values.put("mvegas100", 0);
						values.put("ice1", 0);
						values.put("ice2", 0);
						values.put("ice3", 0);
						values.put("thunder1", 0);
						values.put("thunder2", 0);
						values.put("thunder3", 0);
						values.put("earth1", 0);
						values.put("earth2", 0);
						values.put("earth3", 0);
						values.put("time1", 0);
						values.put("time2", 0);
						values.put("time3", 0);
						values.put("fire1", 0);
						values.put("fire2", 0);
						values.put("fire3", 0);
						values.put("winboth", 0);
						values.put("windraw", 0);
						values.put("moves200", 0);
						values.put("time600", 0);
						values.put("win10", 0);
						values.put("winnocomp", 0);
						values.put("wastetime", 0);

						
						id = db.insertOrThrow("achive", null, values);
						
						
						
						
						db.close();
						Intent i = new Intent( getApplicationContext(), GameField.class);
						
						i.putExtra("playerName", toPut);
						startActivity(i);
						dialog.dismiss();
						overridePendingTransition(R.drawable.transition1,R.drawable.transition2);
						finish();
						}catch(SQLException e){
							Toast toast = new Toast(getApplicationContext());
							toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
							(Toast.makeText(getApplicationContext(), getString(R.string.the_name) + toPut + getString(R.string.belongs), Toast.LENGTH_LONG)).show();
						}
						
					}
				});
			     
			      
			      dialog.show();
				}
				
			
		});
	}

	
	@Override
	public void onResume(){
		super.onResume();
		dbHelper = new DatabaseHelper(this);
		dbWrite =  dbHelper.getWritableDatabase();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		spinnerArray =  new ArrayList<>();
		Cursor cursor = null;
			int k=0;
		try{
			cursor = db.query(TableNotes.TABLE_NAME,
					new String[] { TableNotes.COLUMN_NAME }, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				spinnerArray.add(cursor.getString(0));
			}
		}catch(Exception e){
			Log.d("exception","qualche tipo di ecc");
		}
		

		MyArrayAdapter adapter = new MyArrayAdapter(this);
	    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sItems = (Spinner) findViewById(R.id.timespinner);
	    sItems.setAdapter(adapter);
		db.close();
	}
	
	
	class MyArrayAdapter extends BaseAdapter {

        final LayoutInflater mInflater;

        public MyArrayAdapter(MainActivity con) {
            // TODO Auto-generated constructor stub
            mInflater = LayoutInflater.from(con);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return spinnerArray.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return spinnerArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ListContent holder;
            View v = convertView;
            if (v == null) {
                v = mInflater.inflate(R.layout.spinner_item, null);
                holder = new ListContent();

                holder.name = (TextView) v.findViewById(R.id.settText);

                v.setTag(holder);
            } else {

                holder = (ListContent) v.getTag();
            }

            holder.name.setTypeface(typeFace);
            holder.name.setText("" + spinnerArray.get(position));

            return v;
        }

    }

	
	static class ListContent {

        TextView name;

    }
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
