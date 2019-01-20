package io.aligh.ihttp.enums;

public enum Logs {
    ERROR("ERROR"),
    INFO("INFO"),
    WARN("WARN");


    private String log;

    Logs(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }
}
