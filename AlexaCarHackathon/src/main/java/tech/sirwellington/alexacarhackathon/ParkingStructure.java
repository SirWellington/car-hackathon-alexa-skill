/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tech.sirwellington.alexacarhackathon;

import com.google.gson.JsonObject;
import java.util.Objects;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.annotations.objects.Pojo;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.BooleanAssertions.trueStatement;

/**
 *
 * This class contains the POJO representation of a Parking structure pulled form the 
 * <a href="http://www.parkwhiz.com/developers/">http://www.parkwhiz.com/developers/</a> ParkWhiz API.
 * 
 * 
 * @author SirWellington
 */
@Pojo
public class ParkingStructure
{

    private String name;
    private String city;
    private String state;
    private int zipCode;
    private Location location;
    private String parkWhizURL;
    private int availableSpots;
    private int price;
    private String priceFormatted;
    private String reservationAPIURL;

    public ParkingStructure()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public int getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(int zipCode)
    {
        this.zipCode = zipCode;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public String getParkWhizURL()
    {
        return parkWhizURL;
    }

    public void setParkWhizURL(String parkWhizURL)
    {
        this.parkWhizURL = parkWhizURL;
    }

    public int getAvailableSpots()
    {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots)
    {
        this.availableSpots = availableSpots;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getPriceFormatted()
    {
        return priceFormatted;
    }

    public void setPriceFormatted(String priceFormatted)
    {
        this.priceFormatted = priceFormatted;
    }

    public String getReservationAPIURL()
    {
        return reservationAPIURL;
    }

    public void setReservationAPIURL(String reservationAPIURL)
    {
        this.reservationAPIURL = reservationAPIURL;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.city);
        hash = 29 * hash + Objects.hashCode(this.state);
        hash = 29 * hash + this.zipCode;
        hash = 29 * hash + Objects.hashCode(this.location);
        hash = 29 * hash + Objects.hashCode(this.parkWhizURL);
        hash = 29 * hash + this.availableSpots;
        hash = 29 * hash + this.price;
        hash = 29 * hash + Objects.hashCode(this.priceFormatted);
        hash = 29 * hash + Objects.hashCode(this.reservationAPIURL);
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
        if (!Objects.equals(this.name, other.name))
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
        return "ParkingStructure{" + "name=" + name + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode + ", location=" + location + ", parkWhizURL=" + parkWhizURL + ", availableSpots=" + availableSpots + ", price=" + price + ", priceFormatted=" + priceFormatted + ", reservationAPIURL=" + reservationAPIURL + '}';
    }

    @Pojo
    public static class Location
    {

        private double latitude;
        private double longitude;

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

        
        public static Location fromJSON(@Required JsonObject object)
        {
            checkThat(object).is(notNull());
            checkThat(object.has("lat"))
                .is(trueStatement())
                .usingMessage("Object missing latitude: " + object);
            checkThat(object.has("long"))
                .usingMessage("Object missing longitude: " + object);
            
            double lat = object.get("lat").getAsDouble();
            double lon = object.get("lon").getAsDouble();
            
            return new Location(lat, lon);
        }
        
    }

}
