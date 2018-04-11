package com.voole.utils.log;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
