package com.eneaceolini.fantasysolitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class GameField extends Activity {
  
	
	//LAST MOVE FLAGS
	private static final int  FROM_FIELD_TO_FIELD = 0;
	private static final int  FROM_FIELD_TO_PIT = 1;
	//public static final int  FROM_PIT_TO_PIT = 2;
	private static final int  FROM_PIT_TO_FIELD = 3;
	private static final int  FROM_DRAW_TO_PIT = 4;
	private static final int  FROM_DRAW_TO_FIELD = 5;
	private static final int RESULT_SETTINGS = 1;
	private static final int DRAW=6;
	private int FOUR_COUNTER=0;
	private int inARow;
	private static Context context;
	private static LinearLayout.LayoutParams cut;
	private static LinearLayout.LayoutParams full;
	private static LinearLayout.LayoutParams fullcut;
	private static LinearLayout.LayoutParams fullforearth;
	private static LinearLayout.LayoutParams lp;
	private static LinearLayout.LayoutParams lp2;
	private static LinearLayout.LayoutParams forCompare;
	private static LinearLayout.LayoutParams forScompare;
	private boolean noDraw = true;
	private int FLAG_MOVING_ONE;
	private int FLAG_MOVING_FROM_DRAW=0;
	private int INDEX_TO_DRAW=0;
	private int CARD_IN_PILE=24;
	private int indexToMove;
	private final int SHUFFLE_TIMES=10;
	private DisplayMetrics metrics;
	private ImageView bmImage;
	private ImageView[] transport;
	private Drawable[] images;
	private Vector<Integer> toDraw ;
	private LinearLayout field;
	private LinearLayout pits;
	private final Vector<LastAction> lastActionVector = new Vector<>();
	private LastAction lastAction = new LastAction();
	private int ACTION_POINTER = 0;
	private LinearLayout draw;
	private boolean VEGAS_MODE = false;
	private int INCREMENT_TO_DRAW = 1;
	private int INCREMENT_TO_SHOW = 2;
	private int MULTIPLIER_VEGAS = 1;
	private int MULTIPLIER_DRAW_3 = 1;
	private SharedPreferences sharedPrefs;
	private int VICTORY_CHECK=0;
	private int NUMBER_MOVES=0;
	private ImageView push;
	private Button win;
	private TextView wintext;
	private Chronometer chrono;
	private long timeWhenStopped = 0;
	private TextView moves ;
	private int FINAL_MOVE = 0;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private String nameToUse;
	private int earned;
	private int previousPoint;
	private ProgressBar progBar;
	private int newPoints;
    private int level;
    private TextView lvl;
    private final int MATCH_NORMAL = 0;
    private final int MATCH_THREE = 1;
    private final int MATCH_VEGAS = 2;
    private final int MATCH_BOTH = 3;
    private int MATCH_TYPE;
    private LinearLayout[] verticalCardPlace;
    private ProgressBar progMana;
    private Spinner icespinner;
    private Spinner firespinner;
    private Spinner thunderspinner;
    private Spinner earthspinner;
    private Spinner timespinner;
    private boolean STOP_COUNT = false;
    private int MOVES_NOT_COUNT = 0;
    private int[][] FIELD;
    private boolean SHOW_UNDER_MOVE = false;
    private int TIME_TO_REMOVE=0;
    //protected int LVL_TIME=3; //to save in db
    private int manaCost;
    private int progress;
    private TextView manaText;
    private Typeface typeFace;
    private NumberPicker numbPick;
    private NumberPicker seedPick;
    private int numberCardsToSort;
    private LinearLayout[] pit;
    private int[] powerFlags;
    private TextView textAch;
    private Vector<PopupWindow> toClose;
    private boolean firstMatch=false;

	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.game_field);
    // main variables set
    toClose = new Vector<>();

    
    
    dialogAch = new Dialog(this,R.style.PauseDialog3);
	  dialogAch.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  dialogAch.setContentView(R.layout.for_dialog_ach);
	  textAch = (TextView)dialogAch.findViewById(R.id.dialogText);
	  textAch.setTypeface(typeFace);
	  dialogAch.setCanceledOnTouchOutside(false);
	  Window window = dialogAch.getWindow();
	  WindowManager.LayoutParams wlp = window.getAttributes();

	  wlp.gravity = Gravity.TOP;
	  wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
	  window.setAttributes(wlp);
    powerFlags = new int[15];
    dbHelper = new DatabaseHelper(this);
    context = getApplicationContext();
    LinearLayout field3 = (LinearLayout)findViewById(R.id.Field3);
    metrics = new DisplayMetrics();
	 getWindowManager().getDefaultDisplay().getMetrics(metrics);
	progBar = (ProgressBar)findViewById(R.id.salamello);
	typeFace=Typeface.createFromAsset(getAssets(),"fonts/SkipLegDay.ttf");
	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	VEGAS_MODE = sharedPrefs.getBoolean("vegas", false);
	if(sharedPrefs.getBoolean("draw3", false)) {
		INCREMENT_TO_DRAW = 3;
		INCREMENT_TO_SHOW = 4;
		INDEX_TO_DRAW = 2;
		MULTIPLIER_DRAW_3 = 2;
	}
	if(VEGAS_MODE) {
		MULTIPLIER_VEGAS = 3;
	}
	
	TextView stat = (TextView)findViewById(R.id.statText);
	TextView sett = (TextView)findViewById(R.id.settText);
	TextView game = (TextView)findViewById(R.id.gameText);
	stat.setTypeface(typeFace);
	sett.setTypeface(typeFace);
	game.setTypeface(typeFace);
	
	
	//set up match type
	if(VEGAS_MODE && INCREMENT_TO_DRAW == 3) MATCH_TYPE = MATCH_BOTH;
	else if(VEGAS_MODE) MATCH_TYPE = MATCH_VEGAS;
			else if(INCREMENT_TO_DRAW == 3) MATCH_TYPE = MATCH_THREE;
					else MATCH_TYPE = MATCH_NORMAL;
	
	//SETUP FOR DATABES READ
    db = dbHelper.getWritableDatabase();
    Intent ii = getIntent();
    CharSequence nameOfPlayer = ii.getCharSequenceExtra("playerName");
    Cursor cursor;
    nameToUse = (String)nameOfPlayer;
    String query = "SELECT _id FROM notes WHERE name = ? ORDER BY _id ASC";
    String query2;
    String[] selectionArgs = { nameToUse }; 
    cursor = db.rawQuery(query, selectionArgs);

    
   
    
    
    //END DB STUFF
	 
	//set layout parameters
    
    int ABS_H = (int)((float)(metrics.widthPixels-25)/7)*230/157;
    int ABS_W = (metrics.widthPixels-25)/7;
    
    
    int setCut = (int) ABS_H - ABS_H*30/100;
    int setFull = (int )ABS_H - ABS_H*10/100;

    (cut = new LinearLayout.LayoutParams(ABS_W, ABS_H)).setMargins(0, 0, 0, -(setCut));
    (full = new LinearLayout.LayoutParams(ABS_W, ABS_H)).setMargins(0, 0, 0, -0);
    (fullcut = new LinearLayout.LayoutParams(ABS_W, ABS_H)).setMargins(0, 0, 0, -(setFull));
    (fullforearth = new LinearLayout.LayoutParams(ABS_W, ABS_H)).setMargins(0, 0, 0, -setCut/2);
    forCompare = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    forScompare = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
    
	 //forcing 7 equal layouts
      //noinspection deprecation
      lp = new LinearLayout.LayoutParams(ABS_W, LinearLayout.LayoutParams.FILL_PARENT);
 	 lp2 = new LinearLayout.LayoutParams(ABS_W, ABS_H);
    
    // Initializing field
    //Every cell in Field is a LinearLayout in the Vector VerticalCardPlace 
    verticalCardPlace=new LinearLayout[7];
    //Instatiate these Linear Layouts and give Drag listener 
    (verticalCardPlace[0]=(LinearLayout)findViewById(R.id.first)).setOnDragListener(new MyDragListener());
    (verticalCardPlace[1]=(LinearLayout)findViewById(R.id.second)).setOnDragListener(new MyDragListener());
    (verticalCardPlace[2]=(LinearLayout)findViewById(R.id.third)).setOnDragListener(new MyDragListener());
    (verticalCardPlace[3]=(LinearLayout)findViewById(R.id.fourth)).setOnDragListener(new MyDragListener());
    (verticalCardPlace[4]=(LinearLayout)findViewById(R.id.fifth)).setOnDragListener(new MyDragListener());
    (verticalCardPlace[5]=(LinearLayout)findViewById(R.id.sixth)).setOnDragListener(new MyDragListener());
    (verticalCardPlace[6]=(LinearLayout)findViewById(R.id.seventh)).setOnDragListener(new MyDragListener());
    for (int i=0;i<7;i++) verticalCardPlace[i].setLayoutParams(lp);
    
    
    
    ImageView card;
    
    //creating and shuffuling the deck
    Vector<Integer> v = new Vector<>();
    for(int i=1;i<53;i++) v.add(i);
    for(int i=0;i<SHUFFLE_TIMES;i++) Collections.shuffle(v);

    // Cards on the field in order to turn the while they are free
    FIELD = new int[7][7];
    int k=7;
    for(int i=0;i<7;i++) for(int j=0;j<i;j++) FIELD[i][j]=v.elementAt(k++);

    
    // setupField
    for (int i=1;i<7;i++){
    	for(int j=0;j<i;j++){
    	card = new ImageView(this);
        card.setImageDrawable(getResources().getDrawable(R.drawable.back));
    	//card.setImageDrawable(getResources().getDrawable(R.drawable.radius));
        card.setId(900+FIELD[i][j]);
        card.setVisibility(View.VISIBLE);
        card.setLayoutParams(fullcut);
        card.setScaleType(ScaleType.FIT_XY);
        verticalCardPlace[i].addView(card);
    	}
    }
    
    //show the first card of every column
    for(int i=0;i<7;i++){
    	if(v.get(i)==1||v.get(i)==14||v.get(i)==27||v.get(i)==40) FOUR_COUNTER++;
    	card = new ImageView(this);
        card.setImageDrawable(getResources().getDrawable(retCardImage(v.elementAt(i))));
        card.setId(retId(v.elementAt(i)));
        card.setVisibility(View.VISIBLE);
        card.setOnTouchListener(new MyTouchListener());
        card.setLayoutParams(full);
        card.setScaleType(ScaleType.FIT_XY);
        verticalCardPlace[i].addView(card);
    }
    
   
    

    //end init Field

    //Drawing
    toDraw = new Vector<>();
    for(int i=28;i<52;i++) toDraw.add(v.elementAt(i));
    
    ImageView drawView = (ImageView)findViewById(R.id.pushToDraw);
    drawView.setLayoutParams(lp2);
    drawView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(INDEX_TO_DRAW<0) INDEX_TO_DRAW=0;
			if(CARD_IN_PILE!=0){
			
			
			if(INCREMENT_TO_DRAW==1) {
				if(INDEX_TO_DRAW>=CARD_IN_PILE){
					INDEX_TO_DRAW=0;
					noDraw=false;
				}
			}else {
				if(CARD_IN_PILE>2){
							if(INDEX_TO_DRAW>=CARD_IN_PILE){
								INDEX_TO_DRAW=2;
								noDraw=false;
							}
				} else {
					if(CARD_IN_PILE==1){ INDEX_TO_DRAW = 1;
					}else {INDEX_TO_DRAW = 0; }
			}
			}
			
			
			
			LinearLayout draw = (LinearLayout)findViewById(R.id.draw); 
			ImageView showToDraw = new ImageView(getApplicationContext());//(ImageView)draw.getChildAt(0);
			draw.removeAllViews();
			
			if(INDEX_TO_DRAW<CARD_IN_PILE-INCREMENT_TO_DRAW){
				
			ImageView finish = (ImageView) v;
			finish.setImageDrawable(getResources().getDrawable(R.drawable.back));
			showToDraw.setImageDrawable(getResources().getDrawable(retCardImage(toDraw.elementAt(INDEX_TO_DRAW))));
			showToDraw.setLayoutParams(lp2);
			showToDraw.setScaleType(ScaleType.FIT_XY);
			showToDraw.setId(retId(toDraw.elementAt(INDEX_TO_DRAW)));
			INDEX_TO_DRAW+=INCREMENT_TO_DRAW;

			showToDraw.setOnTouchListener(new forDrawListener());
				draw.addView(showToDraw);
			}else{
				showToDraw.setImageDrawable(getResources().getDrawable(retCardImage(toDraw.elementAt(INDEX_TO_DRAW))));
				showToDraw.setId(retId(toDraw.elementAt(INDEX_TO_DRAW)));
				showToDraw.setLayoutParams(lp2);
				showToDraw.setScaleType(ScaleType.FIT_XY);
				INDEX_TO_DRAW+=INCREMENT_TO_DRAW;
				showToDraw.setOnTouchListener(new forDrawListener());
				draw.addView(showToDraw);
				if(!VEGAS_MODE){
				ImageView finish = (ImageView) v;
				finish.setImageDrawable(getResources().getDrawable(R.drawable.begin));
				finish.setLayoutParams(lp2);
				finish.setScaleType(ScaleType.FIT_XY);
				}else{					
					ImageView finish = (ImageView) v;
					finish.setImageDrawable(getResources().getDrawable(R.drawable.stop));
					finish.setLayoutParams(lp2);
					finish.setScaleType(ScaleType.FIT_XY);
					finish.setOnClickListener(null);
				}
			}
			if(INDEX_TO_DRAW>0){
			lastAction = new LastAction();
			lastAction.MOVE = DRAW;
			lastAction.INDEX  = INDEX_TO_DRAW;
			lastActionVector.add(lastAction);
			lastAction = new LastAction();
			ACTION_POINTER++;
			}
			}
		}
	});
    
    //end Drawing
    
    
    //BEginning Pits Handeling
    pit = new LinearLayout[4];                    

    
    (pit[0]= (LinearLayout)findViewById(R.id.depos1)).setOnDragListener(new forPitsDragListener());
    (pit[1] = (LinearLayout)findViewById(R.id.depos2)).setOnDragListener(new forPitsDragListener());
    (pit[2] = (LinearLayout)findViewById(R.id.depos3)).setOnDragListener(new forPitsDragListener());
    (pit[3] = (LinearLayout)findViewById(R.id.depos4)).setOnDragListener(new forPitsDragListener());
    
    ImageView[] empty = new ImageView[4];
    for(int i=0;i<4;i++) {
    	empty[i]=new ImageView(this);
    	empty[i].setImageDrawable(getResources().getDrawable(R.drawable.new_backas));
    	empty[i].setScaleType(ScaleType.FIT_XY);
    	empty[i].setLayoutParams(lp2);
    	empty[i].setId(800+i);
    	pit[i].addView(empty[i]);
    	pit[i].setLayoutParams(lp2);
    }

    
    //end Pits
    
    
    //make sure that the drop on special layout doesn't make the card to disappear!
    field = (LinearLayout)findViewById(R.id.Field);
    field.setOnDragListener(new doNotDisappear());
    LinearLayout silly = (LinearLayout)findViewById(R.id.silly);
    silly.setOnDragListener(new doNotDisappear());
    pits = (LinearLayout)findViewById(R.id.pits);
    draw = (LinearLayout)findViewById(R.id.draw);

    ((ImageView)draw.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.new_empty));
    ((ImageView)draw.getChildAt(0)).setLayoutParams(lp2);
    
    draw.setOnDragListener(new doNotDisappear());
    push = (ImageView)findViewById(R.id.pushToDraw);
    push.setLayoutParams(lp2);
    push.setScaleType(ScaleType.FIT_XY);
    push.setImageDrawable(getResources().getDrawable(R.drawable.back));
    push.setOnDragListener(new doNotDisappear());

    
    field3.setOnDragListener(new OnDragListener() {
		
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch(event.getAction()){
			
			case DragEvent.ACTION_DROP:
				
				ImageView card= (ImageView)  event.getLocalState(); //view moving
		        LinearLayout owner = (LinearLayout) card.getParent(); //owner
				for(int i=indexToMove;i<owner.getChildCount();i++)((ImageView)owner.getChildAt(i)).setVisibility(View.VISIBLE);
				break;

			case DragEvent.ACTION_DRAG_EXITED:
				ImageView card2= (ImageView)  event.getLocalState(); //view moving
		        LinearLayout owner2 = (LinearLayout) card2.getParent(); //owner
				for(int i=indexToMove;i<owner2.getChildCount();i++)((ImageView)owner2.getChildAt(i)).setVisibility(View.VISIBLE);
	        	//card.setVisibility(View.VISIBLE);

				break;
			
			case DragEvent.ACTION_DRAG_ENTERED:

				break;
			case DragEvent.ACTION_DRAG_ENDED:

				break;
			}
			return false;
		}
	});


    
    
    //BACK BUTTON STUFF
    //Button back = (Button)findViewById(R.id.back);
    moves= (TextView)findViewById(R.id.Column);
    moves.setTypeface(typeFace);
    moves.setText(getString(R.string.moves)+": "+NUMBER_MOVES);
    Button newGame = (Button)findViewById(R.id.dialogbutton2);
    newGame.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			ContentValues values = new ContentValues(); 
			  String[] whereArgs = { nameToUse };
		      if(powerFlags[0]==1) values.put("ice1", 1);
		      if(powerFlags[1]==1) values.put("ice2", 1);
		      if(powerFlags[2]==1) values.put("ice3", 1);
		      if(powerFlags[3]==1) values.put("thunder1", 1);
		      if(powerFlags[4]==1) values.put("thunder2", 1);
		      if(powerFlags[5]==1) values.put("thunder3", 1);
		      if(powerFlags[6]==1) values.put("earth1", 1);
		      if(powerFlags[7]==1) values.put("earth2", 1);
		      if(powerFlags[8]==1) values.put("earth3", 1);
		      if(powerFlags[9]==1) values.put("time1", 1);
		      if(powerFlags[10]==1) values.put("time2", 1);
		      if(powerFlags[11]==1) values.put("time3", 1);
		      if(powerFlags[12]==1) values.put("fire1", 1);
		      if(powerFlags[13]==1) values.put("fire2", 1);
		      if(powerFlags[14]==1) values.put("fire3", 1);
		      
		      if(values.size()!=0) db.update("achive", values, "name = ?",whereArgs);		     
		      
		      values = new ContentValues();
		      values.put("inarow", 0);
		      db.update("notes", values, "name = ?",whereArgs);
		      
		      
			final Dialog dialog = new Dialog(GameField.this,R.style.PauseDialog2);
			  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			  dialog.setContentView(R.layout.for_dialog);
			  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
			  a.setText(R.string.new_game);
			  a.setTypeface(typeFace);
			  Button one =(Button)dialog.findViewById(R.id.dialogbutton1);
			  Button two =(Button)dialog.findViewById(R.id.dialogbutton2);
			  one.setText(R.string.yes);
			  one.setTypeface(typeFace);	  
			  two.setText(R.string.no);
			  two.setTypeface(typeFace);	  
			  
			  one.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					db.close();
					for(int i=0;i<toClose.size();i++) toClose.get(i).dismiss();
		    	 	Intent positveActivity = new Intent(getApplicationContext(),GameField.class);
		            positveActivity.putExtra("playerName", nameToUse);
		            startActivity(positveActivity);
		            overridePendingTransition(R.anim.transition_away,R.anim.transition_in);
		            dialog.dismiss();
		            finish();
					
				}
			});
		      two.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		      
		     
			    
		     
		      dialog.setCanceledOnTouchOutside(false);
		      dialog.show();
			
		}
	});
    
    Button settings = (Button)findViewById(R.id.delete);
    settings.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 Intent i = new Intent(v.getContext(), SettingsActivity.class);
	            startActivityForResult(i, RESULT_SETTINGS);
		}
	});
    
    TextView name = (TextView)findViewById(R.id.name);
    name.setTypeface(typeFace);
    
    wintext = (TextView)findViewById(R.id.wintext);
    wintext.setTypeface(typeFace);
    win = (Button)findViewById(R.id.win);
    win.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			//
			ContentValues values = new ContentValues(); 
			  String[] whereArgs = { nameToUse };
		      if(powerFlags[0]==1) values.put("ice1", 1);
		      if(powerFlags[1]==1) values.put("ice2", 1);
		      if(powerFlags[2]==1) values.put("ice3", 1);
		      if(powerFlags[3]==1) values.put("thunder1", 1);
		      if(powerFlags[4]==1) values.put("thunder2", 1);
		      if(powerFlags[5]==1) values.put("thunder3", 1);
		      if(powerFlags[6]==1) values.put("earth1", 1);
		      if(powerFlags[7]==1) values.put("earth2", 1);
		      if(powerFlags[8]==1) values.put("earth3", 1);
		      if(powerFlags[9]==1) values.put("time1", 1);
		      if(powerFlags[10]==1) values.put("time2", 1);
		      if(powerFlags[11]==1) values.put("time3", 1);
		      if(powerFlags[12]==1) values.put("fire1", 1);
		      if(powerFlags[13]==1) values.put("fire2", 1);
		      if(powerFlags[14]==1) values.put("fire3", 1);
		      if(values.size()!=0) db.update("achive", values, "name = ?",whereArgs);
			
		      showWin();
			v.setClickable(false);
			//animate field
			Animation aaa = new ScaleAnimation(1f, 0f, // Start and end values for the X axis scaling
		            1, 0, // Start and end values for the Y axis scaling
		            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
		            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
			aaa.setDuration(500);
		    aaa.setFillAfter(true);
			for(int i=0;i<7;i++){
				for(int j=0;j<verticalCardPlace[i].getChildCount();j++){
					verticalCardPlace[i].getChildAt(j).startAnimation(aaa);
				}
				
			}
			
			Animation bbb = new ScaleAnimation(0f, 1f, // Start and end values for the X axis scaling
		            0, 1, // Start and end values for the Y axis scaling
		            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
		            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
			bbb.setDuration(500);
		    bbb.setFillAfter(true);
		    LinearLayout[] pittt = new LinearLayout[4];
		    
			pittt[0] = (LinearLayout)findViewById(R.id.depos1);
			pittt[1] = (LinearLayout)findViewById(R.id.depos2);
			pittt[2] = (LinearLayout)findViewById(R.id.depos3);
			pittt[3] = (LinearLayout)findViewById(R.id.depos4);
			Vector<Integer> lastKings = new Vector<>();
			Vector<Integer> toFill = new Vector<>();
			for(int i=1;i<5;i++) lastKings.add(i);
			
			for(int i=0;i<4;i++){
				if(retCorrectKing(((ImageView)pittt[i].getChildAt(0)).getId()/100)!=0){
				((ImageView)pittt[i].getChildAt(0)).setImageDrawable(getResources().getDrawable(retCorrectKing(((ImageView)pittt[i].getChildAt(0)).getId()/100)));
				((ImageView)pittt[i].getChildAt(0)).startAnimation(bbb);
				lastKings.remove(new Integer(((ImageView)pittt[i].getChildAt(0)).getId()/100));
				}else{
					toFill.add(i);
				}
			}
			

			for(int i=0;i<toFill.size();i++){

				ImageView aa = new ImageView(GameField.this);
				switch(lastKings.elementAt(i)){
				case 1:

					aa.setImageDrawable(getResources().getDrawable(R.drawable.kc));
					break;
				case 2:
					aa.setImageDrawable(getResources().getDrawable(R.drawable.kq));
					break;
				case 3:

					aa.setImageDrawable(getResources().getDrawable(R.drawable.kf));
					break;
				case 4:

					aa.setImageDrawable(getResources().getDrawable(R.drawable.kp));
					break;
				}
				
				pittt[toFill.get(i)].removeAllViews();
			    pittt[toFill.get(i)].addView(aa, 0);
			    aa.setVisibility(View.VISIBLE);
			    aa.startAnimation(bbb);
			    
			}
			if(draw.getChildCount()!=0){
			((ImageView)draw.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.new_empty));
			}
            //noinspection deprecation
            draw.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_empty));
			draw.setLayoutParams(lp2);
			push.setImageDrawable(getResources().getDrawable(R.drawable.begin));
			//
		
		}
	});
    
    
    //Animation
    
    //end animation
    
    
    chrono = (Chronometer)findViewById(R.id.chronometer1);
    chrono.setTypeface(typeFace);
    chrono.start();
    
//    Button freeze = (Button)findViewById(R.id.freeze);
//    freeze.setOnClickListener(new 
//    
    
    
    
    
    if(cursor.moveToNext()) name.setText(nameToUse);
   
    

    query = "SELECT exp FROM notes WHERE name = ? ORDER BY _id ASC";
    cursor = db.rawQuery(query, selectionArgs);
    cursor.moveToNext();
    previousPoint = Integer.parseInt(cursor.getString(0));
    if(previousPoint==0) firstMatch=true;
    
    query = "SELECT level FROM notes WHERE name = ? ORDER BY _id ASC";
    cursor = db.rawQuery(query, selectionArgs);
    cursor.moveToNext();
    int level = Integer.parseInt(cursor.getString(0));
    
    query = "SELECT mana FROM power WHERE name = ? ORDER BY _id ASC";
    cursor = db.rawQuery(query, selectionArgs);
    cursor.moveToNext();
    int mana = Integer.parseInt(cursor.getString(0));
    
    progMana = (ProgressBar)findViewById(R.id.mana);
    progMana.setMax(mana);


    progMana.setProgress(0);
    manaText = (TextView)findViewById(R.id.manaText); 
    manaText.setTypeface(typeFace);
    manaText.setText("MANA "+mana);
    
    int[] a =progressLVL(level, previousPoint);
    progBar.setMax(a[2]);
    progBar.setProgress(a[1]);
    
   lvl = (TextView)findViewById(R.id.lvl);
   lvl.setTypeface(typeFace);
   lvl.setText("LVL: "+level);
   
   
   query = "SELECT inarow FROM notes WHERE name = ? ORDER BY _id ASC";
   cursor = db.rawQuery(query, selectionArgs);
   cursor.moveToFirst();
   inARow = cursor.getInt(0);
   
   String tableSelected = "";
   switch(MATCH_TYPE){
   		case MATCH_NORMAL:
   			query = "SELECT "+ TableNotes.COLUMN_MATCH +" FROM normal WHERE name = ? ORDER BY _id ASC";
   			tableSelected = "normal";
   			break;
   		case MATCH_THREE:
   			query = "SELECT "+ TableNotes.COLUMN_MATCH +" FROM three WHERE name = ? ORDER BY _id ASC";
   			tableSelected = "three";
   			break;
   		case MATCH_VEGAS:
   			query = "SELECT "+ TableNotes.COLUMN_MATCH +" FROM vegas WHERE name = ? ORDER BY _id ASC";
   			tableSelected = "vegas";
   			break;
   		case MATCH_BOTH:
   			query = "SELECT "+ TableNotes.COLUMN_MATCH +" FROM three WHERE name = ? ORDER BY _id ASC";
   			//query2 = "SELECT "+ TableNotes.COLUMN_MATCH +" FROM vegas WHERE name = ? ORDER BY _id ASC";
   			tableSelected = "vegas";
   			break;
   }
   
   cursor = db.rawQuery(query, selectionArgs);
   cursor.moveToNext();
   int totalMatch = Integer.parseInt(cursor.getString(0));
   ContentValues values = new ContentValues(); 
   values.put("match", ""+(totalMatch+1));
   //String whereClause = "name = ? ";
   String[] whereArgs = { nameToUse };
   db.update(tableSelected, values, "name = ?",whereArgs);
   
   //
   if(FOUR_COUNTER==4){
   	values = new ContentValues();
   	values.put("fouras",1);
   	db.update("achive", values, "name = ?",whereArgs);
   }
   
   //set flags ach powers
   //values = new ContentValues();
   query = "SELECT "+"ice1," +"ice2,"+"ice3,"+"thunder1,"+"thunder2,"+"thunder3,"+"earth1,"+"earth2,"+"earth3,"+"fire1,"+"fire2,"+"fire3,"+"time1,"+"time2,"+"time2"+" FROM achive WHERE name = ? ORDER BY _id ASC";
   cursor = db.rawQuery(query, selectionArgs);
   cursor.moveToFirst();
   for(int i=0;i<15;i++) powerFlags[i] = cursor.getInt(i);
   
   
   
   Button stats = (Button)findViewById(R.id.stats);
   stats.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		
		Intent positveActivity = new Intent(getApplicationContext(),Stats.class);
        positveActivity.putExtra("playerName", nameToUse);
        startActivity(positveActivity);
        
	}
   });
   Button ach = (Button)findViewById(R.id.achbutton);
   ach.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		//db.close();
		Intent positveActivity = new Intent(getApplicationContext(),Achivement.class);
        positveActivity.putExtra("playerName", nameToUse);
        startActivity(positveActivity);
        overridePendingTransition(R.anim.transition5,R.anim.transition6);
        //TODO
	}
   });
   
   //powers
   query = "SELECT * FROM power WHERE name = ? ORDER BY _id ASC";
   cursor = db.rawQuery(query, selectionArgs);
   cursor.moveToNext();
   int levelIce = Integer.parseInt(cursor.getString(2));
   int levelFire = Integer.parseInt(cursor.getString(3));
   int levelEarth = Integer.parseInt(cursor.getString(4));
   int levelThunder = Integer.parseInt(cursor.getString(5));
   int levelTime = Integer.parseInt(cursor.getString(6));
   
   List<String> spinnerArray ;
   ArrayAdapter<String> adapter;
   
   LinearLayout powers = (LinearLayout)findViewById(R.id.powers);
   
   LinearLayout toPutIce = (LinearLayout)getLayoutInflater().inflate(R.layout.power_layout, null);
   icespinner = (Spinner) toPutIce.getChildAt(0);
   icespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
    	   //TextView v = (TextView)view;
    	   //v.setTypeface(typeFace);
    	   int cost=0;
   			switch(pos){
   			case 0:
   				cost = 20;
   				break;
   			case 1:
   				cost = 50;
   				break;
   			case 2:
   				cost = 100;
   				break;
   			}
          ((Button)((LinearLayout)((Spinner)view.getParent()).getParent()).getChildAt(1)).setText(""+cost);
      }
      public void onNothingSelected(AdapterView<?> parent) {
      }
}); 
   Button iceUse = (Button)toPutIce.getChildAt(1);
   iceUse.setOnClickListener(new IceButton());
      //noinspection deprecation
      iceUse.setBackgroundDrawable(getResources().getDrawable(R.drawable.icebu));
   iceUse.setTypeface(typeFace);
   
   LinearLayout toPutFire = (LinearLayout)getLayoutInflater().inflate(R.layout.power_layout, null);
   firespinner = (Spinner) toPutFire.getChildAt(0);
   Button fireUse = (Button)toPutFire.getChildAt(1);
      //noinspection deprecation
      fireUse.setBackgroundDrawable(getResources().getDrawable(R.drawable.firebu));
   fireUse.setTypeface(typeFace);
   fireUse.setOnClickListener(new FireButton());
   firespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
        	TextView v = (TextView)view;
     	   //v.setTypeface(typeFace);
               //String level = v.getText().toString();
               //int cost = 0;
               ((Button)((LinearLayout)((Spinner)view.getParent()).getParent()).getChildAt(1)).setText(""+100);
           }
           public void onNothingSelected(AdapterView<?> parent) {
           }
   }); 
   
   LinearLayout toPutEarth = (LinearLayout)getLayoutInflater().inflate(R.layout.power_layout, null);
   earthspinner = (Spinner) toPutEarth.getChildAt(0);
   earthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
    	   //TextView v = (TextView)view;
    	   //v.setTypeface(typeFace);
    	   int cost=0;
   			switch(pos){
   			case 0:
   				cost = 50;
   				break;
   			case 1:
   				cost = 100;
   				break;
   			case 2:
   				cost = 150;
   				break;
   			}
          ((Button)((LinearLayout)((Spinner)view.getParent()).getParent()).getChildAt(1)).setText(""+cost);
      }
      public void onNothingSelected(AdapterView<?> parent) {
      }
   }); 
   Button earthUse = (Button)toPutEarth.getChildAt(1);
   earthUse.setOnClickListener(new EarthButton());
      //noinspection deprecation
      earthUse.setBackgroundDrawable(getResources().getDrawable(R.drawable.earthbu));
   earthUse.setTypeface(typeFace);
   
   LinearLayout toPutThunder = (LinearLayout)getLayoutInflater().inflate(R.layout.power_layout, null);
   thunderspinner = (Spinner) toPutThunder.getChildAt(0);
   thunderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
    	   //TextView v = (TextView)view;
    	   //v.setTypeface(typeFace);
    	   int cost=0;
   			switch(pos){
   			case 0:
   				cost = 30;
   				break;
   			case 1:
   				cost = 70;
   				break;
   			case 2:
   				cost = 100;
   				break;
   			}
          ((Button)((LinearLayout)((Spinner)view.getParent()).getParent()).getChildAt(1)).setText(""+cost);
      }
      public void onNothingSelected(AdapterView<?> parent) {
      }
   }); 
   Button thunderUse = (Button)toPutThunder.getChildAt(1);
   thunderUse.setOnClickListener(new ThunderButton());
      //noinspection deprecation
      thunderUse.setBackgroundDrawable(getResources().getDrawable(R.drawable.thunderbu));
   thunderUse.setTypeface(typeFace);
   
   LinearLayout toPutTime = (LinearLayout)getLayoutInflater().inflate(R.layout.power_layout, null);
   timespinner = (Spinner) toPutTime.getChildAt(0);
   timespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
   		//TextView v = (TextView)view ;
   		
 	   //v.setTypeface(typeFace);
          ((Button)((LinearLayout)((Spinner)view.getParent()).getParent()).getChildAt(1)).setText(""+200);
      }
      public void onNothingSelected(AdapterView<?> parent) {
      }
   }); 
   Button timeUse = (Button)toPutTime.getChildAt(1);
   timeUse.setOnClickListener(new TimeButton());
      //noinspection deprecation
      timeUse.setBackgroundDrawable(getResources().getDrawable(R.drawable.timebu));
   timeUse.setTypeface(typeFace);

    
   switch(levelIce){
   
   case 1:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("ICE1");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   icespinner.setAdapter(adapter);
	   powers.addView(toPutIce);
	   
	   break;
   case 2:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("ICE1");
	   spinnerArray.add("ICE2");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   icespinner.setAdapter(adapter);
	   powers.addView(toPutIce);
	   break;
   case 3:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("ICE1");
	   spinnerArray.add("ICE2");
	   spinnerArray.add("ICE3");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   icespinner.setAdapter(adapter);
	   powers.addView(toPutIce);
	   break;
	   
   }
   //
   
switch(levelFire){
   
   case 1:

	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("FIRE1");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   firespinner.setAdapter(adapter);
	   powers.addView(toPutFire);
	   break;
   case 2:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("FIRE1");
	   spinnerArray.add("FIRE2");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   firespinner.setAdapter(adapter);
	   powers.addView(toPutFire);
	   break;
   case 3:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("FIRE1");
	   spinnerArray.add("FIRE2");
	   spinnerArray.add("FIRE3");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   firespinner.setAdapter(adapter);
	   powers.addView(toPutFire);
	   break;
   }

switch(levelThunder){

case 1:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("THUNDER1");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   thunderspinner.setAdapter(adapter);
	   powers.addView(toPutThunder);
	   break;
case 2:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("THUNDER1");
	   spinnerArray.add("THUNDER2");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   thunderspinner.setAdapter(adapter);
	   powers.addView(toPutThunder);
	   break;
case 3:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("THUNDER1");
	   spinnerArray.add("THUNDER2");
	   spinnerArray.add("THUNDER3");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   thunderspinner.setAdapter(adapter);
	   powers.addView(toPutThunder);
	   break;
}
  
switch(levelEarth){

case 1:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("EARTH1");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   earthspinner.setAdapter(adapter);
	   powers.addView(toPutEarth);
	   break;
case 2:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("EARTH1");
	   spinnerArray.add("EARTH2");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   earthspinner.setAdapter(adapter);
	   powers.addView(toPutEarth);
	   break;
case 3:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("EARTH1");
	   spinnerArray.add("EARTH2");
	   spinnerArray.add("EARTH3");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   earthspinner.setAdapter(adapter);
	   powers.addView(toPutEarth);
	   break;
}  
   

switch(levelTime){

case 1:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("TIME1");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   timespinner.setAdapter(adapter);
	   powers.addView(toPutTime);
	   break;
case 2:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("TIME1");
	   spinnerArray.add("TIME2");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   timespinner.setAdapter(adapter);
	   powers.addView(toPutTime);
	   break;
case 3:
	   spinnerArray =  new ArrayList<>();
	   spinnerArray.add("TIME1");
	   spinnerArray.add("TIME2");
	   spinnerArray.add("TIME3");
	   adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
	   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   timespinner.setAdapter(adapter);
	   powers.addView(toPutTime);
	   break;
}  







   //end powers
   
   
   
  }//fine onCreate
  private View popupView;
  private PopupWindow popupWindow;
  
  void showpopUp(String ach){
	  LayoutInflater layoutInflater 
	     = (LayoutInflater)getBaseContext()
	      .getSystemService(LAYOUT_INFLATER_SERVICE);  
	    popupView = layoutInflater.inflate(R.layout.popup, null);  
	             popupWindow = new PopupWindow(
	               popupView, 
	               LayoutParams.MATCH_PARENT,  
	                     LayoutParams.WRAP_CONTENT);  
	       TextView achi = (TextView)popupView.findViewById(R.id.textView1);      
	             achi.setText(ach);
	             achi.setTypeface(typeFace);
	             popupWindow.setAnimationStyle(R.style.MyAnim);
	             popupWindow.showAtLocation((RelativeLayout)findViewById(R.id.RelativeLayout2), Gravity.TOP|Gravity.CENTER, 0, 0);
	             toClose.add(popupWindow);
	             //popupWindow.showAsDropDown((LinearLayout)findViewById(R.id.compButton), -100, -100);
	             final Timer t = new Timer();
	             t.schedule(new TimerTask() {
	            public void run() {
	            	runOnUiThread(new Runnable(){
	            		public void run(){
	            		popupWindow.dismiss(); // when the task active then close the dialog
	            		}
	            		});
	            t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
	            	}
	            },2500);
	               


  }
  
  void showpopUp(int ach){
	  LayoutInflater layoutInflater 
	     = (LayoutInflater)getBaseContext()
	      .getSystemService(LAYOUT_INFLATER_SERVICE);  
	    popupView = layoutInflater.inflate(R.layout.popup, null);  
	             popupWindow = new PopupWindow(
	               popupView, 
	               LayoutParams.MATCH_PARENT,  
	                     LayoutParams.WRAP_CONTENT);  
	       TextView achi = (TextView)popupView.findViewById(R.id.textView1);      
	             achi.setText(getResources().getText(ach));
	             achi.setTypeface(typeFace);
	             popupWindow.setAnimationStyle(R.style.MyAnim);
	             popupWindow.showAtLocation((RelativeLayout)findViewById(R.id.RelativeLayout2), Gravity.TOP|Gravity.CENTER, 0, 0);
	             toClose.add(popupWindow);
	             //popupWindow.showAsDropDown((LinearLayout)findViewById(R.id.compButton), -100, -100);
	             final Timer t = new Timer();
	             t.schedule(new TimerTask() {
	            public void run() {
	            	runOnUiThread(new Runnable(){
	            		public void run(){
	            		popupWindow.dismiss(); // when the task active then close the dialog
	            		}
	            		});
	            t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
	            	}
	            },2500);
	               


  }
  
  
  
  int retCorrectKing(int value){
	  switch(value){
	  case 1:
		  return R.drawable.kc;
	  case 2:
		  return R.drawable.kq;
	  case 3:
		  return R.drawable.kf;
	  case 4:
		  return R.drawable.kp;

	  }
	  return 0;
  }


    @Override
  public void onBackPressed() {
	  db.close();
	  for(int i=0;i<toClose.size();i++) toClose.get(i).dismiss();
	  Intent positveActivity = new Intent(getApplicationContext(),MainActivity.class);
      positveActivity.putExtra("playerName", nameToUse);
      startActivity(positveActivity);
      overridePendingTransition(R.anim.transition3,R.anim.transition4);
      finish();
  }
  
	 @Override
	    protected void onPause() {
	        super.onPause();
	        stopClock();
	    }
	 
	 @Override
	 protected void onDestroy(){
		 super.onDestroy();
		 db.close();
	 }
	 
	 @Override
	    protected void onResume() {
	        super.onResume();
	        restartClock();
	    }
	
	 void stopClock(){
		 
		 timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
		 chrono.stop();
	 }
	 
	 void restartClock(){
		 
		 chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
		 chrono.start();
	 }
  
	 LinearLayout lay2;
	 LinearLayout lay3;
	 Spinner shuffle;
	 Spinner spin;
	 int actual;
  
/*------->BEGIN POWERS*/ //TODO POWERS
private Button toBlock;
	 private Button myButtonToIce;
	 Button myButtonToFire;
	 private Button myButtonToEarth;
	 Button myButtonToTime;
	 private Button myButtonToThunder;
	 
	 void burnMana(View v){
		 			toBlock = (Button)v;
		 			v.setClickable(false);
	    		  progress = progMana.getProgress() + manaCost;
	    		  
	    		  Thread thread = new Thread(){
	  			    @Override
	  			    public void run() {
	  			    	
	  			    	           for(int i=0;i<manaCost/2;i++){
	  			    	            	try{
	  			    	            	manaThread  mm = new manaThread();
	  			    	            	runOnUiThread(mm);
	  			    	            	synchronized(mm){
	  			    	            		mm.wait();
	  			    	            	}
	  			    	               
	  			    	            } catch (InterruptedException e) {
	  			    	                e.printStackTrace();
	  			    	            }
	  			    	           toBlock.setClickable(true); 	
	  			    	           }
	  			    	           
	  			    }

	  			};
	  			thread.start();
	 }
	 
	 
	 private class manaThread extends Thread{
		 @Override
		 public void run(){
			 synchronized(this){
			 try{
			 progMana.incrementProgressBy(2);
			 manaText.setText("MANA "+(progMana.getMax() - progMana.getProgress()));
			  Thread.sleep(0, 1);
			  notify();
			 }catch(InterruptedException ignored){}
			 }
		 }
		 }
	 
	 private class ThunderButton implements  OnClickListener{
		 @Override
		public void onClick(View v) {
			 myButtonToThunder = (Button)v;
			 
			 String powerLevel = thunderspinner.getSelectedItem().toString();

				int caseToUse=0;
				if(powerLevel.compareTo("THUNDER1")==0) caseToUse = 0;
				if(powerLevel.compareTo("THUNDER2")==0) caseToUse = 1;
				if(powerLevel.compareTo("THUNDER3")==0) caseToUse = 2;
				switch(caseToUse){
				case 0:
					if(progMana.getMax() - progress >=30){
						myButtonToThunder.setEnabled(false);
						if(powerFlags[3]==0&&!firstMatch){
							powerFlags[3]=1;
							showpopUp("Thunder Novice");
						}
					manaCost=30;
					LinearLayout barer = (LinearLayout)findViewById(R.id.compButton);
					LinearLayout help = (LinearLayout)barer.getChildAt(0);
					barer.setVisibility(View.VISIBLE);
					barer.setLayoutParams(forCompare);
					for(int i=0;i<7;i++) help.getChildAt(i).setOnClickListener(new forRealShuffleListener());
				      burnMana(v);
					}
					
					break;
				case 1:
					if(progMana.getMax() - progress >=70){
						if(powerFlags[4]==0&&!firstMatch){
							powerFlags[4]=1;
							showpopUp("Thunder Adept");
						}
						myButtonToThunder.setEnabled(false);
					manaCost=70;
					//LinearLayout field = (LinearLayout)findViewById(R.id.Field);
					//LinearLayout[] columns = new LinearLayout[6];
					int[] countPerColumn = new int[6];
					Vector<Integer> toBeShuffled = new Vector<>();
					for(int i=0;i<6;i++){
						countPerColumn[i]=0;
						for(int j=0;j<verticalCardPlace[i].getChildCount();j++){
							
							if(((ImageView)verticalCardPlace[i].getChildAt(j)).getId()/100==9) {

								toBeShuffled.add(FIELD[i+1][j]);
								countPerColumn[i]++;
							}
						}
					}
					
					
					Collections.shuffle(toBeShuffled);
					
					int k=0;
					
					for(int i=1;i<7;i++){
						Log.d("first","for");
						for(int j=0;j<countPerColumn[i-1];j++){
							Log.d("second","for");
							FIELD[i][j] = toBeShuffled.get(k);
							((ImageView)verticalCardPlace[i].getChildAt(j)).setId(900+toBeShuffled.get(k));
							((ImageView)verticalCardPlace[i].getChildAt(j)).setImageDrawable(getResources().getDrawable(R.drawable.back));
							k++;
						}
					}
					//check
					lunchShuffleThread();
		        	 //
					burnMana(v);
					} 
					
					break;
				case 2:
					
					if(progMana.getMax() - progress >=100){
						if(powerFlags[5]==0&&!firstMatch){
							powerFlags[5]=1;
							showpopUp("Thunder Master");
						}
					myButtonToThunder.setEnabled(false);
					manaCost=100;
					LinearLayout field2 = (LinearLayout)findViewById(R.id.Field);
					LinearLayout[] columns2 = new LinearLayout[6];
					int[] countPerColumn2 = new int[6];
					for(int i=0;i<6;i++) columns2[i]=(LinearLayout)field2.getChildAt(i+1);
					Vector<Integer> toBeShuffled2 = new Vector<>();
					for(int i=0;i<6;i++){
						countPerColumn2[i]=0;
						for(int j=0;j<columns2[i].getChildCount();j++){
							
							if(((ImageView)columns2[i].getChildAt(j)).getId()/100==9) {

								toBeShuffled2.add(FIELD[i+1][j]);
								countPerColumn2[i]++;
							}
						}
					}
					
					//add
					if(CARD_IN_PILE>0){
							toBeShuffled2.addAll(toDraw);
							INDEX_TO_DRAW=0;
							((ImageView)draw.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.new_empty));
							draw.setBackgroundColor(Color.TRANSPARENT);
							((ImageView)draw.getChildAt(0)).setOnTouchListener(null);
							((ImageView)draw.getChildAt(0)).setLayoutParams(lp2);
					}
					//
					
					
					Collections.shuffle(toBeShuffled2);

					int k=0;
					
					for(int i=0;i<6;i++){
						for(int j=0;j<countPerColumn2[i];j++){	
							FIELD[i+1][j] = toBeShuffled2.elementAt(k);
							((ImageView)columns2[i].getChildAt(j)).setId(900+toBeShuffled2.elementAt(k++));
						}
					}
					toDraw = new Vector<>();
					
					for(int i=0;i<CARD_IN_PILE;i++)
						toDraw.add(toBeShuffled2.elementAt(k+i));
					burnMana(v);
					lunchShuffleThread();
					}
					
					
					break;
				}
				
				
				
			
		}
	 }
	 
	 void lunchShuffleThread(){
		 Thread thread = new Thread()
			{
			    @Override
			    public void run() {
                    int i = 0;
			    	GLOBAL_CONT=0;
			    	        for(i=0;i<7;i++){ 
			    	        	Log.d("primo for",""+i);
			    	           
			    	            	try{
			    	            	runDisapp  rr = new runDisapp();
			    	            	runOnUiThread(rr);
			    	            	synchronized(rr){
			    	            		rr.wait();
			    	            	}
			    	            	
			    	                	
			    	            
			    	               
			    	            } catch (InterruptedException e) {
			    	                e.printStackTrace();
			    	            }

			    	        } //end first for

					    	GLOBAL_CONT=0;
					    	for(i=0;i<7;i++){ 
					    	     try{
					           runApp  rr = new runApp();
				 	           runOnUiThread(rr);
	    	            	synchronized(rr){				
	    	            		rr.wait();
					    	            	}
					    	            } catch (InterruptedException e) {
					    	                e.printStackTrace();
					    	            }
					    	        }
					    	runOnUiThread(new Runnable(){
					    		public void run(){
					    			myButtonToThunder.setEnabled(true);
					    		}
					    	});
					    	
			    }
			};
			thread.start();
			
			 
	 }
	 
	 private class runDisapp extends Thread{
		 public void run(){
    		  synchronized(this){
    			  try{
    			  Thread.sleep(150);
    		  for(int j=0;j<verticalCardPlace[GLOBAL_CONT].getChildCount();j++){
    			  
    		  if(((ImageView)verticalCardPlace[GLOBAL_CONT].getChildAt(j)).getId()/100==9) {
    			  
    			  ((ImageView)verticalCardPlace[GLOBAL_CONT].getChildAt(j)).setVisibility(View.INVISIBLE);
    		  }
    		  }
    			  }catch(InterruptedException ignored){}
    			  GLOBAL_CONT++;
    		  notify();
    	  }
    	  }
	 }
	 
	 private class runApp extends Thread{
		 public void run(){
    		  synchronized(this){
    			  try{
    			  Thread.sleep(150);
    		  for(int j=0;j<verticalCardPlace[GLOBAL_CONT].getChildCount();j++){
    			  
    		  if(((ImageView)verticalCardPlace[GLOBAL_CONT].getChildAt(j)).getId()/100==9) {
    			  
    			  ((ImageView)verticalCardPlace[GLOBAL_CONT].getChildAt(j)).setVisibility(View.VISIBLE);
    		  }
    		  }
    			  }catch(InterruptedException ignored){}
    			  GLOBAL_CONT++;
    		  notify();
    	  }
    	  }
	 }
	 
	 
	 Vector<Integer> comparePlusInd(Vector<Integer> list){
		 Vector<Integer> ret = new Vector<>();
		 for(int i=0;i<list.size();i++) ret.add(i);
		 
		 for(int i=0; i<list.size()-1; i++) 
		 { 
		 int min = i;

		 for(int j=i+1; j<list.size(); j++) 
		   if(list.elementAt(j) >= list.elementAt(min)) //cambiare questa condizione per invertire l'ordine 
		     min = j;

		 int temp = list.elementAt(min);
		 list.set(min, list.elementAt(i));
		 list.set(i,temp);
		 
		 int temp2 = ret.elementAt(min);
		 ret.set(min, ret.elementAt(i));
		 ret.set(i,temp2);
		 }
		 
	
		 return ret;
	 }
	 private int columnToSort;
	 private TextView toMess;
	 
	 private class forRealShuffleListener implements OnClickListener{
		 @Override
		 public void onClick(View v){
			 columnToSort = ((LinearLayout)v.getParent()).indexOfChild(v);
        	 //LinearLayout field = (LinearLayout)findViewById(R.id.Field);
        	 LinearLayout toShuffle = verticalCardPlace[columnToSort];
        	 int counter = 0;
        	 for(int i=0;i<toShuffle.getChildCount();i++){
        		 if(((ImageView)toShuffle.getChildAt(i)).getId()/100==9) counter++;
        	 }
        	 Vector<Integer> saveElements = new Vector<>();
        	 for(int j=0;j<counter;j++){
        		 saveElements.add(FIELD[columnToSort][j]);
        	 }
        	 
        	 
        	 //Collections.shuffle(saveElements);
        	 Vector<Integer> ordered = new Vector<>();
				Vector<Integer> cent = new Vector<>();
				int id;
				//Collections.shuffle(toBeShuffled);
				for(int i=0;i<saveElements.size();i++){
					id = retId(saveElements.elementAt(i));
					ordered.add(id - (id/100)*100);
					cent.add((id/100)*100);
				}
				
				Vector<Integer> forPrint = comparePlusInd(ordered);
				
				
				for(int i=0;i<forPrint.size();i++){
					
					saveElements.set(i, retValueFromId(ordered.get(i)+cent.get(forPrint.get(i))));
					
				}
				
				numberCardsToSort = forPrint.size();
				
        	 for(int j=0;j<counter;j++){
        		 FIELD[columnToSort][j]=saveElements.elementAt(j);
        		 ((ImageView)toShuffle.getChildAt(j)).setId(900+saveElements.elementAt(j));
        		 ((ImageView)toShuffle.getChildAt(j)).setImageDrawable(getResources().getDrawable(R.drawable.back));
        	 } 
        	 
        	 runOnUiThread(new Runnable() {
           	  public void run() {
           		  new MahClass3().execute(new Void[3]);
           	  }
           	});
        	 
        	 
        	((LinearLayout)findViewById(R.id.compButton)).setVisibility(View.INVISIBLE);
        	((LinearLayout)findViewById(R.id.compButton)).setLayoutParams(forScompare);
		 }
	 }
	 
	 private class FireButton implements  OnClickListener{
		 @Override
		public void onClick(View v) {
			 if(progMana.getMax() - progMana.getProgress()>=100){
				 boolean goFor = true;
				 String powerLevel = null;
				 ArrayAdapter<String> remove;
				 try{
					powerLevel = firespinner.getSelectedItem().toString();
				 }catch(Exception e){goFor=false;}
				
				if(goFor){
					
					LinearLayout barer = (LinearLayout)findViewById(R.id.compButton);
					LinearLayout help = (LinearLayout)barer.getChildAt(0);
					barer.setLayoutParams(forCompare);
					barer.setVisibility(View.VISIBLE);
					for(int i=0;i<7;i++) help.getChildAt(i).setOnClickListener(new forShuffleListener());
                    //noinspection unchecked
                    remove = (ArrayAdapter<String>)firespinner.getAdapter();
					if(powerLevel.compareTo("FIRE1") == 0){
						if(powerFlags[9]==0&&!firstMatch){
							powerFlags[9]=1;
							showpopUp("Fire Novice");
						}
						remove.remove("FIRE1");
					}
					if(powerLevel.compareTo("FIRE2") == 0) {
						remove.remove("FIRE2");
						if(powerFlags[10]==0&&!firstMatch){
							powerFlags[10]=1;
							showpopUp("Fire Adept");
						}
					}
					if(powerLevel.compareTo("FIRE3") == 0) {
						remove.remove("FIRE3");
						if(powerFlags[11]==0&&!firstMatch){
							powerFlags[11]=1;
							showpopUp("Fire Master");
						}
					}
				manaCost=100;
				
				      
				      if(remove.isEmpty()){
							LinearLayout powerLay = (LinearLayout)firespinner.getParent();
							LinearLayout allPowerLay = (LinearLayout)powerLay.getParent();
							allPowerLay.removeView(powerLay);
						}
				      
						burnMana(v);	
				}
				
			 }

				
		 }
	 }
	 
	 private ImageView[][] buttonMatrix;
	 int col;	 
	 private class forShuffleListener implements OnClickListener{
		 @Override
		 public void onClick(View v){
			 try{
	        	 LinearLayout field = (LinearLayout)findViewById(R.id.Field);
	        	 LinearLayout col = (LinearLayout)field.getChildAt(((LinearLayout)v.getParent()).indexOfChild(v)*2);
	        	 int count = col.getChildCount();
	        	 ImageView show = (ImageView)col.getChildAt(count-1);
	        	 ImageView under = (ImageView)col.getChildAt(count-2);
	        	 int idTemp = show.getId();
	        	 show.setId(retId(under.getId()-900));
	        	 show.setImageDrawable(getResources().getDrawable(retCardImageFromId(retId(under.getId()-900))));
	        	 under.setId(900+retValueFromId(idTemp));
	        	 FIELD[((LinearLayout)v.getParent()).indexOfChild(v)][count-2] = retValueFromId(idTemp); 
	        	 ((LinearLayout)findViewById(R.id.compButton)).setLayoutParams(forScompare);
	        	 }catch(Exception ignored){

             }
		 }
	 }
	 
	 private Dialog dialogEarth;
	 
	 private class EarthButton implements  OnClickListener{
		 @Override
		public void onClick(View v) {
			myButtonToEarth = (Button)v;
			
			 String powerLevel = earthspinner.getSelectedItem().toString();
				int caseToUse=0;
				if(powerLevel.compareTo("EARTH1")==0) caseToUse = 0;
				if(powerLevel.compareTo("EARTH2")==0) caseToUse = 1;
				if(powerLevel.compareTo("EARTH3")==0) caseToUse = 2;
				//AlertDialog.Builder alertDialogBuilder;
				//AlertDialog alertDialog;
				switch(caseToUse){
				case 0:
					
					if(progMana.getMax() - progMana.getProgress() >= 50){
					manaCost=50;
					if(powerFlags[6]==0&&!firstMatch){
						powerFlags[6]=1;
						showpopUp("Earth Novice");
					}
					myButtonToEarth.setEnabled(false);
					
				      buttonMatrix = new ImageView[7][7];
				      int[] counter = new int[7];
				      for(int i=0;i<7;i++){
							for(int j=0;j<verticalCardPlace[i].getChildCount();j++)
							((ImageView)verticalCardPlace[i].getChildAt(j)).setOnTouchListener(null);
						}
				      
				      for(int i = 1;i<7;i++){
				    	  counter[i]=0;
				    	  for(int j=0;j<i;j++){
				    		  try{
				    		  if(((ImageView)verticalCardPlace[i].getChildAt(j)).getId()/100==9){
				    			  buttonMatrix[i][j] = (ImageView)verticalCardPlace[i].getChildAt(j);
				    			  buttonMatrix[i][j].setOnTouchListener(new forEarthButtonListener());
				    			  buttonMatrix[i][j].setLayoutParams(fullforearth);
				    			  counter[i]++;
				    		  }
				    		  }catch(Exception ignored){}
				    	  }
				      }
				      
				     
				      
				     
				      burnMana(v);
				}
					
					break;
				case 1:
					if(progMana.getMax() - progMana.getProgress()>=100){
						myButtonToEarth.setEnabled(false);
						
						if(powerFlags[7]==0&&!firstMatch){
							powerFlags[7]=1;
							showpopUp("Earth Adept");
						}
					   
						
					manaCost=100;
					
					
					
					
					
					
					dialogEarth = new Dialog(GameField.this,R.style.PauseDialog);
					  dialogEarth.requestWindowFeature(Window.FEATURE_NO_TITLE);
					  dialogEarth.setContentView(R.layout.my_picker_theme);
					  TextView a = (TextView)dialogEarth.findViewById(R.id.dialogText);
					  a.setText(R.string.card_to_find);
					  a.setTypeface(typeFace);
					  Button one =(Button)dialogEarth.findViewById(R.id.dialogbutton1);
					  //Button two =(Button)dialog.findViewById(R.id.dialogbutton2);
					  toMess = (TextView)dialogEarth.findViewById(R.id.achtext);
					  toMess.setTypeface(typeFace);
					  //two.setTypeface(typeFace);
					  //two.setText("FORGET IT");
					  one.setText(R.string.find_it);
					  one.setTypeface(typeFace);	  
					 	  
					  
					  

				        
				        numbPick = (NumberPicker)dialogEarth.findViewById(R.id.numberPicker1);
					    numbPick.setMinValue(0);
					    numbPick.setMaxValue(12);
					    numbPick.setDisplayedValues(new String[]{"A", "2", "3", "4", "5","6","7","8","9","10","J","Q","K"});
					    //mMinuteSpinner.setOnLongPressUpdateInterval(100);
					    seedPick = (NumberPicker) dialogEarth.findViewById(R.id.numberPicker2);
					    seedPick.setMinValue(0);
					    seedPick.setMaxValue(3);
					    seedPick.setDisplayedValues(new String[]{"\u2764","\u2666","\u2663","\u2660"});
					    one.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								myButtonToEarth.setEnabled(true);
								int number = retCode(numbPick.getDisplayedValues()[numbPick.getValue()], seedPick.getDisplayedValues()[seedPick.getValue()]);

					        	 for(int i=1 ; i<7;i++){
					        		 for(int j=0;j<i;j++){
					        			 if(number == FIELD[i][j]){
					        				 if(((ImageView)verticalCardPlace[i].getChildAt(j)).getId()/100==9){
					        					 ((ImageView)verticalCardPlace[i].getChildAt(j)).setImageDrawable(getResources().getDrawable(retCardImage(number)));
					        					 dialogEarth.dismiss();
					        				 }else{
					        					 toMess.setText(R.string.should_know);
					        				 }
					        			 }else{
					        				 toMess.setText(R.string.not_in_field);
					        			 }
					        		 }
					        	 }
							}
						});
				      
				      
					    
				      dialogEarth.setCanceledOnTouchOutside(false);
				      dialogEarth.show();
					
				     
					
				      burnMana(v);
				}
					
					
					
					break;
				case 2:
					if(progMana.getMax() - progMana.getProgress()>=150){
					manaCost=150;
					if(powerFlags[8]==0&&!firstMatch){
						powerFlags[8]=1;
						showpopUp("Earth Master");
					}
					myButtonToEarth.setEnabled(true);
					SHOW_UNDER_MOVE = true;

					burnMana(v);
					}
					
					break;
				}
			 

				
				
		}
	 }
	 private class forEarthButtonListener implements OnTouchListener{
		 
		 
		 
		 @Override
		public boolean onTouch(View v, MotionEvent e) {
			 ImageView a = (ImageView)v;
			 
			 myButtonToEarth.setEnabled(true);
			a.setImageDrawable(getResources().getDrawable(retCardImage(v.getId() - (v.getId()/100)*100)));
			for(int i=1;i<7;i++)
				for(int j=0;j<i;j++){
					try{
					buttonMatrix[i][j].setOnTouchListener(null);
					}catch(Exception ignored){}
				}
			
			
			
			a.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for(int i=1;i<7;i++)
						for(int j=0;j<i;j++){
							try{
							buttonMatrix[i][j].setLayoutParams(fullcut);
							buttonMatrix[i][j].setImageDrawable(getResources().getDrawable(R.drawable.back));
							}catch(Exception ignored){}
						}
					
					for(int i=0;i<7;i++){
						for(int j=0;j<verticalCardPlace[i].getChildCount();j++){
							if(((ImageView)verticalCardPlace[i].getChildAt(j)).getId()/100!=9){
						((ImageView)verticalCardPlace[i].getChildAt(j)).setOnTouchListener(new MyTouchListener());
			}
						}
					}
					
				}
			});
			
			
			return true;
			
		}
	 }
	 
	 int retCode(String card, String seed){
		 int ret=-1;
		 if(seed.compareTo("\u2660")==0){
			 if(card.compareTo("A")==0) ret=40;
			 if(card.compareTo("2")==0) ret=41;
			 if(card.compareTo("3")==0) ret=42;
			 if(card.compareTo("4")==0) ret=43;
			 if(card.compareTo("5")==0) ret=44;
			 if(card.compareTo("6")==0) ret=45;
			 if(card.compareTo("7")==0) ret=46;
			 if(card.compareTo("8")==0) ret=47;
			 if(card.compareTo("9")==0) ret=48;
			 if(card.compareTo("10")==0) ret=49;
			 if(card.compareTo("J")==0) ret=50;
			 if(card.compareTo("Q")==0) ret=51;
			 if(card.compareTo("K")==0) ret=52;
		 }
		 if(seed.compareTo("\u2764")==0){
			 if(card.compareTo("A")==0) ret=1;
			 if(card.compareTo("2")==0) ret=2;
			 if(card.compareTo("3")==0) ret=3;
			 if(card.compareTo("4")==0) ret=4;
			 if(card.compareTo("5")==0) ret=5;
			 if(card.compareTo("6")==0) ret=6;
			 if(card.compareTo("7")==0) ret=7;
			 if(card.compareTo("8")==0) ret=8;
			 if(card.compareTo("9")==0) ret=9;
			 if(card.compareTo("10")==0) ret=10;
			 if(card.compareTo("J")==0) ret=11;
			 if(card.compareTo("Q")==0) ret=12;
			 if(card.compareTo("K")==0) ret=13;
		 }
		 if(seed.compareTo("\u2666")==0){
			 if(card.compareTo("A")==0) ret=14;
			 if(card.compareTo("2")==0) ret=15;
			 if(card.compareTo("3")==0) ret=16;
			 if(card.compareTo("4")==0) ret=17;
			 if(card.compareTo("5")==0) ret=18;
			 if(card.compareTo("6")==0) ret=19;
			 if(card.compareTo("7")==0) ret=20;
			 if(card.compareTo("8")==0) ret=21;
			 if(card.compareTo("9")==0) ret=22;
			 if(card.compareTo("10")==0) ret=23;
			 if(card.compareTo("J")==0) ret=24;
			 if(card.compareTo("Q")==0) ret=25;
			 if(card.compareTo("K")==0) ret=26;
		 }
		 if(seed.compareTo("\u2663")==0){
			 if(card.compareTo("A")==0) ret=27;
			 if(card.compareTo("2")==0) ret=28;
			 if(card.compareTo("3")==0) ret=29;
			 if(card.compareTo("4")==0) ret=30;
			 if(card.compareTo("5")==0) ret=31;
			 if(card.compareTo("6")==0) ret=32;
			 if(card.compareTo("7")==0) ret=33;
			 if(card.compareTo("8")==0) ret=34;
			 if(card.compareTo("9")==0) ret=35;
			 if(card.compareTo("10")==0) ret=36;
			 if(card.compareTo("J")==0) ret=37;
			 if(card.compareTo("Q")==0) ret=38;
			 if(card.compareTo("K")==0) ret=39;
		 }
		 
		 return ret;
	 }
	 
	 
	 private class AchDialog extends AsyncTask<Void, Void, Void> {

		    @Override
		    protected Void doInBackground(Void... params) {
		    	
		        
		            try {
		            	runOnUiThread(new Runnable(){
		            		public void run(){
		            			dialogAch.show();
		            		}
		            	});

		                Thread.sleep(2000);
		                runOnUiThread(new Runnable(){
		            		public void run(){
		            			try{
		            			dialogAch.dismiss();
		            			}catch(Exception ignored){}
		            		}
		            	});
		               
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        return null;
		    }
		    
		   

		    @Override
		    protected void onProgressUpdate(Void... values) {
		        progMana.incrementProgressBy(1);
		    }
		}
	 
	 
	 
	 
	 private class IceButton implements OnClickListener {
			
			@Override
			public void onClick(View v) {
				myButtonToIce = (Button)v;
				myButtonToIce.setOnClickListener(null);
				
				String powerLevel = icespinner.getSelectedItem().toString();
				int caseToUse=0;
				if(powerLevel.compareTo("ICE1")==0) caseToUse = 0;
				if(powerLevel.compareTo("ICE2")==0) caseToUse = 1;
				if(powerLevel.compareTo("ICE3")==0) caseToUse = 2;
				switch(caseToUse){
				case 0:
					if(progMana.getMax() - progMana.getProgress()>=20){
						if(powerFlags[0]==0&&!firstMatch){
							powerFlags[0]=1;
							showpopUp("Ice Novice");
						}
					manaCost=20;
					myButtonToIce.setEnabled(false);
					chrono.setTextColor(Color.RED);
					timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
					chrono.stop();
					Thread thread = new Thread()
					{
					    @Override
					    public void run() {
					        try {

					        	 sleep(10000);
					        	runOnUiThread(new Runnable() {
					        	     @Override
					        	     public void run() {
					        	    	 myButtonToIce.setClickable(true);
					        	    	 chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
					        	    	 chrono.start();
					        	    	 chrono.setTextColor(Color.WHITE);
					        	    	 myButtonToIce.setOnClickListener(new IceButton());
					        	    	 myButtonToIce.setEnabled(true);
					        	    }
					        	});
					                
					            
					        } catch (InterruptedException e) {
					            e.printStackTrace();
					        }
					    }
					};
		
					thread.start();
					burnMana(v);
					}
					
					break;
					
				case 1:
					
					if(progMana.getMax() - progMana.getProgress()>=50){
						if(progMana.getMax() - progMana.getProgress()>=20){
							if(powerFlags[1]==0&&!firstMatch){
								powerFlags[1]=1;
								showpopUp("Ice Adept");
							}
						}
					manaCost=50;
					myButtonToIce.setEnabled(true);
					moves.setTextColor(Color.RED);
					STOP_COUNT = true;
					burnMana(v);
					}
					
					break;
				case 2:
					if(progMana.getMax() - progMana.getProgress()>=20){
						if(powerFlags[2]==0&&!firstMatch){
							powerFlags[2]=1;
							showpopUp("Ice Master");
						}
					}
					if(progMana.getMax() - progMana.getProgress()>=100){
					manaCost=100;
					myButtonToIce.setEnabled(false);
					moves.setTextColor(Color.RED);
					chrono.setTextColor(Color.RED);
					timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
					chrono.stop();
					STOP_COUNT = true;
					Thread thread2 = new Thread()
					{
					    @Override
					    public void run() {
					        try {
					        	 sleep(10000);
					        	runOnUiThread(new Runnable() {
					        	     @Override
					        	     public void run() {
					        	    	 chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
					        	    	 chrono.start();
					        	    	 chrono.setTextColor(Color.WHITE);
					        	    	 myButtonToIce.setOnClickListener(new IceButton());
					        	    	 myButtonToIce.setEnabled(true);
					        	    }
					        	});
					                
					            
					        } catch (InterruptedException e) {
					            e.printStackTrace();
					        }
					    }
					};
		
					thread2.start();
					burnMana(v);
					}
					
					break;
				}

			}
		}
	 
	 
	 
	 
	 

  
  
  private class TimeButton implements OnClickListener{
	  @Override
		public void onClick(View v) {
		  if((progMana.getMax() - progMana.getProgress()>=200)){
		  try{
		  	@SuppressWarnings("unchecked") ArrayAdapter<String> remove = (ArrayAdapter<String>)timespinner.getAdapter();
		  	
		  	switch(TIME_TO_REMOVE++){
		  	case 0:
		  		manaCost=200;
		  		if(powerFlags[12]==0&&!firstMatch){
					powerFlags[12]=1;
					showpopUp("Time Novice");				
				}
		  		remove.remove("TIME1");
		  		burnMana(v);
		  		break;
		  	case 1:
		  		manaCost=200;
		  		if(powerFlags[13]==0&&!firstMatch){
					powerFlags[13]=1;
					showpopUp("Time Adept");
				}
		  		remove.remove("TIME2");
		  		burnMana(v);
		  		break;
		  	case 2:
		  		manaCost=200;
		  		if(powerFlags[14]==0&&!firstMatch){
					powerFlags[14]=1;
					showpopUp("Time Master");
				}
		  		remove.remove("TIME3");
		  		burnMana(v);
		  		
		  		break;
		  	default:
		  			break;
		  		
		  	}
		  	
		  	if(remove.isEmpty())((LinearLayout)((LinearLayout)timespinner.getParent()).getParent()).removeView((LinearLayout)timespinner.getParent());
		  	
		  	}catch(Exception e){Log.d("error","exception in time button");}
			//if(ACTION_POINTER>0&&TIME_TO_REMOVE<LVL_TIME+1){
		  if(ACTION_POINTER>0){
			  LastAction lastActionSelected = new LastAction();
			  ACTION_POINTER--;
			  
			  lastActionSelected = lastActionVector.elementAt(ACTION_POINTER);
			  lastActionVector.remove(ACTION_POINTER);
			  switch(lastActionSelected.MOVE){
			  case DRAW:
				  //Log.d("index to draw",""+INDEX_TO_DRAW);
				  INDEX_TO_DRAW -= INCREMENT_TO_DRAW;
				  if(INDEX_TO_DRAW>0){
					  ImageView toFill = (ImageView)draw.getChildAt(0);
					  toFill.setImageDrawable(getResources().getDrawable(retCardImage(toDraw.get(INDEX_TO_DRAW-1))));
					  toFill.setId(retId(toDraw.get(INDEX_TO_DRAW-1)));
					  if(INDEX_TO_DRAW == CARD_IN_PILE-1){
						  push.setImageDrawable(getResources().getDrawable(R.drawable.back));  
					  }
				  }else{
					  ImageView toFill = (ImageView)draw.getChildAt(0);
					  draw.setBackgroundColor(Color.TRANSPARENT);
					  Log.d("qui","prob");
					  toFill.setImageDrawable(getResources().getDrawable(R.drawable.new_empty));
				  }
				  break;
			  case FROM_FIELD_TO_FIELD:
				  ImageView[] toMove = new ImageView[lastActionSelected.numberOfCardsMoved];
				  for(int i=0;i<toMove.length;i++){
					  toMove[toMove.length - 1 - i] = (ImageView)verticalCardPlace[lastActionSelected.postColumn].getChildAt(verticalCardPlace[lastActionSelected.postColumn].getChildCount()-1);
					  verticalCardPlace[lastActionSelected.postColumn].removeView(toMove[toMove.length - 1 - i]);
				  }
				  if(verticalCardPlace[lastActionSelected.postColumn].getChildCount() != 0) {
					  ((ImageView)verticalCardPlace[lastActionSelected.postColumn].getChildAt(verticalCardPlace[lastActionSelected.postColumn].getChildCount()-1)).setLayoutParams(full);
				  }
				  
				  if(verticalCardPlace[lastActionSelected.preColumn].getChildCount() != 0){
					  if(lastActionSelected.cardHasBennTurned){
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setImageDrawable(getResources().getDrawable(R.drawable.back));														
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setId(900+retValueFromId(((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).getId()));
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setOnTouchListener(null);
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setLayoutParams(fullcut);
					  }else{
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setLayoutParams(cut);
					  }
					  for(int i = 0;i<lastActionSelected.numberOfCardsMoved-1;i++){
						  toMove[i].setLayoutParams(cut);
						  verticalCardPlace[lastActionSelected.preColumn].addView(toMove[i]);  
					  }
					  toMove[lastActionSelected.numberOfCardsMoved-1].setLayoutParams(full);
					  verticalCardPlace[lastActionSelected.preColumn].addView(toMove[lastActionSelected.numberOfCardsMoved-1]);
				  }else{
					  for(int i = 0;i<lastActionSelected.numberOfCardsMoved-1;i++){
						  toMove[i].setLayoutParams(cut);
						  verticalCardPlace[lastActionSelected.preColumn].addView(toMove[i]);  
					  }
					  toMove[lastActionSelected.numberOfCardsMoved-1].setLayoutParams(full);
					  verticalCardPlace[lastActionSelected.preColumn].addView(toMove[lastActionSelected.numberOfCardsMoved-1]);
				  }
				  break;
			  case FROM_FIELD_TO_PIT:
				  if(verticalCardPlace[lastActionSelected.preColumn].getChildCount() > 0){
					  if(lastActionSelected.cardHasBennTurned){
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setImageDrawable(getResources().getDrawable(R.drawable.back));														
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setId(900+retValueFromId(((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).getId()));
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setOnTouchListener(null);
						  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setLayoutParams(fullcut);
					  }else{
					  ((ImageView)verticalCardPlace[lastActionSelected.preColumn].getChildAt(verticalCardPlace[lastActionSelected.preColumn].getChildCount()-1)).setLayoutParams(cut);
					  }
				  }
					  ImageView toRestore = (ImageView)pit[lastActionSelected.postPit].getChildAt(0);
					  pit[lastActionSelected.postPit].removeView(toRestore);
					  if(toRestore.getId()-(toRestore.getId()/100)*100 != 1){
						  ImageView toPut = new ImageView(GameField.this);
						  toPut.setImageDrawable(getResources().getDrawable(retCardImageFromId(toRestore.getId()-1)));
						  toPut.setId(toRestore.getId()-1);
						  toPut.setOnTouchListener(new MyTouchListener());
						  toPut.setLayoutParams(lp2);
						  toPut.setScaleType(ScaleType.FIT_XY);
						  pit[lastActionSelected.postPit].addView(toPut); 
					  }else{
						  ImageView toPut = new ImageView(GameField.this);
						  toPut.setImageDrawable(getResources().getDrawable(R.drawable.new_backas));
						  toPut.setId(R.id.blank_as);
						  toPut.setLayoutParams(lp2);
						  toPut.setScaleType(ScaleType.FIT_XY);
						  pit[lastActionSelected.postPit].addView(toPut);
						  toRestore.setOnTouchListener(new MyTouchListener());
					  }
					  verticalCardPlace[lastActionSelected.preColumn].addView(toRestore);
				  break;
			  case FROM_DRAW_TO_PIT:
				  //INDEX_TO_DRAW-=INCREMENT_TO_DRAW;
				  ImageView toRestore2 = (ImageView)pit[lastActionSelected.postPit].getChildAt(0);
				  pit[lastActionSelected.postPit].removeView(toRestore2);
				  if(toRestore2.getId()-(toRestore2.getId()/100)*100 != 1){
					  ImageView toPut = new ImageView(GameField.this);
					  
					  toPut.setImageDrawable(getResources().getDrawable(retCardImageFromId(toRestore2.getId()-1)));
					  toPut.setId(toRestore2.getId()-1);
					  toPut.setOnTouchListener(new MyTouchListener());
					  toPut.setLayoutParams(lp2);
					  toPut.setScaleType(ScaleType.FIT_XY);
					  pit[lastActionSelected.postPit].addView(toPut); 
				  }else{
					  
					  ImageView toPut = new ImageView(GameField.this);
					  toPut.setImageDrawable(getResources().getDrawable(R.drawable.new_backas));
					  toPut.setId(R.id.blank_as);
					  toPut.setLayoutParams(lp2);
					  toPut.setScaleType(ScaleType.FIT_XY);
					  pit[lastActionSelected.postPit].addView(toPut);
					  toRestore2.setOnTouchListener(new forDrawListener());
				  }
				  if(INDEX_TO_DRAW<0){
					  INDEX_TO_DRAW = 0;
					  toDraw.add(INDEX_TO_DRAW,retValueFromId(toRestore2.getId()));
					  INDEX_TO_DRAW+=INCREMENT_TO_DRAW;
				  }else{
				  toDraw.add(INDEX_TO_DRAW,retValueFromId(toRestore2.getId()));
				  }
				  if(draw.getChildCount()>0) draw.removeViewAt(0);
				  draw.addView(toRestore2);
				  
				  break;
			  case FROM_DRAW_TO_FIELD:
				  //INDEX_TO_DRAW-=INCREMENT_TO_DRAW;
				  if(verticalCardPlace[lastActionSelected.postColumn].getChildCount() > 1){
					  ((ImageView)verticalCardPlace[lastActionSelected.postColumn].getChildAt(verticalCardPlace[lastActionSelected.postColumn].getChildCount()-2)).setLayoutParams(full);
				  }
				  ImageView toRes = new ImageView(GameField.this);
				  toRes = (ImageView)verticalCardPlace[lastActionSelected.postColumn].getChildAt(verticalCardPlace[lastActionSelected.postColumn].getChildCount()-1);
				  verticalCardPlace[lastActionSelected.postColumn].removeView(toRes);
				  toRes.setOnTouchListener(new forDrawListener());
				  if(INDEX_TO_DRAW<0){
					  INDEX_TO_DRAW = 0;
					  toDraw.add(INDEX_TO_DRAW,retValueFromId(toRes.getId()));
					  //INDEX_TO_DRAW+=INCREMENT_TO_DRAW;
				  }else{
					  
				  toDraw.add(INDEX_TO_DRAW,retValueFromId(toRes.getId()));
				  INDEX_TO_DRAW+=INCREMENT_TO_DRAW;
				  }
				  if(draw.getChildCount()>0) draw.removeViewAt(0);
				  draw.addView(toRes);
				  break;
				  
			  }
		  }
		  }
		}
		  	
  }

  /*END POWERS<-------*/
  
  @SuppressWarnings("deprecation")
  @Override
  public Dialog onCreateDialog(int id){
	  Dialog retDial=new Dialog(context);
	  retDial.setTitle("WIN! New Game?");

	  return retDial;
  }
  
  
  
  
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      switch (requestCode) {
      case RESULT_SETTINGS:
    	 SharedPreferences sharedPrefs = PreferenceManager
                 .getDefaultSharedPreferences(this);
          break;

      }

  }
  
  private final class doNotDisappear implements OnDragListener{
	  @Override
		public boolean onDrag(View v, DragEvent event) {
			
			switch(event.getAction()){
			
			case DragEvent.ACTION_DRAG_ENDED:
				ImageView card= (ImageView)  event.getLocalState(); 
	        	card.setVisibility(View.VISIBLE);
	        	LinearLayout owner = (LinearLayout)card.getParent();
	        	try{
	        		ImageView a = (ImageView)owner.getChildAt(indexToMove-1);
	        		if(a.getId()/100==9){
	        		a.setImageDrawable(getResources().getDrawable(R.drawable.back));
	        		a.setLayoutParams(fullcut);
	        		}
	        	}catch(Exception ignored){

	        	}

				break;
			
			case DragEvent.ACTION_DROP:

				

			case DragEvent.ACTION_DRAG_LOCATION:
				
			}

			return false;
		}
  }
  
  
  private final class forDrawListener implements OnTouchListener{

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			ImageView v = (ImageView) view; 
			//lastAction.cardHasBennTurned = false;
			 switch (event.getAction() & MotionEvent.ACTION_MASK) {
	    	  
		      case MotionEvent.ACTION_DOWN:
		    	  	ClipData data = ClipData.newPlainText("", "");
		    	  	DragShadowBuilder shadowBuilder = new DragShadowBuilder(v); 
		    	  	LinearLayout owner = (LinearLayout)findViewById(R.id.draw);
		    	  	owner.setLayoutParams(lp2);
		    	  	ImageView previousCard = new ImageView(context);
		    	  	try{
		    	  	if(INDEX_TO_DRAW>1) //noinspection deprecation
                        owner.setBackgroundDrawable(getResources().getDrawable(retCardImage(toDraw.elementAt(INDEX_TO_DRAW-INCREMENT_TO_SHOW))));
		    	  	else //noinspection deprecation
                        owner.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_empty));
		    	  	}catch(Exception e){
                        //noinspection deprecation
                        owner.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_empty));
		    	  		Log.d("Exception","line 436");
		    	  	}
		    	  	FLAG_MOVING_FROM_DRAW=1;
		        	FLAG_MOVING_ONE=1;
		        	v.startDrag(data, shadowBuilder, v , 0);
		        	v.setVisibility(View.INVISIBLE);
		        	//v.setVisibility(View.INVISIBLE);
		        	
		        	
		        break;
		        
		      case MotionEvent.ACTION_MOVE:
		    	  
		    	  break;
		      case MotionEvent.ACTION_UP:
		    	  Log.d("position",""+event.getX()+" "+event.getY());
		    	  break;
		      }  
			return false;
		}
	
  }
  
 private int arrival;
 private Dialog dialogAch;

 
  void showWin(){
	  
	  //UPDATE DATABASEs
	  SQLiteDatabase db = dbHelper.getWritableDatabase();
	  //
      //get previous points

      Cursor cursor;
      String query = "SELECT exp FROM notes WHERE name = ? ORDER BY _id ASC";
      
      String[] selectionArgs = { nameToUse }; 
      cursor = db.rawQuery(query, selectionArgs);
      cursor.moveToNext();
      previousPoint = Integer.parseInt(cursor.getString(0));
      //oldpoints.setText("Old points: "+previousPoint);
      //
      //get LVL
      query = "SELECT level FROM notes WHERE name = ? ORDER BY _id ASC";
      cursor = db.rawQuery(query, selectionArgs);
      cursor.moveToNext();
      level = Integer.parseInt(cursor.getString(0));
      //
      //get Time
      long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
      int time = (int) (long) elapsedMillis/1000;
      //calculate points
      earned = MULTIPLIER_VEGAS*MULTIPLIER_DRAW_3*(150*level + (180-NUMBER_MOVES) + (600-time));
      newPoints = previousPoint + earned;
      //earnpoints.setText("Earned points: "+newPoints);
      //update in the db
      ContentValues values = new ContentValues(); 
      values.put("exp", ""+newPoints);
      String whereClause = "name = ? "; 
      String[] whereArgs = { nameToUse };
      int r = db.update("notes", values, "name = ?",whereArgs);
      //checking correct points
      
    	      
      //progress bar animation
      
      runOnUiThread(new Runnable() {
    	  public void run() {
    		  new MahClass().execute(new Void[3]);
    	  }
    	});
      
      
      //update level
      int param[] = progressLVL(level, newPoints);
      values = new ContentValues(); 
  	  values.put("level", ""+param[0]);
      r = db.update("notes", values, "name = ?",whereArgs);
      
      values = new ContentValues(); 
  	  values.put("mana", ""+(param[0]-1)*50);
      r = db.update("power", values, "name = ?",whereArgs);
      
      boolean passLevel = level != param[0];
      
      
      //UPDATE STATS
      
      String tableSelected = "";
      switch(MATCH_TYPE){
      		case MATCH_NORMAL:
      			query = "SELECT * FROM normal WHERE name = ? ORDER BY _id ASC";
      			tableSelected = "normal";
      			break;
      		case MATCH_THREE:
      			query = "SELECT * FROM three WHERE name = ? ORDER BY _id ASC";
      			tableSelected = "three";
      			break;
      		case MATCH_VEGAS:
      			query = "SELECT * FROM vegas WHERE name = ? ORDER BY _id ASC";
      			tableSelected = "vegas";
      			break;
      	case MATCH_BOTH:
      			query = "SELECT * FROM vegas WHERE name = ? ORDER BY _id ASC";
      			//query2 = "SELECT "+ TableNotes.COLUMN_MATCH +" FROM vegas WHERE name = ? ORDER BY _id ASC";
      			tableSelected = "three";
      			break;
      }
      
      cursor = db.rawQuery(query, selectionArgs);
      cursor.moveToFirst();
      int actMatch = Integer.parseInt(cursor.getString(2)); //already n+1
      int actMinMoves = Integer.parseInt(cursor.getString(3));
      int actMinTime = Integer.parseInt(cursor.getString(4));
      float actMoves = Float.parseFloat(cursor.getString(5));
      float actTime = Float.parseFloat(cursor.getString(6));
      int actWin = Integer.parseInt(cursor.getString(7));
      
      int newMinMoves;
      if (actMinMoves>NUMBER_MOVES) newMinMoves = NUMBER_MOVES;
      else newMinMoves = actMinMoves;
      
      int newMinTime;
      if (actMinTime>time) newMinTime = time;
      else newMinTime = actMinTime;
      
      int newWin = actWin + 1;
      float newMoves = actMoves + ( NUMBER_MOVES - actMoves ) / newWin;
      float newTime = actTime + ( time - actTime ) / newWin;
      
      
      
      values = new ContentValues(); 
      values.put(TableNotes.COLUMN_MIN_MOVES, ""+newMinMoves);
      values.put(TableNotes.COLUMN_MIN_TIME, ""+newMinTime);
      values.put(TableNotes.COLUMN_MOVES, ""+newMoves);
      values.put(TableNotes.COLUMN_TIME, ""+newTime);
      values.put(TableNotes.COLUMN_WIN, ""+newWin);
      r = db.update(tableSelected, values, "name = ?",whereArgs);
      
      
      
      if(previousPoint==0){
    	  values = new ContentValues(); 
    	  values.put(TableNotes.COLUMN_EARTH,"0");
    	  values.put(TableNotes.COLUMN_ICE,"0");
    	  values.put(TableNotes.COLUMN_THUNDER,"0");
    	  values.put(TableNotes.COLUMN_FIRE,"0");
    	  values.put(TableNotes.COLUMN_TI,"0");
    	  values.put(TableNotes.COLUMN_MANA,"0");
    	  r = db.update("power", values, "name = ?",whereArgs);
    	  firstMatch=true;
      }
      
      
      
      //ACHIVEMENT
      final Dialog dialogAch = new Dialog(this,R.style.PauseDialog3);
	  dialogAch.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  dialogAch.setContentView(R.layout.for_dialog_ach);
	  TextView a = (TextView)dialogAch.findViewById(R.id.dialogText);
	  
	  a.setTypeface(typeFace);
	  
	  Window window = dialogAch.getWindow();
	  WindowManager.LayoutParams wlp = window.getAttributes();

	  wlp.gravity = Gravity.TOP;
	  wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
	  window.setAttributes(wlp);
      
      
      
      values = new ContentValues(); 
      query = "SELECT * FROM achive WHERE name = ? ORDER BY _id ASC";
      cursor = db.rawQuery(query, selectionArgs);
      cursor.moveToFirst();
      switch(MATCH_TYPE){
		case MATCH_NORMAL:
			if(newWin>=1&&cursor.getInt(2)==0) {
				values.put("wnorm1", 1);
				showpopUp(R.string.wnorm1_0);
			}
			if(newWin>=10&&cursor.getInt(3)==0) {
				values.put("wnorm2", 1);
				showpopUp(R.string.wnorm10_0);
			}
			if(newWin>=100&&cursor.getInt(4)==0){
				values.put("wnorm3", 1);
				showpopUp(R.string.wnorm100_0);
			}
			
			if(time<=300&&cursor.getInt(5)==0){
				values.put("tnorm300",1);
				showpopUp(R.string.tnorm300_0);
			}
			if(time<=200&&cursor.getInt(6)==0){
				values.put("tnorm200",1);
				showpopUp(R.string.tnorm200_0);
			}
			if(time<=100&&cursor.getInt(7)==0) {
				values.put("tnorm100",1);
				showpopUp(R.string.tnorm100_0);
			}
			if(NUMBER_MOVES<=100&&cursor.getInt(8)==0) {
				values.put("mnorm100",1);
				showpopUp(R.string.mnorm100_0);
			}
			if(NUMBER_MOVES<=50&&cursor.getInt(9)==0) {
				values.put("mnorm50",1);
				showpopUp(R.string.mnorm50_0);
			}
			if(NUMBER_MOVES<=25&&cursor.getInt(10)==0) {
				values.put("mnorm25",1);
				showpopUp(R.string.mnorm25_0);
			}
			if(values.size()>0) {
				Log.d("i'm in","powers");
				r = db.update("achive", values, "name = ?",whereArgs);
			}
			break;
		case MATCH_THREE:
			if(newWin>=1&&cursor.getInt(2)==11) {
				values.put("wthree1", 1);
				showpopUp(R.string.wthree1_0);
			}
			if(newWin>=10&&cursor.getInt(12)==0) {
				values.put("wthree2", 1);
				showpopUp(R.string.wthree10_0);
			}
			if(newWin>=100&&cursor.getInt(13)==0){
				values.put("wthree3", 1);
				showpopUp(R.string.wthree100_0);
			}
			
			if(time<=300&&cursor.getInt(14)==0){
				values.put("tthree300",1);
				showpopUp(R.string.tthree300_0);
			}
			if(time<=200&&cursor.getInt(15)==0){
				values.put("tthree200",1);
				showpopUp(R.string.tthree200_0);
			}
			if(time<=100&&cursor.getInt(16)==0) {
				values.put("tthree100",1);
				showpopUp(R.string.tthree100_0);
			}
			if(NUMBER_MOVES<=100&&cursor.getInt(17)==0) {
				values.put("mthree100",1);
				showpopUp(R.string.mthree100_0);
			}
			if(NUMBER_MOVES<=50&&cursor.getInt(18)==0) {
				values.put("mthree50",1);
				showpopUp(R.string.mthree50_0);
			}
			if(NUMBER_MOVES<=25&&cursor.getInt(19)==0) {
				values.put("mthree25",1);
				showpopUp(R.string.mthree25_0);
			}
			if(values.size()>0){
				r = db.update("achive", values, "name = ?",whereArgs);
			}
			break;
		case MATCH_VEGAS:
			if(newWin>=1&&cursor.getInt(20)==0) {
				values.put("wvegas1", 1);
				showpopUp(R.string.wvegas1_0);
			}
			if(newWin>=10&&cursor.getInt(21)==0) {
				values.put("wvegas2", 1);
				showpopUp(R.string.wvegas10_0);
			}
			if(newWin>=100&&cursor.getInt(22)==0){
				values.put("wvegas3", 1);
				showpopUp(R.string.wvegas100_0);
			}
			
			if(time<=300&&cursor.getInt(23)==0){
				values.put("tvegas300",1);
				showpopUp(R.string.tvegas300_0);
			}
			if(time<=200&&cursor.getInt(24)==0){
				values.put("tvegas200",1);
				showpopUp(R.string.tvegas200_0);
			}
			if(time<=100&&cursor.getInt(25)==0) {
				values.put("tvegas100",1);
				showpopUp(R.string.tvegas100_0);
			}
			if(NUMBER_MOVES<=100&&cursor.getInt(26)==0) {
				values.put("mvegas100",1);
				showpopUp(R.string.mvegas100_0);
			}
			if(NUMBER_MOVES<=50&&cursor.getInt(27)==0) {
				values.put("mvegas50",1);
				showpopUp(R.string.mvegas50_0);
			}
			if(NUMBER_MOVES<=25&&cursor.getInt(28)==0) {
				values.put("mvegas25",1);
				showpopUp(R.string.mvegas25_0);
			}
			if(values.size()>0) {
				r = db.update("achive", values, "name = ?",whereArgs);
			}
			break;
      }
      
      if(MULTIPLIER_DRAW_3!=1&&MULTIPLIER_VEGAS!=1&&cursor.getInt(44)==0){
      values = new ContentValues();
      values.put("winboth", 1);
      r = db.update("achive", values, "name = ?",whereArgs);
      showpopUp("Win Both");
      }
      if(time>600&&cursor.getInt(47)==0){
          values = new ContentValues();
          values.put("time600", 1);
          r = db.update("achive", values, "name = ?",whereArgs);
          showpopUp("Are you sleepping");
          }
      if(NUMBER_MOVES>200&&cursor.getInt(46)==0){
          values = new ContentValues();
          values.put("moves200", 1);
          r = db.update("achive", values, "name = ?",whereArgs);
          showpopUp("Touchscreen lover");
          }
      if(noDraw&&cursor.getInt(45)==0){
          values = new ContentValues();
          values.put("windraw", 1);
          r = db.update("achive", values, "name = ?",whereArgs);
          showpopUp("Ready for vegas");
          }
      
      inARow++;
      values = new ContentValues();
      values.put("inarow", inARow);
      r = db.update("notes", values, "name = ?",whereArgs);
      if(inARow==10&&cursor.getInt(49)==0){
          values = new ContentValues();
          values.put("win10", 1);
          r = db.update("achive", values, "name = ?",whereArgs);
          showpopUp("10 in a row");
          }
      
      
      if(!firstMatch){
      values = new ContentValues(); 
      if(powerFlags[0]==1) values.put("ice1", 1);
      if(powerFlags[1]==1) values.put("ice2", 1);
      if(powerFlags[2]==1) values.put("ice3", 1);
      if(powerFlags[3]==1) values.put("thunder1", 1);
      if(powerFlags[4]==1) values.put("thunder2", 1);
      if(powerFlags[5]==1) values.put("thunder3", 1);
      if(powerFlags[6]==1) values.put("earth1", 1);
      if(powerFlags[7]==1) values.put("earth2", 1);
      if(powerFlags[8]==1) values.put("earth3", 1);
      if(powerFlags[9]==1) values.put("time1", 1);
      if(powerFlags[10]==1) values.put("time2", 1);
      if(powerFlags[11]==1) values.put("time3", 1);
      if(powerFlags[12]==1) values.put("fire1", 1);
      if(powerFlags[13]==1) values.put("fire2", 1);
      if(powerFlags[14]==1) values.put("fire3", 1);
      
      if(values.size()!=0) db.update("achive", values, "name = ?",whereArgs);
      
      }
      
      //END UPDATE


      askForNewGame(passLevel,param[0]);
      
      
      //
      
      
  }
  
  
  private class MahClass extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected Void doInBackground(Void... params) {
	    	
	    	int param[] = progressLVL(level, newPoints); //0=lvl_1=punti al succ_2=max lvl

	        if(level==param[0]){
	      	  Log.d("stesso livello","yes");
	      	  arrival = param[1];
	      	 while (progBar.getProgress() < arrival) {
		            publishProgress();
		            try {
		                Thread.sleep(0, 1);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }
	      	  
	        }else{
	        	arrival = param[3];
	        	 while (progBar.getProgress() < arrival) {
	 	            publishProgress();
	 	            try {
	 	                Thread.sleep(0, 1);
	 	            } catch (InterruptedException e) {
	 	                e.printStackTrace();
	 	            }
	 	        }	
	        final int ttt = param[0];
	      	  Log.d("livello","pi�");
	      	runOnUiThread(new Runnable() {
	      	  public void run() {
	      		 lvl.setText("LVL: "+ttt);
	      		 win.setClickable(false);
	      	  }
	      	});
	        
	      	 
	      	  arrival = param[1];
	      	  progBar.setProgress(0);
	      	  progBar.setMax(param[2]);
	      	 while (progBar.getProgress() < arrival) {
		            publishProgress();
		            try {
		                Thread.sleep(0, 1);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }
	        }
	        

	        return null;
	    }

	    @Override
	    protected void onProgressUpdate(Void... values) {
	        progBar.incrementProgressBy(1);
	    }
	    
	   
	}
  
 
  
  
  
  
  
  
  
  
  private class MahClass2 extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected Void doInBackground(Void... params) {
	    	
	        while (progMana.getProgress() < progress) {
	            publishProgress();
	            try {
	                Thread.sleep(10);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        return null;
	    }
	    
	   

	    @Override
	    protected void onProgressUpdate(Void... values) {
	        progMana.incrementProgressBy(1);
	    }
	}
  private int GLOBAL_CONT=0;
  private class MahClass3 extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected Void doInBackground(Void... params) {
	    	int i=0;
	    	GLOBAL_CONT=0;
	    	Log.d("columnToSort",""+columnToSort);
	    	Log.d("GLOBAL_CONT",""+GLOBAL_CONT);
	        while (i++ < numberCardsToSort+1) {
	            try {
	                Thread.sleep(150);
	                runOnUiThread(new Runnable() {
	                 	  public void run() {
	                 		  if(GLOBAL_CONT!=0) ((ImageView)verticalCardPlace[columnToSort].getChildAt(GLOBAL_CONT-1)).setVisibility(View.VISIBLE);
	                 		  if(GLOBAL_CONT!=numberCardsToSort) ((ImageView)verticalCardPlace[columnToSort].getChildAt(GLOBAL_CONT++)).setVisibility(View.INVISIBLE);
	                 		  else ((ImageView)verticalCardPlace[columnToSort].getChildAt(GLOBAL_CONT-1)).setVisibility(View.VISIBLE);
	                 	  
	                 	  }
	                 	 
	                 	});
	                
	                
	            
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        
	        runOnUiThread(new Runnable(){
	        	public void run(){
	        		myButtonToThunder.setEnabled(true);
	        	}
	        });
	        return null;
	    }
  }
  
  
  private class MahClass4 extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected Void doInBackground(Void... params) {
	    	
	        runOnUiThread(new Runnable() {
	           	  public void run() {
	           		  new MahClass4().execute(new Void[3]);
	           	  }
	           	});
	            	        return null;
	    }
}
  
  private class MahClass5 extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected Void doInBackground(Void... params) {
	    	int i=0;
	    	GLOBAL_CONT=0;

	        while (i++ < 8) { 
	            try {
	                Thread.sleep(150);
	                runOnUiThread(new Runnable() {
	                 	  public void run() {
	                 		  for(int j=0;j<verticalCardPlace[GLOBAL_CONT].getChildCount();j++){
	                 			  Log.d("chencking",""+GLOBAL_CONT+"    "+j);
	                 		  if(((ImageView)verticalCardPlace[GLOBAL_CONT].getChildAt(j)).getId()/100==9) {
	                 			  Log.d("found","backcard");
	                 			  ((ImageView)verticalCardPlace[GLOBAL_CONT].getChildAt(j)).setVisibility(View.VISIBLE);
	                 		  }
	                 		  }GLOBAL_CONT++;
	                 		
	                 	  }
	                 	});
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	            
	            	        return null;
	    }
}

  
  private int levelIce;
  private int levelThunder;
  private int levelEarth;
  private int levelTime;
  private int levelFire;
  private String whereClause;
  private AlertDialog powerDialog;
  
  
  void askForNewGame(boolean showPowers, int newLevel){
	  
	  //makePower to choose
	  
	  if((newLevel % 2) == 0 && showPowers){
		  
		  //Dialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameField.this);
	      //alertDialogBuilder.setMessage("Look like you can choose a new Power!");
		  final Dialog dialog = new Dialog(this,R.style.PauseDialog2);
		  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		  dialog.setContentView(R.layout.for_dialog_vertical);
		  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
		  a.setText(R.string.looks_like);
		  a.setTypeface(typeFace);
		  Button firstChoice =(Button)dialog.findViewById(R.id.dialogbutton1);
		  Button secondChoice =(Button)dialog.findViewById(R.id.dialogbutton2);
		  firstChoice.setText(R.string.yes);
		  firstChoice.setTypeface(typeFace);	  
		  secondChoice.setText(R.string.no);
		  secondChoice.setTypeface(typeFace);	  
		  
		  

	      
		  
		  
		  
	      LinearLayout powerBarer = new LinearLayout(getApplicationContext());
	      powerBarer.setOrientation(LinearLayout.VERTICAL);
	      
	      Cursor cursor;
	      
	      String[] selectionArgs = { nameToUse }; 
	      String query = "SELECT * FROM power WHERE name = ? ORDER BY _id ASC";
	      cursor = db.rawQuery(query, selectionArgs);
	      cursor.moveToNext();
	      levelIce = Integer.parseInt(cursor.getString(2));
	      levelFire = Integer.parseInt(cursor.getString(3));
	      levelEarth = Integer.parseInt(cursor.getString(4));
	      levelThunder = Integer.parseInt(cursor.getString(5));
	      levelTime = Integer.parseInt(cursor.getString(6));
	      
	      
	      //choose 2 random power between which you have to choose
	      //the prob of a power is lower if it's level is higher (4-level)*power
	      //total probability sum (3-level)*1/20
	      float nonNormProbIce = (float)(3 - levelIce) / 15;
	      float nonNormProbFire = (float)(3 - levelFire) / 15;
	      float nonNormProbThunder = (float)(3 - levelThunder) / 15;
	      float nonNormProbEarth = (float)(3 - levelEarth) / 15;
	      float nonNormProbTime = (float)(3 - levelTime) / 15;
	      
	      float normFactor = nonNormProbEarth + nonNormProbFire + nonNormProbIce + nonNormProbThunder + nonNormProbTime;
	      
	      float probIce = nonNormProbIce / normFactor;
	      float probFire = nonNormProbFire / normFactor;
	      float probThunder = nonNormProbThunder / normFactor;
	      float probEarth = nonNormProbEarth / normFactor;
	      float probTime = nonNormProbTime / normFactor;
	     
	      Log.d("prob",""+nonNormProbEarth);
	      Log.d("prob",""+nonNormProbFire);
	      Log.d("prob",""+nonNormProbIce);
	      Log.d("prob",""+nonNormProbThunder);
	      Log.d("prob",""+nonNormProbTime);
	      
	      
	      Log.d("prob",""+normFactor);

	      
	      Log.d("prob",""+probIce);
	      Log.d("prob",""+probFire);
	      Log.d("prob",""+probThunder);
	      Log.d("prob",""+probEarth);
	      Log.d("prob",""+probTime);
	      Random rand = new Random();
	      
	      double choice = rand.nextDouble();
	      Log.d("choice1",""+choice);
	      int powerToShow1 = 5;
	      int powerToShow2 = 5;
	      
	      if(choice >= 0 && choice < probIce) powerToShow1 = 0;
	      if(choice>probIce && choice <= probIce+probFire) powerToShow1 = 1;
	      if(choice>probIce+probFire && choice <= probThunder+probFire+probIce) powerToShow1 = 2;
	      if(choice>probThunder+probFire+probIce && choice <= probThunder+probEarth+probFire+probIce) powerToShow1 = 3;
	      if(choice>probThunder+probEarth+probFire+probIce && choice <= probThunder+probEarth+probFire+probIce+probTime) powerToShow1 = 4;
	      
	      choice = rand.nextDouble();
	      Log.d("choice1",""+choice);
	      if(choice>=0 && choice <= probIce) powerToShow2 = 0;
	      if(choice>probIce && choice <= probIce+probFire) powerToShow2 = 1;
	      if(choice>probIce+probFire && choice <= probThunder+probFire+probIce) powerToShow2 = 2;
	      if(choice>probThunder+probFire+probIce && choice <= probThunder+probEarth+probFire+probIce) powerToShow2 = 3;
	      if(choice>probThunder+probEarth+probFire+probIce && choice <= probThunder+probEarth+probFire+probIce+probTime) powerToShow2 = 4;
	      
	      
	      ContentValues values;
	      whereClause = "name = ? "; 
	      boolean noMore1 = false;
	      boolean noMore2 = false;
          int r;
	      switch(powerToShow1){
	      case 0:
	    	  firstChoice.setText("Ice LVL "+(levelIce+1));
	    	  firstChoice.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ContentValues values = new ContentValues(); 
			          values.put("ice", ""+(levelIce+1));
			          String[] whereArgs = { nameToUse };
			          int r = db.update("power", values, whereClause,whereArgs);
			          dialog.dismiss();
					  actualNewGame();
				}
			});
	    	  break;
	      case 1:
	    	  firstChoice.setText("Fire LVL "+(levelFire+1));
	    	  firstChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("fire", ""+(levelFire+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 2:
	    	  firstChoice.setText("Thunder LVL "+(levelThunder+1));
	    	  firstChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("thunder", ""+(levelThunder+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 3:
	    	  firstChoice.setText("Earth LVL "+(levelEarth+1));
	    	  firstChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("earth", ""+(levelEarth+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 4:
	    	  firstChoice.setText("Time LVL "+(levelTime+1));
	    	  firstChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("time", ""+(levelTime+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 5:
	    	  noMore1=true;
	    	  firstChoice.setText(R.string.no_more);
	    	  firstChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      }
	      
	      
	      switch(powerToShow2){
	      case 0:
	    	  secondChoice.setText("Ice LVL "+(levelIce+1));
	    	  secondChoice.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ContentValues values = new ContentValues(); 
			          values.put("ice", ""+(levelIce+1));
			          String[] whereArgs = { nameToUse };
			          int r = db.update("power", values, whereClause,whereArgs);
			          dialog.dismiss();
					  actualNewGame();
				}
			});
	    	  break;
	      case 1:
	    	  secondChoice.setText("Fire LVL "+(levelFire+1));
	    	  secondChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("fire", ""+(levelFire+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 2:
	    	  secondChoice.setText("Thunder LVL "+(levelThunder+1));
	    	  secondChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("thunder", ""+(levelThunder+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 3:
	    	  secondChoice.setText("Earth LVL "+(levelEarth+1));
	    	  secondChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("earth", ""+(levelEarth+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 4:
	    	  secondChoice.setText("Time LVL "+(levelTime+1));
	    	  secondChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ContentValues values = new ContentValues(); 
				          values.put("time", ""+(levelTime+1));
				          String[] whereArgs = { nameToUse };
				          int r = db.update("power", values, whereClause,whereArgs);
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      case 5:
	    	  noMore2=true;
	    	  secondChoice.setText(R.string.no_more);
	    	  secondChoice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
				          dialog.dismiss();
						  actualNewGame();
					}
				});
	    	  break;
	      }
	      
	      //firstChoice.setOnClickListener(new forPowerButton());
	      //secondChoice.setOnClickListener(new forPowerButton());
	      dialog.setCanceledOnTouchOutside(false);
	      
	      if(!((noMore1)&&(noMore2)))dialog.show();
	      else actualNewGame();
		  
		  
	  }else{
		  if(firstMatch) noobNewGame();
		  else{
		  if(showPowers) keepTryActualNewGame();
		  else actualNewGame();
		  }
	  }
	  //
	  
	  
  }
  
  void noobNewGame(){
	  final Dialog dialog = new Dialog(this,R.style.PauseDialog2);
	  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  dialog.setContentView(R.layout.for_dialog);
	  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
	  a.setText("I bet it was easy with all those powers! Show me if you deserve them! New game?");
	  a.setTypeface(typeFace);
	  Button one =(Button)dialog.findViewById(R.id.dialogbutton1);
	  Button two =(Button)dialog.findViewById(R.id.dialogbutton2);
	  one.setText(R.string.yes);
	  one.setTypeface(typeFace);	  
	  two.setText(R.string.no);
	  two.setTypeface(typeFace);	  
	  
	  one.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			db.close();
			for(int i=0;i<toClose.size();i++) toClose.get(i).dismiss();
    	 	Intent positveActivity = new Intent(getApplicationContext(),GameField.class);
            positveActivity.putExtra("playerName", nameToUse);
            startActivity(positveActivity);
            overridePendingTransition(R.anim.transition_away,R.anim.transition_in);
            dialog.dismiss();
            finish();
			
		}
	});
      two.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});
      
     
	    
     
      dialog.setCanceledOnTouchOutside(false);
      dialog.show();
  }
  
  
  private class forPowerButton implements OnClickListener{
	  
	  @Override
	  public void onClick(View v){
		  db.close();
		  powerDialog.dismiss();
		  actualNewGame();
	  }
  }
  
  void actualNewGame(){
	  
	  
	  ContentValues values = new ContentValues(); 
	  String[] whereArgs = { nameToUse };
      if(powerFlags[0]==1) values.put("ice1", 1);
      if(powerFlags[1]==1) values.put("ice2", 1);
      if(powerFlags[2]==1) values.put("ice3", 1);
      if(powerFlags[3]==1) values.put("thunder1", 1);
      if(powerFlags[4]==1) values.put("thunder2", 1);
      if(powerFlags[5]==1) values.put("thunder3", 1);
      if(powerFlags[6]==1) values.put("earth1", 1);
      if(powerFlags[7]==1) values.put("earth2", 1);
      if(powerFlags[8]==1) values.put("earth3", 1);
      if(powerFlags[9]==1) values.put("time1", 1);
      if(powerFlags[10]==1) values.put("time2", 1);
      if(powerFlags[11]==1) values.put("time3", 1);
      if(powerFlags[12]==1) values.put("fire1", 1);
      if(powerFlags[13]==1) values.put("fire2", 1);
      if(powerFlags[14]==1) values.put("fire3", 1);
      
      if(values.size()!=0) db.update("achive", values, "name = ?",whereArgs);	  
	  
	  final Dialog dialog = new Dialog(this,R.style.PauseDialog2);
	  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  dialog.setContentView(R.layout.for_dialog);
	  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
	  a.setText(R.string.new_game);
	  a.setTypeface(typeFace);
	  Button one =(Button)dialog.findViewById(R.id.dialogbutton1);
	  Button two =(Button)dialog.findViewById(R.id.dialogbutton2);
	  one.setText(R.string.yes);
	  one.setTypeface(typeFace);	  
	  two.setText(R.string.no);
	  two.setTypeface(typeFace);	  
	  
	  one.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			db.close();
			for(int i=0;i<toClose.size();i++) toClose.get(i).dismiss();
    	 	Intent positveActivity = new Intent(getApplicationContext(),GameField.class);
            positveActivity.putExtra("playerName", nameToUse);
            startActivity(positveActivity);
            overridePendingTransition(R.anim.transition_away,R.anim.transition_in);
            dialog.dismiss();
            finish();
			
		}
	});
      two.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});
      
     
	    
     
      dialog.setCanceledOnTouchOutside(false);
      dialog.show();
  }
  
  void keepTryActualNewGame(){
	  
	  final Dialog dialog = new Dialog(this,R.style.PauseDialog2);
	  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  dialog.setContentView(R.layout.for_dialog);
	  TextView a = (TextView)dialog.findViewById(R.id.dialogText);
	  a.setText(R.string.keep_try);
	  a.setTypeface(typeFace);
	  Button one =(Button)dialog.findViewById(R.id.dialogbutton1);
	  Button two =(Button)dialog.findViewById(R.id.dialogbutton2);
	  one.setText(R.string.yes);
	  one.setTypeface(typeFace);	  
	  two.setText(R.string.no);
	  two.setTypeface(typeFace);	  
	  
	  one.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			db.close();
			for(int i=0;i<toClose.size();i++) toClose.get(i).dismiss();
    	 	Intent positveActivity = new Intent(getApplicationContext(),GameField.class);
            positveActivity.putExtra("playerName", nameToUse);
            startActivity(positveActivity);
            overridePendingTransition(R.anim.transition_away,R.anim.transition_in);
            dialog.dismiss();
            finish();
			
		}
	});
      two.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});

      dialog.setCanceledOnTouchOutside(false);
      dialog.show();
	  
	  
	  
  }
  
  
  int[] progressLVL(int lvl, int nowPoints){
	  int ret[] = new int[4];
	  int k=0;
	  for(int i=0;i<lvl;i++) k+=i;
	  int BACK_THR = k*1000;
	  int LOW_THR = BACK_THR + lvl*1000;
	  int HIGH_THR = LOW_THR + (lvl+1)*1000;
	  
	  if(nowPoints<LOW_THR){
		  ret[0] = lvl;
		  ret[1] = nowPoints-BACK_THR;//dove devo settare la barra
		  ret[2] = LOW_THR-BACK_THR;  //dove settare il max;
	  }else{
		  ret[0] = ++lvl;
		  ret[1] = nowPoints-LOW_THR;
		  ret[2] = HIGH_THR-LOW_THR;
		  ret[3] = LOW_THR-BACK_THR; // per finire
	  }
	  return ret;
  }
  
  private final class forPitsDragListener implements OnDragListener{
	  @Override
	    public boolean onDrag(View v, DragEvent event) {
	      switch (event.getAction()) {
	      case DragEvent.ACTION_DRAG_STARTED:
	        // do nothing
	        break;
	      case DragEvent.ACTION_DRAG_ENTERED:
	        //v.setBackgroundDrawable(enterShape);
	        break;
	      case DragEvent.ACTION_DRAG_EXITED:
	        //v.setBackgroundDrawable(normalShape);
	        break;
	      case DragEvent.ACTION_DROP:
	        // Dropped, reassign View to ViewGroup
	    	  ImageView card= (ImageView)  event.getLocalState(); //view moving
		      LinearLayout owner = (LinearLayout) card.getParent(); //owner
		      LinearLayout container = (LinearLayout) v; //current view in which i dragged
		      ImageView visibleOnPit = (ImageView)container.getChildAt(0);
		      Card toPut = getDesc(card);
		      Card toCover = getDesc(visibleOnPit);
		      switch(owner.getId()){
		      case R.id.draw:
		    	  if(((card.getId()/100 == visibleOnPit.getId()/100 ) && toPut.number - toCover.number == 1) || (toPut.number == 1 && visibleOnPit.getId()/100 == 8)){
		    		  ACTION_POINTER++;
	        			lastAction = new LastAction();
	        			lastAction.MOVE = FROM_DRAW_TO_PIT;
		    		  if(!STOP_COUNT) {
		    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
		    		  }else{
		    			  if(++MOVES_NOT_COUNT==10) {
		    				  STOP_COUNT = false;
		    				  moves.setTextColor(Color.WHITE);
		    			  }
		    		  }
		    		  
		    		  owner.removeView(card);
		    		  card.setOnTouchListener(new MyTouchListener());
			    	  container.removeAllViews();
			    	  container.addView(card);
			    	  lastAction.postPit = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
			    	  lastActionVector.add(lastAction);
		    		  if(INDEX_TO_DRAW - INCREMENT_TO_DRAW != 0 ){
		    			  
			        		ImageView toRestore = new ImageView(GameField.this);
			        		toRestore.setImageDrawable(getResources().getDrawable(retCardImage(toDraw.get(INDEX_TO_DRAW-INCREMENT_TO_SHOW))));
			        		toRestore.setId(retId(toDraw.get(INDEX_TO_DRAW-INCREMENT_TO_SHOW)));
			        		toRestore.setLayoutParams(lp2);
			        		toRestore.setScaleType(ScaleType.FIT_XY);
			        		toRestore.setOnTouchListener(new forDrawListener());
			        		draw.addView(toRestore);
			        		toDraw.remove(INDEX_TO_DRAW - INCREMENT_TO_DRAW);
			        		INDEX_TO_DRAW-=INCREMENT_TO_DRAW;
			        		CARD_IN_PILE--;
			        		}else{
			        			toDraw.remove(INDEX_TO_DRAW - INCREMENT_TO_DRAW);
				        		INDEX_TO_DRAW-=INCREMENT_TO_DRAW;
				        		CARD_IN_PILE--;
			        		}
			      }else{
			    	  card.setVisibility(View.VISIBLE);
			      }
		    	  break;
		      case R.id.depos1:
		      case R.id.depos2:
		      case R.id.depos3:
		      case R.id.depos4:
		    	  card.setVisibility(View.VISIBLE);
		    	  break;
		      default:
		    	  if(((card.getId()/100 == visibleOnPit.getId()/100 ) && toPut.number - toCover.number == 1) || (toPut.number == 1 && visibleOnPit.getId()/100 == 8)){
		    		  ACTION_POINTER++;
	        			lastAction = new LastAction();
	        			lastAction.MOVE = FROM_FIELD_TO_PIT;
		    		  if(!STOP_COUNT) {
		    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
		    		  }else{
		    			  if(++MOVES_NOT_COUNT==10) {
		    				  STOP_COUNT = false;
		    				  moves.setTextColor(Color.WHITE);
		    			  }
		    		  }
		    		  
		    		  owner.removeView(card);
			    	  card.setOnTouchListener(new MyTouchListener());
			    	  container.removeAllViews(); 
			    	  container.addView(card);
			    	  
			    	  if(indexToMove != 0){// not moving last card of vertical
	        				if(owner.getChildAt(indexToMove-1).getId()/100 != 9){//not have to turn
	        					((ImageView)owner.getChildAt(owner.getChildCount()-1)).setLayoutParams(full);
	        				}else{// i have to turn
	        					if(++VICTORY_CHECK == 21) {
	        						win.setVisibility(View.VISIBLE);wintext.setVisibility(View.VISIBLE);	
	        					}
	        					((ImageView)owner.getChildAt(indexToMove-1)).setLayoutParams(full);
	        					((ImageView)owner.getChildAt(indexToMove-1)).setImageDrawable(getResources().getDrawable(retCardImage(((ImageView)owner.getChildAt(indexToMove-1)).getId()-900)));
	        					((ImageView)owner.getChildAt(indexToMove-1)).setId(retId(((ImageView)owner.getChildAt(indexToMove-1)).getId()-900));
	        					((ImageView)owner.getChildAt(indexToMove-1)).setOnTouchListener(new MyTouchListener());
	        					lastAction.cardHasBennTurned = true;
	        				}//finish turn
	        			}
			    	  lastAction.preColumn = ((LinearLayout)owner.getParent()).indexOfChild(owner)/2;
			    	  lastAction.postPit = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
			    	  Log.d("field_to_pit",""+lastAction.preColumn);
			    	  lastActionVector.add(lastAction);
			      }else{
			    	  card.setVisibility(View.VISIBLE);
			      }
		    	  break;	  
		      }
		      
	        break;
	      case DragEvent.ACTION_DRAG_ENDED:

	      default:
	        break;
	      }

	      return true;
	    }
  }
  
  
  //DragShadow
  class CustomDragShadowBuilder extends View.DragShadowBuilder {

	    final Drawable shadow;

	    public CustomDragShadowBuilder(View v) {
	        super(v);
	        //create the layout with the correct orientation
	        LinearLayout lay = new LinearLayout(v.getContext());
	        lay = (LinearLayout)getLayoutInflater().inflate(R.layout.to_drag_layout, null);
	        
	        // adding images
	        
	        ImageView[] image = new ImageView[images.length];
	        for(int i=0;i<images.length-1;i++){
	        	image[i] = new ImageView(v.getContext());
	        	image[i].setImageDrawable(images[i]);
	        	image[i].setVisibility(View.VISIBLE);
	        	image[i].setLayoutParams(cut);
	        	image[i].setScaleType(ScaleType.FIT_XY);
	        	lay.addView(image[i]);
	        }
	        image[images.length-1] = new ImageView(v.getContext());
        	image[images.length-1].setImageDrawable(images[images.length-1]);
        	image[images.length-1].setVisibility(View.VISIBLE);
        	image[images.length-1].setScaleType(ScaleType.FIT_XY);
        	image[images.length-1].setLayoutParams(lp2);

        	lay.addView(image[images.length-1]);

	        
	        
	        
	        //convert to drawable
	        Bitmap toAdd = ConvertToBitmap(lay);
	        shadow = new BitmapDrawable(getResources(),toAdd);
	        
	    }

	    @Override
	    public void onProvideShadowMetrics(Point size, Point touch) {
	        int width, height;
	        width = shadow.getIntrinsicWidth();//getView().getWidth();// / 2;
	        height = shadow.getIntrinsicHeight();//getView().getHeight();// / 2;
	        shadow.setBounds(0, 0, width, height);
	        size.set(width, height);
	        touch.set(width / 2, height / 5);
	    }

	    public void onDrawShadow(Canvas canvas) {
	        shadow.draw(canvas);
	    }

	}

  
  

  private final class MyTouchListener implements OnTouchListener {
	  @Override
	  public boolean onTouch(View view, MotionEvent motionEvent) {
		  //lastAction.cardHasBennTurned = false;
      switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
    	  
      case MotionEvent.ACTION_DOWN:
    	  
    	  
    	ImageView layoutToMove = (ImageView) view;
        LinearLayout columnLayout = (LinearLayout) view.getParent();
        LinearLayout toFindColumn = (LinearLayout) columnLayout.getParent();
        int indexColumn = toFindColumn.indexOfChild(columnLayout);
    	
        indexToMove=columnLayout.indexOfChild(view);
        int count=columnLayout.getChildCount();
        
        if(SHOW_UNDER_MOVE){
        	try{
        ImageView toShow = (ImageView)columnLayout.getChildAt(indexToMove-1);
        toShow.setImageDrawable(getResources().getDrawable(retCardImage(FIELD[indexColumn/2][indexToMove-1])));
        	}catch(Exception ignored){}
        	}
        ClipData data = ClipData.newPlainText("", "");

        
        //get Images from layout
        images = new Drawable[count-indexToMove];
        transport = new ImageView[count];
        
        if(indexToMove<(count-1)){
        	int k=0;
        	for(int i=indexToMove;i<count;i++){
        		images[k]=((ImageView)(columnLayout.getChildAt(i))).getDrawable();
        		transport[k]=(ImageView)columnLayout.getChildAt(i);
        		transport[k++].setVisibility(View.INVISIBLE);
        	}
        	
        }

        
        //Roba tediosa purtroppo utile
        bmImage = new ImageView(context);
        bmImage.setImageDrawable(getResources().getDrawable(R.drawable.af));
        //
        
        switch(count-indexToMove){
        	case 1:
        		DragShadowBuilder shadowBuilder = new DragShadowBuilder(layoutToMove); 
        		layoutToMove.startDrag(data, shadowBuilder, layoutToMove , 0);
        		layoutToMove.setVisibility(View.INVISIBLE);
        		FLAG_MOVING_ONE=1;
        		break;
        	default:
        		CustomDragShadowBuilder shadowBuilder2 = new CustomDragShadowBuilder(bmImage);
        		layoutToMove.startDrag(data, shadowBuilder2, layoutToMove , 0);
        		FLAG_MOVING_ONE=0;
        		break;
        }
        
        if(((LinearLayout)columnLayout.getParent()).getId()==R.id.pits){

        	if(layoutToMove.getId()-((layoutToMove.getId()/100)*100)!=1)
                //noinspection deprecation
                columnLayout.setBackgroundDrawable(getResources().getDrawable(retCardImageFromId(layoutToMove.getId()-1)));
        	else //noinspection deprecation
                columnLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_backas));
        }
        
        //layoutToMove.setVisibility(View.INVISIBLE);
        
        break;
        
      case MotionEvent.ACTION_MOVE:
    	  
    	  break;
       
	  case MotionEvent.ACTION_UP:
    	  Log.d("position",""+motionEvent.getX()+" "+motionEvent.getY());
    	  break;
      }
        return true;
    }
  }

  

  
  private class MyDragListener implements OnDragListener {
	    
	    

	    @Override
	    public boolean onDrag(View v, DragEvent event) {
	      int action = event.getAction();
	      switch (event.getAction()) {
	      case DragEvent.ACTION_DRAG_STARTED:
	        // do nothing
	        break;
	      case DragEvent.ACTION_DRAG_ENTERED:
	        //v.setBackgroundDrawable(enterShape);
	        break;
	      case DragEvent.ACTION_DRAG_EXITED:
	        //v.setBackgroundDrawable(normalShape);
	        break;
	      case DragEvent.ACTION_DROP:
	    	  ImageView card= (ImageView)  event.getLocalState(); //view moving
		        LinearLayout owner = (LinearLayout) card.getParent(); //owner
		        LinearLayout container = (LinearLayout) v;
		        switch(owner.getId()){
		        case R.id.first:
		        case R.id.second:
		        case R.id.third:
		        case R.id.fourth:
		        case R.id.fifth:
		        case R.id.sixth:
		        case R.id.seventh:
		        	if(container.getChildCount()!=0){
		        		ImageView toCover = (ImageView)container.getChildAt(container.getChildCount()-1);
		        		ImageView toPut = (ImageView)owner.getChildAt(indexToMove);
		        		Card toCoverCard = getDesc(toCover);
		        		Card toPutCard = getDesc(toPut);
		        		if(toCoverCard.red != toPutCard.red && toCoverCard.number - toPutCard.number == 1){//accept move
		        			//setup last action
		        			ACTION_POINTER++;
		        			lastAction = new LastAction();
		        			lastAction.MOVE = FROM_FIELD_TO_FIELD;
		        			if(!STOP_COUNT) {
				    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
				    		  }else{
				    			  if(++MOVES_NOT_COUNT==10) {
				    				  STOP_COUNT = false;
				    				  moves.setTextColor(Color.WHITE);
				    			  }
				    		  }
		        			ImageView[] toMove = new ImageView[owner.getChildCount()-indexToMove];
		        			for(int i=0;i<toMove.length;i++)
		        				toMove[i] = (ImageView)owner.getChildAt(indexToMove+i);
		        			
		        			if(indexToMove != 0){// not moving last card of vertical
		        				if(owner.getChildAt(indexToMove-1).getId()/100 != 9){//not have to turn
		        					((ImageView)owner.getChildAt(owner.getChildCount()-1)).setLayoutParams(full);
		        				}else{// i have to turn
		        					if(++VICTORY_CHECK == 21) {
		        						win.setVisibility(View.VISIBLE);
		        						wintext.setVisibility(View.VISIBLE);	
		        					}
		        						
		        					((ImageView)owner.getChildAt(indexToMove-1)).setLayoutParams(full);
		        					((ImageView)owner.getChildAt(indexToMove-1)).setImageDrawable(getResources().getDrawable(retCardImage(((ImageView)owner.getChildAt(indexToMove-1)).getId()-900)));
		        					((ImageView)owner.getChildAt(indexToMove-1)).setId(retId(((ImageView)owner.getChildAt(indexToMove-1)).getId()-900));
		        					((ImageView)owner.getChildAt(indexToMove-1)).setOnTouchListener(new MyTouchListener());
		        					lastAction.cardHasBennTurned = true;
		        				}//finish turn
		        			}
		        			((ImageView)container.getChildAt(container.getChildCount()-1)).setLayoutParams(cut);
		        			for(int i=0;i<toMove.length;i++){
		        				owner.removeView(toMove[i]);
		        				toMove[i].setVisibility(View.VISIBLE);
		        				container.addView(toMove[i]);
		        				if(i != toMove.length-1)
		        					toMove[i].setLayoutParams(cut);
		        				else toMove[i].setLayoutParams(full);	
		        			}
		        			
		        			lastAction.numberOfCardsMoved = toMove.length;
		        			lastAction.preColumn = ((LinearLayout)owner.getParent()).indexOfChild(owner)/2;
		        			lastAction.postColumn = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
		        			lastActionVector.add(lastAction);
		        		}else{//put back cards
		        			for(int i=0;i<owner.getChildCount();i++)
		        				((ImageView)owner.getChildAt(i)).setVisibility(View.VISIBLE);
		        		}
		        	}else{//accept kings
		        		ImageView toPut = (ImageView)owner.getChildAt(indexToMove);
		        		Card toPutCard = getDesc(toPut);
		        		Log.d("card moving",""+toPutCard.number);
		        		if(toPutCard.number == 13){
		        			ACTION_POINTER++;
		        			lastAction = new LastAction();
		        			lastAction.MOVE = FROM_FIELD_TO_FIELD;
		        			if(!STOP_COUNT) {
				    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
				    		  }else{
				    			  if(++MOVES_NOT_COUNT==10) {
				    				  STOP_COUNT = false;
				    				  moves.setTextColor(Color.WHITE);
				    			  }
				    		  }
		        			
		        			ImageView[] toMove = new ImageView[owner.getChildCount()-indexToMove];
		        			if(indexToMove != 0){// not moving last card of vertical
		        				if(owner.getChildAt(indexToMove-1).getId()/100 != 9){//not have to turn
		        					((ImageView)owner.getChildAt(owner.getChildCount()-1)).setLayoutParams(full);
		        				}else{// i have to turn
		        					if(++VICTORY_CHECK == 21) {
		        						win.setVisibility(View.VISIBLE);wintext.setVisibility(View.VISIBLE);	
		        					}
		        					((ImageView)owner.getChildAt(indexToMove-1)).setLayoutParams(full);
		        					((ImageView)owner.getChildAt(indexToMove-1)).setImageDrawable(getResources().getDrawable(retCardImage(((ImageView)owner.getChildAt(indexToMove-1)).getId()-900)));
		        					((ImageView)owner.getChildAt(indexToMove-1)).setId(retId(((ImageView)owner.getChildAt(indexToMove-1)).getId()-900));
		        					((ImageView)owner.getChildAt(indexToMove-1)).setOnTouchListener(new MyTouchListener());
		        					lastAction.cardHasBennTurned = true;
		        				}//finish turn
		        			}
		        			for(int i=0;i<toMove.length;i++)
		        				toMove[i] = (ImageView)owner.getChildAt(indexToMove+i);
		        			for(int i=0;i<toMove.length;i++){
		        				owner.removeView(toMove[i]);
		        				toMove[i].setVisibility(View.VISIBLE);
		        				container.addView(toMove[i]);
		        				if(i != toMove.length-1) 
		        					toMove[i].setLayoutParams(cut);
		        				else toMove[i].setLayoutParams(full);	
		        			}
		        			lastAction.numberOfCardsMoved = toMove.length;
		        			lastAction.preColumn = ((LinearLayout)owner.getParent()).indexOfChild(owner)/2;
		        			lastAction.postColumn = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
		        			lastActionVector.add(lastAction);
		        		}else{
		        			for(int i=0;i<owner.getChildCount();i++)
		        				((ImageView)owner.getChildAt(i)).setVisibility(View.VISIBLE);
		        		}
		        	}
		        	break;
		        case R.id.depos1:
		        case R.id.depos2:
		        case R.id.depos3:
		        case R.id.depos4:
		        	Card toPutCard = getDesc(card);
		        	boolean accepted = false ;
		        		if(container.getChildCount() != 0){
		        			ImageView toCover = (ImageView)container.getChildAt(container.getChildCount()-1);
			        		Card toCoverCard = getDesc(toCover);
			        		
			        		if(toCoverCard.red != toPutCard.red && toCoverCard.number - toPutCard.number == 1){
			        			ACTION_POINTER++;
			        			lastAction = new LastAction();
			        			lastAction.MOVE = FROM_PIT_TO_FIELD;
			        			if(!STOP_COUNT) {
					    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
					    		  }else{
					    			  if(++MOVES_NOT_COUNT==10) {
					    				  STOP_COUNT = false;
					    				  moves.setTextColor(Color.WHITE);
					    			  }
					    		  }
			        			((ImageView)container.getChildAt(container.getChildCount()-1)).setLayoutParams(cut);
			        			owner.removeView(card);
			        			container.addView(card);
			        			card.setOnTouchListener(new MyTouchListener());
			        			accepted = true;
			        			//lastAction.numberOfCardsMoved = toMove.length;
			        			lastAction.prePit = ((LinearLayout)owner.getParent()).indexOfChild(owner)/2;
			        			lastAction.postColumn = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
			        			lastActionVector.add(lastAction);
			        			
			        		}else card.setVisibility(View.VISIBLE);
		        		}else{
		        			if(toPutCard.number == 13){
		        				ACTION_POINTER++;
			        			lastAction = new LastAction();
			        			lastAction.MOVE = FROM_PIT_TO_FIELD;
		        				if(!STOP_COUNT) {
		  		    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
		  		    		  }else{
		  		    			  if(++MOVES_NOT_COUNT==10) {
		  		    				  STOP_COUNT = false;
		  		    				  moves.setTextColor(Color.WHITE);
		  		    			  }
		  		    		  }
		        				if(++FINAL_MOVE == 4) {
	        						win.setVisibility(View.VISIBLE);wintext.setVisibility(View.VISIBLE);	
	        					}
			        			owner.removeView(card);
			        			container.addView(card);
			        			card.setOnTouchListener(new MyTouchListener());
			        			accepted = true;
			        			lastAction.prePit = ((LinearLayout)owner.getParent()).indexOfChild(owner)/2;
			        			lastAction.postColumn = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
			        			lastActionVector.add(lastAction);
		        			}
		        		}
		        		if(toPutCard.number != 1 && accepted){
		        		ImageView toRestore = new ImageView(GameField.this);
		        		toRestore.setImageDrawable(getResources().getDrawable(retCardImageFromId(card.getId()-1)));
		        		toRestore.setId(card.getId()-1);
		        		toRestore.setLayoutParams(lp2);
		        		toRestore.setScaleType(ScaleType.FIT_XY);
		        		toRestore.setOnTouchListener(new forDrawListener());
		        		owner.addView(toRestore);
		        		}else{
		        			if(accepted){
		        				ImageView toRestore = new ImageView(GameField.this);
				        		toRestore.setImageDrawable(getResources().getDrawable(R.drawable.new_backas));
				        		toRestore.setId(R.id.blank_as);
				        		toRestore.setLayoutParams(lp2);
				        		toRestore.setScaleType(ScaleType.FIT_XY);
				        		owner.addView(toRestore);
		        			}
		        		}
		        	
		        	break;
		        case R.id.draw:
		        	Card toPutCard2 = getDesc(card);
		        	boolean accepted2 = false ;
		        		if(container.getChildCount() != 0){
		        			ImageView toCover = (ImageView)container.getChildAt(container.getChildCount()-1);
			        		Card toCoverCard = getDesc(toCover);
			        		
			        		if(toCoverCard.red != toPutCard2.red && toCoverCard.number - toPutCard2.number == 1){
			        			ACTION_POINTER++;
			        			lastAction = new LastAction();
			        			lastAction.MOVE = FROM_DRAW_TO_FIELD;
			        			if(!STOP_COUNT) {
					    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
					    		  }else{
					    			  if(++MOVES_NOT_COUNT==10) {
					    				  STOP_COUNT = false;
					    				  moves.setTextColor(Color.WHITE);
					    			  }
					    		  }
			        			((ImageView)container.getChildAt(container.getChildCount()-1)).setLayoutParams(cut);
			        			owner.removeView(card);
			        			container.addView(card);
			        			card.setOnTouchListener(new MyTouchListener());
			        			accepted2 = true;
			        			lastAction.postColumn = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
			        			lastActionVector.add(lastAction);
			        		}else card.setVisibility(View.VISIBLE);
		        		}else{
		        			if(toPutCard2.number == 13){
		        				ACTION_POINTER++;
			        			lastAction = new LastAction();
			        			lastAction.MOVE = FROM_DRAW_TO_FIELD;
		        				if(!STOP_COUNT) {
		  		    			  moves.setText(getString(R.string.moves)+": "+(++NUMBER_MOVES));
		  		    		  }else{
		  		    			  if(++MOVES_NOT_COUNT==10) {
		  		    				  STOP_COUNT = false;
		  		    				  moves.setTextColor(Color.WHITE);
		  		    			  }
		  		    		  }
		        				
			        			owner.removeView(card);
			        			container.addView(card);
			        			card.setOnTouchListener(new MyTouchListener());
			        			accepted2 = true;
			        			lastAction.postColumn = ((LinearLayout)container.getParent()).indexOfChild(container)/2;
			        			lastActionVector.add(lastAction);
		        			}
		        		}
		        		if(INDEX_TO_DRAW - INCREMENT_TO_DRAW != 0 && accepted2){
		        		ImageView toRestore = new ImageView(GameField.this);
		        		toRestore.setImageDrawable(getResources().getDrawable(retCardImage(toDraw.get(INDEX_TO_DRAW-INCREMENT_TO_SHOW))));
		        		toRestore.setId(retId(toDraw.get(INDEX_TO_DRAW-INCREMENT_TO_SHOW)));
		        		toRestore.setLayoutParams(lp2);
		        		toRestore.setScaleType(ScaleType.FIT_XY);
		        		toRestore.setOnTouchListener(new forDrawListener());
		        		draw.addView(toRestore);
		        		toDraw.remove(INDEX_TO_DRAW - INCREMENT_TO_DRAW);
		        		INDEX_TO_DRAW-=INCREMENT_TO_DRAW;
		        		CARD_IN_PILE--;
		        		}else{
		        			if(accepted2){
		        			toDraw.remove(INDEX_TO_DRAW - INCREMENT_TO_DRAW);
			        		INDEX_TO_DRAW-=INCREMENT_TO_DRAW;
			        		CARD_IN_PILE--;
		        			}
		        		}
		        	
		        	break;
		        
		        }
	        break;
	      case DragEvent.ACTION_DRAG_ENDED:

	      default:
	        break;
	      }
	      
	      return true;
	    }
	    
	  }
  
  protected class LastAction{
	  public boolean cardHasBennTurned;
	  public int MOVE;
	  public int prePit;
	  public int postPit;
	  public int preColumn;
	  public int postColumn;
	  public int numberOfCardsMoved;
	  public int drawToRestore;
	  public int idTurned;
	  public int INDEX;
	  
	  public LastAction(){
		  
	  }
  }
  
  
  Bitmap ConvertToBitmap(LinearLayout v) {
	  v.setDrawingCacheEnabled(true);
         
	v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
	            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight()); 

	v.buildDrawingCache(true);
	Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
	v.setDrawingCacheEnabled(false); // clear drawing cache
      
      return b;


  }
  
  
  Card getDesc(ImageView v){
	  return new Card((v.getId() - (Integer)(v.getId()/100)*100) , v.getId()/100<3 );
  }
  
  class Card {
	  final int number;
	  final boolean red;
	  
	  public Card(int number, boolean red){
		  this.number=number;
		  this.red=red;
	  }
	  
  }
  
 
  
  public int countCards(LinearLayout[] toCount){
	  int nonNullPositions=0;
      for (LinearLayout aToCount : toCount) {
          try {
              //LinearLayout checkNull = aToCount;
              nonNullPositions++;
          } catch (Exception e) {
              Log.d("Exception", "line 1120");
          }
      }
	  return nonNullPositions;
  }
  
  int retCardImage(int index){
	  switch(index){
	  	case 1:
	  		return R.drawable.ac;
	  	case 2:
	  		return R.drawable.duec;
	  	case 3:
	  		return R.drawable.trec;
	  	case 4:
	  		return R.drawable.quattroc;
	  	case 5:
	  		return R.drawable.cinquec;
	  	case 6:
	  		return R.drawable.seic;
	  	case 7:
	  		return R.drawable.settec;
	  	case 8:
	  		return R.drawable.ottoc;
	  	case 9:
	  		return R.drawable.novec;
	  	case 10:
	  		return R.drawable.diecic;
	  	case 11:
	  		return R.drawable.jc;
	  	case 12:
	  		return R.drawable.qc;
	  	case 13:
	  		return R.drawable.kc;
	  	case 14:
	  		return R.drawable.aq;
	  	case 15:
	  		return R.drawable.dueq;
	  	case 16:
	  		return R.drawable.treq;
	  	case 17:
	  		return R.drawable.quattroq;
	  	case 18:
	  		return R.drawable.cinqueq;
	  	case 19:
	  		return R.drawable.seiq;
	  	case 20:
	  		return R.drawable.setteq;
	  	case 21:
	  		return R.drawable.ottoq;
	  	case 22:
	  		return R.drawable.noveq;
	  	case 23:
	  		return R.drawable.dieciq;
	  	case 24:
	  		return R.drawable.jq;
	  	case 25:
	  		return R.drawable.qq;
	  	case 26:
	  		return R.drawable.kq;
	  	case 27:
	  		return R.drawable.af;
	  	case 28:
	  		return R.drawable.duef;
	  	case 29:
	  		return R.drawable.tref;
	  	case 30:
	  		return R.drawable.quattrof;
	  	case 31:
	  		return R.drawable.cinquef;
	  	case 32:
	  		return R.drawable.seif;
	  	case 33:
	  		return R.drawable.settef;
	  	case 34:
	  		return R.drawable.ottof;
	  	case 35:
	  		return R.drawable.novef;
	  	case 36:
	  		return R.drawable.diecif;
	  	case 37:
	  		return R.drawable.jf;
	  	case 38:
	  		return R.drawable.qf;
	  	case 39:
	  		return R.drawable.kf;
	  	case 40:
	  		return R.drawable.ap;
	  	case 41:
	  		return R.drawable.duep;
	  	case 42:
	  		return R.drawable.trep;
	  	case 43:
	  		return R.drawable.quattrop;
	  	case 44:
	  		return R.drawable.cinquep;
	  	case 45:
	  		return R.drawable.seip;
	  	case 46:
	  		return R.drawable.settep;
	  	case 47:
	  		return R.drawable.ottop;
	  	case 48:
	  		return R.drawable.novep;
	  	case 49:
	  		return R.drawable.diecip;
	  	case 50:
	  		return R.drawable.jp;
	  	case 51:
	  		return R.drawable.qp;
	  	case 52:
	  		return R.drawable.kp;
	  }
	  return 0;
  }
  
  
  int retCardImageFromId(int index){
	  switch(index){
	  	case 101:
	  		return R.drawable.ac;
	  	case 102:
	  		return R.drawable.duec;
	  	case 103:
	  		return R.drawable.trec;
	  	case 104:
	  		return R.drawable.quattroc;
	  	case 105:
	  		return R.drawable.cinquec;
	  	case 106:
	  		return R.drawable.seic;
	  	case 107:
	  		return R.drawable.settec;
	  	case 108:
	  		return R.drawable.ottoc;
	  	case 109:
	  		return R.drawable.novec;
	  	case 110:
	  		return R.drawable.diecic;
	  	case 111:
	  		return R.drawable.jc;
	  	case 112:
	  		return R.drawable.qc;
	  	case 113:
	  		return R.drawable.kc;
	  	case 201:
	  		return R.drawable.aq;
	  	case 202:
	  		return R.drawable.dueq;
	  	case 203:
	  		return R.drawable.treq;
	  	case 204:
	  		return R.drawable.quattroq;
	  	case 205:
	  		return R.drawable.cinqueq;
	  	case 206:
	  		return R.drawable.seiq;
	  	case 207:
	  		return R.drawable.setteq;
	  	case 208:
	  		return R.drawable.ottoq;
	  	case 209:
	  		return R.drawable.noveq;
	  	case 210:
	  		return R.drawable.dieciq;
	  	case 211:
	  		return R.drawable.jq;
	  	case 212:
	  		return R.drawable.qq;
	  	case 213:
	  		return R.drawable.kq;
	  	case 301:
	  		return R.drawable.af;
	  	case 302:
	  		return R.drawable.duef;
	  	case 303:
	  		return R.drawable.tref;
	  	case 304:
	  		return R.drawable.quattrof;
	  	case 305:
	  		return R.drawable.cinquef;
	  	case 306:
	  		return R.drawable.seif;
	  	case 307:
	  		return R.drawable.settef;
	  	case 308:
	  		return R.drawable.ottof;
	  	case 309:
	  		return R.drawable.novef;
	  	case 310:
	  		return R.drawable.diecif;
	  	case 311:
	  		return R.drawable.jf;
	  	case 312:
	  		return R.drawable.qf;
	  	case 313:
	  		return R.drawable.kf;
	  	case 401:
	  		return R.drawable.ap;
	  	case 402:
	  		return R.drawable.duep;
	  	case 403:
	  		return R.drawable.trep;
	  	case 404:
	  		return R.drawable.quattrop;
	  	case 405:
	  		return R.drawable.cinquep;
	  	case 406:
	  		return R.drawable.seip;
	  	case 407:
	  		return R.drawable.settep;
	  	case 408:
	  		return R.drawable.ottop;
	  	case 409:
	  		return R.drawable.novep;
	  	case 410:
	  		return R.drawable.diecip;
	  	case 411:
	  		return R.drawable.jp;
	  	case 412:
	  		return R.drawable.qp;
	  	case 413:
	  		return R.drawable.kp;
	  }
	  return 0;
  }
  
int retValueFromId(int id){
	switch(id){
case 101:
	return 1; //a
case 102:
	return 2;
case 103:	
	return 3;
case 104:
	return 4;
case 105:
	return 5;
case 106:
	return 6;
case 107:
	return 7;
case 108:
	return 8;
case 109:
	return 9;
case 110:
	return 10;
case 111:
	return 11; //j
case 112:
	return 12; //q
case 113:
	return 13; //k
case 201:
	return 14;
case 202:
	return 15;
case 203:
	return 16;
case 204:
	return 17;
case 205:
	return 18;
case 206:
	return 19;
case 207:
	return 20;
case 208:
	return 21;
case 209:
	return 22;
case 210:
	return 23;
case 211:
	return 24;
case 212:
	return 25;
case 213:
	return 26;
case 301:
	return 27;
case 302:
	return 28;
case 303:
	return 29;
case 304:
	return 30;
case 305:
	return 31;
case 306:
	return 32;
case 307:
	return 33;
case 308:
	return 34;
case 309:
	return 35;
case 310:
	return 36;
case 311:
	return 37;
case 312:
	return 38;
case 313:
	return 39;
case 401:
	return 40;
case 402:
	return 41;
case 403:
	return 42;
case 404:
	return 43;
case 405:
	return 44;
case 406:
	return 45;
case 407:
	return 46;
case 408:
	return 47;
case 409:
	return 48;
case 410:
	return 49;
case 411:
	return 50;
case 412:
	return 51;
case 413:
	return 52;
	
}
return 0;	
}
  
int retId(int choice){
	switch(choice){
		case 1:
			return 101; //a
		case 2:
			return 102;
		case 3:	
			return 103;
		case 4:
			return 104;
		case 5:
			return 105;
		case 6:
			return 106;
		case 7:
			return 107;
		case 8:
			return 108;
		case 9:
			return 109;
		case 10:
			return 110;
		case 11:
			return 111; //j
		case 12:
			return 112; //q
		case 13:
			return 113; //k
		case 14:
			return 201;
		case 15:
			return 202;
		case 16:
			return 203;
		case 17:
			return 204;
		case 18:
			return 205;
		case 19:
			return 206;
		case 20:
			return 207;
		case 21:
			return 208;
		case 22:
			return 209;
		case 23:
			return 210;
		case 24:
			return 211;
		case 25:
			return 212;
		case 26:
			return 213;
		case 27:
			return 301;
		case 28:
			return 302;
		case 29:
			return 303;
		case 30:
			return 304;
		case 31:
			return 305;
		case 32:
			return 306;
		case 33:
			return 307;
		case 34:
			return 308;
		case 35:
			return 309;
		case 36:
			return 310;
		case 37:
			return 311;
		case 38:
			return 312;
		case 39:
			return 313;
		case 40:
			return 401;
		case 41:
			return 402;
		case 42:
			return 403;
		case 43:
			return 404;
		case 44:
			return 405;
		case 45:
			return 406;
		case 46:
			return 407;
		case 47:
			return 408;
		case 48:
			return 409;
		case 49:
			return 410;
		case 50:
			return 411;
		case 51:
			return 412;
		case 52:
			return 413;
			
	}
	return 0;
}
  
} 