package com.qinqi.debugtoolbox.dbinspector.cache;

public interface Cache<T> {

    void store(T objectToStore);

    T getFromCache();
}
