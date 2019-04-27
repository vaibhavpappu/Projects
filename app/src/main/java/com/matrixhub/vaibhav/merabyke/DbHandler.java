package com.matrixhub.vaibhav.merabyke;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper
{
    Context context;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usersdb";
    private static final String TABLE_NAME = "MYTABLE";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MNO = "mno";;
    ArrayList idList=new ArrayList();
    ArrayList nameList=new ArrayList();
    ArrayList btAddressList=new ArrayList();
    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT" +
                " PRIMARY KEY NOT NULL," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }
    public void insertUserDetails(String name, String mno)
    {

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ID,mno);
        cValues.put(KEY_NAME, name);
//        cValues.put(KEY_NAME, mno);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME,null, cValues);
        db.close();
    }

  /*  public void checkRecords(String name,String mno)
    {
        String Key="KEY_ID";
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT * FROM " +TABLE_NAME;

        Cursor cursor=db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                btAddressList.add(cursor.getString(cursor.getColumnIndex(KEY_ID)));
             //   btNameList.add(cursor.getString(cursor.getColumnIndex("id")));

            }while (cursor.moveToNext());
            if (btAddressList.contains(mno))
            {
            }
            insertUserDetails(name,mno);

        }

    }*/

    public Cursor getAllUser()
    {
        String Key="KEY_ID";
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT * FROM " +TABLE_NAME;

        Cursor cursor=db.rawQuery(query, null);

        return cursor;

          }
}