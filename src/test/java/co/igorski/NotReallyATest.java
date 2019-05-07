package co.igorski;

import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class NotReallyATest {

    @Test
    public void shouldSendRequest() throws IOException, NoSuchAlgorithmException, KeyStoreException,
            CertificateException, KeyManagementException, UnrecoverableKeyException {

        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(ClassLoader.getSystemResourceAsStream("test.p12"), "qwerty123".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, "qwerty123".toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(ClassLoader.getSystemResourceAsStream("cacerts"), "changeit".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, tms, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        URL url = new URL("https://appapi2.test.bankid.com/rp/v5");

        HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
        printContent(urlConn);
    }

    private void printContent(HttpsURLConnection con){
        if(con!=null){

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
