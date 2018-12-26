module gui {
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires java.logging;
  requires jdk.httpserver;
  requires netty.all;
  requires bcprov.jdk15on;
  requires info.picocli;
  requires guice;
  requires guava;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires jackson.dataformat.yaml;
  requires org.apache.logging.log4j;
  requires icu4j;
  requires io.reactivex.rxjava2;
  requires java.desktop;
  requires rxjavafx;

  exports javafx;
}