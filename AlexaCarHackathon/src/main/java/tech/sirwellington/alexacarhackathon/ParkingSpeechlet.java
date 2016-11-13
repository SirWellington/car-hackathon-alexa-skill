
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.notnoop.apns.APNS;
import com.notnoop.apns.PayloadBuilder;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.client.Urgency;
import tech.sirwellington.alexacarhackathon.parkwhiz.Location;
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
                return createParkMeMessage(session);
            case "HelloIntent":
                return createHelloMessage();
            case "BookIntent":
                return createMessageToBook(session);
            case "DirectionsIntent":
                return createDirectionsMessage(session);
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

    private SpeechletResponse createParkMeMessage(Session session)
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

        String speechText = createSpeechTextFor(parking);
        addParkingToSession(parking, session);

        String repromptText = "Do you want to park there? Say yes or no.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech responseSpeech = new PlainTextOutputSpeech();
        responseSpeech.setText(speechText);

        PlainTextOutputSpeech promptSpeech = new PlainTextOutputSpeech();
        promptSpeech.setText(repromptText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(promptSpeech);

        return SpeechletResponse.newAskResponse(responseSpeech, reprompt, card);
    }

    private SpeechletResponse createMessageToBook(Session session)
    {
        ParkingStructure parking = getParkingFrom(session);

        if (parking == null)
        {
            AROMA.begin().titled("ParkingStructure Parsing Failed").send();
        }
        else
        {
            AROMA.begin().titled("ParkingStructure Parsing Successful")
                .text(parking.toString())
                .send();
        }

        String speechText = "Ok. I have reserved a spot for you using your credit card. ";
        speechText += "Your parking spot is number 33 on the second floor. ";
        speechText += "Would you like directions to your parking spot? ";

        PlainTextOutputSpeech responseSpeech = new PlainTextOutputSpeech();
        responseSpeech.setText(speechText);

        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(speechText);

        String repromtText = "Would you like directions to your parking spot? Say yes give me directions. ";
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromtText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);
        
        AROMA.begin().titled("Parking Booked")
            .text("Parking: {}", parking)
            .send();

        return SpeechletResponse.newAskResponse(responseSpeech, reprompt, card);
    }

    private SpeechletResponse createDirectionsMessage(Session session)
    {
        AROMA.begin()
            .titled("Received Directions Intent")
            .send();

        ParkingStructure parking = null;
        try
        {
            parking = getParkingFrom(session);
        }
        catch (Exception ex)
        {
            LOG.error("Failed to parse Parking Structure");
            AROMA.begin().titled("Failed To Parse")
                .text("Could not deserialize parking structure: {}", ex)
                .withUrgency(Urgency.HIGH)
                .send();
        }

        Location location;

        if (parking != null && parking.getLocation() != null)
        {
            location = parking.getLocation();
        }
        else
        {
            AROMA.begin().titled("Info Missing")
                .text("Missing Parking information from session")
                .send();

            location = Location.DOWNTOWN_LA;
        }

        sendPushNotificationToNavigateTo(location);

        String message = "Ok. I sent navigation instructions to your phone.";
        AROMA.begin().titled("Direction Notification Sent")
            .text("To Location: \n{}Place: {}", location, parking)
            .send();

        SimpleCard card = new SimpleCard();
        card.setTitle("ParkMe");
        card.setContent(message);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(message);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    void sendPushNotificationToNavigateTo(Location location)
    {
        byte[] payload = createNotificationToOpen(location);
        String base64DeviceId = "O3ahNQuzM2E105jGnPIKyhWIcTgtHh/IKErV4uOzrJs=";
        byte[] deviceId = DatatypeConverter.parseBase64Binary(base64DeviceId);

        APIs.APNS.push(deviceId, payload);

        AROMA.begin().titled("Sent Push Notifications")
            .text("To {}", base64DeviceId)
            .send();
    }

    private byte[] createNotificationToOpen(Location location)
    {
        String alertTitle = "Alexa Hackathon";
        String alertBody = "Open Navigation to Parking Spot";

        PayloadBuilder builder = APNS.newPayload()
            .instantDeliveryOrSilentNotification()
            .alertTitle(alertTitle)
            .alertBody(alertBody)
            .customField("json", location.asJSON().toString());

        return builder.buildBytes();
    }

    private String createSpeechTextFor(ParkingStructure parking)
    {
        String speechText = "I found a spot named " + parking.getName() + " . ";
        speechText += "It is " + parking.getDistanceInMeters() + " meters away, ";
        speechText += "and costs " + parking.getPrice() + " dollars. ";
        speechText += "They have " + parking.getAvailableSpots() + " spots available. ";
        speechText += "Do you want to park there?";

        return speechText;
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

    private void addParkingToSession(ParkingStructure parking, Session session)
    {
        session.setAttribute("place", parking.asJSON().toString());
    }

    private ParkingStructure getParkingFrom(Session session)
    {
        if (session == null)
        {
            return null;
        }

        Object placeObject = session.getAttribute("place");
        if (placeObject == null)
        {
            return null;
        }

        String placeJson = placeObject.toString();

        Gson gson = new Gson();
        JsonElement json = gson.fromJson(placeJson, JsonElement.class);

        if (!json.isJsonObject())
        {
            LOG.warn("Unexpected JSON type: " + json);
            AROMA.begin().titled("Unexpected JSON")
                .text("JSON of Type {}", json)
                .send();

            return null;
        }

        return ParkingStructure.fromJSON(json.getAsJsonObject());
    }
}
