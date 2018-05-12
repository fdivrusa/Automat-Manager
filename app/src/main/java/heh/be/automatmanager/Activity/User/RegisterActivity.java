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
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import heh.be.automatmanager.Classes.HashTool;
import heh.be.automatmanager.DB.User.User;
import heh.be.automatmanager.DB.User.UserAccessDB;
import heh.be.automatmanager.R;

public class RegisterActivity extends AppCompatActivity {

    EditText et_activityRegister_name;
    EditText et_activityRegister_surname;
    EditText et_activityRegister_email;
    EditText et_activityRegister_password;
    EditText et_activityRegister_confirmPassword;
    CheckBox cb_activityRegister_showPassword;
    SharedPreferences pref_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Only portrait orientation is allowed
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //AutomatAccessDB automatAccessDB = new AutomatAccessDB(this);
        //automatAccessDB.openForWrite();
        //automatAccessDB.truncateDB();
        //automatAccessDB.close();

        UserAccessDB userAccessDB = new UserAccessDB(this);
        HashTool hashTool = new HashTool();

        et_activityRegister_name = (EditText) findViewById(R.id.et_activityRegister_name);
        et_activityRegister_surname = (EditText) findViewById(R.id.et_activityRegister_surname);
        et_activityRegister_email = (EditText) findViewById(R.id.et_activityRegister_email);
        et_activityRegister_confirmPassword = (EditText) findViewById(R.id.et_activityRegister_confirmPassword);
        et_activityRegister_password = (EditText) findViewById(R.id.et_activityRegister_password);
        cb_activityRegister_showPassword = (CheckBox) findViewById(R.id.cb_activityRegister_showPassword);
        pref_datas = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Verify if admin is already in the database, else, create one

        try {

            userAccessDB.openForWrite();
            String password = hashTool.createHash("android3");
            User userAdmin = new User("Admin", "Admin", "android", password, "Admin");
            userAccessDB.insertUser(userAdmin);
            Log.i("Admin", "Adding to database");

        } catch (Exception e) {

            Log.i("Admin", "Already in database");
            e.printStackTrace();

        } finally {

            userAccessDB.close();
            //userAccessDB.truncateDB();
        }
    }

    public void onRegisterQuit(View view) {

        //Close the app
        Log.i("Status : ", "App succesfully closed");
        Intent intentHome = new Intent(Intent.ACTION_MAIN);
        intentHome.addCategory(Intent.CATEGORY_HOME);
        startActivity(intentHome);

    }

    //Show and hide password fields when user checks the checkbox
    public void onRegisterShowPassword(View view) {

        Log.i("Event Called", "onRegisterShowPassword");

        if (cb_activityRegister_showPassword.isChecked()) {

            //Show the password and put the selector at the end (because hiding again the password reinitialise
            // the selector position
            et_activityRegister_confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            et_activityRegister_password.setSelection(et_activityRegister_password.getText().length());
            et_activityRegister_password.setInputType(InputType.TYPE_CLASS_TEXT);
            et_activityRegister_confirmPassword.setSelection(et_activityRegister_confirmPassword.getText().length());
            Log.i("Password field status", "Visible");

        } else {

            //Hide the password
            et_activityRegister_confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_activityRegister_password.setSelection(et_activityRegister_password.getText().length());
            et_activityRegister_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_activityRegister_confirmPassword.setSelection(et_activityRegister_confirmPassword.getText().length());
            Log.i("Password field status", "Hide");
        }
    }

    //Redirect to the login activity
    public void onRegisterRedirectToLogin(View view) {

        //Redirect to the login activity
        Log.i("Redirect to ", "Login activity");
        Intent intentLogin = new Intent(this, LoginActivity.class);
        startActivity(intentLogin);
    }

    // If all the fields are correct, I register the user in the database if the email is not in use
    public void onRegisterSubmit(View view) {


        HashTool hashTool = new HashTool();
        UserAccessDB userAccessInsert = new UserAccessDB(this);

        //Informations of the user
        String strName = et_activityRegister_name.getText().toString();
        String strSurname = et_activityRegister_surname.getText().toString();
        String strEmail = et_activityRegister_email.getText().toString().toLowerCase();
        String strPassword = hashTool.createHash(et_activityRegister_password.getText().toString());

        //Users have all same authorizations
        String type = "RO";

        //Writable access
        userAccessInsert.openForWrite();

        //userAccessInsert.truncateDB();

        //Making the user
        User user = new User(strName, strSurname, strEmail, strPassword, type);

        //If all info are correct, then add user and redirect to userHome
        if (verifyNameSurname() && verifyEmail() && verifyPassword()) {

            //Insert the user
            userAccessInsert.insertUser(user);

            //When user is insert, close the connection
            userAccessInsert.close();

            //Put data in SharedPreferences
            SharedPreferences.Editor editor_datas = pref_datas.edit();
            editor_datas.putString("Name", strName);
            editor_datas.putString("Surname", strSurname);
            editor_datas.putString("Email", strEmail);
            editor_datas.putString("Password", strPassword);
            editor_datas.putString("Type", type);
            editor_datas.apply();

            //Starting user home activity
            Intent intentHomeUser = new Intent(getApplicationContext(), UserActivity.class);
            startActivity(intentHomeUser);
        }

    }

    //Verify the equality between the two password fields and length
    public boolean verifyPassword() {

        String strPassword = et_activityRegister_password.getText().toString();
        String strConfirmPassword = et_activityRegister_confirmPassword.getText().toString();

        //if the field password is empty
        if (strPassword.isEmpty()) {

            Log.i("PasswordEditText", "IsEmpty");
            et_activityRegister_password.setError("Password can't be empty");
            return false;

            //Same for confirmPassword
        } else if (strConfirmPassword.isEmpty()) {

            Log.i("PasswordConfirmEditText", "IsEmpty");
            et_activityRegister_confirmPassword.setError("You have to confirm the password");
            return false;

            //Veriify size
        } else if (strPassword.length() <= 4) {

            et_activityRegister_password.setError("Password length must be greater than 4 characters");
            return false;
        }

        //If they are not empty and the size is ok, verify if the strings are equals
        else {

            if (strPassword.equals(strConfirmPassword)) {

                Log.i("Password fields", "Match");
                return true;
            } else {

                Log.i("Password fields", "Not Match");
                et_activityRegister_confirmPassword.setError("Passwords does not match");
                return false;
            }
        }
    }

    //Verify the Email field
    public boolean verifyEmail() {

        String strEmail = et_activityRegister_email.getText().toString();

        if (strEmail.isEmpty()) {

            Log.i("EmailEditText", "IsEmpty");
            et_activityRegister_email.setError("Email can't be empty");
            return false;
        } else {

            //If email is not valid
            if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {

                Log.i("Email string", "Is Not valid");
                et_activityRegister_email.setError("Email must be valid");
                return false;

                //If email already in use
            } else if (!checkEmailAlreadyExist()) {

                Log.i("Email status", "Already in use");
                et_activityRegister_email.setError("Email is already in use");
                return false;

            } else {

                Log.i("Email string", "Is valid ");
                return true;
            }
        }
    }

    //Verify if the email is already in the database
    public boolean checkEmailAlreadyExist() {

        UserAccessDB userAccessDB = new UserAccessDB(this);
        String strEmail = et_activityRegister_email.getText().toString();

        //Open a read only access to the DB
        userAccessDB.openForRead();

        try {
            User user = userAccessDB.getUser(strEmail);
            Log.i("Existing mail", user.getEmail());
        } catch (Exception e) {

            e.printStackTrace();
            userAccessDB.close();
            return true;
        }

        userAccessDB.close();
        return false;
    }

    //Verify the name and surname field
    public boolean verifyNameSurname() {

        Log.i("Method launch", "VerifyNameSurname");
        String strName = et_activityRegister_name.getText().toString();
        String strSurname = et_activityRegister_surname.getText().toString();

        if (strName.isEmpty()) {

            Log.i("NameEditText", "IsEmpty");
            et_activityRegister_name.setError("Name can't be empty");
            return false;
        } else if (strSurname.isEmpty()) {

            Log.i("SurnameEditText", "IsEmpty");
            et_activityRegister_surname.setError("Surname can't be empty");
            return false;
        } else {

            Log.i("Name and surname", "Are valid");
            return true;
        }
    }
}


