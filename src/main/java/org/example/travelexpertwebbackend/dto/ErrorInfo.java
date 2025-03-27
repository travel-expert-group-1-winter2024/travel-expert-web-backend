package org.example.travelexpertwebbackend.dto;

public class ErrorInfo {
    private String detail;

    public ErrorInfo() {}

    public ErrorInfo(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
