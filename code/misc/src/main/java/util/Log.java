package util;

import org.apache.logging.log4j.Logger;

public interface Log {
  Logger getLogger();

  default void fatal(Object o) {
    Logger l = getLogger();
    if (l.isFatalEnabled()) {
      l.fatal(o.toString());
    }
  }

  default void error(Object o) {
    Logger l = getLogger();
    if (l.isErrorEnabled()) {
      l.error(o.toString());
    }
  }

  default void warn(Object o) {
    Logger l = getLogger();
    if (l.isWarnEnabled()) {
      l.warn(o.toString());
    }
  }

  default void info(Object o) {
    Logger l = getLogger();
    if (l.isInfoEnabled()) {
      l.info(o.toString());
    }
  }

  default void debug(Object o) {
    Logger l = getLogger();
    if (l.isDebugEnabled()) {
      l.debug(o.toString());
    }
  }

  default void trace(Object o) {
    Logger l = getLogger();
    if (l.isTraceEnabled()) {
      l.trace(o.toString());
    }
  }

  default void f(Object o) {
    fatal(o);
  }

  default void e(Object o) {
    error(o);
  }

  default void w(Object o) {
    warn(o);
  }

  default void i(Object o) {
    info(o);
  }

  default void d(Object o) {
    debug(o);
  }

  default void t(Object o) {
    trace(o);
  }
}
