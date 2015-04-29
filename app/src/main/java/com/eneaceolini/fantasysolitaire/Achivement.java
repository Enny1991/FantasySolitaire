package com.eneaceolini.fantasysolitaire;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Achivement extends Activity {
	private DatabaseHelper dbHelper;
    private Typeface typeFace;
    private SQLiteDatabase db;
    private CharSequence nameOfPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achivement);
		Intent ii = getIntent();
		typeFace=Typeface.createFromAsset(getAssets(),"fonts/SkipLegDay.ttf");
	    nameOfPlayer = ii.getCharSequenceExtra("playerName");
	    LinearLayout toFill = (LinearLayout)findViewById(R.id.ach);
	    dbHelper = new DatabaseHelper(this);
		db = dbHelper.getWritableDatabase();
	    Cursor cursor ;
		String[] selectionArgs = { nameOfPlayer.toString() }; 
		String query;
		query = "SELECT * FROM achive WHERE name = ? ORDER BY _id ASC";
	    cursor = db.rawQuery(query, selectionArgs);
	    cursor.moveToFirst();
	    for(int i=2;i<50;i++){
	    	if(cursor.getInt(i)==1) {
	    		((LinearLayout)toFill.getChildAt(i-2)).setAlpha(1);
                //noinspection deprecation
                ((LinearLayout)toFill.getChildAt(i-2)).setBackgroundDrawable(getResources().getDrawable(R.drawable.new_radius_ach));
	    		((TextView)((LinearLayout)((LinearLayout)toFill.getChildAt(i-2)).getChildAt(0)).getChildAt(1)).setVisibility(View.VISIBLE);
	    	}
	    	((TextView)((LinearLayout)((LinearLayout)toFill.getChildAt(i-2)).getChildAt(0)).getChildAt(0)).setTypeface(typeFace);
	    	((TextView)((LinearLayout)((LinearLayout)toFill.getChildAt(i-2)).getChildAt(0)).getChildAt(1)).setTypeface(typeFace);
	    }

	    	
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.achivement, menu);
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
