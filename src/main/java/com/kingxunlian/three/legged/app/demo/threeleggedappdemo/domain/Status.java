package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain;

public class Status {

    private String description;

    private String message;

    private String path;

    private String returnCode;

    private String serviceCode;

    private Long time;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getServiceCode() {
        return this.serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Status{" +
                "description='" + this.description + '\'' +
                ", message='" + this.message + '\'' +
                ", path='" + this.path + '\'' +
                ", returnCode='" + this.returnCode + '\'' +
                ", serviceCode='" + this.serviceCode + '\'' +
                ", time=" + this.time +
                '}';
    }
}
