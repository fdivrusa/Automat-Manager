package heh.be.automatmanager.DB.Automat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.autofill.AutofillId;

import java.util.ArrayList;

import heh.be.automatmanager.DB.DBSqlite;

/**
 * Created by flori on 12-12-17.
 */

public class AutomatAccessDB {

    private static final int VERSION = 4;
    private static final String NAME_DB = "androidProject.db";
    private static final String TABLE_AUTOMAT = "table_automat";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final int NUM_COL_DESCRIPTION = 2;
    private static final String COL_IP_ADDRESS = "IP_ADDRESS";
    private static final int NUM_COL_IP_ADDRESS = 3;
    private static final String COL_SLOT_NUMBER = "SLOT_NUMBER";
    private static final int NUM_COL_SLOT_NUMBER = 4;
    private static final String COL_RACK_NUMBER = "RACK_NUMBER";
    private static final int NUM_COL_RACK_NUMBER = 5;
    private static final String COL_TYPE_AUTOMAT = "TYPE_AUTOMAT";
    private static final int NUM_COL_TYPE_AUTOMAT = 6;
    private static final String COL_DATABLOC_NUMBER = "DATABLOC_NUMBER";
    private static final int NUM_COL_DATABLOC_NUMBER = 7;

    private SQLiteDatabase db;
    private DBSqlite automatDb;

    public AutomatAccessDB(Context context) {

        automatDb = new DBSqlite(context, NAME_DB, null, VERSION);
    }

    public void openForWrite() {

        db = automatDb.getWritableDatabase();
    }

    public void openForRead() {

        db = automatDb.getReadableDatabase();
    }

    public void close() {

        db.close();
    }

    public long insertAutomat(Automat automat) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, automat.getName());
        contentValues.put(COL_DESCRIPTION, automat.getDescription());
        contentValues.put(COL_IP_ADDRESS, automat.getIpAddress());
        contentValues.put(COL_SLOT_NUMBER, automat.getSlotNumber());
        contentValues.put(COL_RACK_NUMBER, automat.getRackNumber());
        contentValues.put(COL_TYPE_AUTOMAT, automat.getTypeAutomat());
        contentValues.put(COL_DATABLOC_NUMBER, automat.getDatablocNumber());

        return db.insert(TABLE_AUTOMAT, null, contentValues);
    }

    public int updateAutomat(int id, Automat automat) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, automat.getName());
        contentValues.put(COL_DESCRIPTION, automat.getDescription());
        contentValues.put(COL_IP_ADDRESS, automat.getIpAddress());
        contentValues.put(COL_SLOT_NUMBER, automat.getSlotNumber());
        contentValues.put(COL_RACK_NUMBER, automat.getRackNumber());
        contentValues.put(COL_TYPE_AUTOMAT, automat.getTypeAutomat());
        contentValues.put(COL_DATABLOC_NUMBER, automat.getDatablocNumber());

        return db.update(TABLE_AUTOMAT, contentValues, COL_ID + " = " + id, null);
    }

    public int removeAutomat(String name) {

        return db.delete(TABLE_AUTOMAT, "Name=?", new String[]{name});
    }

    public ArrayList<Automat> getAllAutomats() {

        Cursor c = db.query(TABLE_AUTOMAT, new String[]{
                        COL_ID, COL_NAME, COL_DESCRIPTION, COL_IP_ADDRESS, COL_SLOT_NUMBER, COL_RACK_NUMBER, COL_TYPE_AUTOMAT, COL_DATABLOC_NUMBER}, null, null, null, null,
                COL_NAME);

        ArrayList<Automat> tabAutomat = new ArrayList<>();

        if (c.getCount() == 0) {

            c.close();
            return tabAutomat;
        }

        while (c.moveToNext()) {

            Automat automat = new Automat();
            automat.setId(c.getInt(NUM_COL_ID));
            automat.setName(c.getString(NUM_COL_NAME));
            automat.setDescription(c.getString(NUM_COL_DESCRIPTION));
            automat.setIpAddress(c.getString(NUM_COL_IP_ADDRESS));
            automat.setSlotNumber(c.getInt(NUM_COL_SLOT_NUMBER));
            automat.setRackNumber(c.getInt(NUM_COL_RACK_NUMBER));
            automat.setTypeAutomat(c.getString(NUM_COL_TYPE_AUTOMAT));
            automat.setDatablocNumber(c.getInt(NUM_COL_DATABLOC_NUMBER));
            tabAutomat.add(automat);
        }
        c.close();
        return tabAutomat;
    }

    public int truncateDB() {

        return db.delete(TABLE_AUTOMAT, null, null);
    }

    public Automat getAutomat(String name) {

        Cursor c = db.query(TABLE_AUTOMAT, new String[]{
                        COL_ID, COL_NAME, COL_DESCRIPTION, COL_IP_ADDRESS, COL_SLOT_NUMBER, COL_RACK_NUMBER, COL_TYPE_AUTOMAT, COL_DATABLOC_NUMBER},
                COL_NAME + " LIKE \"" + name + "\"",
                null, null, null, COL_NAME);

        return cursorToAutomat(c);
    }

    public Automat cursorToAutomat(Cursor c) {

        if (c.getCount() == 0) {
            c.close();
            return null;
        }

        c.moveToFirst();

        Automat automat = new Automat();
        automat.setId(c.getInt(NUM_COL_ID));
        automat.setName(c.getString(NUM_COL_NAME));
        automat.setDescription(c.getString(NUM_COL_DESCRIPTION));
        automat.setIpAddress(c.getString(NUM_COL_IP_ADDRESS));
        automat.setSlotNumber(c.getInt(NUM_COL_SLOT_NUMBER));
        automat.setRackNumber(c.getInt(NUM_COL_RACK_NUMBER));
        automat.setTypeAutomat(c.getString(NUM_COL_TYPE_AUTOMAT));
        automat.setDatablocNumber(c.getInt(NUM_COL_DATABLOC_NUMBER));
        c.close();

        return automat;
    }
}
