package tech.sirwellington.alexacarhackathon.notifications;

import com.notnoop.apns.ApnsService;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
public class CertificatesTest 
{

    @Before
    public void setUp() throws Exception
    {
        
        setupData();
        setupMocks();
    }


    private void setupData() throws Exception
    {
        
    }

    private void setupMocks() throws Exception
    {
        
    }

    @Test
    public void testGetCertificate() throws Exception
    {
        InputStream certificate = Certificates.getCertificate();
        assertThat(certificate, notNullValue());
    }

    @Test
    public void testCreatePushService()
    {
        ApnsService service = Certificates.createPushService();
        assertThat(service, notNullValue());
    }

}