package heh.be.automatmanager.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import heh.be.automatmanager.DB.User.User;
import heh.be.automatmanager.R;

/**
 * Created by flori on 07-12-17.
 */

public class CustomUserAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> arrayList = null;
    private int layoutRessourceId;

    public CustomUserAdapter(Context context, int layoutRessourceId, ArrayList<User> arrayList) {
        super(context, layoutRessourceId, arrayList);
        this.context = context;
        this.layoutRessourceId = layoutRessourceId;
        this.arrayList = arrayList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup parent) {

        View row = view;
        UserListInfo userListInfo;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutRessourceId, parent, false);

            userListInfo = new UserListInfo();
            userListInfo.tv_customRow_name = (TextView) row.findViewById(R.id.tv_customRow_name);
            userListInfo.tv_customRow_surname = (TextView) row.findViewById(R.id.tv_customRow_surname);
            userListInfo.tv_customRow_email = (TextView) row.findViewById(R.id.tv_customRow_email);
            userListInfo.tv_customRow_type = (TextView) row.findViewById(R.id.tv_customRow_type);

            row.setTag(userListInfo);
        } else {

            userListInfo = (UserListInfo) row.getTag();
        }

        User user = arrayList.get(i);
        userListInfo.tv_customRow_name.setText("Firstname : " + user.getName());
        userListInfo.tv_customRow_surname.setText("Surname : " + user.getSurname());
        userListInfo.tv_customRow_email.setText("Email : " + user.getEmail());
        userListInfo.tv_customRow_type.setText("Authorizations : " + user.getType());

        return row;
    }
    
    static class UserListInfo {
        TextView tv_customRow_name;
        TextView tv_customRow_surname;
        TextView tv_customRow_email;
        TextView tv_customRow_type;
    }
}
