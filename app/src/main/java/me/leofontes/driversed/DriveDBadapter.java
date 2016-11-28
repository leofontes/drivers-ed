package me.leofontes.driversed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Leo on 4/4/16.
 */
public class DriveDBadapter {

    private SQLiteDatabase db;
    private DriveDBhelper dbHelper;
    private final Context context;

    private static DriveDBadapter sInstance;
    private static final String DB_NAME = "drive.db";
    private static int dbVersion = 1;

    private static final String DRIVE_TABLE = "drives";
    public static final String DRIVE_ID = "drive_id";
    public static final String DRIVE_DATE = "drive_date";
    public static final String DRIVE_HOURS = "drive_hours";
    public static final String DRIVE_CONDITION = "drive_condition";
    public static final String DRIVE_LESSON = "drive_Lesson";
    public static final String DRIVE_WEATHER = "drive_weather";
    public static final String DRIVE_DAY = "drive_day";
    public static final String[] DRIVE_COLS = {DRIVE_ID, DRIVE_DATE, DRIVE_HOURS, DRIVE_CONDITION, DRIVE_LESSON, DRIVE_WEATHER, DRIVE_DAY};

    public static synchronized DriveDBadapter getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DriveDBadapter(context.getApplicationContext());
        }
        return sInstance;
    }

    private DriveDBadapter(Context ctx) {
        context = ctx;
        dbHelper = new DriveDBhelper(context, DB_NAME, null, dbVersion);
    }

    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch(SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public void clear() {
        dbHelper.onUpgrade(db, dbVersion, dbVersion+1);  // change version to dump old data
        dbVersion++;
    }

    public long insertItem(DriveInfo drive) {
        //new row of values to insert
        ContentValues cvalues = new ContentValues();
        //assign values for each col
        cvalues.put(DRIVE_DATE, drive.getDate());
        cvalues.put(DRIVE_HOURS, drive.getHours());
        cvalues.put(DRIVE_CONDITION, drive.getCondition());
        cvalues.put(DRIVE_LESSON, drive.getLesson());
        cvalues.put(DRIVE_WEATHER, drive.getWeather());
        cvalues.put(DRIVE_DAY, drive.getDayOfTheWeek());
        //add to course table in database
        return db.insert(DRIVE_TABLE, null, cvalues);
    }

    public boolean removeItem(long did) {
        return db.delete(DRIVE_TABLE, "DRIVE_ID=" + did, null) > 0;
    }

    public boolean updateField(long did, ContentValues cvalues) {
        return db.update(DRIVE_TABLE, cvalues, "DRIVE_ID=" + did, null) > 0;
    }

    //database query methods
    public Cursor getAllItems() {
        return db.query(DRIVE_TABLE, DRIVE_COLS, null, null, null, null, null);
    }

    public Cursor getItemCursor(long did) throws SQLException {
        Cursor result = db.query(true, DRIVE_TABLE, DRIVE_COLS, DRIVE_ID+"="+did, null, null, null, null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
            throw new android.database.SQLException("No job items found for row: " + did);
        }
        return result;
    }

    public DriveInfo getDriveItem(long did) throws SQLException{
        Cursor cursor = db.query(true, DRIVE_TABLE, DRIVE_COLS, DRIVE_ID+"="+did, null, null, null, null, null);
        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new android.database.SQLException("No course items found for row: " + did);
        }
        return new DriveInfo(cursor.getString(1), null, cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
    }



    private static class DriveDBhelper extends SQLiteOpenHelper {

        // SQL statement to create a new database
        private static final String DB_CREATE = "CREATE TABLE " + DRIVE_TABLE
                + " (" + DRIVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DRIVE_DATE + " TEXT,"
                + DRIVE_HOURS + " TEXT, " + DRIVE_CONDITION + " TEXT, " + DRIVE_LESSON + " TEXT, "
                + DRIVE_WEATHER + " TEXT, " + DRIVE_DAY + " TEXT);";

        public DriveDBhelper(Context context, String name, SQLiteDatabase.CursorFactory fct, int version) {
            super(context, name, fct, version);
        }

        @Override
        public void onCreate(SQLiteDatabase adb) {
            adb.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase adb, int oldVersion, int newVersion) {
            Log.w("JobDB", "upgrading from version " + oldVersion + " to "
                    + newVersion + ", destroying old data");
            // drop old table if it exists, create new one
            // better to migrate existing data into new table
            adb.execSQL("DROP TABLE IF EXISTS " + DRIVE_TABLE);
            onCreate(adb);
        }

    } //DriveDBhelper class
} //DriveDBadapter class
