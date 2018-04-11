
package com.voole.utils.rxjava;


/**
 * @author liujingwei
 * @DESC Rxjava的onError的调用
 * @time 2017-11-16 13:21
 */


public class StatusError extends Throwable {
    private String errorMessage;
    private String errorCauseBy;
    private String errorCodestr;
    private Exception error;
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCauseBy() {
        return errorCauseBy;
    }

    public void setErrorCauseBy(String errorCauseBy) {
        this.errorCauseBy = errorCauseBy;
    }

    public String getErrorCodestr() {
        return errorCodestr;
    }

    public void setErrorCodestr(String errorCodestr) {
        this.errorCodestr = errorCodestr;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public static class ErrorBuilder{
        private StatusError error=new StatusError();

        public ErrorBuilder setErrorMessage(String errorMessage){
            error.setErrorMessage(errorMessage);
            return this;
        }

        public ErrorBuilder setErrorCauseBy(String errorCauseBy){
            error.setErrorCauseBy(errorCauseBy);
            return this;
        }

        public ErrorBuilder setErrorCodestr(String errorCodestr){
            error.setErrorCodestr(errorCodestr);
            return this;
        }
        public ErrorBuilder setErrorException(Exception exception){
            error.setError(exception);
            return this;
        }

        public StatusError create(){
            return error;
        }
    }

    @Override
    public String toString() {
        String errorstr = "unknown";
        if(null!=error) {
            errorstr = error.getLocalizedMessage();
        }
        return "StatusError{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorCauseBy='" + errorCauseBy + '\'' +
                ", errorCodestr='" + errorCodestr + '\'' +
                ", error=" + errorstr +
                '}';
    }
}

