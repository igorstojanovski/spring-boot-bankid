package co.igorski;

import co.igorski.model.AuthResponse;
import co.igorski.services.bankId.BankIdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyClientCertTest {

    @Autowired
    private BankIdService bankIdService;

    @Test
    public void performClientRequest() throws Exception {
        AuthResponse authResponse = bankIdService.auth("199404092224", "172.23.134.225");
        assertNotNull(authResponse.getOrderRef());
    }
}
