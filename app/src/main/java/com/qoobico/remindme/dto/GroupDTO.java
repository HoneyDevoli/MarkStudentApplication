package com.qoobico.remindme.dto;

import java.io.Serializable;

public class GroupDTO implements Serializable {

    private long id;
    private String name;
    private int idRasp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdRasp() {
        return idRasp;
    }

    public void setIdRasp(int idRasp) {
        this.idRasp = idRasp;
    }
}
