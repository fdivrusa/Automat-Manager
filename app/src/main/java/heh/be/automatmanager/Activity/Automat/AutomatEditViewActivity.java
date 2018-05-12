package heh.be.automatmanager.Activity.Automat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.regex.Pattern;

import heh.be.automatmanager.Activity.User.UserActivity;
import heh.be.automatmanager.DB.Automat.Automat;
import heh.be.automatmanager.DB.Automat.AutomatAccessDB;
import heh.be.automatmanager.R;

public class AutomatEditViewActivity extends AppCompatActivity {

    EditText et_activityAutomatEdit_name;
    EditText et_activityAutomatEdit_description;
    EditText et_activityAutomatEdit_ipAddress;
    EditText et_activityAutomatEdit_slotNumber;
    EditText et_activityAutomatEdit_rackNumber;
    EditText et_activityAutomatEdit_datablocNumber;
    RadioButton rb_activityAutomatEdit_typePills;
    RadioButton rb_activityAutomatEdit_typeLiquid;
    SharedPreferences pref_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automat_edit_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        et_activityAutomatEdit_name = (EditText) findViewById(R.id.et_activityAutomatEdit_name);
        et_activityAutomatEdit_description = (EditText) findViewById(R.id.et_activityAutomatEdit_description);
        et_activityAutomatEdit_ipAddress = (EditText) findViewById(R.id.et_activityAutomatEdit_ipAddress);
        et_activityAutomatEdit_slotNumber = (EditText) findViewById(R.id.et_activityAutomatEdit_slotNumber);
        et_activityAutomatEdit_rackNumber = (EditText) findViewById(R.id.et_activityAutomatEdit_rackNumber);
        et_activityAutomatEdit_datablocNumber = (EditText) findViewById(R.id.et_activityAddAutomat_datablocNumber);
        rb_activityAutomatEdit_typeLiquid = (RadioButton) findViewById(R.id.rb_activityAutomatEdit_typeLiquid);
        rb_activityAutomatEdit_typePills = (RadioButton) findViewById(R.id.rb_activityAutomatEdit_typePills);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(this);

        et_activityAutomatEdit_name.setText(pref_datas.getString("AutomatName", null));
        et_activityAutomatEdit_description.setText(pref_datas.getString("Description", null));
        et_activityAutomatEdit_ipAddress.setText(pref_datas.getString("IpAddress", null));
        et_activityAutomatEdit_slotNumber.setText(pref_datas.getString("SlotNumber", null));
        et_activityAutomatEdit_rackNumber.setText(pref_datas.getString("RackNumber", null));
        et_activityAutomatEdit_datablocNumber.setText(pref_datas.getString("DatablocNumber", null));

        if (pref_datas.getString("AutomatType", null).equals("Pills Automat")) {

            rb_activityAutomatEdit_typePills.setChecked(true);
        } else {

            rb_activityAutomatEdit_typeLiquid.setChecked(true);
        }

    }

    public void onEditAutomatSave(View view) {

        final String strName = et_activityAutomatEdit_name.getText().toString().trim();
        final String strDescription = et_activityAutomatEdit_description.getText().toString().trim();
        final String strIpAddress = et_activityAutomatEdit_ipAddress.getText().toString();
        final String strSlotNumber = et_activityAutomatEdit_slotNumber.getText().toString();
        final String strRackNumber = et_activityAutomatEdit_rackNumber.getText().toString();
        final String strDatablocNumber = et_activityAutomatEdit_datablocNumber.getText().toString();
        final String strTypeAutomat;

        String regex = "\\b((25[0–5]|2[0–4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0–5]|2[0–4]\\d|[01]?\\d\\d?)\\b";

        if (strName.isEmpty()) {

            et_activityAutomatEdit_name.setError("Name can't be empty");
        } else if (strDescription.isEmpty()) {

            et_activityAutomatEdit_description.setError("Description can't be empty");
        } else if (strIpAddress.isEmpty()) {

            et_activityAutomatEdit_ipAddress.setError("Ip Address can't be empty");
        } else if (!Pattern.matches(regex, strIpAddress)) {

            et_activityAutomatEdit_ipAddress.setError("Please enter a valid IP address");
        } else if (strSlotNumber.isEmpty()) {

            et_activityAutomatEdit_slotNumber.setError("Slot number can't be empty");
        } else if (strRackNumber.isEmpty()) {

            et_activityAutomatEdit_rackNumber.setError("Rack number can't be empty");
        } else if (strDatablocNumber.isEmpty()) {

            et_activityAutomatEdit_datablocNumber.setError("Databloc number can't be empty");
        } else {

            final int slotNumber = Integer.parseInt(strSlotNumber);
            final int rackNumber = Integer.parseInt(strRackNumber);
            final int datablocNumber = Integer.parseInt(strDatablocNumber);

            if (rb_activityAutomatEdit_typeLiquid.isChecked()) {

                strTypeAutomat = "Liquid Automat";
            } else {

                strTypeAutomat = "Pills Automat";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save")
                    .setMessage("Are you sure you want to save modifications ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            AutomatAccessDB automatAccessDB = new AutomatAccessDB(getApplicationContext());
                            automatAccessDB.openForWrite();

                            //Automat update
                            Automat automat = new Automat(strName, strDescription, strIpAddress, slotNumber, rackNumber, strTypeAutomat, datablocNumber);

                            //upgrade in DB
                            automatAccessDB.updateAutomat(Integer.parseInt(pref_datas.getString("AutomatID", null)), automat);
                            automatAccessDB.close();

                            //Redirect to the user activity
                            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
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
    }
}
