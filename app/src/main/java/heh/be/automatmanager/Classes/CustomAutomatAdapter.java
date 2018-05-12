package heh.be.automatmanager.Classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import heh.be.automatmanager.DB.Automat.Automat;
import heh.be.automatmanager.R;

/**
 * Created by flori on 16-12-17.
 */

public class CustomAutomatAdapter extends ArrayAdapter<Automat> {

    private Context context;
    private ArrayList<Automat> arrayList = null;
    private int layoutRessourceId;

    public CustomAutomatAdapter(Context context, int layoutRessourceId, ArrayList<Automat> arrayList) {
        super(context, layoutRessourceId, arrayList);
        this.context = context;
        this.layoutRessourceId = layoutRessourceId;
        this.arrayList = arrayList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup parent) {

        View row = view;
        AutomatListInfo automatListInfo;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutRessourceId, parent, false);

            automatListInfo = new AutomatListInfo();
            automatListInfo.tv_customRow_name = (TextView) row.findViewById(R.id.tv_customRow_automatName);
            automatListInfo.tv_customRow_description = (TextView) row.findViewById(R.id.tv_customRow_description);
            automatListInfo.tv_customRow_ipAddress = (TextView) row.findViewById(R.id.tv_customRow_ipAddress);
            automatListInfo.tv_customRow_slotNumber = (TextView) row.findViewById(R.id.tv_customRow_slotNumber);
            automatListInfo.tv_customRow_rackNumber = (TextView) row.findViewById(R.id.tv_customRow_rackNumber);
            automatListInfo.tv_customRow_typeAutomat = (TextView) row.findViewById(R.id.tv_customRow_typeAutomat);
            automatListInfo.tv_customRow_datablocNumber = (TextView) row.findViewById(R.id.tv_customRow_datablocNumber);

            row.setTag(automatListInfo);
        } else {

            automatListInfo = (AutomatListInfo) row.getTag();
        }

        Automat automat = arrayList.get(i);
        automatListInfo.tv_customRow_name.setText("Name : " + automat.getName());
        automatListInfo.tv_customRow_description.setText("Description : " + automat.getDescription());
        automatListInfo.tv_customRow_ipAddress.setText("IP address : " + automat.getIpAddress());
        automatListInfo.tv_customRow_slotNumber.setText("Slot number : " + automat.getSlotNumber());
        automatListInfo.tv_customRow_rackNumber.setText("Rack number : " + automat.getRackNumber());
        automatListInfo.tv_customRow_datablocNumber.setText("Databloc number : " + automat.getDatablocNumber());
        automatListInfo.tv_customRow_typeAutomat.setText("Automat type : " + automat.getTypeAutomat());

        return row;
    }

    static class AutomatListInfo {
        TextView tv_customRow_name;
        TextView tv_customRow_description;
        TextView tv_customRow_ipAddress;
        TextView tv_customRow_slotNumber;
        TextView tv_customRow_rackNumber;
        TextView tv_customRow_typeAutomat;
        TextView tv_customRow_datablocNumber;

    }
}
