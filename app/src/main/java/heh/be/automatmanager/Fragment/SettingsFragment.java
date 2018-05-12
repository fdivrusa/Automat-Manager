package heh.be.automatmanager.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import heh.be.automatmanager.Classes.HashTool;
import heh.be.automatmanager.DB.User.User;
import heh.be.automatmanager.DB.User.UserAccessDB;
import heh.be.automatmanager.R;

/**
 * Created by flori on 12-12-17.
 */

public class SettingsFragment extends PreferenceFragment {


    EditTextPreference etp_activitySettings_name;
    EditTextPreference etp_activitySettings_surname;
    EditTextPreference etp_activitySettings_password;
    UserAccessDB userAccessDB;
    SharedPreferences pref_datas;
    User userModification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref_datas = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        userAccessDB = new UserAccessDB(getActivity().getApplicationContext());

        //Load the preferences from my xml file
        addPreferencesFromResource(R.xml.pref_settings);

        etp_activitySettings_name = (EditTextPreference) getPreferenceManager().findPreference("Name");
        etp_activitySettings_surname = (EditTextPreference) getPreferenceManager().findPreference("Surname");
        etp_activitySettings_password = (EditTextPreference) getPreferenceManager().findPreference("Password");

        etp_activitySettings_password.setText("");

        etp_activitySettings_name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                //opening
                userAccessDB.openForWrite();

                //New user updated
                userModification = new User(etp_activitySettings_name.getEditText().getText().toString().trim(), pref_datas.getString("Surname", null)
                        , pref_datas.getString("Email", null), pref_datas.getString("Password", null), pref_datas.getString("Type", null));

                //Update in database
                userAccessDB.updateUser(Integer.parseInt(pref_datas.getString("UserID", null)), userModification);

                //close after update
                userAccessDB.close();

                //Updating SharedPreferences
                SharedPreferences.Editor editor = pref_datas.edit();
                editor.putString("Name", etp_activitySettings_name.getEditText().getText().toString());
                editor.apply();

                Toast.makeText(getActivity().getApplicationContext(), "Name update successfully", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        etp_activitySettings_surname.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                userAccessDB.openForWrite();
                userModification = new User(pref_datas.getString("Name", null), etp_activitySettings_surname.getEditText().getText().toString().trim()
                        , pref_datas.getString("Email", null), pref_datas.getString("Password", null), pref_datas.getString("Type", null));
                userAccessDB.updateUser(Integer.parseInt(pref_datas.getString("UserID", null)), userModification);
                userAccessDB.close();

                SharedPreferences.Editor editor = pref_datas.edit();
                editor.putString("Surname", etp_activitySettings_surname.getEditText().getText().toString());
                editor.apply();

                Toast.makeText(getActivity().getApplicationContext(), "Surname update successfully", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        

        etp_activitySettings_password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                String password = etp_activitySettings_password.getEditText().getText().toString();
                HashTool hashTool = new HashTool();

                //If password not correct
                if (password.length() < 4) {

                    etp_activitySettings_password.getEditText().setText("");
                    etp_activitySettings_password.getEditText().setError("Password must be 4 characters length or longer");
                    Toast.makeText(getActivity().getApplicationContext(), "Password not valid", Toast.LENGTH_LONG).show();
                    return false;
                }

                //Hash the string
                String hashPassword = hashTool.createHash(etp_activitySettings_password.getEditText().getText().toString());

                //update the user
                userModification = new User(pref_datas.getString("Name", null), pref_datas.getString("Surname", null),
                        pref_datas.getString("Email", null), hashPassword, pref_datas.getString("Type", null));

                userAccessDB.openForWrite();
                userAccessDB.updateUser(Integer.parseInt(pref_datas.getString("UserID", null)), userModification);
                userAccessDB.close();

                Toast.makeText(getActivity().getApplicationContext(), "Password update successfully", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }
}
