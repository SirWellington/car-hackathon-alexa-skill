/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tech.sirwellington.alexacarhackathon.parkwhiz;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.client.Urgency;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alexacarhackathon.APIs.AROMA;
import static tech.sirwellington.alexacarhackathon.APIs.HTTP;

/**
 *
 * @author SirWellington
 */
public final class ParkWhizAPI
{

    private final static Logger LOG = LoggerFactory.getLogger(ParkWhizAPI.class);

    public static ParkingStructure getParkingNear(Location location) throws Exception
    {
        String link = "http://api.parkwhiz.com/search/?key=cbe9b407cf97223dc1fd051550396e53&lat=34.0522&lng=-118.2437";

        AROMA.begin().titled("Requesting Parking")
            .text("Near {}", location)
            .withUrgency(Urgency.LOW)
            .send();

        LOG.info("Requesting parking near {}", location);

        JsonElement response = HTTP.go()
            .get()
            .at(link)
            .body();

        AROMA.begin().titled("Parking Found")
            .text("JSON\n", response)
            .withUrgency(Urgency.LOW)
            .send();
        LOG.info("Found parking: {}", response);

        checkThat(response.isJsonObject())
            .usingMessage("Expected JSON Object, intead is: " + response);

        JsonObject object = response.getAsJsonObject();
        checkThat(object.has("parking_listing"))
            .usingMessage("JSON Object missing parking listsings: " + object);

        JsonArray listings = object.get("parking_listings").getAsJsonArray();

        if (listings.size() == 0)
        {
            AROMA.begin().titled("No Parking Found")
                .text("Near Location: {}", location)
                .withUrgency(Urgency.HIGH)
                .send();

            return null;
        }
        
        JsonElement firstElement = listings.get(0);
        checkThat(firstElement.isJsonObject())
            .usingMessage("Expected Object in Parking listings, but instead: " + firstElement);
        
        JsonObject firstObject = firstElement.getAsJsonObject();
        ParkingStructure firstParkingStructure = ParkingStructure.fromJSON(firstObject);
        
        AROMA.begin().titled("Found Parking")
            .text(firstParkingStructure.toString())
            .send();
        
        return firstParkingStructure;
    }
}
