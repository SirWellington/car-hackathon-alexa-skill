
package tech.sirwellington.alexacarhackathon.parkwhiz;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.GenerateDouble;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateDouble.Type.RANGE;

/**
 *
 * @author SirWellington
 */
@Repeat(10)
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
        String name = ParkWhizAPI.getParkingNear(location);
        assertThat(name, not(isEmptyOrNullString()));
    }

}
