package com.example.labourmangement.model;

public class TrackLaborModel {
    private String job_id;

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getLastaddress() {
        return lastaddress;
    }

    public void setLastaddress(String lastaddress) {
        this.lastaddress = lastaddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabor_id() {
        return labor_id;
    }

    public void setLabor_id(String labor_id) {
        this.labor_id = labor_id;
    }

    public String getContractor_id() {
        return contractor_id;
    }

    public void setContractor_id(String contractor_id) {
        this.contractor_id = contractor_id;
    }

    private String job_title ;
    private String lastaddress;
    private String time;
    private String labor_id;
    private String contractor_id;
    private String job_area;

    public String getLaborname() {
        return laborname;
    }

    public void setLaborname(String laborname) {
        this.laborname = laborname;
    }

    private String laborname;


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String latitude;
    private String longitude;

    public String getJob_area() {
        return job_area;
    }

    public void setJob_area(String job_area) {
        this.job_area = job_area;
    }

    public String getJob_wages() {
        return job_wages;
    }

    public void setJob_wages(String job_wages) {
        this.job_wages = job_wages;
    }

    private String job_wages;

    public TrackLaborModel() {
        this.job_id = job_id;
        this.job_title = job_title;
        this.lastaddress = lastaddress;
        this.time = time;
        this.labor_id = labor_id;
        this.contractor_id = contractor_id;
        this.job_area=job_area;
        this.job_wages =job_wages;
        this.latitude=latitude;
        this.longitude=longitude;
        this.laborname=laborname;
    }



}
