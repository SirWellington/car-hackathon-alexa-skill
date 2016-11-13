/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tech.sirwellington.alexacarhackathon.parkwhiz;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.wellington.alchemy.collections.lists.Lists;
import tech.aroma.client.Urgency;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.BooleanAssertions.trueStatement;
import static tech.sirwellington.alchemy.arguments.assertions.NetworkAssertions.validURL;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;
import static tech.sirwellington.alexacarhackathon.APIs.AROMA;
import static tech.sirwellington.alexacarhackathon.APIs.HTTP;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;

/**
 *
 * @author SirWellington
 */
public final class ParkWhizAPI
{

    private final static Logger LOG = LoggerFactory.getLogger(ParkWhizAPI.class);
    private static final String API_KEY = "cbe9b407cf97223dc1fd051550396e53";

    public static ParkingStructure getParkingNear(Location location) throws Exception
    {
        String link = "http://api.parkwhiz.com/search/?lat=34.051099&lng=-118.257030";

        AROMA.begin().titled("Requesting Parking")
            .text("Near {}", location)
            .withUrgency(Urgency.LOW)
            .send();

        LOG.info("Requesting parking near {}", location);

        JsonElement response = HTTP.go()
            .get()
            .usingQueryParam("key", API_KEY)
            .at(link)
            .body();

        return anyListingFoundIn(response);
        
    }
    
    /**
     * 
     * @param apiURL The API URL associated with a given parking structure
     * when a search query was made. This allows a quick lookup.
     * @return 
     */
    public static ParkingStructure getParkingByURL(String apiURL) throws Exception
    {
        checkThat(apiURL)
            .is(nonEmptyString())
            .is(validURL());

        AROMA.begin().titled("Getting Parking Info")
            .text(apiURL)
            .withUrgency(Urgency.LOW)
            .send();
            
        JsonElement response = HTTP.go()
            .get()
            .usingQueryParam("key", API_KEY)
            .at(apiURL)
            .body();
        
        checkThat(response.isJsonObject())
            .is(trueStatement())
            .usingMessage("Unexpected JSON type: " + response);

        JsonObject json = response.getAsJsonObject();
        
        return ParkingStructure.fromJSON(json);
    }

    private static ParkingStructure anyListingFoundIn(JsonElement response)
    {
        
        AROMA.begin().titled("Parking Found")
            .text("JSON\n{}", response)
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
                .text(response.toString())
                .withUrgency(Urgency.HIGH)
                .send();

            return null;
        }
        
        List<JsonElement> elementsArray = Lists.create();
        for (JsonElement element : listings)
        {
            elementsArray.add(element);
        }
        
        JsonElement anyParking = Lists.oneOf(elementsArray);
        
        checkThat(anyParking.isJsonObject())
            .usingMessage("Expected Object in Parking listings, but instead: " + anyParking);
        
        JsonObject anyParkingObject = anyParking.getAsJsonObject();
        ParkingStructure parkingStructure = ParkingStructure.fromJSON(anyParkingObject);
        
        AROMA.begin().titled("Found Parking")
            .text(parkingStructure.toString())
            .send();
        
        return parkingStructure;
    }
}
