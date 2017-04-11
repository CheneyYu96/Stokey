package org.xeon.stockey.util;

/**
 * A class to notice the result of an operation and the reason(if fail)
 * T: the type of the extra data bundle
 * Created by Sissel on 2016/5/30.
 */
public class OperationResult<T> {
    public boolean success;
    public String reason;
    private T bundle;

    public OperationResult(boolean isSuccess) {
        this.success = isSuccess;
        this.reason = null;
        this.bundle = null;
    }

    public OperationResult(boolean isSuccess, T bundle){
        this.success = isSuccess;
        this.bundle = bundle;
        this.reason = null;
    }


    public OperationResult(boolean isSuccess, String reason) {
        this.success = isSuccess;
        this.reason = reason;
        this.bundle = null;
    }

    public OperationResult(OperationResult origin) {
        this.success = origin.success;
        this.reason = origin.reason;
        this.bundle = null;
    }

    public T getBundle() {
        return this.bundle;
    }

    public void setBundle(T bundle) {
        this.bundle = bundle;
    }
}
