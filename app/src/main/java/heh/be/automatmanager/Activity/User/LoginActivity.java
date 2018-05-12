package heh.be.automatmanager.Activity.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import heh.be.automatmanager.Activity.Admin.AdminActivity;
import heh.be.automatmanager.DB.User.User;
import heh.be.automatmanager.DB.User.UserAccessDB;
import heh.be.automatmanager.Classes.HashTool;
import heh.be.automatmanager.R;

public class LoginActivity extends AppCompatActivity {

    EditText et_activityLogin_email;
    EditText et_activityLogin_password;
    CheckBox cb_activityLogin_showPassword;
    SharedPreferences pref_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        et_activityLogin_email = (EditText) findViewById(R.id.et_activityLogin_email);
        et_activityLogin_password = (EditText) findViewById(R.id.et_activityLogin_password);
        cb_activityLogin_showPassword = (CheckBox) findViewById(R.id.cb_activityLogin_showPassword);


        UserAccessDB userAccessDB = new UserAccessDB(getApplicationContext());
        userAccessDB.openForRead();

    }

    //By pressing on back button, user return to the register activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAndRemoveTask();
        }

        return super.onKeyDown(keyCode, keyEvent);
    }

    //Redirect to register activity
    public void onLoginRedirectToRegister(View view) {

        //Redirect to the register activity
        Log.i("Redirect to ", "Registration activity");
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentRegister);
    }

    public void onLoginSubmit(View view) {

        //If mail exists and password match, redirect the user
        if (checkInfoLogin()) {

            //If the admin wants to connect, redirect to his page
            if (et_activityLogin_email.getText().toString().toLowerCase().equals("android")) {

                SharedPreferences.Editor editor = pref_datas.edit();
                editor.putString("Email", "android");
                editor.apply();

                Log.i("Redirection", "Admin Activity");
                Intent intentAdmin = new Intent(this, AdminActivity.class);
                startActivity(intentAdmin);
                finishAndRemoveTask();

                //Else, redirect to the home page
            } else {

                UserAccessDB userAccessDB = new UserAccessDB(getApplicationContext());
                userAccessDB.openForWrite();

                //get the user informations
                User userToLog = userAccessDB.getUser(et_activityLogin_email.getText().toString().toLowerCase());

                //put user informations in SharedPreferences
                SharedPreferences.Editor editor = pref_datas.edit();
                editor.putString("Name", userToLog.getName());
                editor.putString("Surname", userToLog.getSurname());
                editor.putString("Email", userToLog.getEmail());
                editor.putString("Type", userToLog.getType());
                editor.putString("UserID", String.valueOf(userToLog.getId()));
                editor.apply();

                //redirect
                Intent intentUserHome = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intentUserHome);
                finishAndRemoveTask();

            }
        }
    }

    public boolean checkInfoLogin() {

        HashTool hashTool = new HashTool();

        String strEmail = et_activityLogin_email.getText().toString().toLowerCase();
        String strPassword = et_activityLogin_password.getText().toString();
        String strPasswordHash = hashTool.createHash(strPassword);

        UserAccessDB userLoginAccessDB = new UserAccessDB(this);

        //Open read
        userLoginAccessDB.openForRead();

        //if field is empty, return error
        if (strEmail.isEmpty()) {

            Log.i("Email status login", "Is Empty");
            et_activityLogin_email.setError("Please enter an email to connect");
            return false;

            //If email is the admin login
        } else if (strEmail.equals("android")) {

            Log.i("Login status", "Email admin detected");

            try {
                //Get admin info
                User admin = userLoginAccessDB.getUser(strEmail);
                Log.i("UserInfo", "OK");

                if (!admin.getPassword().equals(strPasswordHash)) {

                    Log.i("Password status", "Password not correct");
                    et_activityLogin_password.setError("Password is not correct, try again");
                    return false;
                } else {

                    Log.i("Password status", "Password is correct");
                    return true;
                }
            } catch (Exception e) {

                e.printStackTrace();
                et_activityLogin_email.setError("No admin found in the database");
                return false;
            }

            //If email not valid, return error
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {

            Log.i("Email status login", "Not Valid");
            et_activityLogin_email.setError("Please enter a valid email to connect");
            return false;

        } else {

            try {
                //Get the user by his Email (Login)
                Log.i("Verify", "Vzrifiy if email exist");
                User userToLog = userLoginAccessDB.getUser(strEmail);

                //See if the password is correct
                if (!userToLog.getPassword().equals(strPasswordHash)) {

                    et_activityLogin_password.setError("Password is not correct, try again");
                    return false;
                } else {

                    return true;
                }

                //If email not exists
            } catch (Exception e) {

                e.printStackTrace();
                et_activityLogin_email.setError("Email not exists in the database. Please create an account");
                return false;
            }
        }
    }

    public void onLoginShowPassword(View view) {

        if (cb_activityLogin_showPassword.isChecked()) {

            //Show the password and put the selector at the end (because hiding again the password reinitialise
            // the selector position
            et_activityLogin_password.setSelection(et_activityLogin_password.getText().length());
            et_activityLogin_password.setInputType(InputType.TYPE_CLASS_TEXT);
            Log.i("Password field status", "Visible");

        } else {

            //Hide the password
            et_activityLogin_password.setSelection(et_activityLogin_password.getText().length());
            et_activityLogin_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            Log.i("Password field status", "Hide");
        }
    }
}
