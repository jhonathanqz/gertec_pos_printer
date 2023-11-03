package com.qz.gertec_pos_printer;

import com.google.gson.Gson;

public class Response<T> {
    private String message;
    private boolean success;
    private T data;

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String send(String message, T data, boolean success) {
        this.message = message;
        this.success = success;
        this.data = data;
        return toJson();
    }
}
