package com.example.labourmangement.model;

public class JobModel {
    private String job_id;
    private String job_title ;
    private String job_details;
    private String job_wages;
    private String job_area;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;


    public String getContractor_name() {
        return contractor_name;
    }

    public void setContractor_name(String contractor_name) {
        this.contractor_name = contractor_name;
    }

    private String contractor_name;
    private String created_by;

    public JobModel(String job_id,String job_title, String job_details, String job_wages, String job_area,String created_by, String contractor_name,String role) {
        this.job_id = this.job_id;
        this.job_title = this.job_title;
        this.job_details = this.job_details;
        this.job_wages = this.job_wages;
        this.job_area = this.job_area;
        this.created_by = this.created_by;
        this.contractor_name = this.contractor_name;
        this.role =this.role;
    }

    public JobModel() {

    }


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

    public String getJob_details() {
        return job_details;
    }

    public void setJob_details(String job_details) {
        this.job_details = job_details;
    }

    public String getJob_wages() {
        return job_wages;
    }

    public void setJob_wages(String job_wages) {
        this.job_wages = job_wages;
    }

    public String getJob_area() {
        return job_area;
    }

    public void setJob_area(String job_area) {
        this.job_area = job_area;
    }
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }




}
