package com.example.contactslg;

import java.util.List;

public class CommunityUnit {

    private String cuName;
    private String chvName;
    private String chvPhone;

    public CommunityUnit() {
    }

    public CommunityUnit(String cuName) {
        this.cuName = cuName;
    }

    public CommunityUnit(String cuName, String chvName, String chvPhone) {
        this.cuName = cuName;
        this.chvName = chvName;
        this.chvPhone = chvPhone;
    }

    public String getChvName() {
        return chvName;
    }

    public String getChvPhone() {
        return chvPhone;
    }

    public String getCuName() {
        return cuName;
    }
}
