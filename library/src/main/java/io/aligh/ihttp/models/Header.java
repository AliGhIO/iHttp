package io.aligh.ihttp.models;

public class Header {
    private String key = null;
    private String value = null;

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
