package heh.be.automatmanager.DB.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import heh.be.automatmanager.DB.DBSqlite;

/**
 * Created by flori on 02-12-17.
 */

public class UserAccessDB {


    private static final int VERSION = 4;
    private static final String NAME_DB = "androidProject.db";
    private static final String TABLE_USER = "table_user";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_SURNAME = "SURNAME";
    private static final int NUM_COL_SURNAME = 2;
    private static final String COL_EMAIL = "EMAIL";
    private static final int NUM_COL_EMAIL = 3;
    private static final String COL_PASSWORD = "PASSWORD";
    private static final int NUM_COL_PASSWORD = 4;
    private static final String COL_TYPE = "TYPE";
    private static final int NUM_COL_TYPE = 5;

    private SQLiteDatabase db;
    private DBSqlite userDb;

    public UserAccessDB(Context context) {
        userDb = new DBSqlite(context, NAME_DB, null, VERSION);
    }

    public void openForWrite() {
        db = userDb.getWritableDatabase();
    }

    public void openForRead() {
        db = userDb.getReadableDatabase();
    }

    public void close() {
        db.close();
    }

    public long insertUser(User u) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, u.getName());
        contentValues.put(COL_SURNAME, u.getSurname());
        contentValues.put(COL_EMAIL, u.getEmail());
        contentValues.put(COL_PASSWORD, u.getPassword());
        contentValues.put(COL_TYPE, u.getType());

        return db.insert(TABLE_USER, null, contentValues);
    }

    public int updateUser(int id, User u) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, u.getName());
        contentValues.put(COL_SURNAME, u.getSurname());
        contentValues.put(COL_EMAIL, u.getEmail());
        contentValues.put(COL_PASSWORD, u.getPassword());
        contentValues.put(COL_TYPE, u.getType());

        return db.update(TABLE_USER, contentValues, COL_ID + " = " + id, null);
    }

    public int removeUser(String email) {
        return db.delete(TABLE_USER, "Email=?", new String[]{email});
    }

    public int removeAllEcxeptAdmin() {
        return db.delete(TABLE_USER, "Email!=?", new String[]{"android"});
    }

    public ArrayList<User> getAllUsers() {

        Cursor c = db.query(TABLE_USER, new String[]{
                        COL_ID, COL_NAME, COL_SURNAME, COL_EMAIL, COL_PASSWORD, COL_TYPE}, null, null, null, null,
                COL_EMAIL);

        ArrayList<User> tabUser = new ArrayList<>();

        if (c.getCount() == 0) {
            c.close();
            return tabUser;
        }

        while (c.moveToNext()) {

            User user = new User();
            user.setId(c.getInt(NUM_COL_ID));
            user.setName(c.getString(NUM_COL_NAME));
            user.setSurname(c.getString(NUM_COL_SURNAME));
            user.setEmail(c.getString(NUM_COL_EMAIL));
            user.setPassword(c.getString(NUM_COL_PASSWORD));
            user.setType(c.getString(NUM_COL_TYPE));
            tabUser.add(user);
        }
        c.close();
        return tabUser;
    }

    public int truncateDB() {

        return db.delete(TABLE_USER, null, null);
    }

    public User getUser(String login) {

        Cursor c = db.query(TABLE_USER, new String[]{
                        COL_ID, COL_NAME, COL_SURNAME, COL_EMAIL, COL_PASSWORD, COL_TYPE},
                COL_EMAIL + " LIKE \"" + login + "\"",
                null, null, null, COL_EMAIL);

        return cursorToUser(c);
    }

    public User cursorToUser(Cursor c) {

        if (c.getCount() == 0) {
            c.close();
            return null;
        }

        c.moveToFirst();

        User user = new User();
        user.setId(c.getInt(NUM_COL_ID));
        user.setName(c.getString(NUM_COL_NAME));
        user.setSurname(c.getString(NUM_COL_SURNAME));
        user.setEmail(c.getString(NUM_COL_EMAIL));
        user.setPassword(c.getString(NUM_COL_PASSWORD));
        user.setType(c.getString(NUM_COL_TYPE));
        c.close();

        return user;
    }
}
