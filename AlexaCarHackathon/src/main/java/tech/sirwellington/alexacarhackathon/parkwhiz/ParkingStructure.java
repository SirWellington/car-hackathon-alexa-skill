/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tech.sirwellington.alexacarhackathon.parkwhiz;

import com.google.gson.JsonObject;
import java.util.Objects;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.annotations.objects.Pojo;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * This class contains the POJO representation of a Parking structure pulled form the
 * <a href="http://www.parkwhiz.com/developers/">http://www.parkwhiz.com/developers/</a> ParkWhiz API.
 *
 * P.S. This is hackathon code. Be nice.
 *
 * @author SirWellington
 */
@Pojo
public class ParkingStructure
{

    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private int zipCode;
    private Location location;
    private String parkWhizURL;
    private int availableSpots;
    private int price;
    private String priceFormatted;
    private String reservationAPIURL;
    private long distanceInFeet;

    public String getName()
    {
        return name;
    }

    public String getStreetAddress()
    {
        return streetAddress;
    }

    public String getCity()
    {
        return city;
    }

    public String getState()
    {
        return state;
    }

    public int getZipCode()
    {
        return zipCode;
    }

    public Location getLocation()
    {
        return location;
    }

    public String getParkWhizURL()
    {
        return parkWhizURL;
    }

    public int getAvailableSpots()
    {
        return availableSpots;
    }

    public int getPrice()
    {
        return price;
    }

    public String getPriceFormatted()
    {
        return priceFormatted;
    }

    public String getReservationAPIURL()
    {
        return reservationAPIURL;
    }

    public long getDistanceInFeet()
    {
        return distanceInFeet;
    }
    
    public long getDistanceInMeters()
    {
        return (long) (distanceInFeet * 0.3048);
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.name);
        hash = 43 * hash + Objects.hashCode(this.streetAddress);
        hash = 43 * hash + Objects.hashCode(this.city);
        hash = 43 * hash + Objects.hashCode(this.state);
        hash = 43 * hash + this.zipCode;
        hash = 43 * hash + Objects.hashCode(this.location);
        hash = 43 * hash + Objects.hashCode(this.parkWhizURL);
        hash = 43 * hash + this.availableSpots;
        hash = 43 * hash + this.price;
        hash = 43 * hash + Objects.hashCode(this.priceFormatted);
        hash = 43 * hash + Objects.hashCode(this.reservationAPIURL);
        hash = 43 * hash + (int) (this.distanceInFeet ^ (this.distanceInFeet >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ParkingStructure other = (ParkingStructure) obj;
        if (this.zipCode != other.zipCode)
        {
            return false;
        }
        if (this.availableSpots != other.availableSpots)
        {
            return false;
        }
        if (this.price != other.price)
        {
            return false;
        }
        if (this.distanceInFeet != other.distanceInFeet)
        {
            return false;
        }
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        if (!Objects.equals(this.streetAddress, other.streetAddress))
        {
            return false;
        }
        if (!Objects.equals(this.city, other.city))
        {
            return false;
        }
        if (!Objects.equals(this.state, other.state))
        {
            return false;
        }
        if (!Objects.equals(this.parkWhizURL, other.parkWhizURL))
        {
            return false;
        }
        if (!Objects.equals(this.priceFormatted, other.priceFormatted))
        {
            return false;
        }
        if (!Objects.equals(this.reservationAPIURL, other.reservationAPIURL))
        {
            return false;
        }
        if (!Objects.equals(this.location, other.location))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "ParkingStructure{" + "name=" + name + ", streetAddress=" + streetAddress + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode + ", location=" + location + ", parkWhizURL=" + parkWhizURL + ", availableSpots=" + availableSpots + ", price=" + price + ", priceFormatted=" + priceFormatted + ", reservationAPIURL=" + reservationAPIURL + ", distanceInFeet=" + distanceInFeet + '}';
    }

    public static ParkingStructure fromJSON(@Required JsonObject object)
    {
        checkThat(object).is(notNull());

        checkThat(object.has(Keys.NAME)).usingMessage("object missing name");
        checkThat(object.has(Keys.STREET_ADDRESS)).usingMessage("object missing street address");
        checkThat(object.has(Keys.LATITUDE)).usingMessage("object missing location information");
        checkThat(object.has(Keys.LONGITUDE)).usingMessage("object missing location information");
        checkThat(object.has(Keys.PRICE)).usingMessage("object missing price information");

        ParkingStructure parking = new ParkingStructure();
        fillParkingWithJSON(parking, object);

        return parking;
    }

    private static void fillParkingWithJSON(ParkingStructure parking, JsonObject object)
    {
        parking.name = object.get(Keys.NAME).getAsString();
        parking.streetAddress = object.get(Keys.STREET_ADDRESS).getAsString();

        Location location = new Location();
        location.latitude = object.get(Keys.LATITUDE).getAsDouble();
        location.longitude = object.get(Keys.LONGITUDE).getAsDouble();
        parking.location = location;
        parking.price = object.get(Keys.PRICE).getAsInt();

        if (object.has(Keys.DISTANCE))
        {
            parking.distanceInFeet = object.get(Keys.DISTANCE).getAsLong();
        }

        if (object.has(Keys.FORMATTED_PRICE))
        {
            parking.priceFormatted = object.get(Keys.FORMATTED_PRICE).getAsString();
        }

    }

    private static class Keys
    {
        //TODO: Put this in a separate class

        static final String NAME = "location_name";
        static final String STREET_ADDRESS = "address";
        static final String CITY = "city";
        static final String STATE = "state";
        static final String ZIP_CODE = "zip";
        static final String LATITUDE = "lat";
        static final String LONGITUDE = "lng";
        static final String DISTANCE = "distance";
        static final String AVAILABLE_SPOTS = "available_spots";
        static final String PRICE = "price";
        static final String FORMATTED_PRICE = "price_formatted";
        static final String RESERVATION_URL = "api_reserve_url";
    }

}
