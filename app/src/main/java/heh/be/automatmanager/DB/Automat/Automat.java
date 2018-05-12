package heh.be.automatmanager.DB.Automat;

/**
 * Created by flori on 11-12-17.
 */

public class Automat {

    private int id;
    private String name;
    private String description;
    private String ipAddress;
    private int slotNumber;
    private int rackNumber;
    private String typeAutomat;
    private int datablocNumber;

    public Automat() {

    }

    public Automat(String name, String description, String ipAddress, int slotNumber, int rackNumber, String typeAutomat, int datablocNumber) {

        this.name = name;
        this.description = description;
        this.ipAddress = ipAddress;
        this.slotNumber = slotNumber;
        this.rackNumber = rackNumber;
        this.typeAutomat = typeAutomat;
        this.datablocNumber = datablocNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRackNumber() {
        return rackNumber;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getTypeAutomat() {
        return typeAutomat;
    }

    public int getDatablocNumber() {
        return datablocNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIpAddress(String ipAdress) {
        this.ipAddress = ipAdress;
    }

    public void setRackNumber(int rackNumber) {
        this.rackNumber = rackNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public void setTypeAutomat(String typeAutomat) {
        this.typeAutomat = typeAutomat;
    }

    public void setDatablocNumber(int datablocNumber) {
        this.datablocNumber = datablocNumber;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("ID : "
                + Integer.toString(getId()) + "\n"
                + "Name : " + getName() + "\n"
                + "Description : " + getDescription() + "\n"
                + "Adresse IP : " + getIpAddress() + "\n"
                + "Numéro de rack : " + getRackNumber() + "\n"
                + "Numéro de slot : " + getSlotNumber() + "\n"
                + "Type d'automat : " + getTypeAutomat());
        return sb.toString();
    }
}



