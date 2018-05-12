package heh.be.automatmanager.Activity.Automat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.regex.Pattern;

import heh.be.automatmanager.Activity.User.UserActivity;
import heh.be.automatmanager.DB.Automat.Automat;
import heh.be.automatmanager.DB.Automat.AutomatAccessDB;
import heh.be.automatmanager.R;

public class AddAutomatActivity extends AppCompatActivity {

    EditText et_activityAddAutomat_name;
    EditText et_activityAddAutomat_description;
    EditText et_activityAddAutomat_ipAddress;
    EditText et_activityAddAutomat_slotNumber;
    EditText et_activityAddAutomat_rackNumber;
    EditText et_activityAddAutomat_datablocNumber;
    RadioButton rb_activityAddAutomat_typePills;
    RadioButton rb_activityAddAutomat_typeLiquid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_automat);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        et_activityAddAutomat_name = (EditText) findViewById(R.id.et_activityAddAutomat_name);
        et_activityAddAutomat_description = (EditText) findViewById(R.id.et_activityAddAutomat_description);
        et_activityAddAutomat_ipAddress = (EditText) findViewById(R.id.et_activityAddAutomat_ipAddress);
        et_activityAddAutomat_slotNumber = (EditText) findViewById(R.id.et_activityAddAutomat_slotNumber);
        et_activityAddAutomat_rackNumber = (EditText) findViewById(R.id.et_activityAddAutomat_rackNumber);
        et_activityAddAutomat_datablocNumber = (EditText) findViewById(R.id.et_activityAddAutomat_datablocNumber);
        rb_activityAddAutomat_typeLiquid = (RadioButton) findViewById(R.id.rb_activityAddAutomat_typeLiquid);
        rb_activityAddAutomat_typePills = (RadioButton) findViewById(R.id.rb_activityAddAutomat_typePills);
    }


    public void onAddAutomatSave(View view) {

        AutomatAccessDB automatAccessDB = new AutomatAccessDB(this);
        automatAccessDB.openForWrite();

        if (checkName() && checkDescription() && checkIpAddress()
                && checkSlot() && checkRack() && checkDatabloc()) {

            //Automat informations
            String strName = et_activityAddAutomat_name.getText().toString().trim();
            String strDescription = et_activityAddAutomat_description.getText().toString().trim();
            String strIpAddress = et_activityAddAutomat_ipAddress.getText().toString().trim();
            int slotNumber = Integer.parseInt(et_activityAddAutomat_slotNumber.getText().toString());
            int rackNumber = Integer.parseInt(et_activityAddAutomat_rackNumber.getText().toString());
            int datablocNumber = Integer.parseInt(et_activityAddAutomat_datablocNumber.getText().toString().trim());
            String strType = "";

            if (rb_activityAddAutomat_typePills.isChecked()) {

                strType = "Pills Automat";
            } else {

                strType = "Liquid Automat";
            }

            Automat automat = new Automat(strName, strDescription, strIpAddress, slotNumber, rackNumber, strType, datablocNumber);

            automatAccessDB.insertAutomat(automat);

            automatAccessDB.close();

            Intent intentAutomatList = new Intent(this, UserActivity.class);
            startActivity(intentAutomatList);
            finishAndRemoveTask();
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent keyEvent) {

        if (keycode == KeyEvent.KEYCODE_BACK) {

            //If user press on back, informations are not saved
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Return to list")
                    .setMessage("Are you sure you want to return to the automats list ?" +
                            "\n" + "Nothing will be saved")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intentAutomatList = new Intent(getApplicationContext(), UserActivity.class);
                            startActivity(intentAutomatList);
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

    //Name is unique for the automats
    public boolean checkName() {

        AutomatAccessDB automatAccessDB = new AutomatAccessDB(this);
        String strName = et_activityAddAutomat_name.getText().toString();

        //Open readonly to the DB
        automatAccessDB.openForRead();

        if (strName.isEmpty()) {

            et_activityAddAutomat_name.setError("Please enter a name");
            return false;

        } else {

            try {

                Automat automat = automatAccessDB.getAutomat(strName);
                Log.i("Existing automat", automat.getName());
                et_activityAddAutomat_name.setError("Name already in use");
            } catch (Exception e) {

                e.printStackTrace();
                automatAccessDB.close();
                return true;
            }
        }

        automatAccessDB.close();
        return false;
    }

    public boolean checkDescription() {

        String strDescription = et_activityAddAutomat_description.getText().toString();

        if (strDescription.isEmpty()) {

            et_activityAddAutomat_description.setError("Please enter a description");
            return false;
        }

        return true;
    }

    public boolean checkIpAddress() {

        String regex = "\\b((25[0–5]|2[0–4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0–5]|2[0–4]\\d|[01]?\\d\\d?)\\b";
        String strIpAddress = et_activityAddAutomat_ipAddress.getText().toString();

        if (Pattern.matches(regex, strIpAddress)) {

            Log.i("Ip address status", "Valid IP");
            return true;
        }

        Log.i("IP address status", "Not valid IP");
        et_activityAddAutomat_ipAddress.setError("Please enter a valid IP address");
        return false;


    }

    public boolean checkSlot() {

        String strSlot = et_activityAddAutomat_slotNumber.getText().toString();

        if (strSlot.isEmpty()) {

            et_activityAddAutomat_slotNumber.setError("Please enter a slot number");
            return false;
        }

        return true;
    }

    public boolean checkRack() {

        String strRack = et_activityAddAutomat_rackNumber.getText().toString();

        if (strRack.isEmpty()) {

            et_activityAddAutomat_rackNumber.setError("Please enter a rack number");
            return false;
        }

        return true;
    }

    public boolean checkDatabloc() {

        String strDatabloc = et_activityAddAutomat_datablocNumber.getText().toString();

        if (strDatabloc.isEmpty()) {

            et_activityAddAutomat_datablocNumber.setError("Please enter a databloc number");
            return false;
        }

        return true;
    }

}
