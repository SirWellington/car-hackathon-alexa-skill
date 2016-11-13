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

    private static final Set<String> APPLICATION_IDS = Sets.createFrom("amzn1.ask.skill.bb0143f5-53d8-4121-b599-2d5872f4541b");
    
    public ParkingSpeechletHandler()
    {
        super(new ParkingSpeechlet(), APPLICATION_IDS);
    }

}
