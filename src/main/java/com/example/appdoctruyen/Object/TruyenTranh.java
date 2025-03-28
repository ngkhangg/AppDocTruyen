package com.example.appdoctruyen.Object;

import java.io.Serializable;

public class TruyenTranh implements Serializable {

    private String tenTruyen;
    private String tenChap;
    private String linkAnh;
    private String description;

    // Constructor
    public TruyenTranh(String tenTruyen, String tenChap, String linkAnh, String description) {
        this.tenTruyen = tenTruyen;
        this.tenChap = tenChap;
        this.linkAnh = linkAnh;
        this.description = description;
    }

    // Getter and Setter methods
    public String getTenTruyen() {
        return tenTruyen;
    }

    public void setTenTruyen(String tenTruyen) {
        this.tenTruyen = tenTruyen;
    }

    public String getTenChap() {
        return tenChap;
    }

    public void setTenChap(String tenChap) {
        this.tenChap = tenChap;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
