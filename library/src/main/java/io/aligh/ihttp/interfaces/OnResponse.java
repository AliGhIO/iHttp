package io.aligh.ihttp.interfaces;

public interface OnResponse extends Web {
    void response(String response, String source, long time);
}
