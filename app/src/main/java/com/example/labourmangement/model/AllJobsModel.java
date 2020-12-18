package com.example.labourmangement.model;

public class AllJobsModel {
    private String job_id;
    private String job_title;

    private String job_details;
    private String job_wages;

    public AllJobsModel() {
        this.job_id = job_id;
        this.job_title = job_title;
        this.job_details = job_details;
        this.job_wages = job_wages;
        this.job_area = job_area;
        this.contractor_name = contractor_name;
        this.post_date = post_date;
    }

    private String job_area;
    private String contractor_name;
    private String post_date;

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

    public String getContractor_name() {
        return contractor_name;
    }

    public void setContractor_name(String contractor_name) {
        this.contractor_name = contractor_name;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }


}
