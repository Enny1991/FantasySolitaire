package com.eneaceolini.fantasysolitaire;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "notes";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//create personal table
		String create = "";
		create += "CREATE TABLE " + TableNotes.TABLE_NAME + " (";
		create += "  " + TableNotes.COLUMN_ID + " INTEGER PRIMARY KEY,";
		create += "  " + TableNotes.COLUMN_LVL + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_NAME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_EXP + " TEXT NOT NULL,";
		create += "  " + "inarow" + " INTEGER,";
		create += "  UNIQUE (name)";
		create += ")";
		db.execSQL(create);
		
		//create normale game table
		create = "";
		create += "CREATE TABLE " + " normal " + " (";
		create += "  " + TableNotes.COLUMN_ID + " INTEGER PRIMARY KEY,";
		create += "  " + TableNotes.COLUMN_NAME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MATCH + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MIN_MOVES + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MIN_TIME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MOVES + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_TIME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_WIN + " TEXT NOT NULL,";
		create += "  UNIQUE (name)";
		create += ")";
		db.execSQL(create);
		
		
		create = "";
		create += "CREATE TABLE " + " three " + " (";
		create += "  " + TableNotes.COLUMN_ID + " INTEGER PRIMARY KEY,";
		create += "  " + TableNotes.COLUMN_NAME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MATCH + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MIN_MOVES + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MIN_TIME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MOVES + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_TIME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_WIN + " TEXT NOT NULL,";
		create += "  UNIQUE (name)";
		create += ")";
		db.execSQL(create);
		
		create = "";
		create += "CREATE TABLE " + " vegas " + " (";
		create += "  " + TableNotes.COLUMN_ID + " INTEGER PRIMARY KEY,";
		create += "  " + TableNotes.COLUMN_NAME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MATCH + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MIN_MOVES + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MIN_TIME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MOVES + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_TIME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_WIN + " TEXT NOT NULL,";
		create += "  UNIQUE (name)";
		create += ")";
		db.execSQL(create);
		
		create = "";
		create += "CREATE TABLE " + " power " + " (";
		create += "  " + TableNotes.COLUMN_ID + " INTEGER PRIMARY KEY,";
		create += "  " + TableNotes.COLUMN_NAME + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_ICE + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_FIRE + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_EARTH + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_THUNDER + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_TI + " TEXT NOT NULL,";
		create += "  " + TableNotes.COLUMN_MANA + " TEXT NOT NULL,";
		create += "  UNIQUE (name)";
		create += ")";
		db.execSQL(create);
		
		
		create = "";
		create += "CREATE TABLE " + " achive " + " (";
		create += "  " + TableNotes.COLUMN_ID + " INTEGER PRIMARY KEY,"; //0
		create += "  " + TableNotes.COLUMN_NAME + " TEXT NOT NULL,"; //1
		create += "  " + "wnorm1" + " INTEGER,";
		create += "  " + "wnorm2" + " INTEGER,";
		create += "  " + "wnorm3" + " INTEGER,";
		create += "  " + "tnorm300" + " INTEGER,";
		create += "  " + "tnorm200" + " INTEGER,";
		create += "  " + "tnorm100" + " INTEGER,";
		create += "  " + "mnorm100" + " INTEGER,";
		create += "  " + "mnorm50" + " INTEGER,";
		create += "  " + "mnorm25" + " INTEGER,";//10
		create += "  " + "wthree1" + " INTEGER,";
		create += "  " + "wthree2" + " INTEGER,";
		create += "  " + "wthree3" + " INTEGER,";
		create += "  " + "tthree300" + " INTEGER,";
		create += "  " + "tthree200" + " INTEGER,";
		create += "  " + "tthree100" + " INTEGER,";
		create += "  " + "mthree100" + " INTEGER,";
		create += "  " + "mthree50" + " INTEGER,";
		create += "  " + "mthree25" + " INTEGER,";
		create += "  " + "wvegas1" + " INTEGER,";//20
		create += "  " + "wvegas2" + " INTEGER,";
		create += "  " + "wvegas3" + " INTEGER,";
		create += "  " + "tvegas300" + " INTEGER,";
		create += "  " + "tvegas200" + " INTEGER,";
		create += "  " + "tvegas100" + " INTEGER,";
		create += "  " + "mvegas100" + " INTEGER,";
		create += "  " + "mvegas50" + " INTEGER,";
		create += "  " + "mvegas25" + " INTEGER,";
		create += "  " + "ice1" + " INTEGER,";
		create += "  " + "ice2" + " INTEGER,";//30
		create += "  " + "ice3" + " INTEGER,";
		create += "  " + "thunder1" + " INTEGER,";
		create += "  " + "thunder2" + " INTEGER,";
		create += "  " + "thunder3" + " INTEGER,";
		create += "  " + "earth1" + " INTEGER,";
		create += "  " + "earth2" + " INTEGER,";
		create += "  " + "earth3" + " INTEGER,";
		create += "  " + "time1" + " INTEGER,";
		create += "  " + "time2" + " INTEGER,";
		create += "  " + "time3" + " INTEGER,";//40
		create += "  " + "fire1" + " INTEGER,";
		create += "  " + "fire2" + " INTEGER,";
		create += "  " + "fire3" + " INTEGER,";
		create += "  " + "winboth" + " INTEGER,"; //44
		create += "  " + "windraw" + " INTEGER,"; //45
		create += "  " + "moves200" + " INTEGER,";
		create += "  " + "time600" + " INTEGER,";
		create += "  " + "fouras" + " INTEGER,";
		create += "  " + "win10" + " INTEGER,";
		create += "  " + "winnocomp" + " INTEGER,";
		create += "  " + "wastetime" + " INTEGER,";
		
		create += "  UNIQUE (name)";
		create += ")";
		db.execSQL(create);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// non previsto
	}

}
