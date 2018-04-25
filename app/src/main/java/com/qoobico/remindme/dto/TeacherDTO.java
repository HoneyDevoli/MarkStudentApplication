package com.qoobico.remindme.dto;

public class TeacherDTO {

    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String login;
    private String password;
    private int idRasp;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRasp() {
        return idRasp;
    }

    public void setIdRasp(int idRasp) {
        this.idRasp = idRasp;
    }
}
