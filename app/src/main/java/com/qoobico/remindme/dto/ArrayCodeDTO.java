package com.qoobico.remindme.dto;

import java.util.ArrayList;
import java.util.List;

public class ArrayCodeDTO {

    private List<CodeDTO> codes = new ArrayList<>();

    public List<CodeDTO> getCodes() {
        return codes;
    }

    public void setCodes(List<CodeDTO> codes) {
        this.codes = codes;
    }
}

