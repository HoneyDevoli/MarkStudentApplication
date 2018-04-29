package com.qoobico.remindme.dto;

import java.io.Serializable;

public class GroupFromSstuDTO implements Serializable {

    private int id;
    private String ownPage;
    private int idPage;

    public int getIdPage() {
        return idPage;
    }

    public void setIdPage(int idPage) {
        this.idPage = idPage;
    }

    public String getOwnPage() {
        return ownPage;
    }

    public void setOwnPage(String ownPage) {
        this.ownPage = ownPage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
