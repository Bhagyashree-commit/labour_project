package com.example.labourmangement.model;

public class PaymentStatusModel {
    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_wages() {
        return job_wages;
    }

    public void setJob_wages(String job_wages) {
        this.job_wages = job_wages;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String job_id;
    String job_wages;
    String job_title;
    String labor_id;
    String contractor_id;
    String labor_name;
    String contractor_name;
    String status;
    public PaymentStatusModel() {
        this.job_id = job_id;
        this.job_wages = job_wages;
        this.job_title = job_title;
        this.labor_id = labor_id;
        this.contractor_id = contractor_id;
        this.labor_name = labor_name;
        this.contractor_name = contractor_name;
        this.status = status;
    }

}
