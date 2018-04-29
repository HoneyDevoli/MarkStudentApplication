package com.qoobico.remindme.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArrayGroupDTO implements Serializable {

        private List<GroupFromSstuDTO> groups = new ArrayList<GroupFromSstuDTO>();

        public List<GroupFromSstuDTO> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupFromSstuDTO> groups) {
            this.groups = groups;
        }
    }

