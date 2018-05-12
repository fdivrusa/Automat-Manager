package heh.be.automatmanager.Activity.Pills;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import heh.be.automatmanager.R;
import heh.be.automatmanager.SimaticS7.S7;
import heh.be.automatmanager.SimaticS7.S7Client;
import heh.be.automatmanager.SimaticS7.S7OrderCode;

/**
 * Created by flori on 19-12-17.
 */

public class ReadTaskPillsS7 {

    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;
    private AtomicBoolean isRunning = new AtomicBoolean(false);


    private Button bt_activityAutomatPills_connect;
    private Activity activity;
    private TextView tv_activityAutomatPills_plc;
    private TextView tv_activityAutomatPills_bottleFilled;
    private TextView tv_activityAutomatPills_pillsPerBottle;
    private Switch sw_activityAutomatPills_AutomatStatus;
    private Switch sw_activityAutomatPills_BottleArrivalStatus;
    private RadioButton rb_activityAutomatPills_5Pills;
    private RadioButton rb_activityAutomatPills_10Pills;
    private RadioButton rb_activityAutomatPills_15Pills;
    private ImageView iv_activityAutomatPills_bottleFillingDetector;
    private ImageView iv_activityAutomatPills_bottleBouchonningDetector;
    private ImageView iv_activityAutomatPills_bandEngineContactor;
    private ImageView iv_activityAutomatPills_PillsEngineContactor;
    private ImageView iv_activityAutomatPills_pillsFillingDetector;
    private ImageView iv_activityAutomatPills_onlineAccess;

    private PillsData pillsData;
    private AutomateS7 plcS7;
    private Thread readThread;
    private S7Client comS7;
    private String[] param = new String[10];
    private byte[] datasPLC = new byte[512];
    private byte[] testForWrite = new byte[512]; //Test for write
    //Write seems to work better with this


    private int datablocNumber = 24;


    public ReadTaskPillsS7(Activity activity, int datablocNumber) {

        this.activity = activity;
        pillsData = new PillsData();
        this.datablocNumber = datablocNumber;

        bt_activityAutomatPills_connect = (Button) activity.findViewById(R.id.bt_activityAutomatPills_connect);
        tv_activityAutomatPills_plc = (TextView) activity.findViewById(R.id.tv_activityAutomatPills_plc);
        tv_activityAutomatPills_bottleFilled = (TextView) activity.findViewById(R.id.tv_activityAutomatPills_bottleFilled);
        tv_activityAutomatPills_pillsPerBottle = (TextView) activity.findViewById(R.id.tv_activityAutomatPills_pillsPerBottle);
        sw_activityAutomatPills_AutomatStatus = (Switch) activity.findViewById(R.id.sw_activityAutomatPills_AutomatStatus);
        sw_activityAutomatPills_BottleArrivalStatus = (Switch) activity.findViewById(R.id.sw_activityAutomatPills_BottleArrivalStatus);
        rb_activityAutomatPills_5Pills = (RadioButton) activity.findViewById(R.id.rb_activityAutomatPills_5Pills);
        rb_activityAutomatPills_10Pills = (RadioButton) activity.findViewById(R.id.rb_activityAutomatPills_10Pills);
        rb_activityAutomatPills_15Pills = (RadioButton) activity.findViewById(R.id.rb_activityAutomatPills_15Pills);
        iv_activityAutomatPills_bottleFillingDetector = (ImageView) activity.findViewById(R.id.iv_activityAutomatPills_bottleFillingDetector);
        iv_activityAutomatPills_bottleBouchonningDetector = (ImageView) activity.findViewById(R.id.iv_activityAutomatPills_bottleBouchonningDetector);
        iv_activityAutomatPills_bandEngineContactor = (ImageView) activity.findViewById(R.id.iv_activityAutomatPills_bandEngineContactor);
        iv_activityAutomatPills_PillsEngineContactor = (ImageView) activity.findViewById(R.id.iv_activityAutomatPills_PillsEngineContactor);
        iv_activityAutomatPills_pillsFillingDetector = (ImageView) activity.findViewById(R.id.iv_activityAutomatPills_pillsFillingDetector);
        iv_activityAutomatPills_onlineAccess = (ImageView) activity.findViewById(R.id.iv_activityAutomatPills_onlineAccess);

        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        readThread = new Thread(plcS7);
    }

    public void Stop() {
        isRunning.set(false);
        comS7.Disconnect();
        readThread.interrupt();
    }

    public void Start(String a, String r, String s) {
        if (!readThread.isAlive()) {
            param[0] = a;
            param[1] = r;
            param[2] = s;
            readThread.start();
            isRunning.set(true);
        }
    }

    public byte[] getTestDatas() {

        return testForWrite;
    }

    private void downloadOnPreExecute(int t) {

        tv_activityAutomatPills_plc.setText("PLC : " + String.valueOf(t));
    }

    private void downloadOnProgressUpdate(int progress) {

        sw_activityAutomatPills_AutomatStatus.setChecked(pillsData.isOn());
        sw_activityAutomatPills_BottleArrivalStatus.setChecked(pillsData.isGenBottle());
        rb_activityAutomatPills_5Pills.setChecked(pillsData.isDemand5());
        rb_activityAutomatPills_10Pills.setChecked(pillsData.isDemand10());
        rb_activityAutomatPills_15Pills.setChecked(pillsData.isDemand15());
        iv_activityAutomatPills_bottleFillingDetector.setImageResource(pillsData.isDetectFilling() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatPills_bottleBouchonningDetector.setImageResource(pillsData.isDetectBouchonning() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatPills_bandEngineContactor.setImageResource(pillsData.isEngineBandContact() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatPills_PillsEngineContactor.setImageResource(pillsData.isDistribPillContact() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatPills_pillsFillingDetector.setImageResource(pillsData.isDetectPills() ? R.drawable.detecting : R.drawable.not_detecting);
        iv_activityAutomatPills_onlineAccess.setImageResource(pillsData.isOnlineAccess() ? R.drawable.detecting : R.drawable.not_detecting);
        tv_activityAutomatPills_pillsPerBottle.setText("Pills per bottle :" + pillsData.getNbr_comp());
        tv_activityAutomatPills_bottleFilled.setText("Bottle already filled : " + pillsData.getNbr_bout());

        if (pillsData.isOnlineAccess()) {

            iv_activityAutomatPills_onlineAccess.setTag("detecting");
        } else {

            iv_activityAutomatPills_onlineAccess.setTag("not_detecting");
        }
    }

    private void downloadOnPostExecute() {

        tv_activityAutomatPills_plc.setText("PLC : Not available");
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

                //Connection with data
                Integer res = comS7.ConnectTo(param[0], Integer.valueOf(param[1]), Integer.valueOf(param[2]));
                S7OrderCode orderCode = new S7OrderCode();

                //Get the plc number
                Integer result = comS7.GetOrderCode(orderCode);
                int numCPU = -1;
                if (res.equals(0) && result.equals(0)) {
                    //Quelques exemples :
                    // WinAC : 6ES7 611-4SB00-0YB7
                    // S7-315 2DPP?N : 6ES7 315-4EH13-0AB0
                    // S7-1214C : 6ES7 214-1BG40-0XB0
                    // Récupérer le code CPU  611 OU 315 OU 214
                    numCPU = Integer.valueOf(orderCode.Code().substring(5, 8));
                } else numCPU = 0000;
                sendPreExecuteMessage(numCPU);
                Log.i("NumCPU", Integer.toString(numCPU));

                while (isRunning.get()) {

                    //If app is connected to the automat
                    if (res.equals(0)) {

                        int retInfo = comS7.ReadArea(S7.S7AreaDB, datablocNumber, 0, 35, datasPLC);

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
            setPillsData();
            monHandler.sendMessage(progressMsg);
        }

        private void setPillsData() {

            pillsData.setOn(S7.GetBitAt(datasPLC, 0, 0));
            pillsData.setDetectFilling(S7.GetBitAt(datasPLC, 0, 4));
            pillsData.setDetectBouchonning(S7.GetBitAt(datasPLC, 0, 5));
            pillsData.setDetectPills(S7.GetBitAt(datasPLC, 0, 6));
            pillsData.setGenBottle(S7.GetBitAt(datasPLC, 1, 3));
            pillsData.setOnlineAccess(S7.GetBitAt(datasPLC, 1, 6));
            pillsData.setDistribPillContact(S7.GetBitAt(datasPLC, 4, 0));
            pillsData.setEngineBandContact(S7.GetBitAt(datasPLC, 4, 1));
            pillsData.setDemand5(S7.GetBitAt(datasPLC, 4, 3));
            pillsData.setDemand10(S7.GetBitAt(datasPLC, 4, 4));
            pillsData.setDemand15(S7.GetBitAt(datasPLC, 4, 5));
            pillsData.setOnlineAccess(S7.GetBitAt(datasPLC, 5, 6));

            pillsData.setNbr_comp(S7.BCDtoByte(datasPLC[15]));
            pillsData.setNbr_bout(S7.GetWordAt(datasPLC, 16));
        }
    }
}