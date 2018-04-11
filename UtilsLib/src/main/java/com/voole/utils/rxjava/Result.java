package com.voole.utils.rxjava;

/**
 * @author liujingwei
 * @DESC 用于跟踪Rxjava调用过程产生的值
 * @time 2017-11-16 13:21
 */

public class Result {
    private int resultCode = 0;
    private String resultMessage = "error";
    private Object resultObject=null;
    public Result(){

    }

    public Result(int resultCode, String resultMessage) {
        this.resultMessage = resultMessage;
        this.resultCode = resultCode;
    }

    public Result(int resultCode, String resultMessage, Object result) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.resultObject = result;
    }

    public Object getResult() {
        return resultObject;
    }

    public void setResult(Object result) {
        this.resultObject = result;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultCode=" + resultCode +
                ", resultMessage='" + resultMessage + '\'' +
                ", result=" + resultObject +
                '}';
    }
}
