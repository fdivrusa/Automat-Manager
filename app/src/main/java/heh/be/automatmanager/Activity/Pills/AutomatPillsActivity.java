package heh.be.automatmanager.Activity.Pills;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import heh.be.automatmanager.Activity.Automat.AutomatEditViewActivity;
import heh.be.automatmanager.Activity.User.UserActivity;
import heh.be.automatmanager.DB.Automat.AutomatAccessDB;
import heh.be.automatmanager.R;

public class AutomatPillsActivity extends AppCompatActivity {

    SharedPreferences pref_datas;
    Button bt_activityAutomatPills_connect;
    TextView tv_activityAutomatPills_plc;
    NetworkInfo networkInfo;
    ReadTaskPillsS7 readTaskPillsS7;
    WriteTaskPillsS7 writeTaskPillsS7;
    ConnectivityManager connectivityManager;
    TextView tv_activityAutomatPills_status;
    LinearLayout ll_activityAutomatPills_visibility;
    LinearLayout ll_activityAutomatPills_writeData;
    LinearLayout ll_activityAutomatPills_readData;
    TabLayout tl_activityAutomatPills_tabLayout;
    ImageView iv_activityAutomatPills_onlineAccess;

    //Write field
    CheckBox cb_activityAutomatPills_valueBit;
    EditText et_activityAutomatPills_writeBitDataNumber;
    EditText et_activityAutomatPills_writeBit;
    Button bt_activityAutomatPills_writeBit;

    EditText et_activityAutomatPills_writeByteDataNumber;
    EditText et_activityAutomatPills_writeByteDataValue;
    Button bt_activityAutomatPills_writeByte;

    EditText et_activityAutomatPills_writeWordDataNumber;
    EditText et_activityAutomatPills_writeWordValue;
    Button bt_activityAutomatPills_writeWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automat_pills);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbarAutomat);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ll_activityAutomatPills_visibility = (LinearLayout) findViewById(R.id.ll_activityAutomatPills_layoutVisibility);
        ll_activityAutomatPills_readData = (LinearLayout) findViewById(R.id.ll_activityAutomatPills_layoutReadData);
        ll_activityAutomatPills_writeData = (LinearLayout) findViewById(R.id.ll_activityAutomatPills_layoutWriteData);
        tl_activityAutomatPills_tabLayout = (TabLayout) findViewById(R.id.tl_activityAutomatPills_tabLayout);

        tv_activityAutomatPills_plc = (TextView) findViewById(R.id.tv_activityAutomatPills_plc);
        tv_activityAutomatPills_status = (TextView) findViewById(R.id.tv_activityAutomatPills_status);
        bt_activityAutomatPills_connect = (Button) findViewById(R.id.bt_activityAutomatPills_connect);
        iv_activityAutomatPills_onlineAccess = (ImageView) findViewById(R.id.iv_activityAutomatPills_onlineAccess);
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        //Writting info
        cb_activityAutomatPills_valueBit = (CheckBox) findViewById(R.id.cb_activityAutomatPills_valueBit);
        et_activityAutomatPills_writeBit = (EditText) findViewById(R.id.et_activityAutomatPills_writeBit);
        et_activityAutomatPills_writeBitDataNumber = (EditText) findViewById(R.id.et_activityAutomatPills_writeBitDataNumber);
        bt_activityAutomatPills_writeBit = (Button) findViewById(R.id.bt_activityAutomatPills_writeBit);

        et_activityAutomatPills_writeByteDataNumber = (EditText) findViewById(R.id.et_activityAutomatPills_writeByteDataNumber);
        et_activityAutomatPills_writeByteDataValue = (EditText) findViewById(R.id.et_activityAutomatPills_writeByteDataValue);
        bt_activityAutomatPills_writeByte = (Button) findViewById(R.id.bt_activityAutomatPills_writeByte);

        et_activityAutomatPills_writeWordDataNumber = (EditText) findViewById(R.id.et_activityAutomatPills_writeWordDataNumber);
        et_activityAutomatPills_writeWordValue = (EditText) findViewById(R.id.et_activityAutomatPills_writeWordValue);
        bt_activityAutomatPills_writeWord = (Button) findViewById(R.id.bt_activityAutomatPills_writeWord);

        tl_activityAutomatPills_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tl_activityAutomatPills_tabLayout.getSelectedTabPosition()) {

                    case 0:
                        ll_activityAutomatPills_readData.setVisibility(View.VISIBLE);
                        ll_activityAutomatPills_writeData.setVisibility(View.GONE);

                        break;

                    case 1:

                        if (pref_datas.getString("Type", null).equals("RO")) {

                            Toast.makeText(getApplicationContext(), "You are not allowed to write data", Toast.LENGTH_SHORT).show();
                        } else {

                            ll_activityAutomatPills_readData.setVisibility(View.GONE);
                            ll_activityAutomatPills_writeData.setVisibility(View.VISIBLE);
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

            if (bt_activityAutomatPills_connect.getText().equals("Connect Automat")) {

                readTaskPillsS7 = new ReadTaskPillsS7(this, datablocNumber);
                readTaskPillsS7.Start(strIpAddress, strRackNumber, strSlotNumber);
                writeTaskPillsS7 = new WriteTaskPillsS7(datablocNumber);
                writeTaskPillsS7.Start(strIpAddress, strRackNumber, strSlotNumber, readTaskPillsS7.getTestDatas());

                bt_activityAutomatPills_connect.setText("Deconnect Automat");

                //Connexion status
                tv_activityAutomatPills_status.setText("Status : Connected");
                ll_activityAutomatPills_visibility.setVisibility(View.VISIBLE);
                tv_activityAutomatPills_status.setTextColor(Color.parseColor("#1b1d2e"));
            } else {

                readTaskPillsS7.Stop();
                writeTaskPillsS7.Stop();

                bt_activityAutomatPills_connect.setText("Connect Automat");

                //Connexion status
                tv_activityAutomatPills_status.setText("Status : Disconnected");
                ll_activityAutomatPills_visibility.setVisibility(View.GONE);
                tv_activityAutomatPills_status.setTextColor(Color.parseColor("#c71515"));

                Toast.makeText(getApplication(), "Work interrupt by user", Toast.LENGTH_SHORT).show();
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

            case R.id.bt_activityAutomatPills_writeBit:

                if (et_activityAutomatPills_writeBit.getText().toString().isEmpty()) {

                    et_activityAutomatPills_writeBit.setError("Please enter correct value");
                } else if (et_activityAutomatPills_writeBitDataNumber.getText().toString().isEmpty()) {

                    et_activityAutomatPills_writeBitDataNumber.setError("Please enter correct value");
                } else {

                    dbbNumber = Integer.parseInt(et_activityAutomatPills_writeBitDataNumber.getText().toString());
                    bitPosition = Integer.parseInt(et_activityAutomatPills_writeBit.getText().toString());
                    value = cb_activityAutomatPills_valueBit.isChecked() ? 1 : 0;
                    Log.i("Value", +dbbNumber + "\n" + bitPosition);

                    writeTaskPillsS7.setWriteBool(dbbNumber, bitPosition, value);
                    Toast.makeText(this, "Bit wrote successfully", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.bt_activityAutomatPills_writeByte:

                if (et_activityAutomatPills_writeByteDataValue.getText().toString().isEmpty()) {

                    et_activityAutomatPills_writeByteDataValue.setError("Please enter correct value");
                } else if (et_activityAutomatPills_writeByteDataNumber.getText().toString().isEmpty()) {

                    et_activityAutomatPills_writeByteDataNumber.setError("Please enter correct value");
                } else {

                    dbbNumber = Integer.parseInt(et_activityAutomatPills_writeByteDataNumber.getText().toString());
                    value = Integer.parseInt(et_activityAutomatPills_writeByteDataNumber.getText().toString());
                    writeTaskPillsS7.setWriteByte(dbbNumber, value);
                    Toast.makeText(this, "Byte wrote successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_activityAutomatPills_writeWord:

                if (et_activityAutomatPills_writeWordValue.getText().toString().isEmpty()) {

                    et_activityAutomatPills_writeWordValue.setError("Please enter correct value");
                } else if (et_activityAutomatPills_writeWordDataNumber.getText().toString().isEmpty()) {

                    et_activityAutomatPills_writeWordDataNumber.setError("Please enter correct value");
                } else {

                    dbbNumber = Integer.parseInt(et_activityAutomatPills_writeWordDataNumber.getText().toString());
                    value = Integer.parseInt(et_activityAutomatPills_writeWordValue.getText().toString());
                    writeTaskPillsS7.setWriteInt(dbbNumber, value);
                    Toast.makeText(this, "Word wrote successfully", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
