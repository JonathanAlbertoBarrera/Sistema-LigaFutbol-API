package com.sistemaligafutbol.sistemaligafutbol.modules.imagen;

public class ImgurResponse {
    private ImgurData data;
    private boolean success;
    private int status;

    //--GETTERS Y SETTERS

    public ImgurData getData() {
        return data;
    }

    public void setData(ImgurData data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}