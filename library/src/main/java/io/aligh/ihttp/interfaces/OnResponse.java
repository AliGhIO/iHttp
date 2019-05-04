package io.aligh.ihttp.interfaces;

public interface OnResponse extends Web {
    void onResponse(String response, String source, long time, int code);
}
