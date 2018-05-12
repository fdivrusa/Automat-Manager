package heh.be.automatmanager.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by flori on 02-12-17.
 */

public class DBSqlite extends SQLiteOpenHelper {

    private static final String TABLE_USER = "TABLE_USER";
    private static final String COL_ID_USER = "ID";
    private static final String COL_NAME_USER = "NAME";
    private static final String COL_SURNAME = "SURNAME";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_TYPE = "TYPE";

    private static final String TABLE_AUTOMAT = "TABLE_AUTOMAT";
    private static final String COL_ID_AUTOMAT = "ID";
    private static final String COL_NAME_AUTOMAT = "NAME";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_IP_ADDRESS = "IP_ADDRESS";
    private static final String COL_SLOT_NUMBER = "SLOT_NUMBER";
    private static final String COL_RACK_NUMBER = "RACK_NUMBER";
    private static final String COL_TYPE_AUTOMAT = "TYPE_AUTOMAT";
    private static final String COL_DATABLOC_NUMBER = "DATABLOC_NUMBER";

    private static final String CREATE_DB_USER = "CREATE TABLE " +
            TABLE_USER + " (" + COL_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME_USER + " TEXT NOT NULL, " + COL_SURNAME + " TEXT NOT NULL, " +
            COL_EMAIL + " TEXT NOT NULL UNIQUE, " + COL_PASSWORD + " TEXT NOT NULL, " +
            COL_TYPE + " TEXT NOT NULL);";

    private static final String CREATE_DB_AUTOMAT = "CREATE TABLE " +
            TABLE_AUTOMAT + " (" + COL_ID_AUTOMAT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME_AUTOMAT + " TEXT NOT NULL UNIQUE, " + COL_DESCRIPTION + " TEXT NOT NULL, " +
            COL_IP_ADDRESS + " TEXT NOT NULL, " + COL_SLOT_NUMBER + " INT NOT NULL, " +
            COL_RACK_NUMBER + " INT NOT NULL, " + COL_TYPE_AUTOMAT + " TEXT NOT NULL, " + COL_DATABLOC_NUMBER + " TEXT NOT NULL);";

    public DBSqlite(Context context, String name,
                    SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_USER);
        db.execSQL(CREATE_DB_AUTOMAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_DB_USER);
        db.execSQL(CREATE_DB_AUTOMAT);
    }

}
