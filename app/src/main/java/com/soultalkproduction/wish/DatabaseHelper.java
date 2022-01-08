package com.soultalkproduction.wish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.widget.Toast;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String Database_name = "storage.db";
    public static final String TABLE_NAME = "storage";
    public static final String COL1 = "ID";
    public static final String COL2 = "N_AME";
    public static final String COL3 = "D_ATTE";
    public static final String COL4 = "C_ONTACT";
    public static final String COL5 = "M_ESSAGE";
    public static final String COL6 = "E_VVENT";


    public DatabaseHelper(Context context) {
        super(context, Database_name, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
        //System.out.print("0/n0/n0/n0/n0/n0/nDatabase made/n0/n0/n0/n0/n0/n0/n0/n");
    }

    public void onCreate(SQLiteDatabase db) {

        String creattbl = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+"N_AME TEXT,C_ONTACT TEXT,D_ATTE TEXT,M_ESSAGE TEXT,E_VVENT TEXT)";
        db.execSQL(creattbl);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String contact, String Ddate, String message,String event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, Ddate);
        contentValues.put(COL4, contact);
        contentValues.put(COL5, message);
        contentValues.put(COL6,event);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor showData1(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE E_VVENT = 'BIRTHDAY'",null);
        return data;
    }

    public Cursor showData2(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE E_VVENT = 'ANNIVERSARY'",null);
        return data;
    }

    public Cursor showData3(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE E_VVENT LIKE 'CUSTOM%'",null);
        return data;
    }



    public Cursor showData4(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return data;
    }

    public Integer deletee(String id_){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[] {id_});
    }
}
