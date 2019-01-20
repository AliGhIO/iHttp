package io.aligh.ihttp.interfaces;

public interface OnRetry extends Web {
    void onRetry(int tried_times, int retry_times);
}
