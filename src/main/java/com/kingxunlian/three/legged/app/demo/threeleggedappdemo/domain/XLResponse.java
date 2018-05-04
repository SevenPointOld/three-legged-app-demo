package com.kingxunlian.three.legged.app.demo.threeleggedappdemo.domain;

public class XLResponse<T> {

    private T body;

    private boolean ok;

    private Status status;

    public T getBody() {
        return this.body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public boolean isOk() {
        return this.ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
