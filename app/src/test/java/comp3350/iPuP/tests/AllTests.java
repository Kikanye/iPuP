package comp3350.iPuP.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import comp3350.iPuP.objects.ReservationTime;
import comp3350.iPuP.tests.business.AccessParkingSpotsTest;
import comp3350.iPuP.tests.objects.ParkingSpotTest;
import comp3350.iPuP.tests.objects.ReservationTimeTest;

public class AllTests
{
	public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("All tests");
        testObjects();
        testBusiness();
        return suite;
    }

    private static void testObjects()
    {
        suite.addTestSuite(ParkingSpotTest.class);
        suite.addTestSuite(ReservationTimeTest.class);
    }

    private static void testBusiness()
    {
        suite.addTestSuite(AccessParkingSpotsTest.class);
    }
}
