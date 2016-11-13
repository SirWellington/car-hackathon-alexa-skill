/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tech.sirwellington.alexacarhackathon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
public class ParkingSpeechletTest
{

    private ParkingSpeechlet instance;

    @Before
    public void setUp() throws Exception
    {

        setupData();
        setupMocks();
    }

    private void setupData() throws Exception
    {
        instance = new ParkingSpeechlet();
    }

    private void setupMocks() throws Exception
    {

    }

    @Test
    public void testOnSessionStarted() throws Exception
    {
    }

    @Test
    public void testOnLaunch() throws Exception
    {
    }

    @Test
    public void testOnIntent() throws Exception
    {
    }

    @Test
    public void testOnSessionEnded() throws Exception
    {
    }

    @Test
    public void testSendPushNotification()
    {
        instance.sendPushNotification();
    }

}
