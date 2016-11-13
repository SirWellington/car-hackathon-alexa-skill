/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 
package tech.sirwellington.alexacarhackathon.parkwhiz;


import com.google.gson.JsonObject;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.annotations.objects.Pojo;
import tech.sirwellington.alchemy.arguments.Arguments;
import tech.sirwellington.alchemy.arguments.assertions.Assertions;
import tech.sirwellington.alchemy.arguments.assertions.BooleanAssertions;

/**
 *
 * @author SirWellington
 */
@Pojo 
public class Location 
{
    double latitude;
    double longitude;
    
    public static Location DOWNTOWN_LA = new Location(34.051099, -118.257030);

    public Location()
    {
    }

    public Location(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    
    public JsonObject asJSON()
    {
        JsonObject json = new JsonObject();
        
        json.addProperty("latitude", latitude);
        json.addProperty("longitude", longitude);
        
        return json;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
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
        final Location other = (Location) obj;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude))
        {
            return false;
        }
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Location{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
    }

    public static Location fromJSON(
                                    @Required JsonObject object)
    {
        Arguments.checkThat(object).is(Assertions.notNull());
        Arguments.checkThat(object.has("lat")).is(BooleanAssertions.trueStatement()).
            usingMessage("Object missing latitude: " + object);
        Arguments.checkThat(object.has("long")).usingMessage("Object missing longitude: " + object);
        double lat = object.get("lat").getAsDouble();
        double lon = object.get("lon").getAsDouble();
        return new Location(lat, lon);
    }

}
