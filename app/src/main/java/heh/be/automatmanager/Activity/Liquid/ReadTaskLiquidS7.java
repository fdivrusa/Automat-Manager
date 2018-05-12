package heh.be.automatmanager.Activity.Liquid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

import heh.be.automatmanager.R;
import heh.be.automatmanager.SimaticS7.S7;
import heh.be.automatmanager.SimaticS7.S7Client;
import heh.be.automatmanager.SimaticS7.S7OrderCode;

/**
 * Created by flori on 03-01-18.
 */

public class ReadTaskLiquidS7 {

    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private Button bt_activityAutomatLiquid_connect;
    private Activity activity;
    private TabLayout tl_activityAutomatLiquid_tabLayout;
    private LinearLayout ll_activityAutomatLiquid_layoutReadData;
    private LinearLayout ll_activityAutomatLiquid_layoutVisibility;
    private TextView tv_activityAutomatLiquid_status;
    private TextView tv_activityAutomatLiquid_plc;
    private TextView tv_activityAutomatLiquid_liquidLevel;
    private TextView tv_activityAutomatLiquid_manualValue;
    private TextView tv_activityAutomatLiquid_setPoint;
    private TextView tv_activityAutomatLiquid_mainValveOpening;
    private ImageView iv_activityAutomatLiquid_automaticMode;
    private ImageView iv_activityAutomatLiquid_mainValve;
    private ImageView iv_activityAutomatLiquid_valve1;
    private ImageView iv_activityAutomatLiquid_valve2;
    private ImageView iv_activityAutomatLiquid_valve3;


    private LiquidData liquidData;
    private AutomateS7 plcS7;
    private Thread readthread;
    private S7Client comS7;
    private String[] param = new String[10];
    private byte[] dataPLC = new byte[512];
    private byte[] testForWrite = new byte[512]; //Test

    private int datablocNumber = 24;

    public ReadTaskLiquidS7(Activity activity, int datablocNumber) {

        this.activity = activity;
        this.datablocNumber = datablocNumber;
        liquidData = new LiquidData();

        bt_activityAutomatLiquid_connect = (Button) activity.findViewById(R.id.bt_activityAutomatLiquid_connect);
        tl_activityAutomatLiquid_tabLayout = (TabLayout) activity.findViewById(R.id.tl_activityAutomatPills_tabLayout);
        ll_activityAutomatLiquid_layoutReadData = (LinearLayout) activity.findViewById(R.id.ll_activityAutomatPills_layoutReadData);
        ll_activityAutomatLiquid_layoutVisibility = (LinearLayout) activity.findViewById(R.id.ll_activityAutomatLiquid_layoutVisibility);
        tv_activityAutomatLiquid_liquidLevel = (TextView) activity.findViewById(R.id.tv_activityAutomatLiquid_liquidLevel);
        tv_activityAutomatLiquid_mainValveOpening = (TextView) activity.findViewById(R.id.tv_activityAutomatLiquid_mainValveOpening);
        tv_activityAutomatLiquid_manualValue = (TextView) activity.findViewById(R.id.tv_activityAutomatLiquid_manualValue);
        tv_activityAutomatLiquid_plc = (TextView) activity.findViewById(R.id.tv_activityAutomatLiquid_plc);
        tv_activityAutomatLiquid_setPoint = (TextView) activity.findViewById(R.id.tv_activityAutomatLiquid_setPoint);
        tv_activityAutomatLiquid_status = (TextView) activity.findViewById(R.id.tv_activityAutomatLiquid_status);
        iv_activityAutomatLiquid_automaticMode = (ImageView) activity.findViewById(R.id.iv_activityAutomatLiquid_automaticMode);
        iv_activityAutomatLiquid_mainValve = (ImageView) activity.findViewById(R.id.iv_activityAutomatLiquid_mainValve);
        iv_activityAutomatLiquid_valve1 = (ImageView) activity.findViewById(R.id.iv_activityAutomatLiquid_valve1);
        iv_activityAutomatLiquid_valve2 = (ImageView) activity.findViewById(R.id.iv_activityAutomatLiquid_valve2);
        iv_activityAutomatLiquid_valve3 = (ImageView) activity.findViewById(R.id.iv_activityAutomatLiquid_valve3);

        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        readthread = new Thread(plcS7);
    }

    public void Stop() {
        isRunning.set(false);
        comS7.Disconnect();
        readthread.interrupt();
    }

    public void Start(String a, String r, String s) {

        if (!readthread.isAlive()) {

            param[0] = a;
            param[1] = r;
            param[2] = s;
            readthread.start();
            isRunning.set(true);
        }
    }

    public byte[] getTestDatas() {

        return testForWrite;
    }

    private void downloadOnPreExecute(int t) {

        tv_activityAutomatLiquid_plc.setText("PLC : " + String.valueOf(t));
    }

    private void downloadOnProgressUpdate(int progress) {

        tv_activityAutomatLiquid_liquidLevel.setText("Liquid level : " + liquidData.getLiquidLevel());
        tv_activityAutomatLiquid_manualValue.setText("Manual value : " + liquidData.getManual());
        tv_activityAutomatLiquid_setPoint.setText("SetPoint(?) : " + liquidData.getSetPoint());
        tv_activityAutomatLiquid_mainValveOpening.setText("Main valve opening : " + liquidData.getValvePilotWord());

        iv_activityAutomatLiquid_valve1.setImageResource(liquidData.isValve1On() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatLiquid_valve2.setImageResource(liquidData.isValve2On() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatLiquid_valve3.setImageResource(liquidData.isValve3On() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatLiquid_mainValve.setImageResource(liquidData.isMainValve() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatLiquid_automaticMode.setImageResource(liquidData.isValveManuOn() ? R.drawable.detecting : R.drawable.not_detecting);
    }

    private void downloadOnPostExecute() {


    }

    @SuppressLint("HandlerLeak")
    private Handler monHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);
                    break;
                case MESSAGE_PROGRESS_UPDATE:
                    downloadOnProgressUpdate(msg.arg1);
                    break;
                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;
                default:
                    break;
            }
        }
    };

    private class AutomateS7 implements Runnable {

        @Override
        public void run() {

            try {

                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res = comS7.ConnectTo(param[0], Integer.valueOf(param[1]), Integer.valueOf(param[2]));
                S7OrderCode orderCode = new S7OrderCode();
                Integer result = comS7.GetOrderCode(orderCode);

                int numCPU = -1;
                if (res.equals(0) && result.equals(0)) {

                    numCPU = Integer.valueOf(orderCode.Code().substring(5, 8));
                } else {
                    numCPU = 0000;
                }
                sendPreExecuteMessage(numCPU);
                Log.i("NumCPU", Integer.toString(numCPU));

                while (isRunning.get()) {

                    if (res.equals(0)) {

                        int retInfo = comS7.ReadArea(S7.S7AreaDB, datablocNumber, 0, 35, dataPLC);

                        if (retInfo == 0) {

                            sendProgressMessage();
                        }
                        retInfo = comS7.ReadArea(S7.S7AreaDB, datablocNumber, 0, 35, testForWrite);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sendPostExecuteMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void sendPostExecuteMessage() {
        Message postExecuteMsg = new Message();
        postExecuteMsg.what = MESSAGE_POST_EXECUTE;
        monHandler.sendMessage(postExecuteMsg);
    }

    private void sendPreExecuteMessage(int v) {
        Message preExecuteMsg = new Message();
        preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
        preExecuteMsg.arg1 = v;
        monHandler.sendMessage(preExecuteMsg);
    }

    private int i = 0;

    private void sendProgressMessage() {

        Message progressMsg = new Message();
        progressMsg.what = MESSAGE_PROGRESS_UPDATE;
        progressMsg.arg1 = i++;
        setLiquidData();
        monHandler.sendMessage(progressMsg);
    }

    private void setLiquidData() {

        liquidData.setMainValve(S7.GetBitAt(dataPLC, 0, 1));
        liquidData.setValve1On(S7.GetBitAt(dataPLC, 0, 2));
        liquidData.setValve2On(S7.GetBitAt(dataPLC, 0, 3));
        liquidData.setValve3On(S7.GetBitAt(dataPLC, 0, 4));
        liquidData.setValveManuOn(S7.GetBitAt(dataPLC, 0, 5));
        liquidData.setLiquidLevel(S7.GetWordAt(dataPLC, 16));
        liquidData.setSetPoint(S7.GetWordAt(dataPLC, 18));
        liquidData.setManual(S7.GetWordAt(dataPLC, 20));
        liquidData.setValvePilotWord(S7.GetWordAt(dataPLC, 22));
    }
}
