package heh.be.automatmanager.Activity.Liquid;

import java.util.concurrent.atomic.AtomicBoolean;

import heh.be.automatmanager.SimaticS7.S7;
import heh.be.automatmanager.SimaticS7.S7Client;

/**
 * Created by flori on 03-01-18.
 */

public class WriteTaskLiquidS7 {

    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Thread writeThread;
    private AutomateS7 plcS7;
    private S7Client comS7;
    private int datablocNumber = 24;
    private byte[] motCommande = new byte[512];

    private String[] paramConnexion = new String[10];

    public WriteTaskLiquidS7(int datablocNumber) {

        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        writeThread = new Thread(plcS7);
        this.datablocNumber = datablocNumber;
    }

    public void Start(String a, String r, String s, byte[] datas) {

        if (!writeThread.isAlive()) {
            motCommande = datas;
            paramConnexion[0] = a;
            paramConnexion[1] = r;
            paramConnexion[2] = s;
            writeThread.start();
            isRunning.set(true);
        }
    }

    public void Stop() {

        isRunning.set(false);
        comS7.Disconnect();
        writeThread.interrupt();
    }

    private class AutomateS7 implements Runnable {
        @Override
        public void run() {
            try {
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res = comS7.ConnectTo(paramConnexion[0],
                        Integer.valueOf(paramConnexion[1]), Integer.valueOf(paramConnexion[2]));

                while (isRunning.get() && (res.equals(0))) {
                    Integer writePLC = comS7.WriteArea(S7.S7AreaDB, datablocNumber, 0, 35, motCommande);
                    //comS7.WriteArea(S7.S7AreaDB, datablocNumber, 24, 2, dbb24);

                    if (res.equals(0) && writePLC.equals(0)) {
                        //Log.i("test", String.valueOf(res) + "--------" + String.valueOf(writePLC));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setWriteBool(int pos, int bit, int value) {

        S7.SetBitAt(motCommande, pos, bit, value == 1 ? true : false);
    }

    public void setWriteInt(int pos, int value) {

        S7.SetDIntAt(motCommande, pos, value);
    }

    public void setWriteByte(int pos, int value) {

        S7.SetWordAt(motCommande, pos, value);
    }
}
