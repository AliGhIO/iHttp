package io.aligh.ihttp.interfaces;

public interface OnRetry {
    void onRetry(int tried_times, int retry_times);
}
