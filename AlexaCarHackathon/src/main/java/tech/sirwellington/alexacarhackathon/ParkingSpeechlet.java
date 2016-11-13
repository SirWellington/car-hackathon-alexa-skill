/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tech.sirwellington.alexacarhackathon;

import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.client.Urgency;
import tech.sirwellington.alexacarhackathon.parkwhiz.ParkWhizAPI;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;
import static tech.sirwellington.alexacarhackathon.APIs.AROMA;

/**
 *
 * @author SirWellington
 */
public final class ParkingSpeechlet implements Speechlet
{

    private final static Logger LOG = LoggerFactory.getLogger(ParkingSpeechlet.class);

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException
    {
        AROMA.begin()
            .titled("Session Started")
            .text("Request ID: {}, \n SessionID: {}", request.getRequestId(), session.getSessionId())
            .send();
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException
    {
        AROMA.begin()
            .titled("Speechlet Launched")
            .text("Request ID: {}\nSessionID: {}", request.getRequestId(), session.getSessionId())
            .send();

        return createWelcomeMessage();
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException
    {
        AROMA.begin()
            .titled("Speechlet Launched")
            .text("Intent: {}\nSession ID: {}", request.getIntent(), session.getSessionId())
            .send();

        String intent = request.getIntent().getName();

        checkThat(intent).is(nonEmptyString())
            .throwing(SpeechletException.class)
            .usingMessage("Intent missing");

        switch (intent)
        {
            case "ParkMeIntent":
                return createParkMeMessage();
            case "HelloIntent":
                return createHelloMessage();
        }

        return createWelcomeMessage();
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException
    {
        AROMA.begin()
            .titled("Session Ended")
            .text("Session ID: {}", session.getSessionId())
            .send();
    }

    private SpeechletResponse createParkMeMessage()
    {
        String parkingName;
        try
        {
            parkingName = ParkWhizAPI.getParkingNear(null);
        }
        catch (Exception ex)
        {
            AROMA.begin().titled("Operation Failed")
                .text("Could not find nearby parking locations: {}", ex)
                .withUrgency(Urgency.HIGH)
                .send();
            
            return createOperationFailedMessage();
        }
        
        String speechText = "I found a spot named " + parkingName + ".";
        speechText += "Do you want to park there?";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse createHelloMessage()
    {
        String speechText = "Bender here. What's shaking?";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse createOperationFailedMessage()
    {
        String speechText = "Sorry. I can't find anything around your right now.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    
    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse createWelcomeMessage()
    {
        String speechText = "Welcome to the Alexa Car Hackathon. My name is Bender.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

}
