package be.howest.nmct.receptenapp.data.AuthorData;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Mattias on 17/01/2015.
 */
public class AuthorTable {
    // Database table
    public static final String TABLE_AUTHOR = "author";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FNAME = "firstname";
    public static final String COLUMN_LNAME = "lastname";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_AUTHOR + "("
            + COLUMN_ID + " text not null, "
            + COLUMN_FNAME + " text not null, "
            + COLUMN_LNAME + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(AuthorTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR);
        onCreate(database);
    }
}
