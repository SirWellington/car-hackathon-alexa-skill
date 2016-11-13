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
import tech.sirwellington.alexacarhackathon.parkwhiz.ParkingStructure;

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
        String intent = request.getIntent().getName();

        AROMA.begin()
            .titled("Speechlet Launched")
            .text("Intent: {}\nSession ID: {}", intent, session.getSessionId())
            .send();

        checkThat(intent).is(nonEmptyString())
            .throwing(SpeechletException.class)
            .usingMessage("Intent missing");

        switch (intent)
        {
            case "ParkMeIntent":
                return createParkMeMessage();
            case "HelloIntent":
                return createHelloMessage();
            case "BookIntent":
                return createMessageToBook();
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
        ParkingStructure parking;
        try
        {
            parking = ParkWhizAPI.getParkingNear(null);
        }
        catch (Exception ex)
        {
            AROMA.begin().titled("Operation Failed")
                .text("Could not find nearby parking locations: {}", ex)
                .withUrgency(Urgency.HIGH)
                .send();

            return createOperationFailedMessage();
        }

        if (parking == null)
        {
            return createOperationFailedMessage();
        }

        String speechText = "I found a spot named " + parking.getName() + " .";
        speechText += "It is " + parking.getDistanceInMeters() + " meters away, ";
        speechText += "and costs " + parking.getPrice()+ "dollars.";
        
        String promptText = "Do you want to park there?";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech responseSpeech = new PlainTextOutputSpeech();
        responseSpeech.setText(speechText);
        
        PlainTextOutputSpeech promptSpeech = new PlainTextOutputSpeech();
        promptSpeech.setText(promptText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(promptSpeech);

        return SpeechletResponse.newAskResponse(responseSpeech, reprompt, card);
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
    
    private SpeechletResponse createMessageToBook()
    {
        String speechText = "Ok. I have reserved a spot for you using your credit card.";
        String repromtText= "Would you like directions to your parking spot?";
        
        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);
        
        PlainTextOutputSpeech responseSpeech = new PlainTextOutputSpeech();
        responseSpeech.setText(speechText);
        
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromtText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);
        
        return SpeechletResponse.newAskResponse(repromptSpeech, reprompt, card);
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
