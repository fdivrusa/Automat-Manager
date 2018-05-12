package heh.be.automatmanager.Activity.Liquid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import heh.be.automatmanager.Activity.Automat.AutomatEditViewActivity;
import heh.be.automatmanager.Activity.User.UserActivity;
import heh.be.automatmanager.DB.Automat.AutomatAccessDB;
import heh.be.automatmanager.R;

public class AutomatLiquidActivity extends AppCompatActivity {

    SharedPreferences pref_datas;
    Button bt_activityAutomatLiquid_connect;
    TextView tv_activityAutomatLiquid_plc;
    NetworkInfo networkInfo;
    ReadTaskLiquidS7 readTaskLiquidS7;
    WriteTaskLiquidS7 writeTaskLiquidS7;
    ConnectivityManager connectivityManager;
    TextView tv_activityAutomatLiquid_status;
    LinearLayout ll_activityAutomatLiquid_visibility;
    LinearLayout ll_activityAutomatLiquid_readData;
    LinearLayout ll_activityAutomatLiquid_writeData;
    TabLayout tl_activityAutomatLiquid_tabLayout;

    EditText et_activityAutomatLiquid_writeBitDataNumber;
    EditText et_activityAutomatLiquid_writeBit;
    Button bt_activityAutomatLiquid_writeBit;
    CheckBox cb_activityAutomatLiquid_valueBit;

    EditText et_activityAutomatLiquid_writeByteDataNumber;
    EditText et_activityAutomatLiquid_writeByteDataValue;
    Button bt_activityAutomatLiquid_writeByte;

    EditText et_activityAutomatLiquid_writeWordDataNumber;
    EditText et_activityAutomatLiquid_writeWordValue;
    Button bt_activityAutomatLiquid_writeWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automat_liquid);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbarAutomat);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ll_activityAutomatLiquid_visibility = (LinearLayout) findViewById(R.id.ll_activityAutomatLiquid_layoutVisibility);
        ll_activityAutomatLiquid_readData = (LinearLayout) findViewById(R.id.ll_activityAutomatLiquid_layoutReadData);
        ll_activityAutomatLiquid_writeData = (LinearLayout) findViewById(R.id.ll_activityAutomatLiquid_layoutWriteData);
        tl_activityAutomatLiquid_tabLayout = (TabLayout) findViewById(R.id.tl_activityAutomatLiquid_tabLayout);

        tv_activityAutomatLiquid_plc = (TextView) findViewById(R.id.tv_activityAutomatLiquid_plc);
        tv_activityAutomatLiquid_status = (TextView) findViewById(R.id.tv_activityAutomatLiquid_status);
        bt_activityAutomatLiquid_connect = (Button) findViewById(R.id.bt_activityAutomatLiquid_connect);
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        et_activityAutomatLiquid_writeBitDataNumber = (EditText) findViewById(R.id.et_activityAutomatLiquid_writeBitDataNumber);
        et_activityAutomatLiquid_writeBit = (EditText) findViewById(R.id.et_activityAutomatLiquid_writeBit);
        cb_activityAutomatLiquid_valueBit = (CheckBox) findViewById(R.id.cb_activityAutomatLiquid_valueBit);
        bt_activityAutomatLiquid_writeBit = (Button) findViewById(R.id.bt_activityAutomatLiquid_writeBit);

        et_activityAutomatLiquid_writeByteDataNumber = (EditText) findViewById(R.id.et_activityAutomatLiquid_writeByteDataNumber);
        et_activityAutomatLiquid_writeByteDataValue = (EditText) findViewById(R.id.et_activityAutomatLiquid_writeByteDataValue);
        bt_activityAutomatLiquid_writeByte = (Button) findViewById(R.id.bt_activityAutomatLiquid_writeByte);

        et_activityAutomatLiquid_writeWordDataNumber = (EditText) findViewById(R.id.et_activityAutomatLiquid_writeWordDataNumber);
        et_activityAutomatLiquid_writeWordValue = (EditText) findViewById(R.id.et_activityAutomatLiquid_writeWordValue);
        bt_activityAutomatLiquid_writeWord = (Button) findViewById(R.id.bt_activityAutomatLiquid_writeWord);

        tl_activityAutomatLiquid_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tl_activityAutomatLiquid_tabLayout.getSelectedTabPosition()) {

                    case 0:
                        ll_activityAutomatLiquid_readData.setVisibility(View.VISIBLE);
                        ll_activityAutomatLiquid_writeData.setVisibility(View.GONE);

                        break;

                    case 1:
                        if (pref_datas.getString("Type", null).equals("RO")) {

                            Toast.makeText(getApplicationContext(), "You are not allowed to write data", Toast.LENGTH_SHORT).show();
                        } else {

                            ll_activityAutomatLiquid_readData.setVisibility(View.GONE);
                            ll_activityAutomatLiquid_writeData.setVisibility(View.VISIBLE);
                        }

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_toolbar_menu_automat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_deleteAutomat:

                if (pref_datas.getString("Type", null).equals("RO")) {

                    Toast.makeText(getApplicationContext(), "You have not permission to delete this automat", Toast.LENGTH_LONG).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete automat")
                            .setMessage("Are you sure you want to delete this automat ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    AutomatAccessDB automatAccessDB = new AutomatAccessDB(getApplicationContext());
                                    automatAccessDB.openForWrite();

                                    //Delete the automat
                                    automatAccessDB.removeAutomat(pref_datas.getString("AutomatName", null));
                                    automatAccessDB.close();
                                    Intent intentUser = new Intent(getApplicationContext(), UserActivity.class);
                                    startActivity(intentUser);
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

                break;

            case R.id.action_automatSettings:

                if (pref_datas.getString("Type", null).equals("RO")) {

                    Toast.makeText(getApplicationContext(), "You have not permission modify this automat", Toast.LENGTH_LONG).show();
                } else {

                    Intent intentAutomatSettings = new Intent(getApplication(), AutomatEditViewActivity.class);
                    startActivity(intentAutomatSettings);
                }
                break;
        }
        return true;
    }

    public void onAutomatConnect(View view) {

        String strIpAddress = pref_datas.getString("IpAddress", null);
        String strSlotNumber = pref_datas.getString("SlotNumber", null);
        String strRackNumber = pref_datas.getString("RackNumber", null);
        int datablocNumber = Integer.parseInt(pref_datas.getString("DatablocNumber", null));

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            if (bt_activityAutomatLiquid_connect.getText().equals("Connect Automat")) {

                readTaskLiquidS7 = new ReadTaskLiquidS7(this, datablocNumber);
                readTaskLiquidS7.Start(strIpAddress, strRackNumber, strSlotNumber);
                writeTaskLiquidS7 = new WriteTaskLiquidS7(datablocNumber);
                writeTaskLiquidS7.Start(strIpAddress, strRackNumber, strSlotNumber, readTaskLiquidS7.getTestDatas());


                bt_activityAutomatLiquid_connect.setText("Deconnect Automat");

                tv_activityAutomatLiquid_status.setText("Status : Connected");
                ll_activityAutomatLiquid_visibility.setVisibility(View.VISIBLE);
                tv_activityAutomatLiquid_status.setTextColor(Color.parseColor("#1b1d2e"));
            } else {

                readTaskLiquidS7.Stop();
                writeTaskLiquidS7.Stop();

                bt_activityAutomatLiquid_connect.setText("Connect Automat");

                tv_activityAutomatLiquid_status.setText("Status : Disconnected");
                ll_activityAutomatLiquid_visibility.setVisibility(View.GONE);
                tv_activityAutomatLiquid_status.setTextColor(Color.parseColor("#c71515"));

                Toast.makeText(getApplicationContext(), "Work interrupt by user", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(this, "Network connection impossible", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnMainClickManager(View view) {

        int dbbNumber;
        int bitPosition;
        int value;

        switch (view.getId()) {

            case R.id.bt_activityAutomatLiquid_writeBit:

                if (et_activityAutomatLiquid_writeBit.getText().toString().isEmpty()) {

                    et_activityAutomatLiquid_writeBit.setError("Please enter correct value");
                } else if (et_activityAutomatLiquid_writeBitDataNumber.getText().toString().isEmpty()) {

                    et_activityAutomatLiquid_writeBitDataNumber.setError("Please enter correct value");
                } else {

                    dbbNumber = Integer.parseInt(et_activityAutomatLiquid_writeBitDataNumber.getText().toString());
                    bitPosition = Integer.parseInt(et_activityAutomatLiquid_writeBit.getText().toString());
                    value = cb_activityAutomatLiquid_valueBit.isChecked() ? 1 : 0;
                    Log.i("Value", +dbbNumber + "\n" + bitPosition);

                    writeTaskLiquidS7.setWriteBool(dbbNumber, bitPosition, value);
                    Toast.makeText(this, "Bit wrote successfully", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.bt_activityAutomatPills_writeByte:

                if (et_activityAutomatLiquid_writeByteDataValue.getText().toString().isEmpty()) {

                    et_activityAutomatLiquid_writeByteDataValue.setError("Please enter correct value");
                } else if (et_activityAutomatLiquid_writeByteDataNumber.getText().toString().isEmpty()) {

                    et_activityAutomatLiquid_writeByteDataNumber.setError("Please enter correct value");
                } else {

                    dbbNumber = Integer.parseInt(et_activityAutomatLiquid_writeByteDataNumber.getText().toString());
                    value = Integer.parseInt(et_activityAutomatLiquid_writeByteDataNumber.getText().toString());
                    writeTaskLiquidS7.setWriteByte(dbbNumber, value);
                    Toast.makeText(this, "Byte wrote successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_activityAutomatPills_writeWord:

                if (et_activityAutomatLiquid_writeWordValue.getText().toString().isEmpty()) {

                    et_activityAutomatLiquid_writeWordValue.setError("Please enter correct value");
                } else if (et_activityAutomatLiquid_writeWordDataNumber.getText().toString().isEmpty()) {

                    et_activityAutomatLiquid_writeWordDataNumber.setError("Please enter correct value");
                } else {

                    dbbNumber = Integer.parseInt(et_activityAutomatLiquid_writeWordDataNumber.getText().toString());
                    value = Integer.parseInt(et_activityAutomatLiquid_writeWordValue.getText().toString());
                    writeTaskLiquidS7.setWriteInt(dbbNumber, value);
                    Toast.makeText(this, "Word wrote successfully", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }
}
