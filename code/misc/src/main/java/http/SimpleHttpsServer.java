package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.google.common.io.Resources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

/**
 * to run, create self signed certificate
 * keytool -genkey -keyalg RSA -alias selfsigned -keystore testkey.jks -storepass password -validity 360 -keysize 2048
 * (key password for selfsigned is same as keystore
 */
public class SimpleHttpsServer {

  public static class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      String response = "This is the response";
      t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    try {
      // setup the socket address
      InetSocketAddress address = new InetSocketAddress(8000);

      // initialise the HTTPS server
      HttpsServer httpsServer = HttpsServer.create(address, 0);
      SSLContext sslContext = SSLContext.getInstance("TLS");

      // initialise the keystore
      char[] password = "password".toCharArray();
      KeyStore ks = KeyStore.getInstance("JKS");
      InputStream is = Resources.getResource("./testkey.jks").openStream();
      ks.load(is, password);

      // setup the key manager factory
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, password);

      // setup the trust manager factory
      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
      tmf.init(ks);

      // setup the HTTPS context and parameters
      sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
        public void configure(HttpsParameters params) {
          try {
            // initialise the SSL context
            SSLContext c = SSLContext.getDefault();
            SSLEngine engine = c.createSSLEngine();
            params.setNeedClientAuth(false);
            params.setCipherSuites(engine.getEnabledCipherSuites());
            params.setProtocols(engine.getEnabledProtocols());

            // get the default parameters
            SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
            params.setSSLParameters(defaultSSLParameters);
          } catch (Exception ex) {
            System.out.println("Failed to create HTTPS port");
          }
        }
      });
      httpsServer.createContext("/test", new MyHandler());
      httpsServer.setExecutor(new ThreadPoolExecutor(4, 8, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100)));
      httpsServer.start();
    } catch (Exception exception) {
      System.out.println("Failed to create HTTPS server on port " + 8000 + " of localhost");
      exception.printStackTrace();
    }
  }
}
