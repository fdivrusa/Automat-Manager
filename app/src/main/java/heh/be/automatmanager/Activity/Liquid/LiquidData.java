package heh.be.automatmanager.Activity.Liquid;

/**
 * Created by flori on 03-01-18.
 */

public class LiquidData {

    private boolean valve1On;
    private boolean valve2On;
    private boolean valve3On;
    private boolean mainValve;
    private boolean valveManuOn;
    private boolean onlineAccess;

    private int liquidLevel;
    private int setPoint;
    private int manual;
    private int valvePilotWord;



    public int getLiquidLevel() {
        return liquidLevel;
    }

    public int getManual() {
        return manual;
    }

    public int getSetPoint() {
        return setPoint;
    }

    public int getValvePilotWord() {
        return valvePilotWord;
    }

    public void setOnlineAccess(boolean onlineAccess) {
        this.onlineAccess = onlineAccess;
    }

    public boolean isValve1On() {
        return valve1On;
    }

    public boolean isValve2On() {
        return valve2On;
    }

    public boolean isValve3On() {
        return valve3On;
    }

    public boolean isMainValve() {
        return mainValve;
    }

    public boolean isValveManuOn() {
        return valveManuOn;
    }

    public boolean isOnlineAccess() {
        return onlineAccess;
    }

    public void setLiquidLevel(int liquidLevel) {
        this.liquidLevel = liquidLevel;
    }

    public void setSetPoint(int setPoint) {
        this.setPoint = setPoint;
    }

    public void setManual(int manual) {
        this.manual = manual;
    }

    public void setValve1On(boolean valve1On) {
        this.valve1On = valve1On;
    }

    public void setValve2On(boolean valve2On) {
        this.valve2On = valve2On;
    }

    public void setValve3On(boolean valve3On) {
        this.valve3On = valve3On;
    }

    public void setMainValve(boolean mainValve) {
        this.mainValve = mainValve;
    }

    public void setValveManuOn(boolean valveManuOn) {
        this.valveManuOn = valveManuOn;
    }

    public void setValvePilotWord(int valvePilotWord) {
        this.valvePilotWord = valvePilotWord;
    }
}
