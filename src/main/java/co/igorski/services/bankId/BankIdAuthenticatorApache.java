package co.igorski.services.bankId;

import co.igorski.model.AuthRequest;
import co.igorski.model.AuthResponse;
import co.igorski.model.CollectRequest;
import co.igorski.model.CollectResponse;
import co.igorski.model.ErrorResponse;
import co.igorski.services.SecureHttpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@Profile("production")
public class BankIdAuthenticatorApache implements BankIdAuthenticator {

    @Autowired
    private SecureHttpService secureHttpService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuthResponse auth(String personalNumber, String ipAddress) throws BankIdException {
        HttpPost httpPost = new HttpPost("https://appapi.test.bankid.com/rp/v5/auth");

        AuthRequest authRequest = new AuthRequest();
        authRequest.setPersonalNumber(personalNumber);
        authRequest.setEndUserIp(ipAddress);

        try {
            String body = objectMapper.writeValueAsString(authRequest);
            HttpResponse response = post(httpPost, body);

            HttpEntity responseEntity = response.getEntity();
            String content = EntityUtils.toString(responseEntity);

            if(response.getStatusLine().getStatusCode()  == HttpStatus.SC_OK) {
                return objectMapper.readValue(content, AuthResponse.class);
            } else {
                ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
                throw new BankIdException("Error authenticating to BankId: " + errorResponse.getDetails());
            }

        } catch (IOException e) {
            throw new BankIdException("Error when creating the JSON body.", e);
        }
    }

    @Override
    public CollectResponse collect(String refId) throws BankIdException {
        HttpPost post = new HttpPost("https://appapi.test.bankid.com/rp/v5/collect");

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setOrderRef(refId);

        try {
            String body = objectMapper.writeValueAsString(collectRequest);
            HttpResponse response = post(post, body);

            HttpEntity responseEntity = response.getEntity();
            String content = EntityUtils.toString(responseEntity);

            if(response.getStatusLine().getStatusCode()  == HttpStatus.SC_OK) {
                return objectMapper.readValue(content, CollectResponse.class);
            } else {
                ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
                throw new BankIdException("Error authenticating to BankId: " + errorResponse.getDetails());
            }
        } catch (IOException e) {
            throw new BankIdException("Error when creating the JSON body.", e);
        }
    }

    private HttpResponse post(HttpPost post, String body) throws BankIdException {
        try {
            StringEntity entity = new StringEntity(body);
            post.setEntity(entity);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            return secureHttpService.execute(post);
        } catch (UnsupportedEncodingException e) {
            throw new BankIdException("Error when creating the HTTP entity.", e);
        } catch (IOException e) {
            throw new BankIdException("Error when sending the request.", e);
        }
    }
}
