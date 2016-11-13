
package tech.sirwellington.alexacarhackathon.parkwhiz;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.GenerateDouble;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateDouble.Type.RANGE;

/**
 *
 * @author SirWellington
 */
@RunWith(AlchemyTestRunner.class)
public class ParkWhizAPITest
{

    @GenerateDouble(value = RANGE, min = 0.0, max = 110)
    private Double latitude;

    @GenerateDouble(value = RANGE, min = 0.0, max = 110)
    private Double longitude;

    private Location location;

    @Before
    public void setUp() throws Exception
    {
        setupData();
    }

    private void setupData() throws Exception
    {
        location = new Location(latitude, longitude);
    }

    @Test
    public void testGetParkingNear() throws Exception
    {
        ParkingStructure parking = ParkWhizAPI.getParkingNear(location);
        assertThat(parking, notNullValue());
    }

    
    @Test
    public void testGetParkingBy() throws Exception
    {
        ParkingStructure parking = ParkWhizAPI.getParkingByURL("https://api.parkwhiz.com/p/los-angeles-parking/445-s-figueroa-st?start=1479024000&end=1479034800");
        assertThat(parking, notNullValue());
    }
}
