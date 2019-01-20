package io.aligh.ihttp.enums;

public enum Methods {

    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    TRACE("TRACE"),
    GET("GET");

    private String value;

    Methods(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
