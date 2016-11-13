/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 
package tech.sirwellington.alexacarhackathon;


import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.wellington.alchemy.collections.sets.Sets;

/**
 *
 * @author SirWellington
 */
public final class ParkingSpeechletHandler extends SpeechletRequestStreamHandler
{
    private final static Logger LOG = LoggerFactory.getLogger(ParkingSpeechletHandler.class);

    private static final Set<String> APPLICATION_IDS = Sets.createFrom("amzn1.ask.skill.a01de60e-d3d4-4983-85d7-00a3ce26144c");
    
    public ParkingSpeechletHandler()
    {
        super(new ParkingSpeechlet(), APPLICATION_IDS);
    }

}
