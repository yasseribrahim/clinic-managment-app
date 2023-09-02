package com.clinic.management.app.executor;

public interface ExecutorTask<T> {
    T onBackground() throws Exception;

    void onForeground(T result);

    void onError(Exception exception);
}