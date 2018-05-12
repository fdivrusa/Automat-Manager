package heh.be.automatmanager.Activity.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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

import heh.be.automatmanager.Activity.Automat.AddAutomatActivity;
import heh.be.automatmanager.Activity.Liquid.AutomatLiquidActivity;
import heh.be.automatmanager.Activity.Pills.AutomatPillsActivity;
import heh.be.automatmanager.Classes.CustomAutomatAdapter;
import heh.be.automatmanager.DB.Automat.Automat;
import heh.be.automatmanager.DB.Automat.AutomatAccessDB;
import heh.be.automatmanager.DB.User.User;
import heh.be.automatmanager.DB.User.UserAccessDB;
import heh.be.automatmanager.R;

public class UserActivity extends AppCompatActivity {

    SharedPreferences pref_datas;
    ListView lv_file_listAutomat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbarUser);
        setSupportActionBar(toolbar);

        lv_file_listAutomat = (ListView) findViewById(R.id.lv_file_listeAutomat);

        //Access to DB to get all automats
        AutomatAccessDB automatAccessDB = new AutomatAccessDB(this);
        automatAccessDB.openForRead();

        final ArrayList<Automat> tab_automat = automatAccessDB.getAllAutomats();
        automatAccessDB.close();

        if (tab_automat.isEmpty()) {
            Toast.makeText(this, "No automat found in database", Toast.LENGTH_LONG).show();
        } else {

            CustomAutomatAdapter customAutomatAdapter = new CustomAutomatAdapter(this, R.layout.custom_row_automat, tab_automat);
            lv_file_listAutomat.setAdapter(customAutomatAdapter);
        }

        lv_file_listAutomat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AutomatAccessDB automatAccessDB1 = new AutomatAccessDB(getApplicationContext());
                automatAccessDB1.openForRead();

                //Get the name of the automat
                TextView tv_customRow_automatName = (TextView) view.findViewById(R.id.tv_customRow_automatName);
                String[] nameRowText = tv_customRow_automatName.getText().toString().split(":");
                String strName = nameRowText[1].trim();

                //Get the automat
                Automat automat = automatAccessDB1.getAutomat(strName);

                //Closing connection
                automatAccessDB1.close();

                //Put in SharedPreferences
                SharedPreferences.Editor editor_datas = pref_datas.edit();
                editor_datas.putString("AutomatName", automat.getName());
                editor_datas.putString("Description", automat.getDescription());
                editor_datas.putString("IpAddress", automat.getIpAddress());
                editor_datas.putString("SlotNumber", String.valueOf(automat.getSlotNumber()));
                editor_datas.putString("RackNumber", String.valueOf(automat.getRackNumber()));
                editor_datas.putString("AutomatID", String.valueOf(automat.getId()));
                editor_datas.putString("AutomatType", automat.getTypeAutomat());
                editor_datas.putString("DatablocNumber", String.valueOf(automat.getDatablocNumber()));
                editor_datas.apply();

                if (pref_datas.getString("AutomatType", null).equals("Pills Automat")) {

                    Intent intentAutomatPillsView = new Intent(getApplicationContext(), AutomatPillsActivity.class);
                    startActivity(intentAutomatPillsView);
                } else {

                    Intent intentAutomatLiquidView = new Intent(getApplicationContext(), AutomatLiquidActivity.class);
                    startActivity(intentAutomatLiquidView);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding the menu to the toolbar
        getMenuInflater().inflate(R.menu.app_toolbar_menu_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_UserSettings:

                UserAccessDB userAccessDB = new UserAccessDB(getApplicationContext());
                userAccessDB.openForRead();
                User userAdmin = userAccessDB.getUser(pref_datas.getString("Email", null));
                userAccessDB.close();

                SharedPreferences.Editor editor_datas = pref_datas.edit();
                editor_datas.putString("Name", userAdmin.getName());
                editor_datas.putString("Surname", userAdmin.getSurname());
                editor_datas.putString("Email", userAdmin.getEmail());
                editor_datas.putString("Password", userAdmin.getPassword());
                editor_datas.putString("Type", userAdmin.getType());
                editor_datas.putString("UserID", String.valueOf(userAdmin.getId()));
                editor_datas.apply();

                Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intentSettings);
                break;

            case R.id.action_UserQuit:
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
        return true;
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent keyEvent) {

        if (keycode == KeyEvent.KEYCODE_BACK) {

            if (pref_datas.getString("Email", null).equals("android")) {

                finishAndRemoveTask();

            } else {

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
        }
        return super.onKeyDown(keycode, keyEvent);
    }

    public void onUserAddAutomat(View view) {

        if (pref_datas.getString("Type", null).equals("RO")) {

            Toast.makeText(this, "You are not allowed to add a new automat" + "\n" +
                    "Contact your admin", Toast.LENGTH_LONG).show();
        } else {

            Intent intentAddAutomat = new Intent(getApplicationContext(), AddAutomatActivity.class);
            startActivity(intentAddAutomat);
            finishAndRemoveTask();
        }

    }
}
