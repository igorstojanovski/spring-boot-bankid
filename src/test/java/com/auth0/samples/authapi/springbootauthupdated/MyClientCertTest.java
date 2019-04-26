package com.auth0.samples.authapi.springbootauthupdated;

import com.auth0.samples.authapi.springbootauthupdated.model.CollectResponse;
import com.auth0.samples.authapi.springbootauthupdated.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MyClientCertTest {

    private static final String KEYSTOREPATH = "test.p12"; // or .p12
    private static final String KEYSTOREPASS = "qwerty123";
    private static final String KEYPASS = "qwerty123";

    KeyStore readKeyStore() throws Exception {
        try (InputStream keyStoreStream = ClassLoader.getSystemResourceAsStream(KEYSTOREPATH)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12"); // or "PKCS12"
            keyStore.load(keyStoreStream, KEYSTOREPASS.toCharArray());
            return keyStore;
        }
    }

    KeyStore readTrustStore() throws Exception {
        try (InputStream trustStoreStream = ClassLoader.getSystemResourceAsStream("cacerts")) {
            KeyStore trustStore = KeyStore.getInstance("JKS"); // or "PKCS12"
            trustStore.load(trustStoreStream, "changeit".toCharArray());
            return trustStore;
        }
    }

    @Test
    public void shouldReadKeyStore() throws Exception {
        assertNotNull(readKeyStore());
    }
    @Test
    public void performClientRequest() throws Exception {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(readKeyStore(), KEYPASS.toCharArray())
                .loadTrustMaterial(readTrustStore(), acceptingTrustStrategy)// use null as second param if you don't have a separate key password
                .build();

        HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        HttpPost post = new HttpPost("https://appapi.test.bankid.com/rp/v5/collect");

        String json = "{\n" +
                "\t\"orderRef\":\"131daac9-16c6-4618-beb0-365768f37288\"\n" +
                "}";
        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(post);

        HttpEntity responseEntity = response.getEntity();

        System.out.println("----------------------------------------");

        System.out.println(response.getStatusLine());
        String content = EntityUtils.toString(responseEntity);

        ObjectMapper objectMapper = new ObjectMapper();

        if(response.getStatusLine().getStatusCode() / 100 == 4) {
            ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
        } else {

        }







        System.out.println(collectResponse.getStatus());
        assertEquals(200, response.getStatusLine().getStatusCode());
    }
}
