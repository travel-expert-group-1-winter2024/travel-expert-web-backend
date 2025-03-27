package org.example.travelexpertwebbackend.dto;

public class ErrorInfo {
    private String fields;
    private String detail;

    public ErrorInfo() {}

    public ErrorInfo(String detail) {
        this.detail = detail;
    }

    public ErrorInfo(String fields, String detail) {
        this.fields = fields;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
