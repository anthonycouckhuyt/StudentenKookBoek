package be.howest.nmct.receptenapp.data.IngredientData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Mattias on 17/01/2015.
 */
public class IngredientDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredient.db";
    private static final int DATABASE_VERSION = 1;

    public IngredientDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        IngredientTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        IngredientTable.onUpgrade(database, oldVersion, newVersion);
    }

}