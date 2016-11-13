/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 
package tech.sirwellington.alexacarhackathon.notifications;


import com.google.common.io.Resources;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static tech.sirwellington.alexacarhackathon.APIs.AROMA;

/**
 *
 * @author SirWellington
 */
public class Certificates 
{
    private final static Logger LOG = LoggerFactory.getLogger(Certificates.class);

    private static final String RESOURCE_NAME = "/PNSDevCertificates.p12";
    
    static final InputStream getCertificate() throws IOException
    {
        URL url = Resources.getResource(Certificates.class, RESOURCE_NAME);
        
        return url.openStream();
    }
    
    public static ApnsService createPushService()
    {
        
        InputStream cert;
        
        try
        {
            cert = getCertificate();
        }
        catch(IOException ex)
        {
            LOG.error("Failed to load certificate.", ex);
            AROMA.begin().titled("Certificate Failure")
                .text("Failed to load certificate: {}", ex)
                .send();
            
            return null;
        }
        
        return new ApnsServiceBuilder()
            .asQueued()
            .withAppleDestination(true)
            .withSandboxDestination()
            .withCert(cert, "deroderoderoke9")
            .build();
    }
    
}
