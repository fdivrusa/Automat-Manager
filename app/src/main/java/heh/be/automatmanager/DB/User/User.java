package heh.be.automatmanager.DB.User;

/**
 * Created by flori on 02-12-17.
 */

public class User {


    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String type;

    public User() {
    }

    public User(String name, String surname, String email,
                String password, String type) {

        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("ID : "
                + Integer.toString(getId()) + "\n"
                + "Name : " + getName() + "\n"
                + "Surname : " + getSurname() + "\n"
                + "Email : " + getEmail() + "\n"
                + "Password : " + getPassword() + "\n"
                + "Type : " + getType());
        return sb.toString();
    }
}
