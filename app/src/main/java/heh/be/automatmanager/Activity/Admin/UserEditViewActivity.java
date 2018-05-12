package heh.be.automatmanager.Activity.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import heh.be.automatmanager.DB.User.User;
import heh.be.automatmanager.DB.User.UserAccessDB;
import heh.be.automatmanager.R;

public class UserEditViewActivity extends AppCompatActivity {

    EditText et_activityUserEdit_name;
    EditText et_activityUserEdit_surname;
    EditText et_activityUserEdit_mail;
    RadioButton rb_activityUserEdit_readOnly;
    RadioButton rb_activityUserEdit_readWrite;
    SharedPreferences pref_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        et_activityUserEdit_name = (EditText) findViewById(R.id.et_activityUserEdit_name);
        et_activityUserEdit_surname = (EditText) findViewById(R.id.et_activityUserEdit_surname);
        et_activityUserEdit_mail = (EditText) findViewById(R.id.et_activityUserEdit_email);
        rb_activityUserEdit_readOnly = (RadioButton) findViewById(R.id.rb_activityUserEdit_readOnly);
        rb_activityUserEdit_readWrite = (RadioButton) findViewById(R.id.rb_activityUserEdit_readWrite);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(this);

        //Filling the editText with the informations of the user to edit
        et_activityUserEdit_name.setText(pref_datas.getString("Name", null));
        et_activityUserEdit_surname.setText(pref_datas.getString("Surname", null));
        et_activityUserEdit_mail.setText(pref_datas.getString("Email", null));

        if (pref_datas.getString("Type", null).equals("RO")) {

            rb_activityUserEdit_readOnly.setChecked(true);
        } else {

            rb_activityUserEdit_readWrite.setChecked(true);
        }
    }

    public void onUserEditSave(View view) {

        final String strName = et_activityUserEdit_name.getText().toString().trim();
        final String strSurname = et_activityUserEdit_surname.getText().toString().trim();
        final String strEmail = et_activityUserEdit_mail.getText().toString().toLowerCase().trim();
        final String password = pref_datas.getString("Password", null);
        final String strType;
        final int userId = Integer.parseInt(pref_datas.getString("UserID", null));

        if (strName.isEmpty()) {

            et_activityUserEdit_name.setError("Name can't be empty");
        } else if (strSurname.isEmpty()) {

            et_activityUserEdit_surname.setError("Surname can't be empty");
        } else if (strEmail.isEmpty()) {

            et_activityUserEdit_mail.setError("Email can't be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {

            et_activityUserEdit_mail.setError("Email must be valid");
        } else {

            if (rb_activityUserEdit_readOnly.isChecked()) {

                strType = "RO";
            } else {

                strType = "RW";
            }

            //If the admin press on Save, we ask if he is sure to save
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save")
                    .setMessage("Are you sure you want to save modifications ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            UserAccessDB userAccessDB = new UserAccessDB(getApplicationContext());
                            userAccessDB.openForWrite();

                            //User update
                            User userUpdate = new User(strName, strSurname, strEmail, password, strType);

                            //Update in the DB
                            userAccessDB.updateUser(userId, userUpdate);
                            userAccessDB.close();

                            //Redirect to the list activity
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            finishAndRemoveTask();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .create()
                    .show();
        }

    }

    public void onUserEditDelete(View view) {

        final String strEmail = et_activityUserEdit_mail.getText().toString().toLowerCase();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete this user ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        UserAccessDB userAccessDB = new UserAccessDB(getApplicationContext());
                        userAccessDB.openForWrite();

                        //Delete the user
                        userAccessDB.removeUser(strEmail);

                        //Redirect to the list
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        finishAndRemoveTask();
                        startActivity(intent);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent keyEvent) {

        if (keycode == KeyEvent.KEYCODE_BACK) {

            //If the admin press on back, we ask if he is sure to logout
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Return to list")
                    .setMessage("Are you sure you want to return to the list ?" +
                            "All changes will be cancelled")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                            finishAndRemoveTask();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .create()
                    .show();
        }
        return super.onKeyDown(keycode, keyEvent);
    }
}
