package com.example.qrbarcodescanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qrbarcodescanner.dto.ScanData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scandata";
    private static final String TABLE_SCANDATA = "scandata_tb";

    private static final String KEY_ID = "id";
    private static final String KEY_FORMATTYPE = "format_type";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE_TIME = "date";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOREAPPS_TABLE = "CREATE TABLE " + TABLE_SCANDATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FORMATTYPE + " TEXT," + KEY_CONTENT + " TEXT,"
               + KEY_DATE_TIME + " TEXT" + ")";
        db.execSQL(CREATE_MOREAPPS_TABLE);


    }

   @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANDATA);

       onCreate(db);
    }







    public void addEncodeData(ScanData scanData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FORMATTYPE, scanData.getDataFormat());
        values.put(KEY_CONTENT, scanData.getContent());

        values.put(KEY_DATE_TIME, scanData.getDate_time());

        db.insert(TABLE_SCANDATA, null, values);
        db.close(); // Closing database connection
    }

    /*BeanMoreApp getMoreApps(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOREAPPS, new String[]{KEY_ID,
                        KEY_APP_ID, KEY_TITLE,KEY_IMAGE_LINK}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        BeanMoreApp beanMoreApp = new BeanMoreApp(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
        return beanMoreApp;
    }
*/
    public List<ScanData> getAllData() {
        List<ScanData> scanDataArrayList = new ArrayList<ScanData>();
        String selectQuery = "SELECT  * FROM " + TABLE_SCANDATA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ScanData scanData = new ScanData();
                scanData.setId(Integer.parseInt(cursor.getString(0)));
                scanData.setDataFormat(cursor.getString(1));
                scanData.setContent(cursor.getString(2));
                scanData.setDate_time(cursor.getString(3));


                scanDataArrayList.add(scanData);
            } while (cursor.moveToNext());
        }
        Log.d("bharti","value1"+scanDataArrayList.size());

        return scanDataArrayList;

    }




    public void deleteCouponsApps()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_SCANDATA); //delete all rows in a table
        db.close();
    }
    public ArrayList<ScanData> removeByName(String name) {
        //  Cursor res = getReadableDatabase().rawQuery("select " + KEY_LAT + " , " + KEY_LNG +" from "+ TABLE_MAP_DATA + " where "+ KEY_NAME+" ='" + name + "'", null);
        getReadableDatabase().delete(TABLE_SCANDATA, KEY_CONTENT + "=?", new String[]{name});

        return (ArrayList<ScanData>) getAllData();
    }


}
