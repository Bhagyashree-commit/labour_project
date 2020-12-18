package com.example.labourmangement.model;

public class JobWagesModel {

    public String getWages_status() {
        return wages_status;
    }

    public void setWages_status(String wages_status) {
        this.wages_status = wages_status;
    }

    private   String wages_status;
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

    public String getJob_wages() {
        return job_wages;
    }

    public void setJob_wages(String job_wages) {
        this.job_wages = job_wages;
    }


    private String job_id;

    private String job_title ;
    private String job_wages;
    private  String applied_by;
    private String created_by;

    public String getContractor_id() {
        return contractor_id;
    }

    public void setContractor_id(String contractor_id) {
        this.contractor_id = contractor_id;
    }

    public String getLabor_id() {
        return labor_id;
    }

    public void setLabor_id(String labor_id) {
        this.labor_id = labor_id;
    }

    private String contractor_id;
    private String labor_id;

    public String getApplied_by() {
        return applied_by;
    }

    public void setApplied_by(String applied_by) {
        this.applied_by = applied_by;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
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

    private String labor_name;
    private String contractor_name;

    public JobWagesModel() {
        this.job_id = job_id;
        this.job_title = job_title;
        this.job_wages = job_wages;
        this.applied_by=applied_by;
        this.created_by=created_by;
        this.contractor_name=contractor_name;
        this.labor_name=labor_name;
        this.labor_id=labor_id;
        this.contractor_id=contractor_id;

    }


}
