package com.voole.utils.log;

public interface FormatStrategy {

  void log(int priority, String tag, String message);
}
