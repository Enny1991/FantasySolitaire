package com.eneaceolini.fantasysolitaire;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class Stats extends Activity {
	
	private Typeface typeFace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		Intent ii = getIntent();
	    CharSequence nameOfPlayer = ii.getCharSequenceExtra("playerName");
	    typeFace=Typeface.createFromAsset(getAssets(),"fonts/SkipLegDay.ttf");
		
	    //static
	    TextView a = (TextView)findViewById(R.id.TextView02);
	    a.setTypeface(typeFace);
	    TextView b = (TextView)findViewById(R.id.TextView04);
	    b.setTypeface(typeFace);
	    TextView c = (TextView)findViewById(R.id.TextView06);
	    c.setTypeface(typeFace);
	    TextView d = (TextView)findViewById(R.id.TextView08);
	    d.setTypeface(typeFace);
	    TextView e = (TextView)findViewById(R.id.textView4);
	    e.setTypeface(typeFace);
	    TextView f = (TextView)findViewById(R.id.textView6);
	    f.setTypeface(typeFace);
	    TextView g = (TextView)findViewById(R.id.TextView10);
	    g.setTypeface(typeFace);
	    TextView h = (TextView)findViewById(R.id.TextView12);
	    h.setTypeface(typeFace);
	    TextView i = (TextView)findViewById(R.id.TextView15);
	    i.setTypeface(typeFace);
	    TextView j = (TextView)findViewById(R.id.TextView16);
	    j.setTypeface(typeFace);
	    TextView k = (TextView)findViewById(R.id.TextView13);
	    k.setTypeface(typeFace);
	    TextView l = (TextView)findViewById(R.id.TextView24);
	    l.setTypeface(typeFace);
	    TextView m = (TextView)findViewById(R.id.TextView20);
	    m.setTypeface(typeFace);
	    TextView n = (TextView)findViewById(R.id.TextView22);
	    n.setTypeface(typeFace);
	    TextView o = (TextView)findViewById(R.id.TextView25);
	    o.setTypeface(typeFace);
	    TextView p = (TextView)findViewById(R.id.TextView27);
	    p.setTypeface(typeFace);
	    TextView q = (TextView)findViewById(R.id.TextView28);
	    q.setTypeface(typeFace);
	    TextView r = (TextView)findViewById(R.id.manaText);
	    r.setTypeface(typeFace);
	    TextView s = (TextView)findViewById(R.id.settText);
	    s.setTypeface(typeFace);
	    TextView t = (TextView)findViewById(R.id.Column);
	    t.setTypeface(typeFace);
	    TextView u = (TextView)findViewById(R.id.Position);
	    u.setTypeface(typeFace);
	    
	    //
	    
		
		TextView  matchNormal = (TextView)findViewById(R.id.match_normal);
		matchNormal.setTypeface(typeFace);
		TextView  minMovesNormal = (TextView)findViewById(R.id.min_moves_normal);
		minMovesNormal.setTypeface(typeFace);
		TextView  minTimeNormal = (TextView)findViewById(R.id.min_time_normal);
		minTimeNormal.setTypeface(typeFace);
		TextView  movesNormal= (TextView)findViewById(R.id.moves_normal);
		movesNormal.setTypeface(typeFace);
		TextView  timeNormal= (TextView)findViewById(R.id.time_normal);
		timeNormal.setTypeface(typeFace);
		TextView  winNormal= (TextView)findViewById(R.id.win_normal);
		winNormal.setTypeface(typeFace);
		TextView  matchThree = (TextView)findViewById(R.id.match_three);
		matchThree.setTypeface(typeFace);
		TextView  minMovesThree = (TextView)findViewById(R.id.min_moves_three);
		minMovesThree.setTypeface(typeFace);
		TextView  minTimeThree = (TextView)findViewById(R.id.min_time_three);
		minTimeThree.setTypeface(typeFace);
		TextView  movesThree= (TextView)findViewById(R.id.moves_three);
		movesThree.setTypeface(typeFace);
		TextView  timeThree= (TextView)findViewById(R.id.time_three);
		timeThree.setTypeface(typeFace);
		TextView  winThree= (TextView)findViewById(R.id.win_three);
		winThree.setTypeface(typeFace);
		TextView  matchVegas = (TextView)findViewById(R.id.match_vegas);
		matchVegas.setTypeface(typeFace);
		TextView  minMovesVegas = (TextView)findViewById(R.id.min_moves_begas);
		minMovesVegas.setTypeface(typeFace);
		TextView  minTimeVegas = (TextView)findViewById(R.id.min_time_vegas);
		minTimeVegas.setTypeface(typeFace);
		TextView  movesVegas= (TextView)findViewById(R.id.moves_vegas);
		movesVegas.setTypeface(typeFace);
		TextView  timeVegas= (TextView)findViewById(R.id.gameText);
		timeVegas.setTypeface(typeFace);
		TextView  winVegas= (TextView)findViewById(R.id.win_vegas);
		winVegas.setTypeface(typeFace);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor ;
		String[] selectionArgs = { nameOfPlayer.toString() }; 
		String query;
		query = "SELECT * FROM normal WHERE name = ? ORDER BY _id ASC";
	    cursor = db.rawQuery(query, selectionArgs);
	    cursor.moveToFirst();
	    Log.d("try",cursor.getString(2));
	    matchNormal.setText(cursor.getString(2));
	    if(Integer.parseInt(cursor.getString(3))==99999999){
	    	minMovesNormal.setText("-");
		    minTimeVegas.setText("-");
	    }else{
	    minMovesNormal.setText(cursor.getString(3));
	    minTimeNormal.setText(cursor.getString(4)+" s");
	    }
	    movesNormal.setText(String.format("%.0f", Float.parseFloat(cursor.getString(5))));
	    timeNormal.setText(String.format("%.0f", Float.parseFloat(cursor.getString(6)))+" s");
	    if(Integer.parseInt(cursor.getString(2))!=0){
	    	float tot = ((float)Integer.parseInt(cursor.getString(7))  /((float)Integer.parseInt(cursor.getString(2))-1))*100;
	    	winNormal.setText(""+String.format("%.0f", tot)+" %");
	    }
	    else  winNormal.setText(""+(Integer.parseInt(cursor.getString(7)))+" %");
	    
	    
	    query = "SELECT * FROM three WHERE name = ? ORDER BY _id ASC";
	    cursor = db.rawQuery(query, selectionArgs);
	    cursor.moveToFirst();
	    
	    matchThree.setText(cursor.getString(2));
	    if(Integer.parseInt(cursor.getString(3))==99999999){
	    	minMovesThree.setText("-");
		    minTimeThree.setText("-");
	    }else{
	    minMovesThree.setText(cursor.getString(3));
	    minTimeThree.setText(cursor.getString(4)+" s");
	    }
	    movesThree.setText(String.format("%.0f", Float.parseFloat(cursor.getString(5))));
	    timeThree.setText(String.format("%.0f", Float.parseFloat(cursor.getString(6)))+" s");
	    
	    if(Integer.parseInt(cursor.getString(2))!=0){
	    	float tot = ((float)Integer.parseInt(cursor.getString(7))  /((float)Integer.parseInt(cursor.getString(2))-1))*100;
	    	winThree.setText(""+String.format("%.0f", tot)+" %");
	    }
		    
		    else  winThree.setText(""+(Integer.parseInt(cursor.getString(7)))+" %");
		
	    query = "SELECT * FROM vegas WHERE name = ? ORDER BY _id ASC";
	    cursor = db.rawQuery(query, selectionArgs);
	    cursor.moveToFirst();
	    
	    matchVegas.setText(cursor.getString(2));
	    if(Integer.parseInt(cursor.getString(3))==99999999){
	    	minMovesVegas.setText("-");
		    minTimeVegas.setText("-");
	    }else{
	    minMovesVegas.setText(cursor.getString(3));
	    minTimeVegas.setText(cursor.getString(4)+" s");
	    }
	    movesVegas.setText(String.format("%.0f", Float.parseFloat(cursor.getString(5))));
	    timeVegas.setText(String.format("%.0f", Float.parseFloat(cursor.getString(6)))+" s");
	    if(Integer.parseInt(cursor.getString(2))!=0){
	    	float tot = ((float)Integer.parseInt(cursor.getString(7))  /((float)Integer.parseInt(cursor.getString(2))-1))*100;
	    	winVegas.setText(""+String.format("%.0f", tot)+" %");
	    }
		    else  winVegas.setText(""+(Integer.parseInt(cursor.getString(7)))+" %");
		
		db.close();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats, menu);
		
		
		
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
