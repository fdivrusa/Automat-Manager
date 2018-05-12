package heh.be.automatmanager.Activity.Pills;

/**
 * Created by flori on 30-12-17.
 */

public class PillsData {

    //Data of the automat
    private boolean isOn; //I8 ==> Selector "is working"
    private boolean genBottle; //I13

    private boolean demand5; //Q1
    private boolean demand10; //Q2
    private boolean demand15; //Q3

    private boolean distribPillContact; //Q4
    private boolean engineBandContact; //Q5

    private boolean detectFilling; //I1
    private boolean detectBouchonning; //I9
    private boolean detectPills; //I7
    private boolean onlineAccess; //I14

    private int nbr_comp;
    private int nbr_bout;


    public boolean isDetectPills() {
        return detectPills;
    }

    public boolean isGenBottle() {
        return genBottle;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isOnlineAccess() {
        return onlineAccess;
    }

    public boolean isDemand5() {
        return demand5;
    }

    public boolean isDemand10() {
        return demand10;
    }

    public boolean isDemand15() {
        return demand15;
    }

    public boolean isDistribPillContact() {
        return distribPillContact;
    }

    public boolean isDetectBouchonning() {
        return detectBouchonning;
    }

    public boolean isDetectFilling() {
        return detectFilling;
    }

    public boolean isEngineBandContact() {
        return engineBandContact;
    }

    public void setDetectPills(boolean detectPills) {
        this.detectPills = detectPills;
    }

    public void setGenBottle(boolean genBottle) {
        this.genBottle = genBottle;
    }

    public int getNbr_bout() {
        return nbr_bout;
    }

    public int getNbr_comp() {
        return nbr_comp;
    }

    public void setDemand5(boolean demand5) {
        this.demand5 = demand5;
    }

    public void setDemand10(boolean demand10) {
        this.demand10 = demand10;
    }

    public void setDemand15(boolean demand15) {
        this.demand15 = demand15;
    }

    public void setDetectFilling(boolean detectFilling) {
        this.detectFilling = detectFilling;
    }

    public void setDistribPillContact(boolean distribPillContact) {
        this.distribPillContact = distribPillContact;
    }

    public void setDetectBouchonning(boolean detectBouchonning) {
        this.detectBouchonning = detectBouchonning;
    }

    public void setEngineBandContact(boolean engineBandContact) {
        this.engineBandContact = engineBandContact;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setOnlineAccess(boolean onlineAccess) {
        this.onlineAccess = onlineAccess;
    }

    public void setNbr_bout(int nbr_bout) {
        this.nbr_bout = nbr_bout;
    }

    public void setNbr_comp(int nbr_comp) {
        this.nbr_comp = nbr_comp;
    }
}
