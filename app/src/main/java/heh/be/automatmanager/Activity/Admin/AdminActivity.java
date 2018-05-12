package heh.be.automatmanager.Activity.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import heh.be.automatmanager.Activity.User.LoginActivity;
import heh.be.automatmanager.Activity.User.SettingsActivity;
import heh.be.automatmanager.Activity.User.UserActivity;
import heh.be.automatmanager.Classes.CustomUserAdapter;
import heh.be.automatmanager.DB.User.User;
import heh.be.automatmanager.DB.User.UserAccessDB;
import heh.be.automatmanager.R;

public class AdminActivity extends AppCompatActivity {

    ListView lv_file_listeUser;
    SharedPreferences pref_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Telling the activity that the toolbar is the actionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        lv_file_listeUser = (ListView) findViewById(R.id.lv_file_listeUser);

        //Access to DB to get all users
        UserAccessDB userAccessDB = new UserAccessDB(this);
        userAccessDB.openForRead();

        final ArrayList<User> tab_user = userAccessDB.getAllUsers();
        userAccessDB.close();

        //I remove the admin from the tab to not display it in the list
        //He can modify his informations in settings like all users
        tab_user.remove(0);

        if (tab_user.isEmpty()) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
        } else {

            //Display the user with the custom adapter
            CustomUserAdapter customUserAdapter = new CustomUserAdapter(this, R.layout.custom_row, tab_user);
            lv_file_listeUser.setAdapter(customUserAdapter);
        }

        //Adding admin info in pref_data everytime the activity is called
        userAccessDB.openForRead();
        User admin = userAccessDB.getUser("android");

        SharedPreferences.Editor editor = pref_datas.edit();
        editor.putString("Name", admin.getName());
        editor.putString("Surname", admin.getSurname());
        editor.putString("Email", admin.getEmail());
        editor.putString("Password", admin.getPassword());
        editor.putString("Type", admin.getType());
        editor.putString("UserID", String.valueOf(admin.getId()));
        editor.apply();

        //Here we launch activity to edit the selected user (R / W or RO)
        lv_file_listeUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                UserAccessDB userAccessDB1 = new UserAccessDB(getApplicationContext());
                userAccessDB1.openForRead();

                //Get the email of the clicked item
                TextView tv_customRow_mail = (TextView) view.findViewById(R.id.tv_customRow_email);

                //Split to get only info that is useful
                String[] mailRowText = tv_customRow_mail.getText().toString().split(":");

                //Remove space
                String strMail = mailRowText[1].trim();

                //get the user by Email
                User userToEdit = userAccessDB1.getUser(strMail);

                //Closing connection
                userAccessDB1.close();

                //Put the informations of the user selected in SharedPreferences
                SharedPreferences.Editor editor_datas = pref_datas.edit();
                editor_datas.putString("Name", userToEdit.getName());
                editor_datas.putString("Surname", userToEdit.getSurname());
                editor_datas.putString("Email", userToEdit.getEmail());
                editor_datas.putString("Password", userToEdit.getPassword());
                editor_datas.putString("Type", userToEdit.getType());
                editor_datas.putString("UserID", String.valueOf(userToEdit.getId()));
                editor_datas.apply();

                //Redirect to the edit view
                Intent editUserIntent = new Intent(getApplicationContext(), UserEditViewActivity.class);
                finishAndRemoveTask();
                startActivity(editUserIntent);
            }
        });
    }

    //Adding the menu to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_toolbar_menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_quit:
                //If the admin press on back, we ask if he is sure to logout
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to logout ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Clear sharedPreferences at logout
                                SharedPreferences.Editor editor = pref_datas.edit();
                                editor.clear();
                                editor.apply();

                                Intent intentQuit = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intentQuit);
                                finishAndRemoveTask();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create()
                        .show();
                break;

            case R.id.action_settings:

                UserAccessDB userAccessDB = new UserAccessDB(getApplicationContext());
                userAccessDB.openForRead();
                User userAdmin = userAccessDB.getUser("android");
                userAccessDB.close();

                SharedPreferences.Editor editor_datas = pref_datas.edit();
                editor_datas.putString("Name", userAdmin.getName());
                editor_datas.putString("Surname", userAdmin.getSurname());
                editor_datas.putString("Email", userAdmin.getEmail());
                editor_datas.putString("Password", userAdmin.getPassword());
                editor_datas.putString("Type", userAdmin.getType());
                editor_datas.putString("UserID", String.valueOf(userAdmin.getId()));
                editor_datas.apply();

                //Launching the settings activity
                Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intentSettings);

                break;

            case R.id.action_toAutomatView:

                //Redirect to the automat view
                Intent intentAutomat = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intentAutomat);

                break;

            case R.id.action_deleteAll:

                //Delete all the users ecxept the admin
                AlertDialog.Builder builderDel = new AlertDialog.Builder(this);
                builderDel.setTitle("Delete users")
                        .setMessage("Are you sure you want to delete all users ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                UserAccessDB userAccessDB = new UserAccessDB(getApplicationContext());
                                userAccessDB.openForWrite();

                                userAccessDB.removeAllEcxeptAdmin();
                                userAccessDB.close();

                                //Reload the activity
                                Intent intentDelete = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(intentDelete);
                                finishAndRemoveTask();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create()
                        .show();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent keyEvent) {

        if (keycode == KeyEvent.KEYCODE_BACK) {

            //If the admin press on back, we ask if he is sure to logout
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout")
                    .setMessage("Are you sure you want to logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //Clear sharedPreferences at logout
                            SharedPreferences.Editor editor = pref_datas.edit();
                            editor.clear();
                            editor.apply();

                            Intent intentLogout = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogout);
                            finishAndRemoveTask();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
