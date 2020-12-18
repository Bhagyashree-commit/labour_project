package com.example.labourmangement.model;

public class GetWagesModel {
    private String job_id;

    public String getWages_approval_status() {
        return wages_approval_status;
    }

    public void setWages_approval_status(String wages_approval_status) {
        this.wages_approval_status = wages_approval_status;
    }

    private String wages_approval_status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

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

    public String getLabor_name() {
        return labor_name;
    }

    public void setLabor_name(String labor_name) {
        this.labor_name = labor_name;
    }

    public String getContractor_name() {
        return contractor_name;
    }

    public void setContractor_name(String contractor_name) {
        this.contractor_name = contractor_name;
    }

    private String labor_name;
    private String contractor_name;

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

    public GetWagesModel() {
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
        this.contractor_name=contractor_name;
        this.labor_name=labor_name;
    }

}
